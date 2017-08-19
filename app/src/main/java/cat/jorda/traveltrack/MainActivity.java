package cat.jorda.traveltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends BaseActivity {

    private enum loadFrgType_ {ADD_FRG, REPLACE_FRG};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_new_trip);
        fab.setOnClickListener(view -> onFabClick(view));

        loadFragment(new TripListFragment(), loadFrgType_.REPLACE_FRG, R.id.recipe_steps_fragment_container, null);
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
        if (id == R.id.action_settings) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onFabClick(View view)
    {
        startActivity(new Intent(this, AddTripActivity.class));
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void loadFragment(Fragment newFragment, loadFrgType_ loadFrgType, int containerViewID, Bundle args) {
        if (args != null)
            newFragment.setArguments(args);

        FragmentManager frgManager = getSupportFragmentManager();

        if (loadFrgType == loadFrgType_.ADD_FRG) {
            frgManager
                    .beginTransaction()
                    .add(containerViewID, newFragment)
                    .commit();
        } else {
            frgManager
                    .beginTransaction()
                    .replace(containerViewID, newFragment)
                    .commit();
        }
    }
}
