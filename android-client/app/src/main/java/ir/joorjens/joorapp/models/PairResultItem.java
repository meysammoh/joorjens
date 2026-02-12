package ir.joorjens.joorapp.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ir.joorjens.joorapp.adapters.SpinnerPlusItem;

/**
 * Created by meysammoh on 14.11.17.
 */

public class PairResultItem implements SpinnerPlusItem, Comparable {
    @SerializedName("first")
    @Expose
    private Long first;

    @SerializedName("second")
    @Expose
    private String second;

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getFriendlyName(){
        return second;
    }

    @Override
    public int getItemId() {
        return Integer.valueOf(first.toString());
    }

    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof PairResultItem)
        {
            sameSame = this.first.equals (((PairResultItem) object).first )
                && this.second.equals(((PairResultItem) object).second) ;
        }

        return sameSame;
    }

    @Override
    public String toString() {
        return second;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        return second.compareTo(((PairResultItem)o).getSecond());
    }
}
