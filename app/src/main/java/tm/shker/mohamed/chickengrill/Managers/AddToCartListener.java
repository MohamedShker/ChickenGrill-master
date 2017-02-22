package tm.shker.mohamed.chickengrill.Managers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import tm.shker.mohamed.chickengrill.Activities.MealSidesActivity;
import tm.shker.mohamed.chickengrill.Objects.Meal;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;


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
            args.putSerializable(Constants.MEAL_OBJECT, meal);
            args.putSerializable(Constants.MEAL_ORDER_OBJECT, null);
            args.putString(Constants.MEAL_ORDER_DB_KEY, null);
            intent.putExtras(args);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
        else {
            addToCard();
        }

    }

    private void addToCard() {
        MealOrder mealOrder = new MealOrder();
        mealOrder.getOrderedMeal().add(meal);
        uploadToDataBase(mealOrder);
    }

    private void uploadToDataBase(MealOrder mealOrder){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mealOrdersREF = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid).push();
        mealOrdersREF.setValue(mealOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "המנה הוספה בהצלחה לסל", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "כישלון בהוספת המנה לסל, אנא בדוק את חיבור האינטרנט שלך", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
