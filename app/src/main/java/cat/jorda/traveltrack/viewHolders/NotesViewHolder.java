package cat.jorda.traveltrack.viewHolders;
/**
 * Created by xj1 on 12/09/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.TripInfo;

public class NotesViewHolder extends RecyclerView.ViewHolder
{
    public TextView titleTextView_;
    public TextView subTitleTextView_;
    private View itemView_;

    public NotesViewHolder(View itemView)
    {
        super(itemView);
        itemView_ = itemView;
        titleTextView_ = (TextView) itemView.findViewById(R.id.trip_title);
        subTitleTextView_ = (TextView) itemView.findViewById(R.id.trip_subTitle);
    }

    public void bindToNotes(TripInfo trip, View.OnClickListener itemClickListener)
    {
        titleTextView_.setText(trip.title_);
        subTitleTextView_.setText(trip.subTitle_);
        itemView_.setOnClickListener(itemClickListener);
    }
}

