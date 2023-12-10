package sk.catsname.cookbooks.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import sk.catsname.cookbooks.ShoppingItem;

public class MysqlShoppingItemDao implements ShoppingItemDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlShoppingItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public ShoppingItem getById(long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public ShoppingItem save(ShoppingItem recipe, long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {

    }
}
