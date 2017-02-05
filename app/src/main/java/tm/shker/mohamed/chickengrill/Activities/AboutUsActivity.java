package tm.shker.mohamed.chickengrill.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Adapters.DeliveryAreaAdapter;
import tm.shker.mohamed.chickengrill.Objects.DeliveryArea;
import tm.shker.mohamed.chickengrill.R;

public class AboutUsActivity extends AppCompatActivity {
    private TextView tvWorkHours;
    private TextView tvAboutUs;
    private RecyclerView rvDeliveryAreas;
    private DeliveryAreaAdapter adapter;
    private ArrayList<DeliveryArea> deliveryAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        displayAboutUsMessage();
        displayWorkHours();
        displayDeliveryAreas();

        //social media links

    }

    private void displayAboutUsMessage() {
        //about us with firebase database Listener
        tvAboutUs = (TextView) findViewById(R.id.tvAboutUs);
        tvAboutUs.setMovementMethod(new ScrollingMovementMethod());

        DatabaseReference AboutUsMessageRef = FirebaseDatabase.getInstance().getReference().child("עלינו").child("aboutUsMessage");
        AboutUsMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String aboutUsMessage = dataSnapshot.getValue().toString();
                tvAboutUs.setText(aboutUsMessage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayDeliveryAreas() {

        //delivery areas recycler view with firebase database Listener
        DatabaseReference DeliveryAreasRef = FirebaseDatabase.getInstance().getReference().child("עלינו").child("DeliveryAreas");
        DeliveryAreasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deliveryAreas = new ArrayList<>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    DeliveryArea deliveryArea = child.getValue(DeliveryArea.class);
                    deliveryAreas.add(deliveryArea);
                }

                //set adapter to the recyclerView
                rvDeliveryAreas = (RecyclerView) findViewById(R.id.rvDeliveryAreas);
                adapter = new DeliveryAreaAdapter(deliveryAreas, getApplicationContext());
                rvDeliveryAreas.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rvDeliveryAreas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayWorkHours() {
        //work Hours with firebase database Listener
        tvWorkHours = (TextView) findViewById(R.id.tvWorkHours);
        tvWorkHours.setMovementMethod(new ScrollingMovementMethod());
        DatabaseReference WorkHoursRef = FirebaseDatabase.getInstance().getReference().child("עלינו").child("workHours");
        WorkHoursRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                String[] workHours = value.split(",");
                String WHToDisplay = "";
                for (int i = 0; i < workHours.length; i++) {
                    WHToDisplay += workHours[i] + "\n";
                }
                tvWorkHours.setText(WHToDisplay);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
