package sk.catsname.cookbooks;

import javafx.scene.image.Image;

import java.sql.Timestamp;
import java.util.List;

public class Recipe {
    private Long id;
    private String name;
    private Image image;
    private float preparationTime;
    private int servings;
    private List<Ingredient> ingredients;
    private String instructions;
    Timestamp createdAt;
    Timestamp updatedAt;

    public Recipe() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Recipe(Long id, String name, Image image, float preparationTime, int servings, List<Ingredient> ingredients, String instructions, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.preparationTime = preparationTime;
        this.servings = servings;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public float getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(float preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Recept: " + name +
                "\nČas na prípravu: " + preparationTime + " minút" +
                "\nPre " + servings + " osôb" +
                "\nIngrediencie: " +
                "\n" + ingredients +
                "\nPostup: " + instructions +
                "\nČas vytvorenia: " + KimKitsuragi.dateFormat.format(createdAt);
    }
}
