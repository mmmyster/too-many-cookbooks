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
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.IngredientDao;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private ScrollPane scrollPane;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField servingsTextField;

    @FXML
    private ComboBox<String> unitComboBox;

    private RecipeFxModel recipeModel;

    public Recipe editedRecipe;

    public boolean isFromPicker = false;

    public CookbookFxModel cookbookModel = null;

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
    public void initialize() throws FileNotFoundException {
        ControllerKeeper.recipeEditController = this;

        if (editedRecipe != null) { // if the recipe id is not null (we have an existing recipe)
            recipeModel = new RecipeFxModel(editedRecipe); // we set the model according to its recipe so the edit is already filled in with recipe's data
        }

        /*
        recipeModel.setId(editedRecipe.getId());
        recipeModel.setName(editedRecipe.getName());
        recipeModel.setImage(editedRecipe.getImage());
        recipeModel.setPreparationTime(editedRecipe.getPreparationTime());
        recipeModel.setServings(editedRecipe.getServings());
        recipeModel.setIngredients(FXCollections.observableList(editedRecipe.getIngredients()));
        recipeModel.setInstructions(editedRecipe.getInstructions());
        recipeModel.setCreatedAt(editedRecipe.getCreatedAt());
        recipeModel.setUpdatedAt(editedRecipe.getUpdatedAt());
         */

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Image image = new Image(new FileInputStream("src/main/resources/sk/catsname/cookbooks/magnifying-glass-solid.png"));
        ImageView view = new ImageView(image);
        view.setFitWidth(16);
        view.setPreserveRatio(true);
        searchButton.setGraphic(view);

        recipeNameTextField.textProperty().bindBidirectional(recipeModel.nameProperty());
        instructionsTextArea.textProperty().bindBidirectional(recipeModel.instructionsProperty());
        prepTimeTextField.textProperty().bindBidirectional(recipeModel.preparationTimeProperty());
        servingsTextField.textProperty().bindBidirectional(recipeModel.servingsProperty());
        ObservableList<String> units = FXCollections.observableArrayList("ml", "l", "tsp", "tbsp", "cup", "g", "kg", "piece");
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
        System.out.println(recipeModel.getId());
        System.out.println(recipeModel.getName());
        System.out.println(recipeModel.getIngredients());
        System.out.println(recipeModel.getCreatedAt());
        System.out.println(recipeModel.getUpdatedAt());
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

        updateIngredients();
    }

    @FXML
    void onDeleteIngredient(ActionEvent event) {
        Ingredient ingredient = ingredientsListView.getSelectionModel().getSelectedItem();
        IngredientDao ingredientDao = DaoFactory.INSTANCE.getIngredientDao();
        if(ingredient.getId() != null) {
            ingredientDao.deleteIngredientRecipe(ingredient.getId());
            removeIngredient(ingredient);
        } else {
            System.out.println(recipeModel.getIngredients());
            removeIngredient(ingredient);
            System.out.println(recipeModel.getIngredients());
        }

        updateIngredients();
    }

    @FXML
    void onAddRecipe(ActionEvent event) throws SQLException {
        Recipe recipe = recipeModel.getRecipe();
        RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();
        recipe.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        recipe.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        savedRecipe = recipeDao.save(recipe);
        System.out.println(savedRecipe);

        // close the current edit window
        Stage currentStage = (Stage) addRecipeButton.getScene().getWindow();
        currentStage.close();

        if (isFromPicker) { // if the currently edited recipe is from the picker, we don't display the recipe, but we display picker
            isFromPicker = false; // we set the isFromPicker to false, so it can be later used regularly
            try {
                RecipePickerController controller = new RecipePickerController();
                controller.cookbookModel = cookbookModel;
                FXMLLoader fxmlLoader = new FXMLLoader(MainScene.class.getResource("RecipePicker.fxml"));
                fxmlLoader.setController(controller);
                Parent parent = fxmlLoader.load();
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Pick recipes");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else { // otherwise we display the recipe as usual
            try {
                RecipeViewController controller = new RecipeViewController();
                controller.setCurrentRecipe(savedRecipe);
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

        if (ControllerKeeper.cookbookViewController != null) ControllerKeeper.cookbookViewController.updateRecipes(); // and we update the recipes in the cookbook view
    }

    @FXML
    void onDeleteRecipe(ActionEvent event) {
        recipeDao.delete(editedRecipe.getId());

        ControllerKeeper.cookbookViewController.updateRecipes();

        // close the current edit window
        Stage currentStage = (Stage) addRecipeButton.getScene().getWindow();
        currentStage.close();
    }

    private void updateIngredients() { // method for updating the ingredients seen on screen
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

    private void removeIngredient(Ingredient ingredient) { // method for removing ingredients from an observable list of ingredients (for more code clarity)
        List<Ingredient> ingredients = recipeModel.getIngredients(); // gets all the ingredients
        for (Ingredient recipeModelIngredient : ingredients) { // goes through each ingredient
            if (recipeModelIngredient.getName().equals(ingredient.getName())) { // if the ingredients match ...
                ingredients.remove(recipeModelIngredient); // ... the matched ingredient is removed ...
                break; // ... and the loop ends to avoid useless looping afterward
            }
        }
        recipeModel.setIngredients(FXCollections.observableArrayList(ingredients)); // ingredients are set to a new list of ingredients (the one where the ingredient is removed)
    }
}
