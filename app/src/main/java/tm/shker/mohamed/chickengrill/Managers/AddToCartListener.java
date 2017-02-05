package tm.shker.mohamed.chickengrill.Managers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import tm.shker.mohamed.chickengrill.Activities.MealSidesActivity;
import tm.shker.mohamed.chickengrill.Objects.Meal;


/**
 * Created by mohamed on 04/01/2017.
 */

public class AddToCartListener implements View.OnClickListener  {
    private Meal meal;
    private Context context;

    public AddToCartListener() {
    }

    public AddToCartListener(Meal meal, Context context) {
        this.meal = meal;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        if(!meal.getMealType().equals("שתיה קלה")) {

            Intent intent = new Intent(context, MealSidesActivity.class);
            Bundle args = new Bundle();
            args.putSerializable(Constants.MEAL_OPJECT, meal);
            intent.putExtras(args);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else {
            addToCard();
        }

    }

    private void addToCard() {
        // TODO: 23/01/2017
    }

}
