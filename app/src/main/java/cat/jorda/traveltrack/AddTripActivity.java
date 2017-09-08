package cat.jorda.traveltrack;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

/**
 * Created by xj1 on 08/08/2017.
 */

public class AddTripActivity extends AddActivity //implements View.OnTouchListener
{
    private static String TAG = AddTripActivity.class.getSimpleName();
    private String REQUIRED = "REQUIRED";
    private RelativeLayout baseLayout_;

    private int previousFingerPosition_ = 0;
    private int baseLayoutPosition_ = 0;
    private int defaultViewHeight_;

    private boolean isClosing_ = false;
    private boolean isScrollingUp_ = false;
    private boolean isScrollingDown_ = false;

    // [START declare_database_ref]
    private DatabaseReference database_;
    // [END declare_database_ref]

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
    protected void onCreate(Bundle savedInstanceState){
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

    @Override
    public void onTouchActionDown(int rawY)
    {
        // save default base layout height
        defaultViewHeight_ = baseLayout_.getHeight();

        // Init finger and view position
        previousFingerPosition_ = rawY;
        baseLayoutPosition_ = (int) baseLayout_.getY();
    }

    @Override
    public void onTouchActionUp()
    {
        // If user was doing a scroll up
        if(isScrollingUp_)
        {
            // Reset baselayout position
            baseLayout_.setY(0);
            // We are not in scrolling up mode anymore
            isScrollingUp_ = false;
        }

        // If user was doing a scroll down
        if(isScrollingDown_){
            // Reset baselayout position
            baseLayout_.setY(0);
            // Reset base layout size
            baseLayout_.getLayoutParams().height = defaultViewHeight_;
            baseLayout_.requestLayout();
            // We are not in scrolling down mode anymore
            isScrollingDown_ = false;
        }
    }

    @Override
    public boolean onTouchActionMove(int rawY)
    {
        if(!isClosing_)
        {
            int currentYPosition = (int) baseLayout_.getY();

            // If we scroll up
            if(previousFingerPosition_ >rawY)
            {
                // First time android rise an event for "up" move
                if(!isScrollingUp_){
                    isScrollingUp_ = true;
                }

                // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                if(baseLayout_.getHeight()<defaultViewHeight_){
                    baseLayout_.getLayoutParams().height = baseLayout_.getHeight() - (rawY - previousFingerPosition_);
                    baseLayout_.requestLayout();
                }
                else {
                    // Has user scroll enough to "auto close" popup ?
                    if ((baseLayoutPosition_ - currentYPosition) > defaultViewHeight_ / 4) {
                        closeUpAndDismissDialog(currentYPosition);
                        return true;
                    }
                }

                baseLayout_.setY(baseLayout_.getY() + (rawY - previousFingerPosition_));
            }
            // If we scroll down
            else{
                // First time android rise an event for "down" move
                if(!isScrollingDown_){
                    isScrollingDown_ = true;
                }

                // Has user scroll enough to "auto close" popup ?
                if (Math.abs(baseLayoutPosition_ - currentYPosition) > defaultViewHeight_ / 2)
                {
                    closeDownAndDismissDialog(currentYPosition);
                    return true;
                }

                // Change base layout size and position (must change position because view anchor is top left corner)
                baseLayout_.setY(baseLayout_.getY() + (rawY - previousFingerPosition_));
                baseLayout_.getLayoutParams().height = baseLayout_.getHeight() - (rawY - previousFingerPosition_);
                baseLayout_.requestLayout();
            }

            // Update position
            previousFingerPosition_ = rawY;
        }

        return false;
    }

    private void saveTrip()
    {
        final String title  = viewHolder_.title_.getText().toString();
        final String startingDate   = viewHolder_.startingDate_.getText().toString();
        final String endingDate = viewHolder_.endingDate_.getText().toString();
        final String country    = viewHolder_.country_.getText().toString();
        final String shortDescription   = viewHolder_.shortDescription_.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            viewHolder_.title_.setError(REQUIRED);
            return;
        }

        // startingDate_ is required
        if (TextUtils.isEmpty(startingDate)) {
            viewHolder_.startingDate_.setError(REQUIRED);
            return;
        }

        // endingDate_ is required
        if (TextUtils.isEmpty(endingDate)) {
            viewHolder_.endingDate_.setError(REQUIRED);
            return;
        }

        // country is required
        if (TextUtils.isEmpty(country)) {
            viewHolder_.country_.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        viewHolder_.saveTrip_.setEnabled(false);
        Toast.makeText(this, "Posting Trip...", Toast.LENGTH_SHORT).show();

        // [START single_value_read]
        final String userId = getUid();
        database_.child("users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        User user = dataSnapshot.getValue(User.class);

                        // [START_EXCLUDE]
                        if (user == null) {
                            // User is null, error out
                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(AddTripActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
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
                    public void onCancelled(DatabaseError databaseError) {
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
        childUpdates.put("/user-trips/" + userId + "/" + tripKey, postValues);
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

    private void closeUpAndDismissDialog(int currentPosition){
        isClosing_ = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(baseLayout_, "y", currentPosition, -baseLayout_.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {}
            @Override
            public void onAnimationCancel(Animator animation)
            {}
            @Override
            public void onAnimationRepeat(Animator animation)
            {}

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }
        });
        positionAnimator.start();
    }

    private void closeDownAndDismissDialog(int currentPosition){
        isClosing_ = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(baseLayout_, "y", currentPosition, screenHeight + baseLayout_.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {}
            @Override
            public void onAnimationCancel(Animator animation)
            {}
            @Override
            public void onAnimationRepeat(Animator animation)
            {}
            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }
        });
        positionAnimator.start();
    }

    /**
     * DateDiff -- compute the difference between two dates.
     */
    public long dateDiff(YearMonthDay startDate, YearMonthDay endDate) {
        Date d1 = new GregorianCalendar(startDate.year_, startDate.month_ -1, startDate.day_, 23, 59).getTime();
        Date d2 = new GregorianCalendar(endDate.year_, endDate.month_-1, endDate.day_, 23, 59).getTime();

        // Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();

        long daysDiff = (diff / (1000 * 60 * 60 * 24));
        Log.d(TAG,"The 21st century (up to " + d2 + ") is " + daysDiff + " days old.");

        return daysDiff;
    }

    public String plusDate(String startDate, int numDays2Add)
    {
        YearMonthDay yearMonthDay = new YearMonthDay(startDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d1 = new GregorianCalendar(yearMonthDay.year_, yearMonthDay.month_-1, yearMonthDay.day_, 23, 59).getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(d1); // Now use today date.
        c.add(Calendar.DATE, numDays2Add); // Adding 5 days
        String output = sdf.format(c.getTime());

        Log.d(TAG,"plusDate = " + output);

        return output;
    }
}

