package cat.jorda.traveltrack;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by xj1 on 08/09/2017.
 */

public class MainApplication extends Application
{
    private static final String TAG =  MainApplication.class.getSimpleName();

    public void onCreate()
    {
        super.onCreate();

        Log.d(TAG, "onCreate");
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
