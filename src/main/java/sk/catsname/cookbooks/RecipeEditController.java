package sk.catsname.cookbooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
    private TextField amountTextField;

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
    private ComboBox<String> unitComboBox;

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
        ObservableList<String> units = FXCollections.observableArrayList("ml", "l", "dl", "tsp", "tbsp", "cup", "mg", "g", "kg");
        unitComboBox.getItems().clear();
        unitComboBox.setItems(units);
    }

    @FXML
    void onAddIngredient(ActionEvent event) {
        String name = ingredientNameTextField.getText().trim();
        float amount = Float.parseFloat(amountTextField.getText().trim());
        String unit = unitComboBox.getValue();
        Ingredient ingredient = new Ingredient(name, amount, unit);
        recipeModel.ingredientsModel().add(ingredient);
        ingredientNameTextField.clear();
        amountTextField.clear();
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
