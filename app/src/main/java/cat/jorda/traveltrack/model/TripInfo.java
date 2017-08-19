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
public class TripInfo
{
    @PropertyName("uid")
    public String uid_;
    @PropertyName("traveler")
    public String traveler_;
    @PropertyName("title")
    public String title_;
    @PropertyName("subTitle")
    public String subTitle_;
    @PropertyName("startDate")
    public String startDate_;
    @PropertyName("endDate")
    public String endDate_;
    @PropertyName("country")
    public String country_;

    public float budget_;
    public String description_;

    public TripInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TripInfo(String uid, String traveler,
                    String title, String subTitle,
                    String startDate, String endDate, String country)
    {
        uid_    = uid;
        traveler_   = traveler;
        title_  = title;
        subTitle_   = subTitle;
        startDate_  = startDate;
        endDate_    = endDate;
        country_    = country;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid_);
        result.put("title", title_);
        result.put("subTitle", subTitle_);
        result.put("starDate", startDate_);
        result.put("endDate", endDate_);
        result.put("country", country_);
        result.put("traveler", traveler_);

        return result;
    }
    // [END post_to_map]
}
// [END blog_user_class]
