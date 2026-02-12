package ir.joorjens.joorapp.adapters;

/**
 * Created by mohsen on 10/18/2017.
 */

public class NavItem {
    String mTitle;
    ActivityKeys mId;

    public ActivityKeys getId() {
        return mId;
    }



    public NavItem(String title, ActivityKeys id) {
        mTitle = title;
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

}