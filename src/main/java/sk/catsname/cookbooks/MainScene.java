package sk.catsname.cookbooks;

import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScene extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MainSceneController controller = new MainSceneController();
        FXMLLoader fxmlLoader = new FXMLLoader(MainScene.class.getResource("MainScene.fxml"));
        fxmlLoader.setController(controller);
        Parent parent = fxmlLoader.load();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Too Many Cookbooks");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

