package cat.jorda.traveltrack.dayDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.util.Constants;

/**
 * Created by xj1 on 22/08/2017.
 */

public class NotesDayFragment extends DayFragments
{
    private static String TAG = NotesDayFragment.class.getSimpleName();

//    private String tripKey_;
//    private String dayKey_;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Log.d(TAG, "onCreate");
//
//        tripKey_ = getArguments().getString(Constants.TRIP_KEY);
//        dayKey_ = getArguments().getString(Constants.DAY_KEY);
//    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

//        if(context instanceof DayListFragment.ItemSelectedListener)  // context instanceof YourActivity
//            this.listener_ = (DayListFragment.ItemSelectedListener) context; // = (YourActivity) context
//        else
//            throw new ClassCastException(context.toString()
//                    + " must implement StepsFragment.OnItemSelectedListener");
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.note, container, false);

        final EditText editText = (EditText)rootView.findViewById(R.id.note);

        editText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s)
            {
                String text = editText.getText().toString();
                int  textLength = editText.getText().length();

            }
        });

        return rootView;
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference)
    {
        // All my days
        return databaseReference.child(Constants.DAY_MARKERS_TAB)
                .child(dayKey_);
    }
}