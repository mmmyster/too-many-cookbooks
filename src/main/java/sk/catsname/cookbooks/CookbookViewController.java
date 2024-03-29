package sk.catsname.cookbooks;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sk.catsname.cookbooks.storage.DaoFactory;
import sk.catsname.cookbooks.storage.RecipeDao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CookbookViewController {

    @FXML
    private Button editCookbookButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox vBox;

    private CookbookFxModel cookbookModel;

    private Cookbook currentCookbook;

    private List<HBox> recipeHBoxes;

    public CookbookViewController() {
        cookbookModel = new CookbookFxModel();
    }

    public void setCurrentCookbook(Cookbook currentCookbook) {
        this.currentCookbook = currentCookbook;
    }

    @FXML
    void initialize() {
        ControllerKeeper.cookbookViewController = this;

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        recipeHBoxes = new ArrayList<HBox>();

        updateRecipes();
    }

    @FXML
    void onEditCookbook(ActionEvent event) throws IOException {
        CookbookEditController controller = new CookbookEditController();
        controller.currentCookbook = currentCookbook;
        FXMLLoader fxmlLoader = new FXMLLoader(MainScene.class.getResource("CookbookEdit.fxml"));
        fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit cookbook");
        stage.setScene(scene);
        stage.show();

        // close the current view window
        Stage currentStage = (Stage) editCookbookButton.getScene().getWindow();
        currentStage.close();
    }

    public void imageMakeover(ImageView imageView) {
        // set a clip to apply rounded border to the original image
        Rectangle clip = new Rectangle(imageView.getFitWidth(), 100);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

        // snapshot the rounded image
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        // remove the rounding clip so that our effect can show through
        imageView.setClip(null);

        // store the rounded image in the imageView
        imageView.setImage(image);
    }

    EventHandler<MouseEvent> openRecipe = (event) -> {
        try {
            RecipeViewController controller = new RecipeViewController();
            Node clickedNode = (Node) event.getSource(); // gets the currently clicked node
            controller.setCurrentRecipe((Recipe) clickedNode.getUserData()); // sets the current recipe based on the user data of the clicked node
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("RecipeView.fxml"));
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(" "); // should be recipe name
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

    public void updateRecipes() {
        // removes all recipes
        if (!recipeHBoxes.isEmpty()) {
            VBox root = (VBox) recipeHBoxes.get(0).getParent();
            for (HBox recipeHBox : recipeHBoxes) {
                root.getChildren().remove(recipeHBox);
            }
            recipeHBoxes.clear();
        }

        // adds the recipes that are in database
        RecipeDao recipeDao = DaoFactory.INSTANCE.getRecipeDao();
        for (Recipe recipe : recipeDao.getAllByCookbookId(currentCookbook.getId())) {
            ImageView imageView = new ImageView(recipe.getImage());
            imageView.setFitWidth(150);
            imageView.setSmooth(true);
            imageView.setPreserveRatio(true);

            imageMakeover(imageView);

            BorderPane imageViewWrapper = new BorderPane(imageView);
            imageViewWrapper.setMaxWidth(imageView.getFitWidth());
            imageViewWrapper.getStyleClass().add("image-view-wrapper");

            Label label = new Label(recipe.getName());
            label.setPadding(new Insets(0, 0, 20, 0));

            imageView.setOnMouseClicked(openRecipe);
            label.setOnMouseClicked(openRecipe);

            label.setCursor(Cursor.HAND);
            imageView.setCursor(Cursor.HAND);

            // sets the user data for the image and the label to be the recipe they symbolize
            label.setUserData(recipe);
            imageView.setUserData(recipe);

            HBox hBox = new HBox(imageViewWrapper, label);
            hBox.setMaxHeight(imageViewWrapper.getMaxHeight());
            hBox.getStyleClass().add("wrapper");
            hBox.getStyleClass().add("hbox");
            vBox.getChildren().add(hBox);
            recipeHBoxes.add(hBox);
        }
    }
}
