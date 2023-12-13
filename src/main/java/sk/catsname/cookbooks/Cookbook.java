package sk.catsname.cookbooks;

import javafx.scene.image.Image;

import java.sql.Timestamp;
import java.util.List;

public class Cookbook {
    private Long id;
    private String name;
    private Image image;
    private List<Recipe> recipes;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Cookbook(Long id, String name,Image image, List<Recipe> recipes, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.recipes = recipes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
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
}
