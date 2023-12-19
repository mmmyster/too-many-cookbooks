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

    private Recipe currentRecipe;

    public RecipeViewController() {
        recipeModel = new RecipeFxModel();
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    @FXML
    void initialize() {
        Image image = currentRecipe.getImage();
        recipeImageView.setImage(image);
        titleLabel.setText(currentRecipe.getName());
        prepTimeLabel.setText(String.valueOf(currentRecipe.getPreparationTime()));
        servingsLabel.setText(String.valueOf(currentRecipe.getServings()));
        StringJoiner ingredients = new StringJoiner("\n");
        for (Ingredient ingredient : currentRecipe.getIngredients()) {
            ingredients.add("• " + ingredient.toString());
        }
        ingredientsLabel.setText(String.valueOf(ingredients));
        instructionsLabel.setLayoutY(instructionsLabel.getLayoutY() + (17 * currentRecipe.getIngredients().size()));
        instructionsLabel.setText(currentRecipe.getInstructions());
    }
}
