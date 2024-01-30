package sk.catsname.cookbooks;

public class ControllerKeeper {
    public static MainSceneController mainSceneController;
    public static CookbookViewController cookbookViewController;
    public static CookbookEditController cookbookEditController;
    public static RecipePickerController recipePickerController;
    public static RecipeViewController recipeViewController;
    public static RecipeEditController recipeEditController;

    public static void initialize() {
        mainSceneController = null;
        cookbookViewController = null;
        cookbookEditController = null;
        recipePickerController = null;
        recipeViewController = null;
        recipeEditController = null;
    }
}
