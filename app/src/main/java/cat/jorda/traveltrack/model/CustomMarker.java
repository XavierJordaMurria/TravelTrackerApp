package cat.jorda.traveltrack.model;

import android.support.annotation.IntDef;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xj1 on 07/08/2017.
 */


@IntDef({Type.LOCATION})
@Retention(RetentionPolicy.SOURCE)
@interface Type {
    int LOCATION = 0;
}

// [START blog_user_class]
@IgnoreExtraProperties
public class CustomMarker
{
    @PropertyName("userID")
    public String userID_;
    @PropertyName("tripID")
    public String tripID_;
    @PropertyName("title")
    public String title_;
    @PropertyName("geoPosition")
    public LatLng geoPosition_;
    @PropertyName("listPosition")
    public String listPosition_;
    @PropertyName("type")
    public @Type int type_;

    private Marker marker_;

    public CustomMarker() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CustomMarker(String userID, String tripID,
                   Marker marker, @Type int type)
    {
        userID_ = userID;
        tripID_ = tripID;
        title_  = marker.getTitle();
        geoPosition_    = marker.getPosition();
        type_   = type;
        marker_   = marker;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID_);
        result.put("tripID", tripID_);
        result.put("title", title_);
        result.put("geoPosition", geoPosition_);
        result.put("type", type_);
        return result;
    }
    // [END post_to_map]
}
// [END blog_user_class]
