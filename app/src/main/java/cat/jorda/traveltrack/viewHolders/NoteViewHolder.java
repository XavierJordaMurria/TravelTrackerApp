
package cat.jorda.traveltrack.viewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import cat.jorda.traveltrack.R;
import cat.jorda.traveltrack.model.Note;

/**
 * Created by xj1 on 11/09/2017.
 */

public class NoteViewHolder extends RecyclerView.ViewHolder
{
    public TextView noteTextView_;

    public NoteViewHolder(View itemView)
    {
        super(itemView);
        noteTextView_ = (TextView) itemView.findViewById(R.id.note);
    }

    public void bindToNote(Note note, View.OnClickListener starClickListener)
    {
        noteTextView_.setText(note.note_);
    }
}

