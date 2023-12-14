package sk.catsname.cookbooks;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.StringJoiner;

public class RecipeViewController {

    @FXML
    private ImageView recipeImageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label prepTimeLabel;

    @FXML
    private Label servingsLabel;

    @FXML
    private Label ingredientsLabel;

    @FXML
    private Label instructionsLabel;

    private RecipeFxModel recipeModel;

    private Recipe savedRecipe;

    public RecipeViewController() {
        recipeModel = new RecipeFxModel();
    }

    public void setSavedRecipe(Recipe savedRecipe) {
        this.savedRecipe = savedRecipe;
    }

    @FXML
    void initialize() {
        Image image = savedRecipe.getImage();
        recipeImageView.setImage(image);
        titleLabel.setText(savedRecipe.getName());
        prepTimeLabel.setText(String.valueOf(savedRecipe.getPreparationTime()));
        servingsLabel.setText(String.valueOf(savedRecipe.getServings()));
        StringJoiner ingredients = new StringJoiner("\n");
        for (Ingredient ingredient : savedRecipe.getIngredients()) {
            ingredients.add("• " + ingredient.toString());
        }
        ingredientsLabel.setText(String.valueOf(ingredients));
        instructionsLabel.setLayoutY(instructionsLabel.getLayoutY() + (17 * savedRecipe.getIngredients().size()));
        instructionsLabel.setText(savedRecipe.getInstructions());
    }
}
