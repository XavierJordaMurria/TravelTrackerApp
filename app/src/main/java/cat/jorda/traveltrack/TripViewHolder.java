package cat.jorda.traveltrack;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cat.jorda.traveltrack.model.TripInfo;

/**
 * Created by xj1 on 07/08/2017.
 */

public class TripViewHolder extends RecyclerView.ViewHolder {

    public TextView titleTextView_;
    public TextView subTitleTextView_;

    public TripViewHolder(View itemView) {
        super(itemView);

        titleTextView_ = (TextView) itemView.findViewById(R.id.trip_title);
        subTitleTextView_ = (TextView) itemView.findViewById(R.id.trip_subTitle);
    }

    public void bindToTrip(TripInfo trip, View.OnClickListener starClickListener) {
        titleTextView_.setText(trip.title_);
        subTitleTextView_.setText(trip.subTitle_);
//        starView.setOnClickListener(starClickListener);
    }
}

