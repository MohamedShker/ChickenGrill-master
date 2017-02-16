package tm.shker.mohamed.chickengrill.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Adapters.MealOrderAdapter;
import tm.shker.mohamed.chickengrill.Objects.DeliveryArea;
import tm.shker.mohamed.chickengrill.Objects.FullOrder;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.Objects.User;
import tm.shker.mohamed.chickengrill.R;

public class ShoppingCartActivity extends AppCompatActivity {
    private EditText etPhoneNumber;
    private TextView tvTotalCost , tvdeliveryCost;
    private LinearLayout llAdressWrapper;
    private CheckBox cbPickUp;
    private String lastChar;


    private MealOrderAdapter adapter;
    private ArrayList<DataSnapshot> mealOrdersSnapshots;
    private ChildEventListener childEventListener;
    private DatabaseReference mealOrdersREF;

    private FullOrder fullOrder = new FullOrder();
    private boolean withDelivery;

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

        //init users full order.
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User(firebaseUser.getEmail(),firebaseUser.getDisplayName());
        //fullOrder = new FullOrder();
        fullOrder.setUser(user);

       initFields();

        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);
        lastChar = " ";
        enablePhoneNumFormatter();

        withDelivery = true;

        tvdeliveryCost = (TextView) findViewById(R.id.tvdeliveryCost);
        llAdressWrapper = (LinearLayout) findViewById(R.id.llAdressWrapper);
        cbPickUp = (CheckBox) findViewById(R.id.cbPickUp);

        cbPickUp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    llAdressWrapper.setVisibility(View.GONE);
                    tvdeliveryCost.setText("0₪");
                    withDelivery = false;
                    tvTotalCost.setText(String.valueOf(calculateSUM()) + "₪");
                }
                else {
                    llAdressWrapper.setVisibility(View.VISIBLE);
                    tvdeliveryCost.setText("10₪");
                    withDelivery = true;
                    tvTotalCost.setText(String.valueOf(calculateSUM()) + "₪");
                }
            }
        });




        // real time database for recieving mealOrdersSnapshots from curr user
        updateMealOrders();

        //set adapter to the recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMealOrders);
        adapter = new MealOrderAdapter(mealOrdersSnapshots,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void enablePhoneNumFormatter() {
        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = etPhoneNumber.getText().toString().length();
                if (digits > 1)
                    lastChar = etPhoneNumber.getText().toString().substring(digits-1);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = etPhoneNumber.getText().toString().length();
                if (!lastChar.equals("-")) {
                    if (digits == 3 || digits == 7) {
                        etPhoneNumber.append("-");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initFields(){
        mealOrdersSnapshots = new ArrayList<DataSnapshot>();


        //init my real time ChildEventListener:
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mealOrdersSnapshots.add(dataSnapshot);
                adapter.notifyItemInserted(mealOrdersSnapshots.size() - 1);
                tvTotalCost.setText(String.valueOf(calculateSUM()) + "₪");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    int position = getChildPosition(dataSnapshot.getKey());
                    mealOrdersSnapshots.set(position, dataSnapshot);
                    adapter.notifyItemChanged(position, dataSnapshot);
                    tvTotalCost.setText(String.valueOf(calculateSUM()) + "₪");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int position = getChildPosition(dataSnapshot.getKey());
                mealOrdersSnapshots.remove(position);
                adapter.notifyItemRemoved(position);
                tvTotalCost.setText(String.valueOf(calculateSUM()) + "₪");
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

    private int calculateSUM() {
        int sum = 0;
        for (DataSnapshot dataSnapshot : mealOrdersSnapshots) {
            MealOrder mealOrder = dataSnapshot.getValue(MealOrder.class);
            int currMealCost = Integer.parseInt(mealOrder.getOrderedMeal().get(0).getMealCost());//already changed if num of duplication is changed.
            sum += currMealCost;
        }

        if(withDelivery)
            sum += 10;

        return sum;
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
