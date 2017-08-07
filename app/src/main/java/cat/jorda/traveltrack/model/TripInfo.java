package cat.jorda.traveltrack.model;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by xj1 on 07/08/2017.
 */

// [START blog_user_class]
@IgnoreExtraProperties
public class TripInfo
{

   private String title_;
   private String subTitle_;
   private String startDate_;
   private String endDate_;
   private String country_;

    private float budget_;
    private String description_;

    public TripInfo() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public TripInfo(String title, String subTitle, String startDate, String endDate, String country)
    {
        title_ = title;
        subTitle_   = subTitle;
        startDate_ = startDate;
        endDate_    = endDate;
        country_    = country;
    }

    public void setBudget(float budget)
    {
        if(budget < 0)
            throw new IllegalArgumentException("Budget can not be negative");

        budget_ =   budget;
    }

    public float getbudget()
    {
        return budget_;
    }

    public void setDescription(String description)
    {
        description_ = description;
    }

    public String getDescription()
    {
        return description_;
    }

    public String getTitle_() {
        return title_;
    }

    public String getSubTitle_() {
        return subTitle_;
    }

    public String getStartDate_() {
        return startDate_;
    }

    public String getEndDate_() {
        return endDate_;
    }

    public String getCountry_() {
        return country_;
    }

    public void setTitle_(String title_) {
        this.title_ = title_;
    }

    public void setSubTitle_(String subTitle_) {
        this.subTitle_ = subTitle_;
    }

    public void setStartDate_(String startDate_) {
        this.startDate_ = startDate_;
    }

    public void setEndDate_(String endDate_) {
        this.endDate_ = endDate_;
    }

    public void setCountry_(String country_) {
        this.country_ = country_;
    }
}
// [END blog_user_class]
