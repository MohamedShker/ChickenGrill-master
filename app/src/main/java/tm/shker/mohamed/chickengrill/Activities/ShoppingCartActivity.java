package tm.shker.mohamed.chickengrill.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // TODO: 19/01/2017 real time database for recieving mealOrders from curr user
        updateMealOrders();

        //set adapter to the recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMealOrders);
        adapter = new MealOrderAdapter(mealOrders,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void updateMealOrders(){
        ArrayList<MealOrder> currUserMealOrders = new ArrayList<MealOrder>();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // TODO: 05/02/2017 implement listener functions
        DatabaseReference mealOrdersREF = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid);
        mealOrdersREF.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        this.mealOrders = currUserMealOrders;
    }
}
