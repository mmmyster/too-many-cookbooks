package sk.catsname.cookbooks.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import sk.catsname.cookbooks.Ingredient;
import sk.catsname.cookbooks.Recipe;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;

public class MysqlIngredientDao implements IngredientDao {
    private JdbcTemplate jdbcTemplate;
    private Ingredient savedIngredient = new Ingredient();

    public MysqlIngredientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Ingredient> ingredientRM() { // RowMapper for "ingredient" table
        return new RowMapper<Ingredient>() {
            @Override
            public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                String name = rs.getString("name");

                return new Ingredient(id, name);
            }
        };
    }

    private RowMapper<Ingredient> ingredientRecipeRM() { // RowMapper for "ingredient_recipe" table
        return new RowMapper<Ingredient>() {
            @Override
            public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long ingredientId = rs.getLong("ingredient_id");
                Float amount = rs.getFloat("amount");
                String unit = rs.getString("unit");

                return new Ingredient(ingredientId, null, amount, unit);
            }
        };
    }

    @Override
    public List<Ingredient> getAllByRecipeId(Long id) { // gets all ingredients based on recipe id
        String sql = "SELECT i.id, i.name, ir.amount, ir.unit " +
                "FROM ingredient AS i " +
                "LEFT JOIN ingredient_recipe AS ir ON i.id = ir.ingredient_id " +
                "WHERE ir.ingredient_id = " + id;

        return jdbcTemplate.query(sql, ingredientRecipeRM());
    }

    @Override
    public Ingredient getById(Long id) throws EntityNotFoundException { // gets a single ingredient based on it's id
        String sql = "SELECT id, name FROM ingredient WHERE id = " + id;

        return jdbcTemplate.queryForObject(sql, ingredientRM());
    }

    @Override
    public void saveRecipeIngredient(Ingredient ingredient, Recipe recipe) {
        Objects.requireNonNull(ingredient, "Ingredient cannot be null");
        Objects.requireNonNull(ingredient.getAmount(), "Ingredient amount cannot be null");
        Objects.requireNonNull(ingredient.getUnit(), "Ingredient unit cannot be null");

        if (!getAllByRecipeId(recipe.getId()).contains(ingredient)) { // INSERT
            String query = "INSERT INTO ingredient_recipe (ingredient_id, recipe_id,amount, unit) VALUES (?, ?, ?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, ingredient.getId());
                statement.setLong(2, recipe.getId());
                statement.setFloat(3, ingredient.getAmount());
                statement.setString(4, ingredient.getUnit());
                return statement;
            }, keyHolder);
            //Long id = keyHolder.getKey().longValue();

            /*
            return new Ingredient(
                    id,
                    ingredient.getName(),
                    ingredient.getAmount(),
                    ingredient.getUnit()
            );
             */
        } else { // UPDATE TODO

        }
    }

    @Override
    public Ingredient saveIngredient(Ingredient ingredient) { // saves the ingredient into the "ingredient" table
        Objects.requireNonNull(ingredient, "Ingredient cannot be null");

        if (ingredient.getId() == null) { // INSERT
            String query = "INSERT INTO ingredient (name) VALUES (?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, ingredient.getName());
                return statement;
            }, keyHolder);
            Long id = keyHolder.getKey().longValue();

            return new Ingredient(
                    id,
                    ingredient.getName(),
                    ingredient.getAmount(),
                    ingredient.getUnit()
            );
        } else { // UPDATE TODO
            return null;
        }
    }

    @Override
    public void save(Ingredient ingredient, Recipe recipe) throws EntityNotFoundException { // saves the ingredient
        savedIngredient = saveIngredient(ingredient); // first it saves a new ingredient into the ingredient table
        saveRecipeIngredient(savedIngredient, recipe); // after that it saves the ingredient into ingredient_recipe table
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {

    }
}
