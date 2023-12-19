package sk.catsname.cookbooks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

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
    private Button deleteIngredientButton;

    @FXML
    private Button deleteRecipeButton;

    @FXML
    private TextField ingredientNameTextField;

    @FXML
    private ListView<Ingredient> ingredientsListView;

    @FXML
    private TextArea instructionsTextArea;

    @FXML
    private TextField prepTimeTextField;

    @FXML
    private TextField recipeNameTextField;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField servingsTextField;

    @FXML
    private ComboBox<String> unitComboBox;

    private RecipeFxModel recipeModel;

    public RecipeEditController() {
        recipeModel = new RecipeFxModel();
    }

    public ArrayList<String> createIngredientsNameList() {
        ArrayList<String> ingredientsNames = new ArrayList<>();
        if (recipeModel != null) {
            for (Ingredient ingredient : recipeModel.getIngredients()) {
                ingredientsNames.add(ingredient.getName());
            }
        }
        return ingredientsNames;
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

        Callback<ListView<Ingredient>, ListCell<Ingredient>> cellFactory = new Callback<>() { // for displaying ingredients as their names
            @Override
            public ListCell<Ingredient> call(ListView<Ingredient> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Ingredient ingredient, boolean empty) {
                        super.updateItem(ingredient, empty);

                        if (empty || ingredient == null) {
                            setText(null);
                        } else {
                            setText(ingredient.getName());
                        }
                    }
                };
            }
        };

        ingredientsListView.setCellFactory(cellFactory);

        FilteredList<Ingredient> filteredData = new FilteredList<>(FXCollections.observableList(recipeModel.getIngredients()), b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(ingredients -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                return ingredients.getName().toLowerCase().contains(searchKeyword);
            });
        });

        ingredientsListView.setItems(filteredData);
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
            recipeModel.setImage(new Image(file.toURI().toString(), 500, 500, true, true));
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
    void onDeleteIngredient(ActionEvent event) {
        Ingredient ingredient = ingredientsListView.getSelectionModel().getSelectedItem();
        System.out.println(ingredient); // TODO: add ability delete ingredient
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

    @FXML
    void onDeleteRecipe(ActionEvent event) {
        // TODO: add ability to delete recipe
    }
}
