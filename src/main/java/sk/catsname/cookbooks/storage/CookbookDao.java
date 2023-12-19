package sk.catsname.cookbooks.storage;

import sk.catsname.cookbooks.Cookbook;
import sk.catsname.cookbooks.Recipe;

public interface CookbookDao {
    Cookbook getById(Long id) throws EntityNotFoundException;
    Cookbook saveCookbook(Cookbook cookbook) throws EntityNotFoundException;
    void saveRecipeCookbook(Cookbook cookbook, Recipe recipe) throws EntityNotFoundException;
    Cookbook save(Cookbook cookbook) throws EntityNotFoundException;
    void delete(Long id);
    void deleteCookbook(Long id) throws EntityNotFoundException;
    void deleteRecipeCookbook(Long id) throws EntityNotFoundException;
}
