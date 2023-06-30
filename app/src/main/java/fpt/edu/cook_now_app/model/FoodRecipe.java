package fpt.edu.cook_now_app.model;

import java.util.List;

public class FoodRecipe {
    private int id;
    private String name;
    private String image;
    private List<String> ingredients;
    private List<String> steps;
    private int typeId;

    public FoodRecipe() {
        // Default constructor required for Firebase
    }

    public FoodRecipe(int id, String name, String image, List<String> ingredients, List<String> steps, int typeId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
        this.typeId = typeId;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
