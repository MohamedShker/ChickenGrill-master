package tm.shker.mohamed.chickengrill.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Adapters.MealAdapter;
import tm.shker.mohamed.chickengrill.Managers.Constants;
import tm.shker.mohamed.chickengrill.Objects.Meal;
import tm.shker.mohamed.chickengrill.Objects.MealSides;
import tm.shker.mohamed.chickengrill.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    private ArrayList<Meal> meals;
    private MealAdapter adapter;
    private String mealType;
    private View v;


    public static MenuFragment newInstance(String MealType) {

        Bundle args = new Bundle();
        args.putString(Constants.MEAL_TYPE , MealType);
        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_menu, container, false);

        //get which kind of meals to dis[lay
         mealType = getArguments().getString(Constants.MEAL_TYPE);

        //fetch data once:

        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference().child(mealType);
        databaseref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 meals = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Meal meal = child.getValue(Meal.class);
                    MealSides mealSides = child.child("Mealsides").getValue(MealSides.class);
                    meal.setMealSides(mealSides);
                    meals.add(meal);
                }

                //set adapter to the recyclerView
                RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
                adapter = new MealAdapter(meals,getApplicationContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }


}
