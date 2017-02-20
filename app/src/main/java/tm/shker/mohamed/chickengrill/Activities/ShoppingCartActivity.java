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
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Adapters.MealOrderAdapter;
import tm.shker.mohamed.chickengrill.Managers.Constants;
import tm.shker.mohamed.chickengrill.Objects.DeliveryArea;
import tm.shker.mohamed.chickengrill.Objects.FullOrder;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.Objects.User;
import tm.shker.mohamed.chickengrill.R;

public class ShoppingCartActivity extends AppCompatActivity {
    private EditText etPhoneNumber;
    private TextView tvTotalCost , tvdeliveryCost;
    private AutoCompleteTextView actvDeliveryAddress;
    private LinearLayout llAdressWrapper;
    private CheckBox cbPickUp , cbSavePhoneNum;

    private MealOrderAdapter adapter;
    private ArrayList<DataSnapshot> mealOrdersSnapshots;
    private ChildEventListener childEventListener;
    private DatabaseReference mealOrdersREF;
    DatabaseReference userREF;
    User user;

    private FullOrder fullOrder;
    private boolean withDelivery;
    private String lastChar;
    private int PreviousLength;
    private boolean deleting;

    private ArrayAdapter<String> autoCompleteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        initFields();

        // real time database for recieving mealOrdersSnapshots from curr user
        updateMealOrders();

        //set adapter to the recyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMealOrders);
        adapter = new MealOrderAdapter(mealOrdersSnapshots,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        //for receiving the user contact number.
        enablePhoneNumFormatter();

        //if there is a saved number--> display it.
        DatabaseReference phoneNumberREF = userREF.child("phoneNumber");
        phoneNumberREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String phoneNumber = dataSnapshot.getValue(String.class);
                if(phoneNumber != null)
                    etPhoneNumber.setText(phoneNumber);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //for receiving the users choice whether he want to pickup the order/ getting it by Delivery.
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

        //for saving the phone number that the user entered.
        cbSavePhoneNum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String phoneNumber = etPhoneNumber.getText().toString();
                    if(phoneNumber.length() == 12) {
                        if(validatePhoneNumber()) {
                            userREF.child("phoneNumber").setValue(phoneNumber);
                        }
                    }
                    else{
                        showError("המספר שהכנסת לא תקין, נסה שנית.",etPhoneNumber);
                        cbSavePhoneNum.setChecked(false);
                    }
                }
            }
        });

        //drop down list for auto complete text view (for addresses):

        //for testing purposes:
//        ArrayList<String> temp = new ArrayList<>();
//        temp.add("בית: חיפה רחוב וולפסון דירה 306 כניסה 2.");
//        temp.add("עבודה: חיפה חוצות המפרץ.");
//        temp.add("בירת הקרמל.");
//        temp.add("עבודי דודי בטיזו ברודי");
//
//        userREF.child("addresses").setValue(temp);
        userREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                ArrayList<String> addresses = user.getAddresses();
                if(addresses != null && addresses.size() !=0) {
                    for (String address : addresses) {
                        autoCompleteAdapter.add(address);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        actvDeliveryAddress.setThreshold(1);
        actvDeliveryAddress.setAdapter(autoCompleteAdapter);


        //pay onclick listener:
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabShoppingCartActivity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //init users full order.
                fullOrder.setUser(user);
                fullOrder.setWithDelivery(withDelivery);

                if(fullOrder.isWithDelivery()) {
                    //testMinOrderCost(); //// TODO: 17/02/2017 check if the cost of the full order is more than 70 shekels else throw an error
                }
            }
        });
    }

    private void initViews() {

        //for receiving the user contact number.
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        tvTotalCost = (TextView) findViewById(R.id.tvTotalCost);

        //for receiving the users choice whether he want to pickup the order/ getting it by Delivery.
        tvdeliveryCost = (TextView) findViewById(R.id.tvdeliveryCost);
        llAdressWrapper = (LinearLayout) findViewById(R.id.llAdressWrapper);
        cbPickUp = (CheckBox) findViewById(R.id.cbPickUp);

        //for saving the phone number that the user entered.
        cbSavePhoneNum = (CheckBox) findViewById(R.id.cbSavePhoneNum);

        //drop down list for auto complete text view that displays addresses:
        actvDeliveryAddress = (AutoCompleteTextView) findViewById(R.id.actvDeliveryAddress);
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

        fullOrder = new FullOrder();
        withDelivery = true;

        //for formatting the phone number. used in enablePhoneNumFormatter() function.
        lastChar = " ";

        //for auto complete text view:
        autoCompleteAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_dropdown_item_1line);

        //firebase database references:
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userREF = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
    }

    private void enablePhoneNumFormatter() {

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = etPhoneNumber.getText().toString().length();
                if (digits > 1)
                    lastChar = etPhoneNumber.getText().toString().substring(digits-1);
                PreviousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                deleting = PreviousLength > s.length();
                int digits = etPhoneNumber.getText().toString().length();
                if(!deleting) {
                    if (!lastChar.equals("-")) {
                        if (digits == 2) {
                            String areaCode = etPhoneNumber.getText().toString();
                            checkAreaCode(areaCode);
                        } else if (digits == 3 || digits == 7) {
                            etPhoneNumber.append("-");
                        } else if (digits == 12) {
                            validatePhoneNumber();
                        }
                    }
                }
                else {
                    if(digits == 2){
                        etPhoneNumber.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkAreaCode(String areaCode) {

        if (areaCode.equals("02") || areaCode.equals("03") || areaCode.equals("04") || areaCode.equals("08") || areaCode.equals("09")){
            etPhoneNumber.setText("0" + areaCode);
            etPhoneNumber.setSelection(4);
        }

        else if(!areaCode.equals("05") && !areaCode.equals("07")){
            showError("קידומת לא תקינה, נסה שנית",etPhoneNumber);
            etPhoneNumber.setText("");
        }

    }

    private boolean validatePhoneNumber() {
        boolean ans = true;
        String phoneNum = etPhoneNumber.getText().toString();
        if(phoneNum.replaceAll("\\D", "").length() != 10){
            //show error
            ans = false;
            showError("המספר שהכנסת לא תקין, נסה שנית.",etPhoneNumber);
            etPhoneNumber.setText("");
        }
        return ans;
    }

    private void showError(String exception, View view) {
        Snackbar.make(view,
                exception,
                Snackbar.LENGTH_INDEFINITE
        ).setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();
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
