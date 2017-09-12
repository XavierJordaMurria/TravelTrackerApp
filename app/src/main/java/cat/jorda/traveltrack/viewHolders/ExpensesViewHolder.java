package cat.jorda.traveltrack.viewHolders;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.Expenses;

/**
 * Created by xj1 on 11/09/2017.
 */

public class ExpensesViewHolder extends RecyclerView.ViewHolder
{
    public TextView nameTextView_;
    public TextView amountTextView_;

    private Context context_;

    public ExpensesViewHolder(View itemView)
    {
        super(itemView);
        nameTextView_ = (TextView) itemView.findViewById(R.id.expenses_name);
        amountTextView_ = (TextView) itemView.findViewById(R.id.expenses_amount);
    }

    public void bindToExpenses(Context applicationContext, Expenses expenses, View.OnClickListener starClickListener)
    {
        context_ = applicationContext;
        nameTextView_.setText(expenses.name_);
        Resources res = context_.getResources();
        String amountS = String.format(Locale.US, "%.2f", (float)expenses.amount_);
        String expenses_amount = res.getString(R.string.expenses_amount, amountS);
        amountTextView_.setText(expenses_amount);
    }
}

