package cat.jorda.traveltrack;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import cat.jorda.traveltrack.model.Expenses;
import cat.jorda.traveltrack.model.User;
import cat.jorda.traveltrack.util.Constants;
import cat.jorda.traveltrack.viewHolders.AddExpensesViewHolder;

/**
 * Created by xj1 on 08/08/2017.
 */

public class AddExpensesActivity extends AddActivity //implements View.OnTouchListener
{
    private static String TAG = AddExpensesActivity.class.getSimpleName();
    private String REQUIRED = "REQUIRED";

    private AddExpensesViewHolder viewHolder_;

    private String dayKey_;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = this.getIntent();
        dayKey_ = intent.getStringExtra(Constants.DAY_KEY);

        // [START initialize_database_ref]
        database_ = FirebaseDatabase.getInstance().getReference();
        // [END initialize_database_ref]

        setContentView(R.layout.add_expenses);
        baseLayout_ = (RelativeLayout) findViewById(R.id.base_popup_layout);
        viewHolder_ = new AddExpensesViewHolder(baseLayout_);
        baseLayout_.setOnTouchListener(this);

        viewHolder_.saveExpenses_.setOnClickListener(v->saveExpenses());
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
