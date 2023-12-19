package sk.catsname.cookbooks;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CookbookViewController {

    @FXML
    private VBox vBox;

    private CookbookFxModel cookbookModel;

    private Cookbook savedCookbook;

    public CookbookViewController() {
        cookbookModel = new CookbookFxModel();
    }

    public void setSavedCookbook(Cookbook savedCookbook) {
        this.savedCookbook = savedCookbook;
    }

    @FXML
    void initialize() {

        for (Recipe recipe : savedCookbook.getRecipes()) {
            ImageView imageView = new ImageView(recipe.getImage());
            imageView.setFitWidth(300);
            imageView.setSmooth(true);
            imageView.setPreserveRatio(true);

            imageClipper(imageView);

            Label label = new Label(recipe.getName());
            label.setPadding(new Insets(0,0,20, 0));

            imageView.setOnMouseClicked(openRecipe);
            label.setOnMouseClicked(openRecipe);

            label.setCursor(Cursor.HAND);
            imageView.setCursor(Cursor.HAND);

            vBox.getChildren().add(imageView);
            vBox.getChildren().add(label);
        }

    }

    public void imageClipper(ImageView imageView) {
        // set a clip to apply rounded border to the original image
        Rectangle clip = new Rectangle(imageView.getFitWidth(), 200);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);

        // snapshot the rounded image
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        // remove the rounding clip so that our effect can show through
        imageView.setClip(null);

        // apply a shadow effect
        imageView.setEffect(new DropShadow(7, Color.BLACK));

        // store the rounded image in the imageView
        imageView.setImage(image);
    }

    EventHandler<MouseEvent> openRecipe = (event) -> {
        try {
            RecipeViewController controller = new RecipeViewController();
            // TODO: add requested recipe
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

}
