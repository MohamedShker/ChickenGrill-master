package tm.shker.mohamed.chickengrill.Activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tm.shker.mohamed.chickengrill.Managers.Constants;
import tm.shker.mohamed.chickengrill.Objects.User;
import tm.shker.mohamed.chickengrill.R;

public class PersonalInformationActivity extends AppCompatActivity {
    EditText etPIAPhoneNumber, etPIAHomeAddress, etPIAWorkAddress, etPIARandomAddress, etPIANickName;
    ImageView ivPIAPhoto;
    String lastChar, uid;
    private int PreviousLength;
    private boolean deleting;
    DatabaseReference userREF;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init fields:
        lastChar = " ";
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userREF = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        initViews();

        displayPersonalDetails();

        enablePhoneNumFormatter();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePhoneNumber();
                saveAddresses();
                saveNickName();
            }
        });


//        //display photo for facebook and google users:
//        List<? extends UserInfo> providerData = FirebaseAuth.getInstance().getCurrentUser().getProviderData();
//        for (UserInfo profile : providerData) {
//            if(profile.getProviderId().equals("facebook.com")) {
//                String facebookuid = profile.getUid();
//                String photoUrl = "https://graph.facebook.com/" + facebookuid + "/picture?height=500";
//                Picasso.with(this).load(photoUrl).into(ivPIAPhoto);
//            }
//            else if(profile.getProviderId().equals("google.com")){
//                String s = profile.getPhotoUrl().toString();
//                Picasso.with(this).load(s).into(ivPIAPhoto);
//            }
//        }
    }

    private void initViews() {
        etPIAPhoneNumber = (EditText) findViewById(R.id.etPIAPhoneNumber);
        etPIAHomeAddress = (EditText) findViewById(R.id.etPIAHomeAddress);
        etPIAWorkAddress = (EditText) findViewById(R.id.etPIAWorkAddress);
        etPIARandomAddress = (EditText) findViewById(R.id.etPIARandomAddress);
        etPIANickName = (EditText) findViewById(R.id.etPIANickName);
        ivPIAPhoto = (ImageView) findViewById(R.id.ivPIAPhoto);

    }

    private void displayPersonalDetails() {
        userREF.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Log.d(Constants.TAG, user.toString());
                String phoneNumber = null;
                String homeAddress = null;
                String workAddress = null;
                String randomAddress = null;
                String nickName = null;
                if(user != null) {
                    phoneNumber = user.getPhoneNumber();
                    nickName = user.getDisplayName();
                    ArrayList<String> addresses = user.getAddresses();
                    if(addresses != null && addresses.size()!=0) {
                        homeAddress = user.getAddresses().get(0);
                        homeAddress = homeAddress.substring(5);
                        workAddress = user.getAddresses().get(1);
                        workAddress = workAddress.substring(7);
                        randomAddress = user.getAddresses().get(2);
                    }
                }

                if (phoneNumber != null && phoneNumber.length()!=0) {
                    etPIAPhoneNumber.setText(phoneNumber);
                    etPIAPhoneNumber.setSelection(12);
                }
                if (homeAddress != null)
                    etPIAHomeAddress.setText(homeAddress);
                if (workAddress != null)
                    etPIAWorkAddress.setText(workAddress);
                if (randomAddress != null)
                    etPIARandomAddress.setText(randomAddress);
                if(nickName != null)
                    etPIANickName.setText(nickName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void savePhoneNumber() {
        String phoneNumber = etPIAPhoneNumber.getText().toString();
        if (phoneNumber.length() == 12) {
            if (validatePhoneNumber()) {
                userREF.child("phoneNumber").setValue(phoneNumber);
            }
        } else {
            showError("המספר שהכנסת לא תקין, נסה שנית.", etPIAPhoneNumber);
        }
    }

    private void saveAddresses() {
        String homeAddress = etPIAHomeAddress.getText().toString();
        String workAddress = etPIAWorkAddress.getText().toString();
        String randomAddress = etPIARandomAddress.getText().toString();

        ArrayList<String> addresses = new ArrayList<>();
        addresses.add("בית: " +homeAddress);
        addresses.add("עבודה: " +workAddress);
        addresses.add(randomAddress);

        userREF.child("addresses").setValue(addresses).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(ivPIAPhoto, View.ALPHA,0);
                alphaAnimation.setRepeatCount(1);
                alphaAnimation.setDuration(800);
                alphaAnimation.setRepeatMode(ValueAnimator.REVERSE);
                alphaAnimation.start();


//                AnimatorSet animatorSet = new AnimatorSet();
//                animatorSet.play(alphaAnimation);

                Toast.makeText(PersonalInformationActivity.this, "הפרטים נשמרו בהצלחה", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PersonalInformationActivity.this, "בעיה בשמירת פרטים, אנא בדוק את חיבור האינטרנט שלך", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveNickName() {
        String nickName = etPIANickName.getText().toString();
        if(nickName.length() !=0){
            userREF.child("displayName").setValue(nickName);
        }
    }

    private void enablePhoneNumFormatter() {

        etPIAPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = etPIAPhoneNumber.getText().toString().length();
                if (digits > 1)
                    lastChar = etPIAPhoneNumber.getText().toString().substring(digits - 1);
                PreviousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                deleting = PreviousLength > s.length();
                int digits = etPIAPhoneNumber.getText().toString().length();
                if (!deleting) {
                    if (!lastChar.equals("-")) {
                        if (digits == 2) {
                            String areaCode = etPIAPhoneNumber.getText().toString();
                            checkAreaCode(areaCode);
                        } else if (digits == 3 || digits == 7) {
                            etPIAPhoneNumber.append("-");
                        } else if (digits == 12) {
                            validatePhoneNumber();
                        }
                    }
                } else {
                    if (digits == 2) {
                        etPIAPhoneNumber.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkAreaCode(String areaCode) {

        if (areaCode.equals("02") || areaCode.equals("03") || areaCode.equals("04") || areaCode.equals("08") || areaCode.equals("09")) {
            etPIAPhoneNumber.setText("0" + areaCode);
            etPIAPhoneNumber.setSelection(4);
        } else if (!areaCode.equals("05") && !areaCode.equals("07")) {
            showError("קידומת לא תקינה, נסה שנית", etPIAPhoneNumber);
            etPIAPhoneNumber.setText("");
        }

    }

    private boolean validatePhoneNumber() {
        boolean ans = true;
        String phoneNum = etPIAPhoneNumber.getText().toString();
        if (phoneNum.replaceAll("\\D", "").length() != 10) {
            //show error
            ans = false;
            showError("המספר שהכנסת לא תקין, נסה שנית.", etPIAPhoneNumber);
            etPIAPhoneNumber.setText("");
        }
        return ans;
    }

    private void showError(String exception, View view) {
        Toast.makeText(this, exception, Toast.LENGTH_SHORT).show();
    }


}
