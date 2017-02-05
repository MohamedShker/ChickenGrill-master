package tm.shker.mohamed.chickengrill.Managers;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by mohamed on 01/10/2016.
 * reson of creation: its one of the demands to be able to login with facebook.
 */

public class AppManager extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        //for offline mode( saving data on the device disk):
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    //AFTER LOGIN
// TODO: 05/02/2017 init mealorder indixing after succesful login

}
