package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.Cookbook;

public interface CookbookDao {
    Cookbook getById(Long id) throws EntityNotFoundException;
    Cookbook save(Cookbook recipe) throws EntityNotFoundException;
    void delete(Long id) throws EntityNotFoundException;
}
