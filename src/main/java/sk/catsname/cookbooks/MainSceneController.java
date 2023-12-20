package sk.catsname.cookbooks;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.Button;
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
import sk.catsname.cookbooks.storage.CookbookDao;
import sk.catsname.cookbooks.storage.DaoFactory;

import java.io.IOException;

public class MainSceneController {

    CookbookDao cookbookDao = DaoFactory.INSTANCE.getCookbookDao();

    @FXML
    private VBox vBox;
    @FXML
    private Button addCookbookButton;

    private CookbookFxModel cookbookModel;

    private Cookbook savedCookbook;

    @FXML
    void initialize() {

        for (Cookbook cookbook : cookbookDao.getAll()) {
            ImageView imageView = new ImageView(cookbook.getImage());
            imageView.setFitWidth(300);
            imageView.setSmooth(true);
            imageView.setPreserveRatio(true);

            imageMakeover(imageView);

            Label label = new Label(cookbook.getName());
            label.setPadding(new Insets(0, 0, 20, 0));

            imageView.setOnMouseClicked(openCookbook);
            label.setOnMouseClicked(openCookbook);

            label.setCursor(Cursor.HAND);
            imageView.setCursor(Cursor.HAND);

            // sets the user data for the image and the label to be the cookbook they symbolize
            label.setUserData(cookbook);
            imageView.setUserData(cookbook);

            vBox.getChildren().add(imageView);
            vBox.getChildren().add(label);
        }
    }

    @FXML
    void onAddCookbook(ActionEvent event) throws IOException {
        CookbookEditController controller = new CookbookEditController();
        FXMLLoader fxmlLoader = new FXMLLoader(MainScene.class.getResource("CookbookEdit.fxml"));
        fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Too Many Cookbooks");
        stage.setScene(scene);
        stage.show();
    }

    public void imageMakeover(ImageView imageView) {
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

    EventHandler<MouseEvent> openCookbook = (event) -> {
        try {
            CookbookViewController controller = new CookbookViewController();
            Node clickedNode = (Node) event.getSource(); // gets the currently clicked node
            controller.setCurrentCookbook((Cookbook) clickedNode.getUserData()); // sets the current cookbook based on the user data of the clicked node
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("CookbookView.fxml"));
            loader.setController(controller);
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(" "); // should be cookbook name
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };

}
