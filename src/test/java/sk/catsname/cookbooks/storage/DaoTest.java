package sk.catsname.cookbooks.storage;

import org.junit.jupiter.api.Test;
import sk.catsname.cookbooks.Cookbook;
import sk.catsname.cookbooks.Ingredient;
import sk.catsname.cookbooks.Recipe;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class DaoTest {
    IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
    RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();
    CookbookDao cookbookDao = DaoFactory.INSTANCE.getCookbookDao();
    ShoppingItemDao shoppingItemDao = DaoFactory.INSTANCE.getShoppingItemDao();

    @Test
    void fullSaveDaoTest() throws Exception {
        // apple pie ingredients
        Ingredient flourAP = new Ingredient("flour", 500, "g");
        Ingredient waterAP = new Ingredient("water", 0.25f, "cup");
        Ingredient applesAP = new Ingredient("apple", 8, "piece");
        Ingredient cinnamonAP = new Ingredient("cinnamon", 0.5f, "tsp");

        Ingredient flourBP = new Ingredient("flour", 500, "g");
        Ingredient waterBP = new Ingredient("water", 0.25f, "cup");
        Ingredient blueberriesBP = new Ingredient("blueberry", 5, "cup");

        Ingredient flourPP = new Ingredient("flour", 500, "g");
        Ingredient waterPP = new Ingredient("water", 0.25f, "cup");
        Ingredient pearsPP = new Ingredient("pear", 8, "piece");
        Ingredient cinnamonPP = new Ingredient("cinnamon", 0.25f, "tsp");

        List<Ingredient> applePieIngredients = Arrays.asList(flourAP, waterAP, applesAP, cinnamonAP);
        List<Ingredient> blueberryPieIngredients = Arrays.asList(flourBP, waterBP, blueberriesBP);
        List<Ingredient> pearPieIngredients = Arrays.asList(flourPP, waterPP, pearsPP, cinnamonPP);

        Recipe applePie = new Recipe(
                null,
                "Apple Pie",
                null,
                120f,
                8,
                applePieIngredients,
                "make apple pie",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Recipe blueberryPie = new Recipe(
                null,
                "Blueberry Pie",
                null,
                120f,
                8,
                blueberryPieIngredients,
                "make blueberry pie",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Recipe pearPie = new Recipe(
                null,
                "Pear Pie",
                null,
                120f,
                8,
                pearPieIngredients,
                "make pear pie",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Recipe savedApplePie = recipeDao.save(applePie);
        Recipe savedBlueberryPie = recipeDao.save(blueberryPie);
        Recipe savedPearPie = recipeDao.save(pearPie);

        Recipe loadedApplePie = recipeDao.getById(savedApplePie.getId());
        Recipe loadedBlueberryPie = recipeDao.getById(savedBlueberryPie.getId());
        Recipe loadedPearPie = recipeDao.getById(savedPearPie.getId());

        // apple pie testing
        assertEquals("Apple Pie", loadedApplePie.getName());
        assertEquals(120f, loadedApplePie.getPreparationTime());
        assertEquals(8, loadedApplePie.getServings());
        assertEquals("make apple pie", loadedApplePie.getInstructions());

        // blueberry pie testing
        assertEquals("Blueberry Pie", loadedBlueberryPie.getName());
        assertEquals(120f, loadedBlueberryPie.getPreparationTime());
        assertEquals(8, loadedBlueberryPie.getServings());
        assertEquals("make blueberry pie", loadedBlueberryPie.getInstructions());

        // pear pie testing
        assertEquals("Pear Pie", loadedPearPie.getName());
        assertEquals(120f, loadedPearPie.getPreparationTime());
        assertEquals(8, loadedPearPie.getServings());
        assertEquals("make pear pie", loadedPearPie.getInstructions());

        // apple pie ingredients testing
        for (Ingredient ingredient : ingredientDao.getAllByRecipeId(loadedApplePie.getId())) {
            if ("flour".equals(ingredient.getName())) {
                assertEquals(500, ingredient.getAmount());
                assertEquals("g", ingredient.getUnit());
            }
            if ("water".equals(ingredient.getName())) {
                assertEquals(0.25f, ingredient.getAmount());
                assertEquals("cup", ingredient.getUnit());
            }
            if ("apple".equals(ingredient.getName())) {
                assertEquals(8, ingredient.getAmount());
                assertEquals("piece", ingredient.getUnit());
            }
            if ("cinnamon".equals(ingredient.getName())) {
                assertEquals(0.5f, ingredient.getAmount());
                assertEquals("tsp", ingredient.getUnit());
            }
        }

        Cookbook pies = new Cookbook(
                null,
                "Pies",
                null,
                Arrays.asList(loadedApplePie, loadedBlueberryPie, loadedPearPie),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Cookbook savedPies = cookbookDao.save(pies);

        Cookbook loadedPies = cookbookDao.getById(savedPies.getId());

        // pies cookbook testing
        assertEquals("Pies", loadedPies.getName());
    }

    @Test
    void fullUpdateDaoTest() throws Exception {
        // ramen ingredients
        Ingredient pork = new Ingredient("pork", 3, "piece");
        Ingredient water = new Ingredient("water", 1, "l");
        Ingredient soySauce = new Ingredient("soy sauce", 100, "ml");

        List<Ingredient> ramenIngredients = Arrays.asList(pork, water, soySauce);

        Recipe ramen = new Recipe(
                null,
                "Ramen",
                null,
                120f,
                1,
                ramenIngredients,
                "make ramen",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Recipe savedRamen = recipeDao.save(ramen);

        Recipe loadedRamen = recipeDao.getById(savedRamen.getId());

        Cookbook asianCuisine = new Cookbook(
                null,
                "Asian Cuisine",
                null,
                Collections.singletonList(loadedRamen),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Cookbook savedAsianCuisine = cookbookDao.save(asianCuisine);

        Cookbook loadedAsianCuisine = cookbookDao.getById(savedAsianCuisine.getId());

        // updates
        Ingredient databaseSoySauce = null;
        for (Ingredient ingredient : loadedRamen.getIngredients()) {
            if ("soy sauce".equals(ingredient.getName())) {
                databaseSoySauce = ingredient;
            }
        }
        assert databaseSoySauce != null;
        Ingredient updatedSoySauce = new Ingredient(databaseSoySauce.getId(), "soy sauce", 1f, "l"); // we update soy sauce
        Ingredient springOnion = new Ingredient("spring onion", 1, "piece");
        Ingredient databaseWater = null;
        for (Ingredient ingredient : loadedRamen.getIngredients()) {
            if ("water".equals(ingredient.getName())) {
                databaseWater = ingredient;
            }
        }

        List<Ingredient> newRamenIngredients = Arrays.asList(databaseWater, updatedSoySauce, springOnion); // we removed pork, changed soy sauce and added spring onion

        Recipe newRamen = new Recipe(
                loadedRamen.getId(),
                "New Ramen",
                null,
                120f,
                1,
                newRamenIngredients,
                "make ramen",
                ramen.getCreatedAt(),
                ramen.getUpdatedAt()
        );

        Recipe savedNewRamen = recipeDao.save(newRamen); // we save the updated ramen

        Recipe loadedNewRamen = recipeDao.getById(savedNewRamen.getId());

        assertEquals("New Ramen", loadedNewRamen.getName());

        for (Ingredient ingredient : ingredientDao.getAllByRecipeId(loadedNewRamen.getId())) {
            if ("pork".equals(ingredient.getName())) {
                throw new Exception("pork shouldn't be in the recipe anymore");
            }
            if ("water".equals(ingredient.getName())) {
                assertEquals(1, ingredient.getAmount());
                assertEquals("l", ingredient.getUnit());
            }
            if ("soy sauce".equals(ingredient.getName())) {
                assertEquals(1, ingredient.getAmount());
                assertEquals("l", ingredient.getUnit());
            }
            if ("spring onion".equals(ingredient.getName())) {
                assertEquals(1, ingredient.getAmount());
                assertEquals("piece", ingredient.getUnit());
            }
        }

        Recipe ramen2 = new Recipe(
                null,
                "Ramen 2",
                null,
                120f,
                1,
                null,
                "make ramen",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Recipe savedRamen2 = recipeDao.save(ramen2);

        Recipe loadedRamen2 = recipeDao.getById(savedRamen2.getId());

        Cookbook newAsianCuisine = new Cookbook(
                loadedAsianCuisine.getId(),
                "Ramens",
                null,
                Arrays.asList(loadedNewRamen, loadedRamen2),
                asianCuisine.getCreatedAt(),
                asianCuisine.getUpdatedAt()
        );

        Cookbook savedNewAsianCuisine = cookbookDao.save(newAsianCuisine);

        Cookbook loadedNewAsianCuisine = cookbookDao.getById(savedNewAsianCuisine.getId());

        assertEquals("Ramens", loadedNewAsianCuisine.getName());
        assertEquals(2, loadedNewAsianCuisine.getRecipes().size());
    }

    @Test
    void fullDeleteDaoTest() throws SQLException {
        Ingredient flourPM = new Ingredient("flour", 400, "g");
        Ingredient mozzarellaPM = new Ingredient("mozzarella", 10, "piece");
        Ingredient cheesePM = new Ingredient("cheese", 0.5f, "piece");

        Ingredient flourPO = new Ingredient("flour", 400, "g");
        Ingredient olivesPO = new Ingredient("olive", 20, "piece");
        Ingredient cheesePO = new Ingredient("cheese", 0.5f, "piece");

        List<Ingredient> margaritaPizzaIngredients = Arrays.asList(flourPM, mozzarellaPM, cheesePM);
        List<Ingredient> olivePizzaIngredients = Arrays.asList(flourPO, olivesPO, cheesePO);

        Recipe margaritaPizza = new Recipe(
                null,
                "Pizza Margarita",
                null,
                30f,
                8,
                margaritaPizzaIngredients,
                "make pizza",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Recipe olivePizza = new Recipe(
                null,
                "Olive Pizza",
                null,
                30f,
                8,
                olivePizzaIngredients,
                "make pizza",
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Recipe savedMargaritaPizza = recipeDao.save(margaritaPizza);
        Recipe savedOlivePizza = recipeDao.save(olivePizza);

        Recipe loadedMargaritaPizza = recipeDao.getById(savedMargaritaPizza.getId());
        Recipe loadedOlivePizza = recipeDao.getById(savedOlivePizza.getId());

        Cookbook pizzas = new Cookbook(
                null,
                "Pizzas",
                null,
                Arrays.asList(savedMargaritaPizza, savedOlivePizza),
                new Timestamp(System.currentTimeMillis()),
                new Timestamp(System.currentTimeMillis())
        );

        Cookbook savedPizzas = cookbookDao.save(pizzas);

        recipeDao.delete(savedMargaritaPizza.getId());

        cookbookDao.delete(savedPizzas.getId());
    }
}