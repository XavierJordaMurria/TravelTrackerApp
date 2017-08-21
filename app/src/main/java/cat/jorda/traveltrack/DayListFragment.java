package cat.jorda.traveltrack;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import cat.jorda.traveltrack.model.DayInfo;
import cat.jorda.traveltrack.util.Constants;

public class DayListFragment extends Fragment {

    private static final String TAG = DayListFragment.class.getSimpleName();

    // [START define_database_reference]
    private DatabaseReference database_;
    // [END define_database_reference]

    private ItemSelectedListener listener_;
    private FirebaseRecyclerAdapter<DayInfo, DayViewHolder> adapter_;
    private RecyclerView recycler_;
    private LinearLayoutManager manager_;
    private String tripKey_;

    // Define the events that the fragment will use to communicate
    public interface ItemSelectedListener
    {
        // This can be any number of events to be sent to the activity
        void onDaySelected(String tripSelectedKey);
    }

    public DayListFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
        // Get back arguments
        if (getArguments() == null)
            return;

        tripKey_ = getArguments().getString(Constants.TRIP_KEY, "");
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        if(context instanceof ItemSelectedListener)  // context instanceof YourActivity
            this.listener_ = (ItemSelectedListener) context; // = (YourActivity) context
        else
            throw new ClassCastException(context.toString()
                    + " must implement StepsFragment.OnItemSelectedListener");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "onCreonCreateView");

        View rootView = inflater.inflate(R.layout.list_view, container, false);

        // [START create_database_reference]
        database_ = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        recycler_ = (RecyclerView) rootView.findViewById(R.id.items_list);
        recycler_.setHasFixedSize(true);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated");

        // Set up Layout Manager, reverse layout
        manager_ = new GridLayoutManager(getActivity(), getCardsNumberOnScreen());
//        manager_.setReverseLayout(true);
//        manager_.setStackFromEnd(true);
        recycler_.setLayoutManager(manager_);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(database_);
        adapter_ = new FirebaseRecyclerAdapter<DayInfo, DayViewHolder>(DayInfo.class, R.layout.day_item,
                DayViewHolder.class, postsQuery) {

            @Override
            protected void populateViewHolder(final DayViewHolder viewHolder, final DayInfo model, final int position) {
                final DatabaseReference dayRef = getRef(position);

                // Set click listener for the whole post view
                final String dayKey = dayRef.getKey();
                viewHolder.itemView.setOnClickListener(v -> {
                    //Load the day details screen
                    listener_.onDaySelected(dayKey);
                });

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToTrip(model, starView -> {
                    // Need to write to both places the post is stored
//                    DatabaseReference globalPostRef = database_.child("posts").child(postRef.getKey());
//                    DatabaseReference userPostRef = database_.child("user-posts").child(model.uid).child(postRef.getKey());
//
//                    // Run two transactions
//                    onStarClicked(globalPostRef);
//                    onStarClicked(userPostRef);
                });
            }
        };
        recycler_.setAdapter(adapter_);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter_ != null) {
            adapter_.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery(DatabaseReference databaseReference) {
        // All my posts
        return databaseReference.child("trips-days")
                .child(tripKey_);
    }

    private int getCardsNumberOnScreen() {
        int cardsNumber;

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                cardsNumber = 3;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                cardsNumber = 1;
                break;
            default:
                cardsNumber = 1;
        }
        return cardsNumber;
    }

}
