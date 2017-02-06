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

import tm.shker.mohamed.chickengrill.Adapters.MealOrderAdapter;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.R;

public class ShoppingCartActivity extends AppCompatActivity {
    private MealOrderAdapter adapter;
    private ArrayList<DataSnapshot> mealOrdersSnapshots;
    private ChildEventListener childEventListener;
    private DatabaseReference mealOrdersREF;

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

       initFields();

        // real time database for recieving mealOrdersSnapshots from curr user
        updateMealOrders();

        //set adapter to the recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMealOrders);
        adapter = new MealOrderAdapter(mealOrdersSnapshots,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void initFields(){
        mealOrdersSnapshots = new ArrayList<DataSnapshot>();
        //init my real time ChildEventListener:
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.i(Constants.TAG, "onChildAdded: " + mealOrder.toString());
                mealOrdersSnapshots.add(dataSnapshot);
                adapter.notifyItemInserted(mealOrdersSnapshots.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int position = getChildPosition(dataSnapshot.getKey());
                mealOrdersSnapshots.set(position,dataSnapshot);
                adapter.notifyItemChanged(position);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int position = getChildPosition(dataSnapshot.getKey());
                mealOrdersSnapshots.remove(position);
                adapter.notifyItemRemoved(position);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw new UnsupportedOperationException();
            }
        };

    }

    private int getChildPosition(String key) {
        for (int i = 0; i < mealOrdersSnapshots.size(); i++) {
            if(mealOrdersSnapshots.get(i).getKey().equals(key)){
                return i;
            }
        }
        //this exception is not supposed to be reached.
        throw new IllegalArgumentException("no index found for this key");
    }

    private void updateMealOrders(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mealOrdersREF = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid);
        mealOrdersREF.addChildEventListener(childEventListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mealOrdersREF.removeEventListener(childEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mealOrdersREF.removeEventListener(childEventListener);
    }
}
