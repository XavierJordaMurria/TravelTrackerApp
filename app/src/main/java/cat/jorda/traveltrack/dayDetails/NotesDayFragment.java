package cat.jorda.traveltrack.dayDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cat.jorda.traveltrack.AddExpensesActivity;
import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.Expenses;
import cat.jorda.traveltrack.model.User;
import cat.jorda.traveltrack.util.Constants;

/**
 * Created by xj1 on 22/08/2017.
 */

public class NotesDayFragment extends DayFragments
{
    private static String TAG = NotesDayFragment.class.getSimpleName();

    private String dayKey_;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        dayKey_ = getArguments().getString(Constants.DAY_KEY);
    }

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

    private void saveExpenses()
    {
        final String name  = viewHolder_.name_.getText().toString();
        final String amount   = viewHolder_.amount_.getText().toString();
        final float amountFloat = Float.parseFloat(amount);

        // name is required
        if (TextUtils.isEmpty(name))
        {
            viewHolder_.name_.setError(REQUIRED);
            return;
        }

        // amount is required
        if (TextUtils.isEmpty(amount))
        {
            viewHolder_.amount_.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        viewHolder_.saveExpenses_.setEnabled(false);
        Toast.makeText(this, "Posting Expenses...", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AddExpensesActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // Write new trip
                            writeNewExpenses(userId, dayKey_,
                                    name, amountFloat);
                        }

                        // Finish this Activity, back to the stream
                        viewHolder_.saveExpenses_.setEnabled(true);
                        finish();
                        // [END_EXCLUDE]
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // [START_EXCLUDE]
                        viewHolder_.saveExpenses_.setEnabled(true);
                        // [END_EXCLUDE]
                    }
                });
        // [END single_value_read]
    }

    // [START write_fan_out]
    private void writeNewExpenses(String userId, String dayKey,
                                  String name, Float amount)
    {
        // Create new trip at /user-trips/$userid/$tripid and at
        // /trips/$tripid simultaneously
        String expensesKey = database_.child("expenses").push().getKey();
        Expenses expenses = new Expenses(userId, name, amount);
        Map<String, Object> postValues = expenses.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Constants.EXPENSES + "/" + expensesKey, postValues);
        childUpdates.put("/" + Constants.DAY_EXPENSES + "/" + dayKey_ + "/" + expensesKey, postValues);

        database_.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}