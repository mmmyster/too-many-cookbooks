package sk.catsname.cookbooks;

public class ShoppingItem {
    private Long id;
    private Long ingredientId;
    private Integer amount;
    private String unit;

    public ShoppingItem(Long id, Long ingredientId, Integer amount, String unit) {
        this.id = id;
        this.ingredientId = ingredientId;
        this.amount = amount;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
