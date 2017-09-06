package cat.jorda.traveltrack.dayDetails;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cat.jorda.traveltrack.R;

/**
 * Created by xj1 on 22/08/2017.
 */

public class NotesDayFragment extends Fragment
{
    private static String TAG = NotesDayFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

//        if(context instanceof DayListFragment.ItemSelectedListener)  // context instanceof YourActivity
//            this.listener_ = (DayListFragment.ItemSelectedListener) context; // = (YourActivity) context
//        else
//            throw new ClassCastException(context.toString()
//                    + " must implement StepsFragment.OnItemSelectedListener");
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.map_details, container, false);

        return rootView;
    }
}