package cat.jorda.traveltrack;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cat.jorda.traveltrack.model.TripInfo;

/**
 * add_trip.xml view holder for the AddTripActivity
 */

public class AddTripViewHolder {

    public EditText title_;
    public EditText startingDate_;
    public EditText endingDate_;
    public EditText country_;
    public EditText shortDescription_;
    public Button saveTrip_;

    public AddTripViewHolder(View view)
    {
        title_  = (EditText)view.findViewById(R.id.trip_name);
        startingDate_   = (EditText)view.findViewById(R.id.start_date);
        endingDate_ = (EditText)view.findViewById(R.id.end_date);
        country_        = (EditText)view.findViewById(R.id.country);
        shortDescription_   = (EditText)view.findViewById(R.id.short_description);
        saveTrip_   = (Button)view.findViewById(R.id.save_trip_btn);
    }
}
