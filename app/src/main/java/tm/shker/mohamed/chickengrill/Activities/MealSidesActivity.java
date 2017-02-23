package tm.shker.mohamed.chickengrill.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Managers.Constants;
import tm.shker.mohamed.chickengrill.Objects.Meal;
import tm.shker.mohamed.chickengrill.Objects.MealOrder;
import tm.shker.mohamed.chickengrill.Objects.MealSides;
import tm.shker.mohamed.chickengrill.R;

public class MealSidesActivity extends AppCompatActivity {

    //for meal big display:
    private ImageView ivBigMealImage;
    private TextView tvBigMealName, tvBigMealPrice, tvMealSidesDrinkTitle, tvMealSideTitle, tvMealPossibleModificationsTitle, tvMealSaladsTitle, tvMealSaucesTitle;
    private RelativeLayout rlMealSidesDrink, rlMealSide, rlMealNotes;
    private LinearLayout llMealPossibleModifications, llMealSalads, llMealSauces, llMealSidesWrapper;
    private RadioGroup rgDrinks, rgSides;
    private EditText etMealNotes;
    private Button btnAddToCart;
    private Meal mealToDisplay;
    private MealOrder mealOrderToEdit;
    private String mealType, mealOrderDBKey;

    ArrayList<String> possibleModifications, salads, sides, drinks, sauces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_sides);
        Bundle bundle = this.getIntent().getExtras();
        mealToDisplay = (Meal) bundle.getSerializable(Constants.MEAL_OBJECT);
        mealOrderToEdit = (MealOrder) bundle.getSerializable(Constants.MEAL_ORDER_OBJECT);
        mealOrderDBKey = (String) bundle.getString(Constants.MEAL_ORDER_DB_KEY);

        initViews();
        initMealSides(mealToDisplay);

        if(mealType.equals("עסקיות בורגרים") || mealType.equals("עסקיות")) {
            displayMeal();
        }
        else if(mealType.equals("תוספות")){
            displaySides();

        }
        else if( mealType.equals("מנות בג'בטה") || mealType.equals("מנות בבגט")){
            displayBagetMeal();
        }
        else if(mealType.equals("קומבינציות")){
            displayCombinationMeal();
        }

        if(mealOrderToEdit != null){
            btnAddToCart.setText("עדכן מנה");
        }

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCart();
            }
        });

    }

    private void initViews() {
        //for meal big display:
        ivBigMealImage = (ImageView) findViewById(R.id.ivBigMealImage);
        tvBigMealName = (TextView) findViewById(R.id.tvBigMealName);
        tvBigMealPrice = (TextView) findViewById(R.id.tvBigMealPrice);

        //for displaying drinks options:
        rlMealSidesDrink = (RelativeLayout) findViewById(R.id.rlMealSidesDrink);
        tvMealSidesDrinkTitle = (TextView) findViewById(R.id.tvMealSidesDrinkTitle);
        rgDrinks = (RadioGroup) findViewById(R.id.rgDrinks);


        //for displaying sides options:
        rlMealSide = (RelativeLayout) findViewById(R.id.rlMealSide);
        tvMealSideTitle = (TextView) findViewById(R.id.tvMealSideTitle);
        rgSides = (RadioGroup) findViewById(R.id.rgSides);

        //for displaying possibleModifications options:
        llMealPossibleModifications = (LinearLayout) findViewById(R.id.llMealPossibleModifications);
        tvMealPossibleModificationsTitle = (TextView) findViewById(R.id.tvMealpossibleModificationsTitle);

        //for displaying salad options:
        llMealSalads = (LinearLayout) findViewById(R.id.llMealSalads);
        tvMealSaladsTitle = (TextView) findViewById(R.id.tvMealSaladsTitle);

        //for displaying sauces options:
        llMealSauces = (LinearLayout) findViewById(R.id.llMealSauces);
        tvMealSaucesTitle = (TextView) findViewById(R.id.tvMealSaucesTitle);

        //for receiving the meal notes from the customer:
        rlMealNotes = (RelativeLayout) findViewById(R.id.rlMealNotes);
        etMealNotes = (EditText) findViewById(R.id.etMealNotes);

        //main linear layout (parent)
        llMealSidesWrapper = (LinearLayout) findViewById(R.id.llMealSidesWrapper);

        //add to cart button
        btnAddToCart = (Button) findViewById(R.id.btnAddToCart);
    }

    private void AddToCart() {
        MealOrder currMealOrder = new MealOrder();
        currMealOrder.setNumOfDuplicationOfTheMeal(1);
        Meal orderedMeal = new Meal();
        orderedMeal.setMealName(mealToDisplay.getMealName());
        orderedMeal.setMealCost(mealToDisplay.getMealCost());
        orderedMeal.setMealIngredients(mealToDisplay.getMealIngredients());
        orderedMeal.setMealType(mealToDisplay.getMealType());
        orderedMeal.setMealURLImage(mealToDisplay.getMealURLImage());

        String mealType = orderedMeal.getMealType();
        MealSides orderdMealSides;
        ArrayList<MealSides> combinationMealSides;
        ArrayList<Meal> combinationMeals = new ArrayList<Meal>();

        if(mealType.equals("עסקיות בורגרים") || mealType.equals("עסקיות")) {
            orderdMealSides = getMeal();
            orderedMeal.setMealSides(orderdMealSides);
            currMealOrder.getOrderedMeal().add(orderedMeal);
            Toast.makeText(this, orderdMealSides.toString(), Toast.LENGTH_SHORT).show();
        }
        else if(mealType.equals("תוספות")){
            orderdMealSides = getSide();

            //set chosen cost for displaying in shopping card activity:
            String chosen = orderdMealSides.getPossibleModifications().get(0);
            int sideChosenCoast = getSideChosenCoast(chosen);
            orderedMeal.setMealCost(String.valueOf(sideChosenCoast));

            orderedMeal.setMealSides(orderdMealSides);
            currMealOrder.getOrderedMeal().add(orderedMeal);
            Toast.makeText(this, orderdMealSides.toString(), Toast.LENGTH_SHORT).show();

        }
        else if( mealType.equals("מנות בג'בטה") || mealType.equals("מנות בבגט")){
            orderdMealSides = getBagetMeal();
            orderedMeal.setMealSides(orderdMealSides);
            currMealOrder.getOrderedMeal().add(orderedMeal);
            Toast.makeText(this, orderdMealSides.toString(), Toast.LENGTH_SHORT).show();
        }
        else if(mealType.equals("קומבינציות")){
            combinationMealSides = getCombinationMeal();
            for (int i = 0; i < combinationMealSides.size() ; i++) {
                Meal orderedCombinationMeal = new Meal();
                orderedCombinationMeal.setMealName(mealToDisplay.getMealName());
                orderedCombinationMeal.setMealCost(mealToDisplay.getMealCost());
                orderedCombinationMeal.setMealIngredients(mealToDisplay.getMealIngredients());
                orderedCombinationMeal.setMealType(mealToDisplay.getMealType());
                orderedCombinationMeal.setMealURLImage(mealToDisplay.getMealURLImage());
                orderedCombinationMeal.setMealSides(combinationMealSides.get(i));
                combinationMeals.add(orderedCombinationMeal);
            }
            currMealOrder.setOrderedMeal(combinationMeals);
            Toast.makeText(this, combinationMealSides.toString(), Toast.LENGTH_LONG).show();
        }

        //edit meal order scenario:
        if(mealOrderToEdit != null){
            updateMealOrderInDataBase(currMealOrder);
            onBackPressed();
        }
        //add new meal order to cart scenario:
        else {
            uploadToDataBase(currMealOrder);
            onBackPressed();
        }
    }

    private void updateMealOrderInDataBase(MealOrder currMealOrder) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference mealOrderREF = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid).child(mealOrderDBKey);
        mealOrderREF.setValue(currMealOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MealSidesActivity.this, "השינויים עודכנו בהצלחה", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MealSidesActivity.this, "כישלון בעדכון שינויים, אנא בדוק את חיבור האינטרנט שלך", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getSideChosenCoast(String chosen) {
        int i = Integer.parseInt(chosen.replaceAll("[\\D]", ""));
        return i;
    }

    private MealSides getMeal() {
        MealSides ans = new MealSides();

        //get chosen side:(from radioBtn)
        if(!sides.get(1).equals("none")) {
            String chosenSide = getCheckedRadioButton(rgSides);
            ans.getSides().add(chosenSide);
        }

        //get chosen drink:(from radioBtn)
        if(!drinks.get(1).equals("none")) {
            String chosenDrink = getCheckedRadioButton(rgDrinks);
            ans.getDrinks().add(chosenDrink);
        }

        //get chosen sauces:(from checkBoxes)
        if(!sauces.get(1).equals("none")) {
            ans.setSauces(getCheckedBoxes(llMealSauces));
        }

        //get chosen salads:(from checkBoxes)
        if(!salads.get(1).equals("none")) {
            ans.setSalad(getCheckedBoxes(llMealSalads));
        }

        //get chosen possible modifications:(from checkBoxes)
        if(!possibleModifications.get(1).equals("none")) {
            ans.setPossibleModifications(getCheckedBoxes(llMealPossibleModifications));
        }


        //get meal notes:
        ans.setMealNotes(etMealNotes.getText().toString());

        return ans;
    }

    private MealSides getSide() {
        MealSides ans = new MealSides();
        //get chosen possible modifications:(from radioBtn)
        if(!possibleModifications.get(1).equals("none")) {
            int childCount = llMealPossibleModifications.getChildCount();
            RadioGroup radioGroup = null;
            for (int i = 0; i < childCount; i++) {
                View child = llMealPossibleModifications.getChildAt(i);
                if(child instanceof RadioGroup){
                    radioGroup = (RadioGroup) child;
                }
            }
            if(radioGroup !=null) {
                String chosen = getCheckedRadioButton(radioGroup);
                ans.getPossibleModifications().add(chosen);
            }
        }


        //get meal notes:
        ans.setMealNotes(etMealNotes.getText().toString());
        return ans;
    }

    private MealSides getBagetMeal() {
        MealSides ans = new MealSides();

        //get chosen side:(from radioBtn)
        if(!sides.get(1).equals("none")) {
            String chosenSide = getCheckedRadioButton(rgSides);
            ans.getSides().add(chosenSide);
        }

        //get chosen drink:(from radioBtn)
        if(!drinks.get(1).equals("none")) {
            String chosenDrink = getCheckedRadioButton(rgDrinks);
            ans.getDrinks().add(chosenDrink);
        }

        //get chosen sauces:(from checkBoxes)
        if(!sauces.get(1).equals("none")) {
            ans.setSauces(getCheckedBoxes(llMealSauces));
        }

        //get chosen salads:(from checkBoxes)
        if(!salads.get(1).equals("none")) {
            ans.setSalad(getCheckedBoxes(llMealSalads));
        }

        //get chosen possible modifications:(from radioBtn)
        if(!possibleModifications.get(1).equals("none")) {
            if(mealToDisplay.getMealName().contains("חזה עוף") || mealToDisplay.getMealName().contains("שניצל")) {
                int childCount = llMealPossibleModifications.getChildCount();
                RadioGroup radioGroup = null;
                for (int i = 0; i < childCount; i++) {
                    View child = llMealPossibleModifications.getChildAt(i);
                    if (child instanceof RadioGroup) {
                        radioGroup = (RadioGroup) child;
                    }
                }
                if (radioGroup != null) {
                    ans.getPossibleModifications().add(getCheckedRadioButton(radioGroup));
                }
            }
            else{
                ans.setPossibleModifications(getCheckedBoxes(llMealPossibleModifications));
            }
        }

        //get meal notes:
        ans.setMealNotes(etMealNotes.getText().toString());

        return ans;
    }

// convention1: chosen side and drink and the notes are saved only in the first mealSide.
// convention2: chosen sauces and salads of every meal are saved in the salads arrayList of that meal mealSides.
    private ArrayList<MealSides> getCombinationMeal() {
        ArrayList<MealSides> ans = new ArrayList<MealSides>();
        int i = getNumOfMealsInCombination();
        for (int j = 0; j < i; j++) {
            MealSides mealSide = new MealSides();
            LinearLayout llPossibleModifications = null;
            LinearLayout llSaucesAndSalads = null;
            switch (j){
                case 0 :
                    llPossibleModifications = (LinearLayout) findViewById(R.id.FIRST_MEAL_POSSIBLE_MODIFICATIONS);
                    llSaucesAndSalads = (LinearLayout) findViewById(R.id.FIRST_MEAL_SAUCES_AND_SALADS);
                    mealSide.getDrinks().add(getCheckedRadioButton(rgDrinks));
                    mealSide.getSides().add(getCheckedRadioButton(rgSides));
                    mealSide.setMealNotes(etMealNotes.getText().toString());
                    break;
                case 1 :
                    llPossibleModifications = (LinearLayout) findViewById(R.id.SECOND_MEAL_POSSIBLE_MODIFICATIONS);
                    llSaucesAndSalads = (LinearLayout) findViewById(R.id.SECOND_MEAL_SAUCES_AND_SALADS);
                    break;
                case 2 :
                    llPossibleModifications = (LinearLayout) findViewById(R.id.THIRD_MEAL_POSSIBLE_MODIFICATIONS);
                    llSaucesAndSalads = (LinearLayout) findViewById(R.id.THIRD_MEAL_SAUCES_AND_SALADS);
                    break;
                case 3 :
                    llPossibleModifications = (LinearLayout) findViewById(R.id.FOURTH_MEAL_POSSIBLE_MODIFICATIONS);
                    llSaucesAndSalads = (LinearLayout) findViewById(R.id.FOURTH_MEAL_SAUCES_AND_SALADS);
                    break;
            }

                RadioGroup radioGroup  = null;
                int childCount = llPossibleModifications.getChildCount();
                for (int k = 0; k < childCount; k++) {
                    View child = llPossibleModifications.getChildAt(k);
                    if(child instanceof RadioGroup){
                         radioGroup = (RadioGroup) child;
                    }
                }
                if(radioGroup != null){
                    String checkedRadioButton = getCheckedRadioButton(radioGroup);
                    mealSide.getPossibleModifications().add(checkedRadioButton);
                }
            ArrayList<String> saucesAndSalads = getCheckedBoxes(llSaucesAndSalads);
            mealSide.setSalad(saucesAndSalads);//sauces and salad choices are saved in the salads array of the meal side.
                ans.add(mealSide);
        }
        return ans;
    }


    private ArrayList<String> getCheckedBoxes(LinearLayout parentLayout){

        ArrayList<String> ans = new ArrayList<String>();
        int childCount = parentLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View nextChild = parentLayout.getChildAt(i);
            if(nextChild instanceof CheckBox)
            {
                CheckBox check = (CheckBox) nextChild;
                if (check.isChecked()) {
                    ans.add(check.getText().toString());
                }
            }
        }
        return ans;
    }

    private String getCheckedRadioButton(RadioGroup radioGroup){
        String ans;
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        View checkedRadioButtonView = radioGroup.findViewById(checkedRadioButtonId);
        int index = radioGroup.indexOfChild(checkedRadioButtonView);
        RadioButton checkedRadioBtn = (RadioButton) radioGroup.getChildAt(index);
        ans = checkedRadioBtn.getText().toString();
        return ans;
    }

    private void displayBigMeal(String mealName, String mealCost, String mealURLImage) {
        Picasso.with(this).
                load(mealURLImage).
                into(ivBigMealImage);

        tvBigMealName.setText(mealName);
        if(!mealCost.equals(" ")) {
            tvBigMealPrice.setText(mealCost + "₪");
        }
        else{
            tvBigMealPrice.setText(" ");
        }
    }

    private void initMealSides(Meal meal){
        MealSides mealSides = meal.getMealSides();
        drinks = mealSides.getDrinks();
        sides = mealSides.getSides();
        possibleModifications = mealSides.getPossibleModifications();
        salads = mealSides.getSalad();
        sauces = mealSides.getSauces();
        mealType = meal.getMealType();
    }

    private int getNumOfMealsInCombination() {
        int ans = 2;

        if(mealToDisplay.getMealName().equals("קומבינציה משולשת")){
            ans = 3;
        }
        else if(mealToDisplay.getMealName().equals("קומבינציה מרובעת")){
            ans = 4;
        }
        return ans;
    }

    private void displayCombinationMeal() {
        displayBigMeal(mealToDisplay.getMealName(), mealToDisplay.getMealCost(), mealToDisplay.getMealURLImage());

        int i = getNumOfMealsInCombination();

        llMealSalads.setVisibility(View.GONE);
        llMealSauces.setVisibility(View.GONE);
        llMealPossibleModifications.setVisibility(View.GONE);

        LayoutInflater inflater = LayoutInflater.from(this);
        int numOfMeal = 0;
        for (int j = 0; j < i*2; j++) {
            if( numOfMeal <= i)
                numOfMeal++;
            View view = inflater.inflate(R.layout.layout_for_duplicating_meal_side, null);
            LinearLayout llMealPM = (LinearLayout) view;
            setCombinationLayoutsID(llMealPM,j);   //for getting from those linear layouts the customer choices back
            TextView myTv = (TextView) llMealPM.getChildAt(0);
            if(j%2 == 0) {
                //display possible modification options:
                StringBuilder stringBuilder = new StringBuilder(" בחר סוג בשר ");
                stringBuilder.append(numOfMeal);
                stringBuilder.append(" לבחירה : " );
                myTv.setText(stringBuilder.toString());
                if (possibleModifications.get(1).equals("none")) {
                    llMealPM.setVisibility(View.GONE);
                } else {
                    RadioGroup radioGroup = new RadioGroup(this);
                    llMealPM.addView(radioGroup);
                    radioButtonDisplay(possibleModifications, radioGroup);
                }
                llMealSidesWrapper.addView(view,j+2);
            }
            else{
                //display salads and sauces options:
                numOfMeal--;
                StringBuilder stringBuilder = new StringBuilder(" סלט ורטבים לתוך מנה ");
                stringBuilder.append(numOfMeal);
                stringBuilder.append(" לבחירה : " );
                myTv.setText(stringBuilder.toString());
                if (salads.get(1).equals("none") && sauces.get(1).equals("none")) {
                    llMealPM.setVisibility(View.GONE);
                } else {
                    checkBoxDisplay(salads,llMealPM);
                    checkBoxDisplay(sauces,llMealPM);
                }
                llMealSidesWrapper.addView(view,j+2);
            }
        }

        //display sides options:
        tvMealSideTitle.setText("תוספת לבחירה :");
        if (sides.get(1).equals("none")) {
            rlMealSide.setVisibility(View.GONE);
        } else {
            radioButtonDisplay(sides,rgSides);
        }

        //display drinks:
        tvMealSidesDrinkTitle.setText("שתיה לבחירה :");
        if (drinks.get(1).equals("none")) {
            rlMealSidesDrink.setVisibility(View.GONE);
        } else {
            radioButtonDisplay(drinks,rgDrinks);
        }

        if(mealOrderToEdit != null){
            checkUserChoices(mealOrderToEdit);
        }
    }

    private void setCombinationLayoutsID(LinearLayout linearLayout, int j) {
        switch (j){
            case 0 :
                linearLayout.setId(R.id.FIRST_MEAL_POSSIBLE_MODIFICATIONS);
                break;
            case 1 :
                linearLayout.setId(R.id.FIRST_MEAL_SAUCES_AND_SALADS);
                break;
            case 2 :
                linearLayout.setId(R.id.SECOND_MEAL_POSSIBLE_MODIFICATIONS);
                break;
            case 3 :
                linearLayout.setId(R.id.SECOND_MEAL_SAUCES_AND_SALADS);
                break;
            case 4 :
                linearLayout.setId(R.id.THIRD_MEAL_POSSIBLE_MODIFICATIONS);
                break;
            case 5 :
                linearLayout.setId(R.id.THIRD_MEAL_SAUCES_AND_SALADS);
                break;
            case 6 :
                linearLayout.setId(R.id.FOURTH_MEAL_POSSIBLE_MODIFICATIONS);
                break;
            case 7 :
                linearLayout.setId(R.id.FOURTH_MEAL_SAUCES_AND_SALADS);
                break;
        }
    }

    private void displayBagetMeal() {
        displayBigMeal(mealToDisplay.getMealName(), mealToDisplay.getMealCost(), mealToDisplay.getMealURLImage());

        //display possible modification options:
        tvMealPossibleModificationsTitle.setText("בחר רוטב :");
        if (possibleModifications.get(1).equals("none")) {
            llMealPossibleModifications.setVisibility(View.GONE);
        } else {
            if(mealToDisplay.getMealName().contains("חזה עוף")) {
                RadioGroup radioGroup = new RadioGroup(this);
                llMealPossibleModifications.addView(radioGroup);
                radioButtonDisplay(possibleModifications, radioGroup);
            }
            else if(mealToDisplay.getMealName().contains("שניצל")){
                tvMealPossibleModificationsTitle.setText("בחר סוג שניצל :");
                RadioGroup radioGroup = new RadioGroup(this);
                llMealPossibleModifications.addView(radioGroup);
                radioButtonDisplay(possibleModifications, radioGroup);
            }
            else{
                tvMealPossibleModificationsTitle.setText("שינויים אפשריים במנה :");
                checkBoxDisplay(possibleModifications,llMealPossibleModifications);
            }

        }

        //display salads options:
        tvMealSaladsTitle.setText("סלט לתוך המנה לבחירה :");
        if (salads.get(1).equals("none")) {
            llMealSalads.setVisibility(View.GONE);
        } else {
            checkBoxDisplay(salads,llMealSalads);
        }

        //display sides options:
        tvMealSideTitle.setText("תוספת אישית לבחירה :");
        if (sides.get(1).equals("none")) {
            rlMealSide.setVisibility(View.GONE);
        } else {
            radioButtonDisplay(sides,rgSides);
        }

        //display drinks:
        tvMealSidesDrinkTitle.setText("שתיה לבחירה :");
        if (drinks.get(1).equals("none")) {
            rlMealSidesDrink.setVisibility(View.GONE);
        } else {
            radioButtonDisplay(drinks,rgDrinks);
        }

        //display sauses options:
        tvMealSaucesTitle.setText("רטבים לתוך המנה לבחירה :");
        if (sauces.get(1).equals("none")) {
            llMealSauces.setVisibility(View.GONE);
        } else {
            checkBoxDisplay(sauces,llMealSauces);
        }

        if(mealOrderToEdit != null){
            checkUserChoices(mealOrderToEdit);
        }

    }

    private void displaySides() {
        displayBigMeal(mealToDisplay.getMealName(), mealToDisplay.getMealCost(), mealToDisplay.getMealURLImage());

        //display possible modification options:
        tvMealPossibleModificationsTitle.setText("בחר גודל :");
        if (possibleModifications.get(1).equals("none")) {
            llMealPossibleModifications.setVisibility(View.GONE);
        } else {
            RadioGroup radioGroup = new RadioGroup(this);
            llMealPossibleModifications.addView(radioGroup);
            radioButtonDisplay(possibleModifications,radioGroup);
        }

        llMealSalads.setVisibility(View.GONE);
        rlMealSide.setVisibility(View.GONE);
        rlMealSidesDrink.setVisibility(View.GONE);
        llMealSauces.setVisibility(View.GONE);

        if(mealOrderToEdit != null){
            checkUserChoices(mealOrderToEdit);
        }
    }

    private void displayMeal() {
        displayBigMeal(mealToDisplay.getMealName(), mealToDisplay.getMealCost(), mealToDisplay.getMealURLImage());

        //display possible modifications:
        tvMealPossibleModificationsTitle.setText("שינויים אפשריים במנה :");
        if (possibleModifications.get(1).equals("none")) {
            llMealPossibleModifications.setVisibility(View.GONE);
        } else {
            checkBoxDisplay(possibleModifications,llMealPossibleModifications);
        }


        //display salads options:
        tvMealSaladsTitle.setText("סלט לבחירה :");
        if (salads.get(1).equals("none")) {
            llMealSalads.setVisibility(View.GONE);
        } else {
            checkBoxDisplay(salads,llMealSalads);
        }

        //display sides options:
        tvMealSideTitle.setText("תוספת אישית לבחירה :");
        if (sides.get(1).equals("none")) {
            rlMealSide.setVisibility(View.GONE);
        } else {
            radioButtonDisplay(sides,rgSides);
        }

        //display drinks:
        tvMealSidesDrinkTitle.setText("שתיה לבחירה :");
        if (drinks.get(1).equals("none")) {
            rlMealSidesDrink.setVisibility(View.GONE);
        } else {
            radioButtonDisplay(drinks,rgDrinks);
        }

        //display sauses options:
        tvMealSaucesTitle.setText("רטבים לבחירה :");
        if (sauces.get(1).equals("none")) {
            llMealSauces.setVisibility(View.GONE);
        } else {
            checkBoxDisplay(sauces,llMealSauces);
        }

        if(mealOrderToEdit != null){
            checkUserChoices(mealOrderToEdit);
        }
    }

    private void radioButtonDisplay(ArrayList<String> arrToDisplay, RadioGroup radioGroup){
        for (int i = 1; i < arrToDisplay.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            if(i==1){
                radioButton.setId(R.id.FIRST_RB);
            }
            else {
                radioButton.setId(i + 200);
            }
            radioButton.setText(arrToDisplay.get(i));
            radioButton.setTextColor(ContextCompat.getColor(this, R.color.white));
            radioButton.setTextSize(20);
            radioGroup.addView(radioButton);
        }
        radioGroup.check(R.id.FIRST_RB);
    }

    private void checkBoxDisplay(ArrayList<String> arrToDisplay,LinearLayout linearLayout) {
        for (int i = 1; i < arrToDisplay.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(i + 100);
            checkBox.setText(arrToDisplay.get(i));
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.white));
            checkBox.setTextSize(20);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(checkBox);

        }
    }

    private void uploadToDataBase(MealOrder mealOrder){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
      // add un indexing to the Meal orders when uploading to database. index must be managed in user level. saved in AppManager class after login
        DatabaseReference mealOrdersREF = FirebaseDatabase.getInstance().getReference().child("MealOrders").child(uid).push();
        mealOrdersREF.setValue(mealOrder).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(MealSidesActivity.this, "המנה הוספה בהצלחה לסל", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MealSidesActivity.this, "כישלון בהוספת המנה לסל, אנא בדוק את חיבור האינטרנט שלך", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //for Edit Meal Order OnClick Listener
    private void checkUserChoices(MealOrder mealOrder){
        ArrayList<Meal> orderedMeals = mealOrder.getOrderedMeal();
        ArrayList<String> possibleModifications, drinks, sides, sauces, salad;
        String mealNotes;
        if(!orderedMeals.get(0).getMealType().equals("קומבינציות")){
            Meal meal = orderedMeals.get(0);
            MealSides mealSides = meal.getMealSides();
             possibleModifications = mealSides.getPossibleModifications();
             drinks = mealSides.getDrinks();
             sides = mealSides.getSides();
             sauces = mealSides.getSauces();
             salad = mealSides.getSalad();
             mealNotes = mealSides.getMealNotes();

            check(possibleModifications,llMealPossibleModifications);
            check(drinks,rlMealSidesDrink);
            check(sides,rlMealSide);
            check(sauces, llMealSauces);
            check(salad, llMealSalads);

            etMealNotes.setText(mealNotes);
        }
        else{
            for (int i = 0; i < orderedMeals.size(); i++) {
                Meal meal = orderedMeals.get(i);
                MealSides mealSides = meal.getMealSides();
                possibleModifications = mealSides.getPossibleModifications();
                drinks = mealSides.getDrinks();
                sides = mealSides.getSides();
                salad = mealSides.getSalad();
                mealNotes = mealSides.getMealNotes();
                switch (i){
                    case 0 :
                        LinearLayout llPossibleModification = (LinearLayout) findViewById(R.id.FIRST_MEAL_POSSIBLE_MODIFICATIONS);
                        LinearLayout llSaucesAndSalads = (LinearLayout) findViewById(R.id.FIRST_MEAL_SAUCES_AND_SALADS);
                        check(possibleModifications,llPossibleModification);
                        check(salad,llSaucesAndSalads);
                        check(sides,rlMealSide);
                        check(drinks,rlMealSidesDrink);
                        etMealNotes.setText(mealNotes);
                        break;
                    case 1:
                        LinearLayout llPossibleModification1 = (LinearLayout) findViewById(R.id.SECOND_MEAL_POSSIBLE_MODIFICATIONS);
                        LinearLayout llSaucesAndSalads1 = (LinearLayout) findViewById(R.id.SECOND_MEAL_SAUCES_AND_SALADS);
                        check(possibleModifications,llPossibleModification1);
                        check(salad,llSaucesAndSalads1);
                        break;
                    case 2:
                        LinearLayout llPossibleModification2 = (LinearLayout) findViewById(R.id.THIRD_MEAL_POSSIBLE_MODIFICATIONS);
                        LinearLayout llSaucesAndSalads2 = (LinearLayout) findViewById(R.id.THIRD_MEAL_SAUCES_AND_SALADS);
                        check(possibleModifications,llPossibleModification2);
                        check(salad,llSaucesAndSalads2);
                        break;
                    case 3:
                        LinearLayout llPossibleModification3 = (LinearLayout) findViewById(R.id.FOURTH_MEAL_POSSIBLE_MODIFICATIONS);
                        LinearLayout llSaucesAndSalads3 = (LinearLayout) findViewById(R.id.FOURTH_MEAL_SAUCES_AND_SALADS);
                        check(possibleModifications,llPossibleModification3);
                        check(salad,llSaucesAndSalads3);
                        break;
                }
            }

        }
    }

    private void check(ArrayList<String> stringsToCheck, ViewGroup containingLayout){
        if(stringsToCheck.size() !=0) {
            for (int i = 0; i < stringsToCheck.size(); i++) {
                String s = stringsToCheck.get(i);
                int childCount = containingLayout.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View childAt = containingLayout.getChildAt(j);
                    if(childAt instanceof CheckBox){
                        CheckBox cb = (CheckBox) childAt;
                        if(cb.getText().equals(s)){
                            cb.setChecked(true);
                        }
                    }
                    else if(childAt instanceof RadioGroup){
                        RadioGroup radioGroup1 = (RadioGroup) childAt;
                        int childCount1 = radioGroup1.getChildCount();
                        for (int k = 0; k < childCount1; k++) {
                            RadioButton rb = (RadioButton) radioGroup1.getChildAt(k);
                            if(rb.getText().equals(s)){
                                radioGroup1.check(rb.getId());
                            }
                        }
                    }
                }
            }
        }
    }

}
