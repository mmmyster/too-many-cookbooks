package sk.catsname.cookbooks;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import sk.catsname.cookbooks.storage.CookbookDao;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CookbookEditController {

    RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();

    CookbookDao cookbookDao = DaoFactory.INSTANCE.getCookbookDao();

    @FXML
    private Button addCookbookButton;

    @FXML
    private Button addImageButton;

    @FXML
    private Button newRecipeButton;

    @FXML
    private TextField cookbookNameTextField;

    @FXML
    private Button deleteCookbookButton;

    @FXML
    private Button deleteRecipeButton;

    @FXML
    private ListView<Recipe> recipeListView;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    private CookbookFxModel cookbookModel;
    public Cookbook currentCookbook;

    public CookbookEditController() {
        cookbookModel = new CookbookFxModel();
    }

    public ArrayList<String> createRecipeNameList() {
        ArrayList<String> recipesNames = new ArrayList<>();
        if (cookbookModel != null) {
            for (Recipe recipe : cookbookModel.getRecipes()) {
                recipesNames.add(recipe.getName());
            }
        }
        return recipesNames;
    }

    @FXML
    void initialize() throws FileNotFoundException {
        if (currentCookbook != null) {
            cookbookModel = new CookbookFxModel(currentCookbook);
        }

        cookbookNameTextField.textProperty().bindBidirectional(cookbookModel.nameProperty());

        Image image = new Image(new FileInputStream("src/main/resources/sk/catsname/cookbooks/magnifying-glass-solid.png"));
        ImageView view = new ImageView(image);
        view.setFitWidth(16);
        view.setPreserveRatio(true);
        searchButton.setGraphic(view);

        Callback<ListView<Recipe>, ListCell<Recipe>> cellFactory = new Callback<>() { // for displaying recipes as their names
            @Override
            public ListCell<Recipe> call(ListView<Recipe> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Recipe recipe, boolean empty) {
                        super.updateItem(recipe, empty);

                        if (empty || recipe == null) {
                            setText(null);
                        } else {
                            setText(recipe.getName());
                        }
                    }
                };
            }
        };

        recipeListView.setCellFactory(cellFactory);

        FilteredList<Recipe> filteredData = new FilteredList<>(FXCollections.observableList(cookbookModel.getRecipes()), b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(recipes -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                return recipes.getName().toLowerCase().contains(searchKeyword);
            });
        });

        recipeListView.setItems(filteredData);
    }

    @FXML
    void onAddCookbook(ActionEvent event) throws SQLException {
        CookbookDao cookbookDao = DaoFactory.INSTANCE.getCookbookDao();
        Cookbook modelCookbook = cookbookModel.getCookbook();
        modelCookbook.setId(currentCookbook.getId());
        Cookbook savedCookbook = cookbookDao.save(modelCookbook);

        try {
            CookbookViewController controller = new CookbookViewController();
            controller.setCurrentCookbook(savedCookbook);
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("CookbookView.fxml"));
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Cookbook");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // close the current edit window
        Stage currentStage = (Stage) addCookbookButton.getScene().getWindow();
        currentStage.close();
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
            cookbookModel.setImage(new Image(file.toURI().toString(), 500, 500, true, true));
        }
    }

    @FXML
    void onNewRecipe(ActionEvent event) throws IOException {
        RecipePickerController controller = new RecipePickerController();
        controller.cookbookModel = this.cookbookModel;
        FXMLLoader fxmlLoader = new FXMLLoader(MainScene.class.getResource("RecipePicker.fxml"));
        fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Pick recipes");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onDeleteCookbook(ActionEvent event) {
        cookbookDao.delete(currentCookbook.getId());
    }

    @FXML
    void onDeleteRecipe(ActionEvent event) {
        Recipe recipe = recipeListView.getSelectionModel().getSelectedItem();
        CookbookDao cookbookDao = DaoFactory.INSTANCE.getCookbookDao();
        cookbookDao.deleteRecipeCookbook(recipe.getId(), currentCookbook.getId());
        cookbookModel.getRecipes().remove(recipe);
    }
}
