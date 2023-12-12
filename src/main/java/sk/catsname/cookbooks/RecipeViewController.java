package sk.catsname.cookbooks;

import java.io.File;
import java.net.URL;

import javafx.fxml.FXML;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RecipeViewController {

    private Recipe savedRecipe;

    @FXML
    private URL location;
    @FXML
    private ImageView recipeImageView;

    private RecipeFxModel recipeModel;

    public RecipeViewController() {
        recipeModel = new RecipeFxModel();
    }

    public void setSavedRecipe(Recipe savedRecipe) {
        this.savedRecipe = savedRecipe;
    }

    @FXML
    void initialize() {
        //File file = new File("");
        Image image = savedRecipe.getImage();//new Image(file.toURI().toString());
        recipeImageView.setImage(image);
    }
}
