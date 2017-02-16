package tm.shker.mohamed.chickengrill.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tm.shker.mohamed.chickengrill.Objects.Meal;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.R;

/**
 * Created by mohamed on 19/01/2017.
 */

public class MealOrderAdapter extends RecyclerView.Adapter<MealOrderAdapter.MealOrderViewHolder> {
    private ArrayList<DataSnapshot> data;
    private LayoutInflater inflater;
    private Context context;
    private String uid;


    public MealOrderAdapter(ArrayList<DataSnapshot> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public MealOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.meal_order, parent, false);
        return new MealOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MealOrderViewHolder holder, final int position) {

        final MealOrder currMealOrder = data.get(position).getValue(MealOrder.class);
        final DataSnapshot dataSnapshot = data.get(position);

        holder.fabActionMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.numOfDuplication = currMealOrder.getNumOfDuplicationOfTheMeal();
                if (holder.numOfDuplication != 0) {
                    if (holder.numOfDuplication == 1) {

                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        DatabaseReference userMealOrders = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid).child(dataSnapshot.getKey());
                                        userMealOrders.removeValue();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("האם אתה בטוח שאתה רוצה למחוק פריט זה?").setPositiveButton("כן", dialogClickListener)
                                .setNegativeButton("לא", dialogClickListener).show();
                    } else {
                        holder.currMealOrderCost = Integer.valueOf(currMealOrder.getOrderedMeal().get(0).getMealCost());
                        holder.singleMealCost = holder.currMealOrderCost / holder.numOfDuplication;
                        holder.numOfDuplication = holder.numOfDuplication - 1;

                        //set new number of duplication:
                        DatabaseReference numOfDupREF = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid).child(dataSnapshot.getKey());
                        Map<String, Object> childUpdate = new HashMap<String, Object>();
                        childUpdate.put("/numOfDuplicationOfTheMeal", (long) holder.numOfDuplication);
                        numOfDupREF.updateChildren(childUpdate);


                        //set new cost:
                        int newCost = holder.numOfDuplication * holder.singleMealCost;
                        DatabaseReference orderedMealREF = numOfDupREF.child("orderedMeal").child("0");
                        Map<String, Object> childUpdate1 = new HashMap<String, Object>();
                        childUpdate1.put("/mealCost", String.valueOf(newCost));
                        orderedMealREF.updateChildren(childUpdate1);
                    }
                }
            }
        });

        holder.fabActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.currMealOrderCost = Integer.valueOf(currMealOrder.getOrderedMeal().get(0).getMealCost());
                holder.numOfDuplication = currMealOrder.getNumOfDuplicationOfTheMeal();
                holder.singleMealCost = holder.currMealOrderCost / holder.numOfDuplication;
                holder.numOfDuplication = holder.numOfDuplication + 1;

                //set new number of duplication:
                DatabaseReference numOfDupREF = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid).child(dataSnapshot.getKey());
                Map<String, Object> childUpdate = new HashMap<String, Object>();
                childUpdate.put("/numOfDuplicationOfTheMeal", (long) holder.numOfDuplication);
                numOfDupREF.updateChildren(childUpdate);

                //set new cost:
                int newCost = holder.numOfDuplication * holder.singleMealCost;
                DatabaseReference orderedMealREF = numOfDupREF.child("orderedMeal").child("0");
                Map<String, Object> childUpdate1 = new HashMap<String, Object>();
                childUpdate1.put("/mealCost", String.valueOf(newCost));
                orderedMealREF.updateChildren(childUpdate1);
            }
        });

        if(currMealOrder.getOrderedMeal().get(0).getMealType().equals("שתיה קלה")){
            holder.ibEditMealOrder.setVisibility(View.INVISIBLE);
        }
        else {
            holder.ibEditMealOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 19/01/2017  return the user to the specific meal side activity with his own choices.
                }
            });
        }

        holder.tvMealOrderName.setText(currMealOrder.getOrderedMeal().get(0).getMealName());

        //prepare user choices string to display:
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Meal> orderedMeals = currMealOrder.getOrderedMeal();
        for (Meal meal : orderedMeals) {
            if (!meal.getMealType().equals("שתיה קלה"))
                stringBuilder.append(" { " + meal.getMealSides().toString() + " } ");
        }
        holder.tvMealOrderIngredients.setText(stringBuilder.toString());

        holder.tvMealOrderCost.setText(currMealOrder.getOrderedMeal().get(0).getMealCost() + "₪");

        holder.tvNumOfDuplications.setText(String.valueOf(currMealOrder.getNumOfDuplicationOfTheMeal()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MealOrderViewHolder extends RecyclerView.ViewHolder {
        FloatingActionButton fabActionAdd, fabActionMinus;
        TextView tvMealOrderName, tvMealOrderIngredients, tvMealOrderCost, tvNumOfDuplications;
        ImageButton ibEditMealOrder;
        int numOfDuplication, singleMealCost, currMealOrderCost;

        public MealOrderViewHolder(View v) {
            super(v);
            fabActionAdd = (FloatingActionButton) v.findViewById(R.id.fabActionAdd);
            fabActionMinus = (FloatingActionButton) v.findViewById(R.id.fabActionMinus);
            tvMealOrderName = (TextView) v.findViewById(R.id.tvMealOrderName);
            tvMealOrderIngredients = (TextView) v.findViewById(R.id.tvMealOrderIngredients);
            tvMealOrderCost = (TextView) v.findViewById(R.id.tvMealOrderCost);
            tvNumOfDuplications = (TextView) v.findViewById(R.id.tvNumOfDuplications);
            ibEditMealOrder = (ImageButton) v.findViewById(R.id.ibEditMealOrder);
        }
    }
}