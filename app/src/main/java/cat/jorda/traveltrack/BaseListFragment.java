package cat.jorda.traveltrack;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import cat.jorda.traveltrack.model.TripInfo;

public abstract class BaseListFragment extends Fragment {

    private static final String TAG = "BaseListFragment";

    // [START define_database_reference]
    private DatabaseReference database_;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<TripInfo, TripViewHolder> adapter_;
    private RecyclerView recycler_;
    private LinearLayoutManager manager_;

    public BaseListFragment() {}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
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

        // Set up Layout Manager, reverse layout
        manager_ = new LinearLayoutManager(getActivity());
        manager_.setReverseLayout(true);
        manager_.setStackFromEnd(true);
        recycler_.setLayoutManager(manager_);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(database_);
        adapter_ = new FirebaseRecyclerAdapter<TripInfo, TripViewHolder>(TripInfo.class, R.layout.trip_item,
                TripViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final TripViewHolder viewHolder, final TripInfo model, final int position) {
                final DatabaseReference tripRef = getRef(position);

                // Set click listener for the whole post view
                final String tripKey = tripRef.getKey();
                viewHolder.itemView.setOnClickListener(v -> {
                    // Launch PostDetailActivity
//                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
//                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
//                        startActivity(intent);
                });

                // Determine if the current user has liked this post and set UI accordingly
//                if (model.stars.containsKey(getUid())) {
//                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_24);
//                } else {
//                    viewHolder.starView.setImageResource(R.drawable.ic_toggle_star_outline_24);
//                }

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
        return databaseReference.child("user-posts")
                .child(getUid());
    }

}
