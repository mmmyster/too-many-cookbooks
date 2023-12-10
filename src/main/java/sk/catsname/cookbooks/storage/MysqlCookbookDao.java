package sk.catsname.cookbooks.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import sk.catsname.cookbooks.Cookbook;

public class MysqlCookbookDao implements CookbookDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlCookbookDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Cookbook getById(long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public Cookbook save(Cookbook recipe, long id) throws EntityNotFoundException {
        return null;
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {

    }
}
