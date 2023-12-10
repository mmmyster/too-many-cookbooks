package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.Cookbook;

public interface CookbookDao {
    Cookbook getById(long id) throws EntityNotFoundException;
    Cookbook save(Cookbook recipe, long id) throws EntityNotFoundException;
    void delete(long id) throws EntityNotFoundException;
}
