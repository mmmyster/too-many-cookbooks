package sk.catsname.cookbooks.storage;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import sk.catsname.cookbooks.Ingredient;

public enum DaoFactory {
    INSTANCE;

    private MysqlRecipeDao recipeDao;
    private MysqlCookbookDao cookbookDao;
    private MysqlIngredientDao ingredientDao;
    private MysqlShoppingItemDao shoppingItemDao;

    private JdbcTemplate jdbcTemplate;

    private JdbcTemplate getJddbcTemplate() {
        if (jdbcTemplate == null) {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("root");
            dataSource.setPassword("root");
            dataSource.setUrl("jdbc:mysql://localhost:3306/too_many_cookbooks");
            jdbcTemplate = new JdbcTemplate(dataSource);
        }

        return jdbcTemplate;
    }

    public RecipeDao getRecipeDao() {
        if (recipeDao == null) {
            recipeDao = new MysqlRecipeDao(getJddbcTemplate());
        }
        return recipeDao;
    }

    public CookbookDao getCookbookDao() {
        if (cookbookDao == null) {
            cookbookDao = new MysqlCookbookDao(getJddbcTemplate());
        }
        return cookbookDao;
    }

    public IngredientDao getIngredientDao() {
        if (ingredientDao == null) {
            ingredientDao = new MysqlIngredientDao(getJddbcTemplate());
        }
        return ingredientDao;
    }

    public ShoppingItemDao getShoppingItemDao() {
        if (shoppingItemDao == null) {
            shoppingItemDao = new MysqlShoppingItemDao(getJddbcTemplate());
        }
        return shoppingItemDao;
    }
}
