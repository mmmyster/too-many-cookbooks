package sk.catsname.cookbooks;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainSceneController {

    @FXML
    private Button addRecipeButton;

    @FXML
    void initialize() {
        addRecipeButton.setOnAction(actionEvent -> openRecipeEditWindow());
    }

    private void openRecipeEditWindow() {
        try {
            RecipeEditController controller = new RecipeEditController();
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("RecipeEdit.fxml"));
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

}
