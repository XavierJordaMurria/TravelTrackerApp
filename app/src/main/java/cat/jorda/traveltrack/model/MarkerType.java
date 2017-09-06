package cat.jorda.traveltrack.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by xj1 on 05/09/2017.
 */

public final class MarkerType
{
    public static final int MARKER_LOCATION         = 1200;

    @IntDef({MARKER_LOCATION})
    @Retention(RetentionPolicy.SOURCE)

    public @interface IMarkerType {}
}