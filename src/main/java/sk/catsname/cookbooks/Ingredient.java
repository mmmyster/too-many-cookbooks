package sk.catsname.cookbooks;

public class Ingredient {
    private String name;
    private float amount;
    private String unit;

    public Ingredient() {
    }

    public Ingredient(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
