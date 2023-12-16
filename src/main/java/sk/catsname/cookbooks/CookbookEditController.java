package sk.catsname.cookbooks;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import sk.catsname.cookbooks.storage.CookbookDao;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CookbookEditController {
    RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();
    @FXML
    private Button addCookbookButton;

    @FXML
    private Button addImageButton;

    @FXML
    private TextField cookbookNameTextField;

    @FXML
    private ListView<String> recipeListView;

    @FXML
    private TextField searchTextField;

    private CookbookFxModel cookbookModel;

    public CookbookEditController() {
        cookbookModel = new CookbookFxModel();
    }

    public ArrayList<String> createRecipeList() {
        ArrayList<String> recipesNames = new ArrayList<>();
        if (cookbookModel != null) {
            for (Recipe recipe : recipeDao.getAll()) {
                recipesNames.add(recipe.getName());
            }
        }
        return recipesNames;
    }

    @FXML
    void initialize() {
        cookbookNameTextField.textProperty().bindBidirectional(cookbookModel.nameProperty());

        FilteredList<String> filteredData = new FilteredList<>(FXCollections.observableList(createRecipeList()), b -> true);
        recipeListView.setItems(filteredData);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(recipes -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                return recipes.toLowerCase().contains(searchKeyword);
            });
        });

        recipeListView.setItems(filteredData);

    }

    public void addRecipe(){
        String recipe = recipeListView.getSelectionModel().getSelectedItem();
//        cookbookModel.recipesModel().add(recipe);
        System.out.println(recipe);
    }

    @FXML
    void onAddCookbook(ActionEvent event) {
        Cookbook cookbook = cookbookModel.getCookbook();
        CookbookDao cookbookDao = DaoFactory.INSTANCE.getCookbookDao();
        cookbook.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        cookbook.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
//        savedCookbook = cookbookDao.save(cookbook);
//        Cookbook loadCookbook = cookbookDao.getById(savedCookbook.getId());
//        System.out.println(loadCookbook);
//
//        try {
//            CookbookViewController controller = new CookbookViewController();
//            controller.setSavedCookbook(loadCookbook);
//            FXMLLoader loader = new FXMLLoader(
//                    getClass().getResource("CookbookView.fxml"));
//            loader.setController(controller);
//            Parent parent = loader.load();
//            Scene scene = new Scene(parent);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.setTitle("Cookbook");
//            stage.show();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
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
            cookbookModel.setImage(new Image(file.toURI().toString()));
            System.out.println("Vybratý súbor: " + file); // TODO: test output, remove later
        }
    }

}
