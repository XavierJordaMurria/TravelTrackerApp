package cat.jorda.traveltrack.dayDetails;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.Expenses;
import cat.jorda.traveltrack.model.TripInfo;

/**
 * Created by xj1 on 11/09/2017.
 */

public class ExpensesViewHolder extends RecyclerView.ViewHolder
{
    public TextView nameTextView_;
    public TextView amountTextView_;

    public ExpensesViewHolder(View itemView)
    {
        super(itemView);

        nameTextView_ = (TextView) itemView.findViewById(R.id.trip_title);
        amountTextView_ = (TextView) itemView.findViewById(R.id.trip_subTitle);
    }

    public void bindToTrip(Expenses expenses, View.OnClickListener starClickListener)
    {
        nameTextView_.setText(expenses.name_);
        amountTextView_.setText(String.format(Locale.US, "%f", (float)expenses.amount_));
//        starView.setOnClickListener(starClickListener);
    }
}

