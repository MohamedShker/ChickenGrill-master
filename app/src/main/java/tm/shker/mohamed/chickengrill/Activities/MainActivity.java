package tm.shker.mohamed.chickengrill.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import tm.shker.mohamed.chickengrill.Adapters.ViewPagerAdapter;
import tm.shker.mohamed.chickengrill.Fragments.MenuFragment;
import tm.shker.mohamed.chickengrill.Objects.User;
import tm.shker.mohamed.chickengrill.R;

public class MainActivity extends AppCompatActivity  {
    private ViewPager viewPager;
    private TabLayout tabs;
    private ImageView background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabMainActivity);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ShoppingCartActivity.class);
                startActivity(intent);
            }
        });

        /*added---------------------------------------------------*/
        testLogin();

        //set adapter to the viewpager
        ArrayList<Fragment> myFrags = new ArrayList<Fragment>();

        myFrags.add(MenuFragment.newInstance("עסקיות בורגרים"));
        myFrags.add(MenuFragment.newInstance("עסקיות"));
        myFrags.add(MenuFragment.newInstance("קומבינציות"));
        myFrags.add(MenuFragment.newInstance("מנות בג'בטה"));
        myFrags.add(MenuFragment.newInstance("מנות בבגט"));
        myFrags.add(MenuFragment.newInstance("תוספות"));
        myFrags.add(MenuFragment.newInstance("שתיה קלה"));


        ViewPagerAdapter pagerAdapter =
                new ViewPagerAdapter(getSupportFragmentManager(), myFrags);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(pagerAdapter);

        //set tabs
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //set background
        background = (ImageView)findViewById(R.id.ivMainActivityBackground);
        Picasso.with(this).load(R.drawable.main_activity_background).error(R.mipmap.ic_launcher).into(background);

    }

    private void testLogin() {
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser!=null){
                    User currUser = new User(currentUser.getEmail());
                    Toast.makeText(MainActivity.this, "Hello, "
                                    + currUser.getDisplayName(),
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this,
                            LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){
            return true;
        }

        if(id == R.id.action_logout) {
            //sign out from firebase
            FirebaseAuth.getInstance().signOut();
            //sign out from facebook
            LoginManager.getInstance().logOut();
            return true;
        }
        if(id == R.id.action_about_us){
            Intent intent = new Intent(this,AboutUsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}