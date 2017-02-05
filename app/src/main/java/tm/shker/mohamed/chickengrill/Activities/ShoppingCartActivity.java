package tm.shker.mohamed.chickengrill.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Adapters.MealAdapter;
import tm.shker.mohamed.chickengrill.Adapters.MealOrderAdapter;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ShoppingCartActivity extends AppCompatActivity {
    private MealOrderAdapter adapter;
    private ArrayList<MealOrder> mealOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabShoppingCartActivity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // TODO: 19/01/2017 real time database for recieving meal orders from curr user
        //set adapter to the recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMealOrders);
        adapter = new MealOrderAdapter(mealOrders,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}
