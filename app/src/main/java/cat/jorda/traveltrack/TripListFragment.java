package cat.jorda.traveltrack;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by xj1 on 19/08/2017.
 */

public class TripListFragment extends BaseListFragment {

    public TripListFragment() {
    }

    @Override
    public Query getQuery(DatabaseReference databaseReference)
    {
        // All my posts
        return databaseReference.child("user-trips")
                .child(getUid());

    }
}
