package cat.jorda.traveltrack;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by xj1 on 07/09/2017.
 */

public abstract class AddActivity extends AppCompatActivity implements View.OnTouchListener
{
    public abstract void onTouchActionDown(int rawY);
    public abstract void onTouchActionUp();
    public abstract boolean onTouchActionMove(int rawY);

    public boolean onTouch(View view, MotionEvent event)
    {
        // Get finger position on screen
        final int Y = (int) event.getRawY();

        // Switch on motion event type
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                onTouchActionDown(Y);
                break;

            case MotionEvent.ACTION_UP:
                onTouchActionUp();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchActionMove(Y);
                break;
        }
        return true;
    }

    protected String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}
