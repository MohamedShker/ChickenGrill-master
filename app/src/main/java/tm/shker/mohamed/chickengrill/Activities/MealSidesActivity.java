package tm.shker.mohamed.chickengrill.Activities;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
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
    private String mealType;

    ArrayList<String> possibleModifications, salads, sides, drinks, sauces;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_sides);
        Bundle bundle = this.getIntent().getExtras();
        mealToDisplay = (Meal) bundle.getSerializable(Constants.MEAL_OPJECT);
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

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddToCart(v);
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void AddToCart(View v) {
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
                ans.getPossibleModifications().add(getCheckedRadioButton(radioGroup));
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
                    radioButtonDislay(possibleModifications, radioGroup);
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
            radioButtonDislay(sides,rgSides);
        }

        //display drinks:
        tvMealSidesDrinkTitle.setText("שתיה לבחירה :");
        if (drinks.get(1).equals("none")) {
            rlMealSidesDrink.setVisibility(View.GONE);
        } else {
            radioButtonDislay(drinks,rgDrinks);
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
                radioButtonDislay(possibleModifications, radioGroup);
            }
            else if(mealToDisplay.getMealName().contains("שניצל")){
                tvMealPossibleModificationsTitle.setText("בחר סוג שניצל :");
                RadioGroup radioGroup = new RadioGroup(this);
                llMealPossibleModifications.addView(radioGroup);
                radioButtonDislay(possibleModifications, radioGroup);
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
            radioButtonDislay(sides,rgSides);
        }

        //display drinks:
        tvMealSidesDrinkTitle.setText("שתיה לבחירה :");
        if (drinks.get(1).equals("none")) {
            rlMealSidesDrink.setVisibility(View.GONE);
        } else {
            radioButtonDislay(drinks,rgDrinks);
        }

        //display sauses options:
        tvMealSaucesTitle.setText("רטבים לתוך המנה לבחירה :");
        if (sauces.get(1).equals("none")) {
            llMealSauces.setVisibility(View.GONE);
        } else {
            checkBoxDisplay(sauces,llMealSauces);
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
            radioButtonDislay(possibleModifications,radioGroup);
        }

        llMealSalads.setVisibility(View.GONE);
        rlMealSide.setVisibility(View.GONE);
        rlMealSidesDrink.setVisibility(View.GONE);
        llMealSauces.setVisibility(View.GONE);
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
            radioButtonDislay(sides,rgSides);
        }

        //display drinks:
        tvMealSidesDrinkTitle.setText("שתיה לבחירה :");
        if (drinks.get(1).equals("none")) {
            rlMealSidesDrink.setVisibility(View.GONE);
        } else {
            radioButtonDislay(drinks,rgDrinks);
        }

        //display sauses options:
        tvMealSaucesTitle.setText("רטבים לבחירה :");
        if (sauces.get(1).equals("none")) {
            llMealSauces.setVisibility(View.GONE);
        } else {
            checkBoxDisplay(sauces,llMealSauces);
        }
    }

    private void radioButtonDislay(ArrayList<String> arrToDisplay,RadioGroup radioGroup){
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

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MealSides Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
