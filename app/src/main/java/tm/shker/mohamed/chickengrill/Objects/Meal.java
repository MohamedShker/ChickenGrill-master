package tm.shker.mohamed.chickengrill.Objects;

import java.io.Serializable;

/**
 * Created by mohamed on 30/09/2016.
 * pojo
 */
public class Meal implements Serializable {
    private String MealName;
    private String MealType;
    private String MealIngredients;
    private String MealCost;
    private String MealURLImage;
    private MealSides MealSides;


    public Meal() {
    }

    public Meal(String mealName, String mealIngredients, String mealCost, String mealURLImage) {
        MealName = mealName;
        MealIngredients = mealIngredients;
        MealCost = mealCost;
        MealURLImage = mealURLImage;
    }

    public Meal(String mealName, String mealIngredients, String mealCost, String mealURLImage, tm.shker.mohamed.chickengrill.Objects.MealSides mealSides) {
        MealName = mealName;
        MealIngredients = mealIngredients;
        MealCost = mealCost;
        MealURLImage = mealURLImage;
        MealSides = mealSides;
    }

    public Meal(String mealName, String mealType, String mealIngredients, String mealCost, String mealURLImage, tm.shker.mohamed.chickengrill.Objects.MealSides mealSides) {
        MealName = mealName;
        MealType = mealType;
        MealIngredients = mealIngredients;
        MealCost = mealCost;
        MealURLImage = mealURLImage;
        MealSides = mealSides;
    }

    public String getMealType() {
        return MealType;
    }

    public void setMealType(String mealType) {
        MealType = mealType;
    }

    public tm.shker.mohamed.chickengrill.Objects.MealSides getMealSides() {
        return MealSides;
    }

    public void setMealSides(tm.shker.mohamed.chickengrill.Objects.MealSides mealSides) {
        MealSides = mealSides;
    }

    public String getMealName() {
        return MealName;
    }

    public void setMealName(String mealName) {
        MealName = mealName;
    }

    public String getMealIngredients() {
        return MealIngredients;
    }

    public void setMealIngredients(String mealIngredients) {
        MealIngredients = mealIngredients;
    }

    public String getMealCost() {
        return MealCost;
    }

    public void setMealCost(String mealCost) {
        MealCost = mealCost;
    }

    public String getMealURLImage() {
        return MealURLImage;
    }

    public void setMealURLImage(String mealURLImage) {
        MealURLImage = mealURLImage;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "MealName='" + MealName + '\'' +
                ", MealType='" + MealType + '\'' +
                ", MealIngredients='" + MealIngredients + '\'' +
                ", MealCost='" + MealCost + '\'' +
                ", MealURLImage='" + MealURLImage + '\'' +
                ", MealSides=" + MealSides +
                '}';
    }
}
