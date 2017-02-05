package tm.shker.mohamed.chickengrill.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohamed on 19/01/2017.
 */

public class FullOrder implements Serializable {
    private User user;//user that ordered the meal.
    private DeliveryArea deliveryArea;
    private ArrayList<MealOrder> mealOrders;
    private int sum;

    public FullOrder() {
    }

    public FullOrder(User user, DeliveryArea deliveryArea) {
        this.user = user;
        this.deliveryArea = deliveryArea;
        this.mealOrders = new ArrayList<MealOrder>();
        sum = 0;
    }

    private int calculateSUM() {
        int sum = 0;
        for (MealOrder mealOrder : mealOrders) {

            int currMealCost = Integer.parseInt(mealOrder.getOrderedMeal().get(0).getMealCost());
            int numOfDuplication = mealOrder.getNumOfDuplicationOfTheMeal();
            int CurrMealOrderTotalCost = currMealCost * numOfDuplication;

            sum += CurrMealOrderTotalCost;
        }

        if(deliveryArea != null)
        sum +=Integer.parseInt(deliveryArea.getDeliveryCost());

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

    public DeliveryArea getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(DeliveryArea deliveryArea) {
        this.deliveryArea = deliveryArea;
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
