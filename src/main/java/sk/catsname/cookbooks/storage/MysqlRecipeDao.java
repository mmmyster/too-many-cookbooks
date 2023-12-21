package sk.catsname.cookbooks.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import sk.catsname.cookbooks.Ingredient;
import sk.catsname.cookbooks.KimKitsuragi;
import sk.catsname.cookbooks.Recipe;

import javafx.scene.image.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MysqlRecipeDao implements RecipeDao{
    private JdbcTemplate jdbcTemplate;
    private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();

    public MysqlRecipeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Recipe> recipeRM() { // RowMapper for "ingredient" table
        return new RowMapper<Recipe>() {
            @Override
            public Recipe mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                Image image = KimKitsuragi.convertBlobToImage(rs.getBlob("image"));
                Float prepTime = rs.getFloat("prep_time");
                Integer servings = rs.getInt("servings");
                List<Ingredient> ingredients = ingredientDao.getAllByRecipeId(id);
                String instructions = rs.getString("instructions");
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                return new Recipe(id, name, image, prepTime, servings, ingredients, instructions, createdAt, updatedAt);
            }
        };
    }

    @Override
    public List<Recipe> getAll() {
        String sql = "SELECT id, name, image, prep_time, servings, instructions, created_at, updated_at " +
                "FROM recipe";

        return jdbcTemplate.query(sql, recipeRM());
    }

    @Override
    public List<Recipe> getAllByCookbookId(long id) {
        String sql = "SELECT r.id, r.name, r.image, r.prep_time, r.servings, r.instructions, r.created_at, r.updated_at " +
                "FROM recipe r " +
                "INNER JOIN recipe_cookbook rc ON r.id = rc.recipe_id " +
                "INNER JOIN cookbook c ON rc.cookbook_id = c.id " +
                "WHERE c.id = " + id;

        return jdbcTemplate.query(sql, recipeRM());
    }

    @Override
    public Recipe getById(long id) throws EntityNotFoundException {
        String sql = "SELECT id, name, image, prep_time, servings, instructions, created_at, updated_at " +
                "FROM recipe WHERE id = " + id;

        return jdbcTemplate.queryForObject(sql, recipeRM());
    }

    @Override
    public Recipe save(Recipe recipe) throws EntityNotFoundException, SQLException {
        // sets the objects that require to eb null to be null
        Objects.requireNonNull(recipe, "Recipe cannot be null");
        Objects.requireNonNull(recipe.getName(), "Recipe name cannot be null");
        Objects.requireNonNull(recipe.getCreatedAt(), "Recipe time of creation cannot be null");
        Objects.requireNonNull(recipe.getUpdatedAt(), "Recipe time of last update cannot be null");

        if (recipe.getImage() == null) { recipe.setImage(new Image("sk/catsname/cookbooks/no_image_sad.jpg")); }

        if (recipe.getId() == null) { // INSERT
            String query = "INSERT INTO recipe (name, image, prep_time, servings, instructions, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, recipe.getName());
                statement.setBlob(2, KimKitsuragi.convertImageToBlob(recipe.getImage()));
                statement.setFloat(3, recipe.getPreparationTime());
                statement.setInt(4, recipe.getServings());
                statement.setString(5, recipe.getInstructions());
                statement.setTimestamp(6, recipe.getUpdatedAt());
                statement.setTimestamp(7, recipe.getCreatedAt());
                return statement;
            }, keyHolder);
            Long id = keyHolder.getKey().longValue();

            Recipe newRecipe = new Recipe(
                    id,
                    recipe.getName(),
                    recipe.getImage(),
                    recipe.getPreparationTime(),
                    recipe.getServings(),
                    recipe.getIngredients(),
                    recipe.getInstructions(),
                    recipe.getCreatedAt(),
                    recipe.getUpdatedAt()
            );

            if (recipe.getIngredients() != null) {
                for (Ingredient ingredient : recipe.getIngredients()) { // saves the ingredients from the recipe
                    ingredientDao.save(ingredient, newRecipe);
                }
            }

            return newRecipe;
        } else { // UPDATE
            String query = "UPDATE recipe SET name=?, image=?, prep_time=?, servings=?, instructions=?, created_at=?, updated_at=? " +
                    "WHERE id = " + recipe.getId();

            Timestamp updateTime = recipe.getUpdatedAt();

            int count = jdbcTemplate.update(query,
                    recipe.getName(),
                    KimKitsuragi.convertImageToBlob(recipe.getImage()),
                    recipe.getPreparationTime(),
                    recipe.getServings(),
                    recipe.getInstructions(),
                    recipe.getCreatedAt(),
                    updateTime = new Timestamp(System.currentTimeMillis())
            );

            if (count == 0) {
                throw new EntityNotFoundException("Recipe with id " + recipe.getId() + " not found");
            }

            for (Ingredient ingredient : recipe.getIngredients()) { // updating the ingredients
                ingredientDao.save(ingredient, recipe);
            }

            List<Ingredient> ingredientsToDelete = new ArrayList<>();

            for (Ingredient ingredient : ingredientDao.getAllByRecipeId(recipe.getId())) {
                boolean found = false;
                for (Ingredient recipeIngredient : recipe.getIngredients()) {
                    if (ingredient.getName().equals(recipeIngredient.getName())) {
                        found = true;
                        break; // no need to check further, as the ingredient is already in the recipe
                    }
                }

                if (!found) {
                    ingredientsToDelete.add(ingredient);
                }
            }

            // delete the ingredients
            for (Ingredient ingredient : ingredientsToDelete) {
                try {
                    ingredientDao.deleteIngredientRecipe(ingredient.getId());
                } catch (EntityNotFoundException e) {} // no need to handle the exception
            }

            return new Recipe(
                    recipe.getId(),
                    recipe.getName(),
                    recipe.getImage(),
                    recipe.getPreparationTime(),
                    recipe.getServings(),
                    recipe.getIngredients(),
                    recipe.getInstructions(),
                    recipe.getCreatedAt(),
                    updateTime
            );
        }
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        // first we delete the recipe from ingredient_recipe table
        deleteIngredientRecipe(id);

        // then we delete the recipe from recipe_cookbook table
        deleteRecipeCookbook(id);

        // then we delete it from recipe table
        deleteRecipe(id);
    }

    @Override
    public void deleteIngredientRecipe(long id) throws EntityNotFoundException {
        String query = "DELETE FROM ingredient_recipe WHERE recipe_id = " + id;

        int count = jdbcTemplate.update(query);

        if (count == 0) { throw new EntityNotFoundException("Recipe with id " + id + " not found in ingredient_recipe table"); }
    }

    @Override
    public void deleteRecipe(long id) throws EntityNotFoundException {
        String query = "DELETE FROM recipe WHERE id = " + id;

        int count = jdbcTemplate.update(query);

        if (count == 0) { throw new EntityNotFoundException("Recipe with id " + id + " not found in recipe table"); }
    }

    @Override
    public void deleteRecipeCookbook(long id) throws EntityNotFoundException {
        String query = "DELETE FROM recipe_cookbook WHERE recipe_id = " + id;

        int count = jdbcTemplate.update(query);

        if (count == 0) { throw new EntityNotFoundException("Recipe with id " + id + " not found in recipe_cookbook table"); }
    }
}
