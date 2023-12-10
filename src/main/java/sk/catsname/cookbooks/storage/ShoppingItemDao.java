package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.ShoppingItem;

public interface ShoppingItemDao {
    ShoppingItem getById(long id) throws EntityNotFoundException;
    ShoppingItem save(ShoppingItem recipe, long id) throws EntityNotFoundException;
    void delete(long id) throws EntityNotFoundException;
}
