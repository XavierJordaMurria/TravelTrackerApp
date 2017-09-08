package cat.jorda.traveltrack;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by xj1 on 07/09/2017.
 */

public abstract class AddActivity extends AppCompatActivity implements View.OnTouchListener
{
    protected boolean isClosing_ = false;
    protected boolean isScrollingUp_ = false;
    protected boolean isScrollingDown_ = false;

    protected int previousFingerPosition_ = 0;
    protected int baseLayoutPosition_ = 0;
    protected int defaultViewHeight_;

    protected RelativeLayout baseLayout_;

    // [START declare_database_ref]
    protected DatabaseReference database_;
    // [END declare_database_ref]

    protected String getUid()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

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

    private void onTouchActionDown(int rawY)
    {
        // save default base layout height
        defaultViewHeight_ = baseLayout_.getHeight();

        // Init finger and view position
        previousFingerPosition_ = rawY;
        baseLayoutPosition_ = (int) baseLayout_.getY();
    }

    private void onTouchActionUp()
    {
        // If user was doing a scroll up
        if(isScrollingUp_)
        {
            // Reset baselayout position
            baseLayout_.setY(0);
            // We are not in scrolling up mode anymore
            isScrollingUp_ = false;
        }

        // If user was doing a scroll down
        if(isScrollingDown_){
            // Reset baselayout position
            baseLayout_.setY(0);
            // Reset base layout size
            baseLayout_.getLayoutParams().height = defaultViewHeight_;
            baseLayout_.requestLayout();
            // We are not in scrolling down mode anymore
            isScrollingDown_ = false;
        }
    }

    private boolean onTouchActionMove(int rawY)
    {
        if (isClosing_)
            return false;

        int currentYPosition = (int) baseLayout_.getY();

        // If we scroll up
        if(previousFingerPosition_ > rawY)
        {
            // First time android rise an event for "up" move
            if(!isScrollingUp_)
                isScrollingUp_ = true;

            // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
            if(baseLayout_.getHeight()<defaultViewHeight_){
                baseLayout_.getLayoutParams().height = baseLayout_.getHeight() - (rawY - previousFingerPosition_);
                baseLayout_.requestLayout();
            }
            else
            {
                // Has user scroll enough to "auto close" popup ?
                if ((baseLayoutPosition_ - currentYPosition) > defaultViewHeight_ / 4)
                {
                    closeUpAndDismissDialog(currentYPosition);
                    return true;
                }
            }

            baseLayout_.setY(baseLayout_.getY() + (rawY - previousFingerPosition_));
        }
        // If we scroll down
        else
        {
            // First time android rise an event for "down" move
            if(!isScrollingDown_)
                isScrollingDown_ = true;

            // Has user scroll enough to "auto close" popup ?
            if (Math.abs(baseLayoutPosition_ - currentYPosition) > defaultViewHeight_ / 2)
            {
                closeDownAndDismissDialog(currentYPosition);
                return true;
            }

            // Change base layout size and position (must change position because view anchor is top left corner)
            baseLayout_.setY(baseLayout_.getY() + (rawY - previousFingerPosition_));
            baseLayout_.getLayoutParams().height = baseLayout_.getHeight() - (rawY - previousFingerPosition_);
            baseLayout_.requestLayout();
        }

        // Update position
        previousFingerPosition_ = rawY;

        return false;
    }

    private void closeUpAndDismissDialog(int currentPosition)
    {
        isClosing_ = true;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(baseLayout_, "y", currentPosition, -baseLayout_.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {}
            @Override
            public void onAnimationCancel(Animator animation)
            {}
            @Override
            public void onAnimationRepeat(Animator animation)
            {}

            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }
        });
        positionAnimator.start();
    }

    private void closeDownAndDismissDialog(int currentPosition)
    {
        isClosing_ = true;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        ObjectAnimator positionAnimator = ObjectAnimator.ofFloat(baseLayout_, "y", currentPosition, screenHeight + baseLayout_.getHeight());
        positionAnimator.setDuration(300);
        positionAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {}
            @Override
            public void onAnimationCancel(Animator animation)
            {}
            @Override
            public void onAnimationRepeat(Animator animation)
            {}
            @Override
            public void onAnimationEnd(Animator animator)
            {
                finish();
            }
        });
        positionAnimator.start();
    }
}
