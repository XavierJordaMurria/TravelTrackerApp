package cat.jorda.traveltrack.dayDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import cat.jorda.traveltrack.AddNoteActivity;
import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.Note;
import cat.jorda.traveltrack.util.Constants;
import cat.jorda.traveltrack.viewHolders.NoteViewHolder;

/**
 * Created by xj1 on 22/08/2017.
 */

public class NotesDayFragment extends DayFragments
{
    private static String TAG = NotesDayFragment.class.getSimpleName();

    private FirebaseRecyclerAdapter<Note, NoteViewHolder> adapter_;
    public RecyclerView recycler_;
    private LinearLayoutManager manager_;

    private FloatingActionButton fab_;

    private INotesDayFragment delegate_;

    public interface INotesDayFragment
    {
        void onFabClicked();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(context instanceof INotesDayFragment)  // context instanceof YourActivity
            this.delegate_ = (INotesDayFragment) context; // = (YourActivity) context
        else
            throw new ClassCastException(context.toString()
                    + " must implement StepsFragment.OnItemSelectedListener");
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.list_view, container, false);

        recycler_ = (RecyclerView) rootView.findViewById(R.id.items_list);
        recycler_.setHasFixedSize(true);

        fab_ = (FloatingActionButton) rootView.findViewById(R.id.list_view_fab);
        fab_.setOnClickListener(view -> onFabClick(view));
        fab_.setVisibility(View.VISIBLE);

        fab_.setContentDescription(getString(R.string.notesFABDescription));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        manager_ = new LinearLayoutManager(getActivity());
        manager_.setReverseLayout(true);
        manager_.setStackFromEnd(true);
        recycler_.setLayoutManager(manager_);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(database_);

        adapter_ = new FirebaseRecyclerAdapter<Note, NoteViewHolder>(Note.class, R.layout.note_item,
                NoteViewHolder.class, postsQuery)
        {
            @Override
            protected void populateViewHolder(final NoteViewHolder viewHolder, final Note model, final int position)
            {
                final DatabaseReference noteRef = getRef(position);

                // Set click listener for the whole post view
                final String noteKey = noteRef.getKey();
                viewHolder.itemView.setOnClickListener(v -> {
                    // Load Days for the trip.
//                    listener_.onTripSelected(expensesKey);
                });

                if (model == null)
                    return;

                // Bind Expenses to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToNote( model, starView -> {
                });
            }
        };

        recycler_.setAdapter(adapter_);
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference)
    {
        // All my days
        return databaseReference.child(Constants.DAY_NOTES)
                .child(dayKey_);
    }

    private void onFabClick(View view)
    {
        Log.d(TAG, "onFabClick");
        delegate_.onFabClicked();
        Intent intent = new Intent(getActivity(), AddNoteActivity.class);
        intent.putExtra(Constants.DAY_KEY, dayKey_);

        startActivity(intent);
    }
}