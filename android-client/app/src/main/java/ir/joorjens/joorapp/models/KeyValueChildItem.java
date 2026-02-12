package ir.joorjens.joorapp.models;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ir.joorjens.joorapp.adapters.SpinnerPlusItem;

/**
 * Created by meysammoh on 14.11.17.
 */

public class KeyValueChildItem implements SpinnerPlusItem,Comparable {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("child")
    @Expose
    private List<KeyValueChildItem> child = null;
    @Override
    public boolean equals(Object object)
    {
        boolean sameSame = false;

        if (object != null && object instanceof KeyValueChildItem)
        {
            sameSame = this.id.equals (((KeyValueChildItem) object).id );
        }

        return sameSame;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(@NonNull Object o) {

        return name.compareTo(((KeyValueChildItem)o).getName());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getFriendlyName() {
        return name;
    }

    @Override
    public int getItemId() {
        return Integer.valueOf(id.toString());
    }

    public List<KeyValueChildItem> getChild() {
        return child;
    }

    public void setChild(List<KeyValueChildItem> child) {
        this.child = child;
    }
}
