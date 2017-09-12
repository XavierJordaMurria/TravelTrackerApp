
package cat.jorda.traveltrack.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.DayInfo;
import cat.jorda.traveltrack.model.TripInfo;

/**
 * Created by xj1 on 07/08/2017.
 */

public class DayViewHolder extends RecyclerView.ViewHolder
{
    public TextView titleTextView_;
    public TextView subTitleTextView_;
    public TextView dateTextView_;

    private View itemView_;

    public DayViewHolder(View itemView)
    {
        super(itemView);

        itemView_ = itemView;
        titleTextView_ = (TextView) itemView.findViewById(R.id.day_title);
        subTitleTextView_ = (TextView) itemView.findViewById(R.id.day_sub_title);
        dateTextView_ = (TextView) itemView.findViewById(R.id.day_date);
    }

    public void bindToDay(DayInfo dayInfo, View.OnClickListener itemViewClickListener)
    {
        titleTextView_.setText(dayInfo.title_);
        subTitleTextView_.setText(dayInfo.subTitle_);
        dateTextView_.setText(dayInfo.date_);
        itemView_.setOnClickListener(itemViewClickListener);
    }
}

