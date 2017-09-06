package cat.jorda.traveltrack.model;

import android.support.annotation.IntDef;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import cat.jorda.traveltrack.util.LatLng;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xj1 on 07/08/2017.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class CustomMarker
{
    @PropertyName("userID")
    public String userID_;
    @PropertyName("tripID")
    public String tripID_;
    @PropertyName("dayID")
    public String dayID_;
    @PropertyName("title")
    public String title_;
    @PropertyName("latitude")
    public double latitude_;
    @PropertyName("longitude")
    public double longitude_;
    @PropertyName("listPosition")
    public String listPosition_;
    @PropertyName("type")
    public @MarkerType.IMarkerType int type_;

    private Marker marker_;

    public CustomMarker() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public CustomMarker(String userID, String tripID, String dayID,
                   Marker marker, @MarkerType.IMarkerType int type)
    {
        userID_ = userID;
        tripID_ = tripID;
        dayID_  = dayID;
        title_  = marker.getTitle();
        latitude_   = marker.getPosition().latitude;
        longitude_  = marker.getPosition().longitude;
        type_   = type;
        marker_   = marker;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID_);
        result.put("tripID", tripID_);
        result.put("dayID", dayID_);
        result.put("title", title_);
        result.put("latitude", latitude_);
        result.put("longitude", longitude_);
        result.put("type", type_);
        return result;
    }
    // [END post_to_map]
}
// [END blog_user_class]
