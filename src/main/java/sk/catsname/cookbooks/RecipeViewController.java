package sk.catsname.cookbooks;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.StringJoiner;

public class RecipeViewController {

    @FXML
    private Button editRecipeButton;

    @FXML
    private HBox hBox;

    @FXML
    private Label titleLabel;

    @FXML
    private Label prepTimeLabel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Label servingsLabel;

    @FXML
    private Label ingredientsLabel;

    @FXML
    private Label instructionsLabel;

    private RecipeFxModel recipeModel;

    private Recipe currentRecipe;

    public RecipeViewController() {
        recipeModel = new RecipeFxModel();
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    @FXML
    void initialize() {
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        ImageView imageView = new ImageView(currentRecipe.getImage());
        imageView.setFitWidth(377);
        imageView.setSmooth(true);
        imageView.setPreserveRatio(true);

        imageMakeover(imageView);

        BorderPane imageViewWrapper = new BorderPane(imageView);
        imageViewWrapper.setMaxWidth(imageView.getFitWidth());
        imageViewWrapper.getStyleClass().add("image-view-wrapper");

        hBox.getChildren().add(imageViewWrapper);

        titleLabel.setText(currentRecipe.getName());
        prepTimeLabel.setText(String.valueOf(currentRecipe.getPreparationTime()) + "m");
        servingsLabel.setText(String.valueOf(currentRecipe.getServings()));
        StringJoiner ingredients = new StringJoiner("\n");
        for (Ingredient ingredient : currentRecipe.getIngredients()) {
            ingredients.add("• " + ingredient.toString());
        }
        ingredientsLabel.setText(String.valueOf(ingredients));
        instructionsLabel.setLayoutY(instructionsLabel.getLayoutY() + (17 * currentRecipe.getIngredients().size()));
        instructionsLabel.setText(currentRecipe.getInstructions());
    }

    @FXML
    void onEditRecipe(ActionEvent event) throws IOException { // TODO: edit book i had open
        RecipeEditController controller = new RecipeEditController();
        FXMLLoader fxmlLoader = new FXMLLoader(MainScene.class.getResource("RecipeEdit.fxml"));
        fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit recipe");
        stage.setScene(scene);
        stage.show();
    }

    public void imageMakeover(ImageView imageView) {
        // set a clip to apply rounded border to the original image
        Rectangle clip = new Rectangle(imageView.getFitWidth(), 277);
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

}
