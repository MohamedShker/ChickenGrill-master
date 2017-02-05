package tm.shker.mohamed.chickengrill.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Managers.AddToCartListener;
import tm.shker.mohamed.chickengrill.Managers.Constants;
import tm.shker.mohamed.chickengrill.Objects.Meal;
import tm.shker.mohamed.chickengrill.R;

/**
 * Created by mohamed on 05/10/2016.
 */

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.foodOptionViewHolder>{
    private ArrayList<Meal> data;
    private LayoutInflater inflater;
    private Context context;

    private View.OnClickListener addToCart = new AddToCartListener();

    private View.OnClickListener showDetails = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            v.setLayoutParams(new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getPx(200)));
            v.postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.setLayoutParams(new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getPx(130)));
                }
            }, 5000);
        }
    };


    public MealAdapter(ArrayList<Meal> data, Context context) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    private int getPx(int dimensionDp) {
        float density =  context.getResources().getDisplayMetrics().density;
        return (int) (dimensionDp * density + 0.5f);
    }


    @Override
    public foodOptionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.meal,parent,false);
        return new foodOptionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final foodOptionViewHolder holder, final int position) {
        Meal currMeal = data.get(position);

        holder.currMeal = currMeal;

        holder.tvMealName.setText(currMeal.getMealName());
        holder.tvMealDetails.setText(currMeal.getMealIngredients());
        if(!currMeal.getMealCost().equals(" ")) {
            holder.tvMealCost.setText(currMeal.getMealCost() + "₪");
        }else {
            holder.tvMealCost.setText(" ");
        }

        Picasso.with(context).
                load(currMeal.getMealURLImage()).
                error(R.mipmap.ic_launcher).
                into(holder.ivMealImage);

        holder.ibAddToCard.setOnClickListener(new AddToCartListener(holder.currMeal,context));

        holder.vCard.setOnClickListener(showDetails);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class foodOptionViewHolder extends RecyclerView.ViewHolder{
        View vCard;
        TextView tvMealName , tvMealDetails, tvMealCost;
        ImageView ivMealImage;
        ImageButton ibAddToCard;
        Meal currMeal;


        public foodOptionViewHolder(View currView) {
            super(currView);
            vCard = currView.findViewById(R.id.cvMealOption);
            tvMealName = (TextView) currView.findViewById(R.id.tvMealName);
            tvMealDetails = (TextView) currView.findViewById(R.id.tvMealDetails);
            tvMealCost = (TextView) currView.findViewById(R.id.tvMealCost);
            ivMealImage = (ImageView) currView.findViewById(R.id.ivMealImage);
            ibAddToCard = (ImageButton) currView.findViewById(R.id.ibAddToCard);
            currMeal = new Meal();
        }
    }
}
