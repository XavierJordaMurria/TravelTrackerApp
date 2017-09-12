package cat.jorda.traveltrack.dayDetails;


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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import cat.jorda.traveltrack.AddExpensesActivity;
import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.Expenses;
import cat.jorda.traveltrack.util.Constants;
import cat.jorda.traveltrack.viewHolders.ExpensesViewHolder;

/**
 * Created by xj1 on 22/08/2017.
 */

public class FinancesDayFragment extends DayFragments
{
    private static String TAG = FinancesDayFragment.class.getSimpleName();

    private FirebaseRecyclerAdapter<Expenses, ExpensesViewHolder> adapter_;
    private RecyclerView recycler_;
    private LinearLayoutManager manager_;

    private FloatingActionButton fab_;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
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
        adapter_ = new FirebaseRecyclerAdapter<Expenses, ExpensesViewHolder>(Expenses.class, R.layout.expenses_item,
                ExpensesViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ExpensesViewHolder viewHolder, final Expenses model, final int position)
            {
                final DatabaseReference expensesRef = getRef(position);

                // Set click listener for the whole post view
                final String expensesKey = expensesRef.getKey();
                viewHolder.itemView.setOnClickListener(v -> {
                    // Load Days for the trip.
//                    listener_.onTripSelected(expensesKey);
                });

                if (model == null)
                    return;

                // Bind Expenses to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToExpenses(getActivity().getApplicationContext(), model, starView -> {
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
    public void onDestroy()
    {
        super.onDestroy();

        if (adapter_ != null)
            adapter_.cleanup();
    }

    protected Query getQuery(DatabaseReference databaseReference)
    {
        // All my days
        return databaseReference.child(Constants.DAY_EXPENSES)
                .child(dayKey_);
    }

    private void onFabClick(View view)
    {
        Log.d(TAG, "onFabClick");
        startActivity(new Intent(getActivity(), AddExpensesActivity.class));
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
