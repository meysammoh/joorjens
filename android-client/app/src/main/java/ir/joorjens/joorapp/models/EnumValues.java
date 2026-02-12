package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 10/7/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ir.joorjens.joorapp.adapters.SpinnerPlusItem;

public class EnumValues implements SpinnerPlusItem{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fa")
    @Expose
    private String fa;
    @SerializedName("en")
    @Expose
    private String en;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    @Override
    public String getFriendlyName() {
        return getFa();
    }

    @Override
    public int getItemId() {
        return getId();
    }
}
