package cat.jorda.traveltrack;

import android.animation.Animator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;

import cat.jorda.traveltrack.dayDetails.DayDetailsActivity;
import cat.jorda.traveltrack.util.Constants;

import static cat.jorda.traveltrack.util.Constants.TRIP_KEY;

public class MainActivity extends BaseActivity implements  TripListFragment.ItemSelectedListener, DayListFragment.ItemSelectedListener
{
    private final String TAG = MainActivity.class.getSimpleName();

    private enum loadFrgType_ {ADD_FRG, REPLACE_FRG};

    private View fadeBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout details = (FrameLayout) findViewById(R.id.days_fragment_container);
        fadeBackground = findViewById(R.id.fadeBackground);

        Bundle args = new Bundle();

        if (savedInstanceState == null)
        {
            loadFragment(new TripListFragment(), loadFrgType_.REPLACE_FRG, R.id.trips_fragment_container, null);

            if (details != null)
            {
                args.putBoolean(Constants.IS_TABLET, true);
                loadFragment(new DayListFragment(), loadFrgType_.REPLACE_FRG, R.id.days_fragment_container, args);
            }

        }
        else if (currentFragment() instanceof TripListFragment &&
                (details != null))
        {
            args.putBoolean(Constants.IS_TABLET, true);
            loadFragment(new TripListFragment(), loadFrgType_.REPLACE_FRG, R.id.trips_fragment_container, null);
            loadFragment(new DayListFragment(), loadFrgType_.REPLACE_FRG, R.id.days_fragment_container, args);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume");

        fadeBackground.setVisibility(View.GONE);
    }


    @Override
    public void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause");

        fadeBackground.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        }
        else if (item.getItemId() == android.R.id.home)
        {
            loadFragment(new TripListFragment(), loadFrgType_.REPLACE_FRG, R.id.trips_fragment_container, null);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTripSelected(String tripSelectedKey)
    {
        DayListFragment dayListFrag = new DayListFragment();
        Bundle args = new Bundle();
        args.putString(Constants.TRIP_KEY, tripSelectedKey);

        FrameLayout daysFrag = (FrameLayout) findViewById(R.id.days_fragment_container);

        if (daysFrag == null)
                loadFragment(dayListFrag, loadFrgType_.REPLACE_FRG, R.id.trips_fragment_container, args);
        else
        {
            args.putBoolean(Constants.IS_TABLET, true);
            loadFragment(dayListFrag, loadFrgType_.REPLACE_FRG, R.id.days_fragment_container, args);
        }
    }

    @Override
    public void onDaySelected(String daySelectedKey, String tripKey)
    {
        Intent intent = new Intent(this, DayDetailsActivity.class);
        intent.putExtra(Constants.TRIP_KEY, tripKey);
        intent.putExtra(Constants.DAY_KEY, daySelectedKey);
        startActivity(intent);
    }

    private Fragment currentFragment()
    {
        return getSupportFragmentManager().findFragmentById(R.id.trips_fragment_container);
    }

    private void loadFragment(Fragment newFragment, loadFrgType_ loadFrgType, int containerViewID, Bundle args)
    {
        if (args != null)
            newFragment.setArguments(args);

        FragmentManager frgManager = getSupportFragmentManager();

        if (loadFrgType == loadFrgType_.ADD_FRG)
        {
            frgManager
                    .beginTransaction()
                    .add(containerViewID, newFragment)
                    .commit();
        }
        else
        {
            frgManager
                    .beginTransaction()
                    .replace(containerViewID, newFragment)
                    .commit();
        }
    }
}
