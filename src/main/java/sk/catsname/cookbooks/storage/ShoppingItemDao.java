package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.ShoppingItem;

public interface ShoppingItemDao {
    ShoppingItem getById(long id) throws EntityNotFoundException;
    ShoppingItem getByIngredientId(Long id) throws EntityNotFoundException;
    ShoppingItem save(ShoppingItem recipe);
    void delete(long id) throws EntityNotFoundException;
}
