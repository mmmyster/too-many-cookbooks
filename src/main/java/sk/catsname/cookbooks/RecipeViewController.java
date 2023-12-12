package sk.catsname.cookbooks;

import java.io.File;
import java.net.URL;

import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RecipeViewController {

    @FXML
    private URL location;
    @FXML
    private ImageView recipeImageView;

    private RecipeFxModel recipeModel;

    public RecipeViewController() {
        recipeModel = new RecipeFxModel();
    }


    @FXML
    void initialize() {
        // TODO: get image from db
        File file = new File("");
        Image image = new Image(file.toURI().toString());
        recipeImageView.setImage(image);
    }
}
