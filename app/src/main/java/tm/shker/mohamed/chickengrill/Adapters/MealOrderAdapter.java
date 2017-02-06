package tm.shker.mohamed.chickengrill.Adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Objects.Meal;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.R;

/**
 * Created by mohamed on 19/01/2017.
 */

public class MealOrderAdapter extends RecyclerView.Adapter<MealOrderAdapter.MealOrderViewHolder>{
    private ArrayList<DataSnapshot> data;
    private LayoutInflater inflater;
    private Context context;

    public MealOrderAdapter(ArrayList<DataSnapshot> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MealOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =inflater.inflate(R.layout.meal_order,parent,false);
        return new MealOrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MealOrderViewHolder holder, int position) {

        MealOrder currMealOrder = data.get(position).getValue(MealOrder.class);

        holder.fabActionMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.numOfDuplication != 0) {
                    holder.numOfDuplication = holder.numOfDuplication - 1;
                    holder.tvNumOfDuplications.setText(String.valueOf(holder.numOfDuplication));

                    if(holder.numOfDuplication == 1){
                        // TODO: 06/02/2017   numOfDuplication = 1 and the user asks to dec to 0 ==> ask the user if he realy wants to delete this meal order and act accordingly.
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid);
                    }
                }
            }
        });

        holder.fabActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.numOfDuplication = holder.numOfDuplication + 1;
                holder.tvNumOfDuplications.setText(String.valueOf(holder.numOfDuplication));
            }
        });

        holder.ibEditMealOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 19/01/2017  return the user to the specific meal side activity with his own choices
            }
        });

        holder.tvMealOrderName.setText(currMealOrder.getOrderedMeal().get(0).getMealName());

        //prepare user choices string to display:
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Meal> orderedMeals = currMealOrder.getOrderedMeal();
        for (Meal meal: orderedMeals) {
            stringBuilder.append(" { "+meal.getMealSides().toString() + " } ");
        }
        holder.tvMealOrderIngredients.setText(stringBuilder.toString());
        if(currMealOrder.getOrderedMeal().get(0).getMealType().equals("תוספות"))
        holder.tvMealOrderCost.setText(currMealOrder.getOrderedMeal().get(0).getMealCost() + "₪");
        holder.tvNumOfDuplications.setText(String.valueOf(holder.numOfDuplication));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MealOrderViewHolder extends RecyclerView.ViewHolder{
        FloatingActionButton fabActionAdd, fabActionMinus;
        TextView tvMealOrderName, tvMealOrderIngredients, tvMealOrderCost, tvNumOfDuplications;
        ImageButton ibEditMealOrder;
        int numOfDuplication;

        public MealOrderViewHolder(View v) {
            super(v);

            fabActionAdd = (FloatingActionButton) v.findViewById(R.id.fabActionAdd);
            fabActionMinus = (FloatingActionButton) v.findViewById(R.id.fabActionMinus);
            tvMealOrderName = (TextView) v.findViewById(R.id.tvMealOrderName);
            tvMealOrderIngredients = (TextView) v.findViewById(R.id.tvMealOrderIngredients);
            tvMealOrderCost = (TextView) v.findViewById(R.id.tvMealOrderCost);
            tvNumOfDuplications = (TextView) v.findViewById(R.id.tvNumOfDuplications);
            ibEditMealOrder = (ImageButton) v.findViewById(R.id.ibEditMealOrder);
            numOfDuplication = 1;


        }
    }
}
