package cat.jorda.traveltrack;

import android.view.View;
import android.widget.EditText;

/**
 * add_trip.xml view holder for the AddTripActivity
 */

public class AddTripViewHolder {

    public EditText title_;
    public EditText startingDate_;
    public EditText endingtingDate_;
    public EditText country_;
    public EditText shortDescription_;

    public AddTripViewHolder(View view)
    {
        title_  = (EditText)view.findViewById(R.id.trip_name);
        startingDate_   = (EditText)view.findViewById(R.id.start_date);
        endingtingDate_ = (EditText)view.findViewById(R.id.end_date);
        country_        = (EditText)view.findViewById(R.id.country);
        shortDescription_   = (EditText)view.findViewById(R.id.short_description);
    }
}
