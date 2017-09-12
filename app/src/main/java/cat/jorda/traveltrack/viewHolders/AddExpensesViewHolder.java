
package cat.jorda.traveltrack.viewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cat.jorda.traveltrack.R;

/**
 * add_trip.xml view holder for the AddTripActivity
 */

public class AddExpensesViewHolder
{

    public EditText name_;
    public EditText amount_;
    public Button saveExpenses_;

    public AddExpensesViewHolder(View view)
    {
        name_  = (EditText)view.findViewById(R.id.expenses_name);
        amount_   = (EditText)view.findViewById(R.id.expenses_amount);
        saveExpenses_   = (Button)view.findViewById(R.id.save_expenses_btn);
    }
}
