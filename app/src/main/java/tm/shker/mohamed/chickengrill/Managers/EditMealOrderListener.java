package tm.shker.mohamed.chickengrill.Managers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tm.shker.mohamed.chickengrill.Activities.MealSidesActivity;
import tm.shker.mohamed.chickengrill.Objects.Meal;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.Objects.MealSides;

/**
 * Created by mohamed on 22/02/2017.
 */

public class EditMealOrderListener implements View.OnClickListener {
    private MealOrder currMealOrder;
    private Meal meal;
    private Context context;
    private String uid , mealOrderDBKey;

    public EditMealOrderListener(MealOrder currMealOrder, Context context, String key) {
        this.currMealOrder = currMealOrder;
        this.context = context;
        this.mealOrderDBKey = key;
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getMealFromDB(currMealOrder.getOrderedMeal().get(0).getMealType(),currMealOrder.getOrderedMeal().get(0).getMealName());
    }

    private void getMealFromDB(String mealType, final String mealName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(mealType);
       ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Meal meal1 = child.getValue(Meal.class);
                    if(meal1.getMealName().equals(mealName)){
                        meal = meal1;
                        MealSides mealSides = child.child("Mealsides").getValue(MealSides.class);
                        meal.setMealSides(mealSides);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, MealSidesActivity.class);
        Bundle args = new Bundle();
        args.putSerializable(Constants.MEAL_OBJECT, meal);
        args.putSerializable(Constants.MEAL_ORDER_OBJECT,currMealOrder);
        args.putString(Constants.MEAL_ORDER_DB_KEY, mealOrderDBKey);
        intent.putExtras(args);
        context.startActivity(intent);
    }
}
