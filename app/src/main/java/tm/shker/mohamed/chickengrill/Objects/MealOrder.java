package tm.shker.mohamed.chickengrill.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohamed on 18/01/2017.
 */

public class MealOrder implements Serializable {
    private ArrayList<Meal> orderedMeal;//this contains all the data about the specific order that the customer ordered.
    private int numOfDuplicationOfTheMeal;//how much meals with the exact same ingredients do the customer ordered.

    public MealOrder() {
        this.orderedMeal = new ArrayList<Meal>();
        this.numOfDuplicationOfTheMeal = 1;
    }

    public MealOrder(ArrayList<Meal> orderedMeal) {
        this.orderedMeal = orderedMeal;
        this.numOfDuplicationOfTheMeal = 1;
    }

    public MealOrder(ArrayList<Meal> orderedMeal, int numOfDuplicationOfTheMeal) {
        this.orderedMeal = orderedMeal;
        this.numOfDuplicationOfTheMeal = numOfDuplicationOfTheMeal;
    }

    public ArrayList<Meal> getOrderedMeal() {
        return orderedMeal;
    }

    public void setOrderedMeal(ArrayList<Meal> orderedMeal) {
        this.orderedMeal = orderedMeal;
    }

    public int getNumOfDuplicationOfTheMeal() {
        return numOfDuplicationOfTheMeal;
    }

    public void setNumOfDuplicationOfTheMeal(int numOfDuplicationOfTheMeal) {
        this.numOfDuplicationOfTheMeal = numOfDuplicationOfTheMeal;
    }

    @Override
    public String toString() {
        return "MealOrder{" +
                "orderedMeal=" + orderedMeal +
                ", numOfDuplicationMeals=" + numOfDuplicationOfTheMeal +
                '}';
    }
}
