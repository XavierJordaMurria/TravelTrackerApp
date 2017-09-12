package cat.jorda.traveltrack.viewHolders;
/**
 * Created by xj1 on 12/09/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cat.jorda.traveltrack.R;

public class AddNotesViewHolder extends RecyclerView.ViewHolder
{
    public EditText note_;
    public Button saveExpenses_;

    public AddNotesViewHolder(View itemView)
    {
        super(itemView);
        note_ = (EditText) itemView.findViewById(R.id.add_note);
        saveExpenses_   = (Button)itemView.findViewById(R.id.save_note_btn);
    }
}

