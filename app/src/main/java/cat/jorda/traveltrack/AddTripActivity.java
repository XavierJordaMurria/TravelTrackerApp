package cat.jorda.traveltrack;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import cat.jorda.traveltrack.model.DayInfo;
import cat.jorda.traveltrack.model.TripInfo;
import cat.jorda.traveltrack.model.User;
import cat.jorda.traveltrack.util.Constants;
import cat.jorda.traveltrack.viewHolders.AddTripViewHolder;

/**
 * Created by xj1 on 08/08/2017.
 */

public class AddTripActivity extends AddActivity //implements View.OnTouchListener
{
    private static String TAG = AddTripActivity.class.getSimpleName();
    private String REQUIRED = "REQUIRED";

    private AddTripViewHolder viewHolder_;

    private class YearMonthDay
    {
        int year_;
        int month_;
        int day_;

        YearMonthDay(String date)
        {
            year_   = Integer.parseInt(date.split("/")[2]);
            month_  = Integer.parseInt(date.split("/")[1]);
            day_    = Integer.parseInt(date.split("/")[0]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // [START initialize_database_ref]
        database_ = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        setContentView(R.layout.add_trip);
        baseLayout_ = (RelativeLayout) findViewById(R.id.base_popup_layout);
        viewHolder_ = new AddTripViewHolder(baseLayout_);
        baseLayout_.setOnTouchListener(this);

        viewHolder_.saveTrip_.setOnClickListener(v->saveTrip());
    }

    private void saveTrip()
    {
        final String title  = viewHolder_.title_.getText().toString();
        final String startingDate   = viewHolder_.startingDate_.getText().toString();
        final String endingDate = viewHolder_.endingDate_.getText().toString();
        final String country    = viewHolder_.country_.getText().toString();
        final String shortDescription   = viewHolder_.shortDescription_.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title))
        {
            viewHolder_.title_.setError(REQUIRED);
            return;
        }

        // startingDate_ is required
        if (TextUtils.isEmpty(startingDate))
        {
            viewHolder_.startingDate_.setError(REQUIRED);
            return;
        }

        // endingDate_ is required
        if (TextUtils.isEmpty(endingDate))
        {
            viewHolder_.endingDate_.setError(REQUIRED);
            return;
        }

        // country is required
        if (TextUtils.isEmpty(country))
        {
            viewHolder_.country_.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        viewHolder_.saveTrip_.setEnabled(false);
        Toast.makeText(this, "Posting Trip...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        database_.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null)
                        {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddTripActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                            {
                            // Write new trip
                            writeNewTrip(userId, user.username,
                                    title, startingDate,
                                    endingDate, country, shortDescription);
                        }

                        // Finish this Activity, back to the stream
                        viewHolder_.saveTrip_.setEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        viewHolder_.saveTrip_.setEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    // [START write_fan_out]
    private void writeNewTrip(String userId, String username,
                              String title, String startingDate,
                              String endingDate, String country, String shortDescripton)
    {
        // Create new trip at /user-trips/$userid/$tripid and at
        // /trips/$tripid simultaneously
        String tripKey = database_.child("trips").push().getKey();
        TripInfo tripInfo = new TripInfo(userId, username, title, shortDescripton, startingDate, endingDate, country);
        Map<String, Object> postValues = tripInfo.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/trips/" + tripKey, postValues);
        childUpdates.put(Constants.USER_TRIPS + userId + "/" + tripKey, postValues);
        database_.updateChildren(childUpdates);

        writeDays4Trip(userId, tripKey, tripInfo);
    }

    private void writeDays4Trip(String userId, String tripKey, TripInfo tripInfo)
    {
        long numberDays = dateDiff(new YearMonthDay(tripInfo.startDate_), new YearMonthDay(tripInfo.endDate_));

        for (int i = 0; i <= numberDays; i++)
        {
            String dayKey = database_.child("days").push().getKey();
            DayInfo dayInfo = new DayInfo(userId, tripKey, "Day "+i, "", plusDate(tripInfo.startDate_,i));
            Map<String, Object> postValues = dayInfo.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/days/" + dayKey, postValues);
            childUpdates.put("/trips-days/" + tripKey + "/" + dayKey, postValues);
            database_.updateChildren(childUpdates);
        }
    }
    // [END write_fan_out]

    /**
     * DateDiff -- compute the difference between two dates.
     */
    private long dateDiff(YearMonthDay startDate, YearMonthDay endDate)
    {
        Date d1 = new GregorianCalendar(startDate.year_, startDate.month_ -1, startDate.day_, 23, 59).getTime();
        Date d2 = new GregorianCalendar(endDate.year_, endDate.month_-1, endDate.day_, 23, 59).getTime();

        // Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();
        long daysDiff = (diff / (1000 * 60 * 60 * 24));

        return daysDiff;
    }

    private String plusDate(String startDate, int numDays2Add)
    {
        YearMonthDay yearMonthDay = new YearMonthDay(startDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = new GregorianCalendar(yearMonthDay.year_, yearMonthDay.month_-1, yearMonthDay.day_, 23, 59).getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(d1); // Now use today date.
        c.add(Calendar.DATE, numDays2Add); // Adding 5 days
        String output = sdf.format(c.getTime());

        return output;
    }
}

