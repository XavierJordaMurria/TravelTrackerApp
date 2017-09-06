package cat.jorda.traveltrack.dayDetails;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import cat.jorda.traveltrack.BaseActivity;
import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.SignInActivity;
import cat.jorda.traveltrack.model.CustomMarker;
import cat.jorda.traveltrack.model.DayInfo;
import cat.jorda.traveltrack.model.MarkerType;
import cat.jorda.traveltrack.util.Constants;

/**
 * Created by xj1 on 21/08/2017.
 */

public class  DayDetailsActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private FragmentPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;

    private String tripKey_;
    private String dayKey_;
    // [START declare_database_ref]
    private DatabaseReference database_;
    // [END declare_database_ref]

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day_details_activity);

        Intent intent = this.getIntent();
        tripKey_ = intent.getStringExtra(Constants.TRIP_KEY);
        dayKey_ = intent.getStringExtra(Constants.DAY_KEY);

        getSupportActionBar().setTitle("Days");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Create the adapter that will return a fragment for each section
        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
            private final Fragment[] mFragments = new Fragment[] {
                    new MapDayFragment(),
                    new FinancesDayFragment(),
                    new NotesDayFragment(),
            };
            private final String[] mFragmentNames = new String[] {
                    getString(R.string.day_maps),
                    getString(R.string.day_finances),
                    getString(R.string.day_notes)
            };
            @Override
            public Fragment getItem(int position) {
                return mFragments[position];
            }
            @Override
            public int getCount() {
                return mFragments.length;
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentNames[position];
            }
        };

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.day_container);
        mViewPager.setAdapter(mPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        findViewById(R.id.fab_day_details).setOnClickListener(v -> {
//                startActivity(new Intent(DayDetailsActivity.this, NewPostActivity.class));
        });

        // [START initialize_database_ref]
        database_ = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]
    }

    void saveMarker(Marker marker)
    {
        String markerKey = database_.child("marker").push().getKey();

        CustomMarker customMarker = new CustomMarker("", tripKey_, dayKey_,marker, MarkerType.MARKER_LOCATION);
        Map<String, Object> postValues = customMarker.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/customMarkers/" + markerKey, postValues);
        childUpdates.put("/days-customMarkers/" + dayKey_ + "/" + markerKey, postValues);
        database_.updateChildren(childUpdates);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_day_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
