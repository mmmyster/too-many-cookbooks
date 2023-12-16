package sk.catsname.cookbooks;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.sql.Timestamp;

public class CookbookFxModel {
    private Long id;
    private SimpleStringProperty name = new SimpleStringProperty();
    private ObjectProperty<Image> image = new SimpleObjectProperty<>();
    private ObservableList<Recipe> recipes;
    private ObjectProperty<Timestamp> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<Timestamp> updatedAt = new SimpleObjectProperty<>();

    public CookbookFxModel() {
        recipes = FXCollections.observableArrayList();
    }

    public CookbookFxModel(Cookbook cookbook) {
        setName(cookbook.getName());
        setImage(cookbook.getImage());
        recipes = FXCollections.observableArrayList(getRecipes());
        setCreatedAt(cookbook.getCreatedAt());
        setUpdatedAt(cookbook.getUpdatedAt());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ObservableList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ObservableList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public ObservableList<Recipe> recipesModel() {
        return recipes;
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

    public ObjectProperty<Timestamp> updatedAtProperty() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt.set(updatedAt);
    }

    public Cookbook getCookbook() {
        return new Cookbook(getId(), getName(), getImage(), getRecipes(), getCreatedAt(), getUpdatedAt());
    }
}
