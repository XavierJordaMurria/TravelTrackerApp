package cat.jorda.traveltrack.dayDetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import cat.jorda.traveltrack.util.Constants;

/**
 * Created by xj1 on 22/08/2017.
 */

public abstract class DayFragments extends Fragment
{

    private static String TAG = NotesDayFragment.class.getSimpleName();
    protected String tripKey_;
    protected String dayKey_;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");

        tripKey_ = getArguments().getString(Constants.TRIP_KEY);
        dayKey_ = getArguments().getString(Constants.DAY_KEY);
    }

    public abstract Query getQuery(DatabaseReference databaseReference);
}
