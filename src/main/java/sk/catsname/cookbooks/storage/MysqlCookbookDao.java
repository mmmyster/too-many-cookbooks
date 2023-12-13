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
                List<Recipe> recipes = null; // TODO: load recipes
                Timestamp createdAt = rs.getTimestamp("created_at");
                Timestamp updatedAt = rs.getTimestamp("updated_at");

                return new Cookbook(id, name, image, recipes, createdAt, updatedAt);
            }
        };
    }

    @Override
    public Cookbook getById(Long id) throws EntityNotFoundException { // gets the cookbook based on it's recipe
        String sql = "SELECT id, name, image, created_at, updated_at " +
                "FROM cookbook " +
                "WHERE id = " + id;
        return jdbcTemplate.queryForObject(sql, cookbookRM());
    }

    @Override
    public Cookbook save(Cookbook cookbook) throws EntityNotFoundException {
        // sets the objects that require to be null to be null
        Objects.requireNonNull(cookbook, "Cookbook cannot be null");
        Objects.requireNonNull(cookbook.getName(), "Cookbook name cannot be null");
        Objects.requireNonNull(cookbook.getCreatedAt(), "Cookbook time of creation cannot be null");
        Objects.requireNonNull(cookbook.getUpdatedAt(), "Cookbook time of last update cannot be null");

        if (cookbook.getId() == null) {
            String query = "INSERT INTO recipe (name, image, created_at, updated_at) " +
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
            return null; // TODO: UPDATING WHEN SAVING
        }
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

    }
}
