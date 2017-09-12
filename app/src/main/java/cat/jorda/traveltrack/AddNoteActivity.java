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
import cat.jorda.traveltrack.model.Note;
import cat.jorda.traveltrack.model.User;
import cat.jorda.traveltrack.util.Constants;
import cat.jorda.traveltrack.viewHolders.AddExpensesViewHolder;
import cat.jorda.traveltrack.viewHolders.AddNotesViewHolder;

/**
 * Created by xj1 on 08/08/2017.
 */

public class AddNoteActivity extends AddActivity
{
    private static String TAG = AddExpensesActivity.class.getSimpleName();
    private String REQUIRED = "REQUIRED";

    private AddNotesViewHolder viewHolder_;
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

        setContentView(R.layout.add_note);
        baseLayout_ = (RelativeLayout) findViewById(R.id.base_popup_layout);
        viewHolder_ = new AddNotesViewHolder(baseLayout_);
        baseLayout_.setOnTouchListener(this);

        viewHolder_.saveExpenses_.setOnClickListener(v->saveNote());
    }

    private void saveNote()
    {

        final String note  = viewHolder_.note_.getText().toString();

        // name is required
        if (TextUtils.isEmpty(note))
        {
            viewHolder_.note_.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        viewHolder_.saveExpenses_.setEnabled(false);
        Toast.makeText(this, "Posting Note...", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(AddNoteActivity.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                            writeNewNote(userId, dayKey_, note);

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
    private void writeNewNote(String userId, String dayKey, String note)
    {
        // Create new trip at /user-trips/$userid/$tripid and at
        // /trips/$tripid simultaneously
        String notesKey = database_.child(Constants.NOTES).push().getKey();
        Note newNote = new Note(userId, note);
        Map<String, Object> postValues = newNote.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + Constants.NOTES + "/" + notesKey, postValues);
        childUpdates.put("/" + Constants.DAY_NOTES + "/" + dayKey + "/" + notesKey, postValues);

        database_.updateChildren(childUpdates);
    }
    // [END write_fan_out]
}
