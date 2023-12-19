package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.Recipe;

import java.util.List;

public interface RecipeDao {
    List<Recipe> getAll();
    List<Recipe> getAllByCookbookId(long id);
    Recipe getById(long id) throws EntityNotFoundException;
    Recipe save(Recipe recipe) throws EntityNotFoundException;
    void delete(long id);
    void deleteIngredientRecipe(long id) throws EntityNotFoundException;
    void deleteRecipe(long id) throws EntityNotFoundException;
    void deleteRecipeCookbook(long id) throws EntityNotFoundException;
}
