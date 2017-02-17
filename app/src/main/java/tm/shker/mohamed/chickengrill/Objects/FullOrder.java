package tm.shker.mohamed.chickengrill.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohamed on 19/01/2017.
 */

public class FullOrder implements Serializable {
    private User user;//user that ordered the meal.
    private boolean withDelivery;
    private ArrayList<MealOrder> mealOrders;
    private int sum;

    public FullOrder() {
        mealOrders = new ArrayList<MealOrder>();
        sum = 0;
    }

    public FullOrder(User user, boolean withDelivery, ArrayList<MealOrder> mealOrders, int sum) {
        this.user = user;
        this.withDelivery = withDelivery;
        this.mealOrders = mealOrders;
        this.sum = sum;
    }

    private int calculateSUM() {
        int sum = 0;
        for (MealOrder mealOrder : mealOrders) {
            int currMealCost = Integer.parseInt(mealOrder.getOrderedMeal().get(0).getMealCost());
            sum += currMealCost;
        }

        if(withDelivery)
        sum += 10;

        return sum;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<MealOrder> getMealOrders() {
        return mealOrders;
    }

    public int getSum() {
        return sum;
    }

    public boolean isWithDelivery() {
        return withDelivery;
    }

    public void setWithDelivery(boolean withDelivery) {
        this.withDelivery = withDelivery;
    }

    @Override
    public String toString() {
        return "FullOrder{" +
                "user=" + user +
                ", mealOrders=" + mealOrders +
                ", sum=" + sum +
                '}';
    }
}
