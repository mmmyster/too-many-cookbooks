package sk.catsname.cookbooks;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RecipeFxModel {
    Long id;
    private SimpleStringProperty name = new SimpleStringProperty();
    private ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private SimpleStringProperty preparationTime = new SimpleStringProperty();
    private SimpleStringProperty servings = new SimpleStringProperty();
    private ObservableList<Ingredient> ingredients;
    private SimpleStringProperty instructions = new SimpleStringProperty();
    private ObjectProperty<Timestamp> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<Timestamp> updatedAt = new SimpleObjectProperty<>();

    public RecipeFxModel() {
        ingredients = FXCollections.observableArrayList();
    }

    public RecipeFxModel(Recipe recipe) {
        setId(recipe.getId());
        setName(recipe.getName());
        setImage(recipe.getImage());
        setPreparationTime(recipe.getPreparationTime());
        setServings(recipe.getServings());
        ingredients = FXCollections.observableArrayList(recipe.getIngredients());
        setInstructions(recipe.getInstructions());
        setCreatedAt(recipe.getCreatedAt());
        setUpdatedAt(recipe.getUpdatedAt());
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public Image getImage() {
        return image.get();
    }

    public ObjectProperty<Image> imageProperty() {
        return image;
    }

    public void setImage(Image image) {
        this.image.set(image);
    }

    public String getPreparationTime() {
        return preparationTime.get();
    }

    public Property<String> preparationTimeProperty() {
        return preparationTime;
    }

    public void setPreparationTime(float preparationTime) {
        this.preparationTime.set(String.valueOf(preparationTime));
    }

    public String getServings() {
        return servings.get();
    }

    public Property<String> servingsProperty() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings.set(String.valueOf(servings));
    }

    public List<Ingredient> getIngredients() {
        return new ArrayList<Ingredient>(ingredients);
    }

    public ObservableList<Ingredient> ingredientsModel() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions.get();
    }

    public SimpleStringProperty instructionsProperty() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions.set(instructions);
    }

    public Timestamp getCreatedAt() {
        return createdAt.get();
    }

    public ObjectProperty<Timestamp> createdAtProperty() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt.set(createdAt);
    }

    public Timestamp getUpdatedAt() {
        return updatedAt.get();
    }

    public ObjectProperty<Timestamp> UpdatedAtProperty() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt.set(updatedAt);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime.set(preparationTime);
    }

    public void setServings(String servings) {
        this.servings.set(servings);
    }

    public void setIngredients(ObservableList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public ObjectProperty<Timestamp> updatedAtProperty() {
        return updatedAt;
    }

    public Recipe getRecipe() {
        return new Recipe(getId(), getName(), getImage(), Float.parseFloat(getPreparationTime()), Integer.parseInt(getServings()), getIngredients(), getInstructions(), getCreatedAt(), getUpdatedAt());
    }
}
