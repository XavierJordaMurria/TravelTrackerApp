package cat.jorda.traveltrack.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xj1 on 07/08/2017.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class DayInfo
{
    @PropertyName("userID")
    public String userID_;
    @PropertyName("tripID")
    public String tripID_;
    @PropertyName("title")
    public String title_;
    @PropertyName("subTitle")
    public String subTitle_;
    @PropertyName("date")
    public String date_;

    public DayInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public DayInfo(String userID, String tripID,
                    String title, String subTitle,
                    String date)
    {
        userID_ = userID;
        tripID_ = tripID;
        title_  = title;
        subTitle_   = subTitle;
        date_   = date;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID_);
        result.put("tripID", tripID_);
        result.put("title", title_);
        result.put("subTitle", subTitle_);
        result.put("date", date_);
        return result;
    }
    // [END post_to_map]
}
// [END blog_user_class]
