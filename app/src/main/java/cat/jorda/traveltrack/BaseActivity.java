package cat.jorda.traveltrack;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import cat.jorda.traveltrack.util.Constants;


public class BaseActivity extends AppCompatActivity
{
    private ProgressDialog mProgressDialog;

    public void showProgressDialog()
    {
        if (mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog()
    {
        if (mProgressDialog != null && mProgressDialog.isShowing())
        {
            mProgressDialog.dismiss();
        }
    }

    public String getUid()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    protected Bundle setBundleTripInfo(String tripKey, String tripName, String dayKey, String dayName)
    {
        Bundle b = new Bundle();
        b.putString(Constants.TRIP_KEY, tripKey);
        b.putString(Constants.TRIP_NAME, tripName);
        b.putString(Constants.DAY_KEY, dayKey);
        b.putString(Constants.DAY_NAME, dayName);

        return b;
    }
}
