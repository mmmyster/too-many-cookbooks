package sk.catsname.cookbooks;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.sql.Timestamp;

public class RecipeEditController {
    Recipe savedRecipe;

    @FXML
    private Button addIngredientButton;

    @FXML
    private Button addRecipeButton;

    @FXML
    private Spinner<?> ingredientAmountSpinner;

    @FXML
    private TextField ingredientNameTextField;

    @FXML
    private TextArea instructionsTextArea;

    @FXML
    private TextField prepTimeTextField;

    @FXML
    private TextField recipeNameTextField;

    @FXML
    private TextField servingsTextField;

    @FXML
    private ComboBox<?> unitComboBox;

    private RecipeFxModel recipeModel;

    public RecipeEditController() {
        recipeModel = new RecipeFxModel();
    }

    @FXML
    public void initialize() {
        recipeNameTextField.textProperty().bindBidirectional(recipeModel.nameProperty());
        instructionsTextArea.textProperty().bindBidirectional(recipeModel.instructionsProperty());
        prepTimeTextField.textProperty().bindBidirectional(recipeModel.preparationTimeProperty());
        servingsTextField.textProperty().bindBidirectional(recipeModel.servingsProperty());
    }

    @FXML
    void onAddIngredient(ActionEvent event) {
        String name = ingredientNameTextField.getText().trim();
        Ingredient ingredient = new Ingredient(name);
        recipeModel.ingredientsModel().add(ingredient);
    }

    @FXML
    void onAddRecipe(ActionEvent event) {
        Recipe recipe = recipeModel.getRecipe();
        RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();
        recipe.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        recipe.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        savedRecipe = recipeDao.save(recipe);
        System.out.println(recipe);
    }
}
