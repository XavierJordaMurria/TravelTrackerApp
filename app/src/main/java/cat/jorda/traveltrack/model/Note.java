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
public class Note
{
    @PropertyName("userID")
    public String userID_;
    @PropertyName("name")
    public String name_;
    @PropertyName("amount")
    public float amount_;

    public Note() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Note(String uid, String name, float amount)
    {
        userID_ = uid;
        name_   = name;
        amount_ = amount;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID_);
        result.put("name", name_);
        result.put("amount", amount_);
        return result;
    }
    // [END post_to_map]
}
// [END blog_user_class]

