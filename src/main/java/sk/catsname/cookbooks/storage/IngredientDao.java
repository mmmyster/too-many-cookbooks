package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.Ingredient;
import sk.catsname.cookbooks.Recipe;

import java.util.List;

public interface IngredientDao {
    List<Ingredient> getAllByRecipeId(Long id);
    Ingredient getByName(String name);
    Ingredient getById(Long id) throws EntityNotFoundException;
    void saveRecipeIngredient(Ingredient ingredient, Recipe recipe);
    Ingredient saveIngredient(Ingredient ingredient);
    void save(Ingredient ingredient, Recipe recipe) throws EntityNotFoundException;
    void delete(long id);
    void deleteIngredientRecipe(long id) throws EntityNotFoundException;
    void deleteIngredient(long id) throws EntityNotFoundException;
}
