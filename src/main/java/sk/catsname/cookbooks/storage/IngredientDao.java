package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.Ingredient;

import java.util.List;

public interface IngredientDao {
    List<Ingredient> getAllByRecipeId(long id);
    Ingredient getById(long id) throws EntityNotFoundException;
    Ingredient save(Ingredient recipe, long id) throws EntityNotFoundException;
    void delete(long id) throws EntityNotFoundException;
}
