package sk.catsname.cookbooks.storage;

import javafx.scene.image.Image;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import sk.catsname.cookbooks.Cookbook;
import sk.catsname.cookbooks.KimKitsuragi;
import sk.catsname.cookbooks.Recipe;

import java.sql.*;
import java.util.List;
import java.util.Objects;

public class MysqlCookbookDao implements CookbookDao {
    private JdbcTemplate jdbcTemplate;
    private RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();

    public MysqlCookbookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Cookbook> cookbookRM() { // RowMapper for "cookbook" table
        return new RowMapper<Cookbook>() {
            @Override
            public Cookbook mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String name = rs.getString("name");
                Image image = KimKitsuragi.convertBlobToImage(rs.getBlob("image"));
                List<Recipe> recipes = recipeDao.getAllByCookbookId(id);
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                return new Cookbook(id, name, image, recipes, createdAt, updatedAt);
            }
        };
    }

    @Override
    public List<Cookbook> getAll() {
        String sql = "SELECT id, name, image, created_at, updated_at " +
                "FROM cookbook";

        return jdbcTemplate.query(sql, cookbookRM());
    }

    @Override
    public Cookbook getById(Long id) throws EntityNotFoundException { // gets the cookbook based on it's recipe
        String sql = "SELECT id, name, image, created_at, updated_at " +
                "FROM cookbook " +
                "WHERE id = " + id;
        return jdbcTemplate.queryForObject(sql, cookbookRM());
    }

    @Override
    public Cookbook saveCookbook(Cookbook cookbook) throws EntityNotFoundException {
        // sets the objects that require to be null to be null
        Objects.requireNonNull(cookbook, "Cookbook cannot be null");

        if (cookbook.getId() == null) {
            String query = "INSERT INTO cookbook (name, image, created_at, updated_at) " +
                    "VALUES (?, ?, ?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, cookbook.getName());
                statement.setBlob(2, KimKitsuragi.convertImageToBlob(cookbook.getImage()));
                statement.setTimestamp(3, cookbook.getUpdatedAt());
                statement.setTimestamp(4, cookbook.getCreatedAt());
                return statement;
            }, keyHolder);
            Long id = keyHolder.getKey().longValue();

            return new Cookbook(
                    id,
                    cookbook.getName(),
                    cookbook.getImage(),
                    cookbook.getRecipes(),
                    cookbook.getCreatedAt(),
                    cookbook.getUpdatedAt()
            );
        } else {
            String query = "UPDATE cookbook SET name=?, image=?, created_at=?, updated_at=? " +
                    "WHERE id = " + cookbook.getId();

            Timestamp updateTime = cookbook.getUpdatedAt();

            int count = jdbcTemplate.update(query,
                    cookbook.getName(),
                    cookbook.getImage(),
                    cookbook.getCreatedAt(),
                    updateTime = new Timestamp(System.currentTimeMillis())
            );

            if (count == 0) {
                throw new EntityNotFoundException("Recipe with id " + cookbook.getId() + " not found");
            }

            return new Cookbook(
                    cookbook.getId(),
                    cookbook.getName(),
                    cookbook.getImage(),
                    cookbook.getRecipes(),
                    cookbook.getCreatedAt(),
                    updateTime
            );
        }
    }

    private boolean cookbookRecipeCheck(long cID, long rID) { // method for checking if there already is a recipe in such cookbook in "recipe_cookbook" table
        boolean contains = true;
        for (Recipe recipe : recipeDao.getAllByCookbookId(cID)) {
            if (recipe.getId() == rID) {
                contains = false;
                break;
            }
        }

        return contains;
    }

    public void saveRecipeCookbook(Cookbook cookbook, Recipe recipe) throws EntityNotFoundException {
        Objects.requireNonNull(cookbook, "Cookbook cannot be null");
        Objects.requireNonNull(recipe, "Recipe cannot be null");

        if (cookbookRecipeCheck(cookbook.getId(), recipe.getId())) {
            String query = "INSERT INTO recipe_cookbook (recipe_id, cookbook_id) " +
                    "VALUES (?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, recipe.getId());
                statement.setLong(2, cookbook.getId());
                return statement;
            }, keyHolder);
        }
    }

    public Cookbook save(Cookbook cookbook) throws EntityNotFoundException {
        Objects.requireNonNull(cookbook, "Cookbook cannot be null");
        Objects.requireNonNull(cookbook.getName(), "Cookbook name cannot be null");
        Objects.requireNonNull(cookbook.getCreatedAt(), "Cookbook time of creation cannot be null");
        Objects.requireNonNull(cookbook.getUpdatedAt(), "Cookbook time of last update cannot be null");

        Cookbook savedCookbook = saveCookbook(cookbook); // first a new cookbook is saved

        for (Recipe recipe : cookbook.getRecipes()) { // then it is iterated through all recipes in the cookbook...
            saveRecipeCookbook(savedCookbook, recipe); // ...and they are saved
        }

        return savedCookbook;
    }

    @Override
    public void delete(Long id) {
        deleteRecipeCookbook(id);
        deleteCookbook(id);
    }

    @Override
    public void deleteCookbook(Long id) throws EntityNotFoundException {
        String query = "DELETE FROM cookbook WHERE id = " + id;

        int count = jdbcTemplate.update(query);

        if (count == 0) {
            throw new EntityNotFoundException("Cookbook with id " + id + " not found in cookbook table");
        }
    }

    @Override
    public void deleteRecipeCookbook(Long id) throws EntityNotFoundException {
        String query = "DELETE FROM recipe_cookbook WHERE cookbook_id = " + id;

        int count = jdbcTemplate.update(query);

        if (count == 0) {
            throw new EntityNotFoundException("Cookbook with id " + id + " not found in recipe_cookbook table");
        }
    }
}
