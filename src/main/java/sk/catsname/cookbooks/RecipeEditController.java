package sk.catsname.cookbooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

public class RecipeEditController {
    RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();
    Recipe savedRecipe;

    @FXML
    private Button addImageButton;

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
    void onAddImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        // set extension filter
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterJpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterPng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterJpg, extFilterPNG, extFilterPng);

        // show open file dialog
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            recipeModel.setImage(new Image(file.toURI().toString()));
            System.out.println("Vybratý súbor: " + file); // TODO: test output, remove later
        }
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
        Recipe loadedRecipe = recipeDao.getById(savedRecipe.getId());
        System.out.println(loadedRecipe);

        try {
            RecipeViewController controller = new RecipeViewController();
            controller.setCurrentRecipe(loadedRecipe);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("RecipeView.fxml"));
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Recipe");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
