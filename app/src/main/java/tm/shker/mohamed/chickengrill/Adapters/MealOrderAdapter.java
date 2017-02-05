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

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.R;

/**
 * Created by mohamed on 19/01/2017.
 */

public class MealOrderAdapter extends RecyclerView.Adapter<MealOrderAdapter.MealOrderViewHolder>{
    private ArrayList<MealOrder> data;
    private LayoutInflater inflater;
    private Context context;

    public MealOrderAdapter(ArrayList<MealOrder> data, Context context) {
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

        MealOrder currMealOrder = data.get(position);

        holder.fabActionMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.numOfDuplication = holder.numOfDuplication -1;
                holder.etNumOfDuplications.setText(holder.numOfDuplication);
            }
        });

        holder.fabActionAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.numOfDuplication = holder.numOfDuplication + 1;
                holder.etNumOfDuplications.setText(holder.numOfDuplication);
            }
        });

        holder.ibEditMealOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 19/01/2017  return the user to the specific meal side activity with his own choices
            }
        });

        holder.tvMealOrderName.setText(currMealOrder.getOrderedMeal().get(0).getMealName());
        holder.tvMealOrderIngredients.setText(currMealOrder.getOrderedMeal().get(0).getMealIngredients());
        holder.tvMealOrderCost.setText(currMealOrder.getOrderedMeal().get(0).getMealCost());
        holder.etNumOfDuplications.setText(holder.numOfDuplication);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MealOrderViewHolder extends RecyclerView.ViewHolder{
        FloatingActionButton fabActionAdd, fabActionMinus;
        TextView tvMealOrderName, tvMealOrderIngredients, tvMealOrderCost;
        EditText etNumOfDuplications;
        ImageButton ibEditMealOrder;
        int numOfDuplication;

        public MealOrderViewHolder(View v) {
            super(v);

            fabActionAdd = (FloatingActionButton) v.findViewById(R.id.fabActionAdd);
            fabActionMinus = (FloatingActionButton) v.findViewById(R.id.fabActionMinus);
            tvMealOrderName = (TextView) v.findViewById(R.id.tvMealOrderName);
            tvMealOrderIngredients = (TextView) v.findViewById(R.id.tvMealOrderIngredients);
            tvMealOrderCost = (TextView) v.findViewById(R.id.tvMealOrderCost);
            etNumOfDuplications = (EditText) v.findViewById(R.id.etNumOfDuplications);
            ibEditMealOrder = (ImageButton) v.findViewById(R.id.ibEditMealOrder);
            numOfDuplication = 1;


        }
    }
}
