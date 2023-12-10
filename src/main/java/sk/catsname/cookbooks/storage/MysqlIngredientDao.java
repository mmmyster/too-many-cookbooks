package sk.catsname.cookbooks.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import sk.catsname.cookbooks.Ingredient;

import java.util.List;

public class MysqlIngredientDao implements IngredientDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlIngredientDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Ingredient> getAllByRecipeId(long id) {
        return null;
    }

    @Override
    public Ingredient getById(long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Ingredient save(Ingredient recipe, long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {

    }
}
