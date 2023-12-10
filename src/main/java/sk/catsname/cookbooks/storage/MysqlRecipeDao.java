package sk.catsname.cookbooks.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import sk.catsname.cookbooks.KimKitsuragi;
import sk.catsname.cookbooks.Recipe;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class MysqlRecipeDao implements RecipeDao{
    private JdbcTemplate jdbcTemplate;
    private IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();

    public MysqlRecipeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> getAllByCookbookId(long id) {
        return null;
    }

    @Override
    public Recipe getById(long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Recipe save(Recipe recipe) throws EntityNotFoundException {
        // sets the objects that require to eb null to be null
        Objects.requireNonNull(recipe, "Recipe cannot be null");
        Objects.requireNonNull(recipe.getName(), "Recipe name cannot be null");
        Objects.requireNonNull(recipe.getCreatedAt(), "Recipe time of creation cannot be null");
        Objects.requireNonNull(recipe.getUpdatedAt(), "Recipe time of last update cannot be null");

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
            return new Recipe(
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
        } else { // UPDATE
            String query = "UPDATE recipe SET name=?, image=?, prep_time=?, servings=?, instructions=?, created_at=?, updated_at=? " +
                    "WHERE id=?";
            int count = jdbcTemplate.update(query,
                    recipe.getName(),
                    recipe.getImage(),
                    recipe.getPreparationTime(),
                    recipe.getServings(),
                    recipe.getInstructions(),
                    recipe.getCreatedAt(),
                    recipe.getUpdatedAt()
            );

            if (count == 0) {
                throw new EntityNotFoundException("Recipe with id " + recipe.getId() + " not found");
            }

            return  recipe;
        }
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {
        /*
        String query = "DELETE FROM recipe WHERE id=?";
        int count = jdbcTemplate.update(query, id);
        if (count == 0) { throw new EntityNotFoundException("Recipe with id " + id + " not found"); }
         */
    }
}
