package sk.catsname.cookbooks;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class RecipeEditController {
    @FXML
    private Button addIngredientButton;

    @FXML
    private Button addRecipeButton;

    @FXML
    private Spinner<?> amountSpinner;

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
    }

    @FXML
    void onAddIngredient(ActionEvent event) {

    }

    @FXML
    void onAddRecipe(ActionEvent event) {

    }
}
