package tm.shker.mohamed.chickengrill.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohamed on 25/12/2016.
 */

public class MealSides implements Serializable{
    private ArrayList<String> possibleModifications = new ArrayList<>();
    private ArrayList<String> drinks = new ArrayList<>();
    private ArrayList<String> sides = new ArrayList<>();
    private ArrayList<String> salad = new ArrayList<>();
    private ArrayList<String> Sauces = new ArrayList<>();
    private String mealNotes = " ";

    public MealSides() {

    }

    public MealSides(ArrayList<String> possibleModifications, ArrayList<String> drinks, ArrayList<String> sides, ArrayList<String> salad, ArrayList<String> sauces, String mealNotes) {
        this.possibleModifications = possibleModifications;
        this.drinks = drinks;
        this.sides = sides;
        this.salad = salad;
        Sauces = sauces;
        this.mealNotes = mealNotes;
    }

    public ArrayList<String> getPossibleModifications() {
        return possibleModifications;
    }

    public ArrayList<String> getDrinks() {
        return drinks;
    }

    public ArrayList<String> getSides() {
        return sides;
    }

    public ArrayList<String> getSalad() {
        return salad;
    }

    public ArrayList<String> getSauces() {
        return Sauces;
    }

    public String getMealNotes() {
        return mealNotes;
    }

    public void setPossibleModifications(ArrayList<String> possibleModifications) {
        this.possibleModifications = possibleModifications;
    }

    public void setDrinks(ArrayList<String> drinks) {
        this.drinks = drinks;
    }

    public void setSides(ArrayList<String> sides) {
        this.sides = sides;
    }

    public void setSalad(ArrayList<String> salad) {
        this.salad = salad;
    }

    public void setSauces(ArrayList<String> sauces) {
        Sauces = sauces;
    }

    public void setMealNotes(String mealNotes) {
        this.mealNotes = mealNotes;
    }

    @Override
    public String toString() {
        return "MealSidesActivity{" +
                "possibleModifications=" + possibleModifications +
                ", drinks=" + drinks +
                ", sides=" + sides +
                ", salad=" + salad +
                ", Sauces=" + Sauces +
                ", mealNotes='" + mealNotes + '\'' +
                '}';
    }
}
