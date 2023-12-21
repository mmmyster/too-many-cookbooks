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
import javafx.stage.Stage;
import javafx.util.Callback;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class RecipePickerController {

    RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();

    @FXML
    private Button addRecipeButton;

    @FXML
    private ListView<Recipe> allRecipesListView;

    @FXML
    private Button newRecipeButton;

    @FXML
    private Button searchButton;

    @FXML
    private TextField searchTextField;

    public CookbookFxModel cookbookModel;

    public RecipePickerController() {
        cookbookModel = new CookbookFxModel();
    }

    public ArrayList<String> createRecipeNameList() {
        ArrayList<String> recipesNames = new ArrayList<>();
        for (Recipe recipe : recipeDao.getAll()) {
            recipesNames.add(recipe.getName());

        }
        return recipesNames;
    }

    @FXML
    void initialize() throws FileNotFoundException {
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

        allRecipesListView.setCellFactory(cellFactory);

        FilteredList<Recipe> filteredData = new FilteredList<>(FXCollections.observableList(recipeDao.getAll()), b -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(recipes -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                return recipes.getName().toLowerCase().contains(searchKeyword);
            });
        });

        allRecipesListView.setItems(filteredData);
    }

    @FXML
    void onAddRecipe(ActionEvent event) {
        Recipe recipe = allRecipesListView.getSelectionModel().getSelectedItem();
        cookbookModel.recipesModel().add(recipe);
        System.out.println(recipe);
    }

    @FXML
    void onNewRecipeButton(ActionEvent event) throws IOException {
        // close the current edit window
        Stage currentStage = (Stage) addRecipeButton.getScene().getWindow();
        currentStage.close();

        RecipeEditController controller = new RecipeEditController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RecipeEdit.fxml"));
        fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Too Many Cookbooks");
        stage.setScene(scene);
        stage.show();
    }

}
