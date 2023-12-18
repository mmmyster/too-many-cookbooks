package sk.catsname.cookbooks.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import sk.catsname.cookbooks.ShoppingItem;

import java.sql.*;
import java.util.Objects;

public class MysqlShoppingItemDao implements ShoppingItemDao {
    private JdbcTemplate jdbcTemplate;

    public MysqlShoppingItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<ShoppingItem> shoppingItemRM() { // RowMapper for "shopping_item" table
        return new RowMapper<ShoppingItem>() {
            @Override
            public ShoppingItem mapRow(ResultSet rs, int rowNum) throws SQLException {
                Long id = rs.getLong("id");
                Long ingredient_id = rs.getLong("ingredient_id");
                Integer amount = rs.getInt("amount");
                String unit = rs.getString("unit");

                return new ShoppingItem(id, ingredient_id, amount, unit);
            }
        };
    }

    @Override
    public ShoppingItem getById(long id) throws EntityNotFoundException {
        String sql = "SELECT id, ingredient_id, amount, unit " +
                "FROM shopping_item WHERE id = " + id;

        return jdbcTemplate.queryForObject(sql, shoppingItemRM());
    }

    @Override
    public ShoppingItem getByIngredientId(Long id) throws EntityNotFoundException {
        String sql = "SELECT id, ingredient_id, amount, unit " +
                "FROM shopping_item " +
                "WHERE ingredient_id = " + id;

        // tries to find the ingredient_id
        try { // if the ingredient_id is found...
            return jdbcTemplate.queryForObject(sql, shoppingItemRM()); // ...returns the found shoppingItem
        } catch (EmptyResultDataAccessException e) { // if the result set is empty (no ingredient_id is found)...
            return null; // ...returns null, because there is no shoppingItem to return
        }
    }


    @Override
    public ShoppingItem save(ShoppingItem shoppingItem) {
        Objects.requireNonNull(shoppingItem, "Shopping item cannot be null");
        Objects.requireNonNull(shoppingItem.getIngredientId(), "Shopping item ingredient id cannot be null");
        Objects.requireNonNull(shoppingItem.getAmount(), "Shopping item amount cannot be null");
        Objects.requireNonNull(shoppingItem.getUnit(), "Shopping item unit cannot be null");

        if (getByIngredientId(shoppingItem.getIngredientId()) == null) { // INSERT
            String query = "INSERT INTO shopping_item (ingredient_id, amount, unit) " +
                    "VALUES (?, ?, ?)";

            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement statement = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                statement.setLong(1, shoppingItem.getIngredientId());
                statement.setInt(2, shoppingItem.getAmount());
                statement.setString(3, shoppingItem.getUnit());
                return statement;
            }, keyHolder);
            Long id = keyHolder.getKey().longValue();

            return new ShoppingItem(
                    id,
                    shoppingItem.getIngredientId(),
                    shoppingItem.getAmount(),
                    shoppingItem.getUnit()
            );
        } else { // UPDATE
            String query = "UPDATE shopping_item SET amount=?, unit=? WHERE id = " + shoppingItem.getId();
            jdbcTemplate.update(query,
                    shoppingItem.getAmount(),
                    shoppingItem.getUnit());

            return shoppingItem;
        }
    }

    @Override
    public void delete(long id) throws EntityNotFoundException {

    }
}
