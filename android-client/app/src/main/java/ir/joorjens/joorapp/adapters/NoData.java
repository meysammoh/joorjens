package ir.joorjens.joorapp.adapters;

/**
 * Created by batman on 10/6/2017.
 */

public class NoData implements SpinnerPlusItem {
    public NoData(String data, int id) {
        this.mData = data;
        this.mId = id;
    }

    public String getData() {
        return mData;
    }

    public void setData(String mData) {
        this.mData = mData;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    private String mData;
    private int mId;

    @Override
    public String getFriendlyName() {
        return mData;
    }

    @Override
    public int getItemId() {
        return mId;
    }
}
