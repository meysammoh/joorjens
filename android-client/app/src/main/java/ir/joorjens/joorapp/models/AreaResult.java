package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 10/7/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import ir.joorjens.joorapp.adapters.SpinnerPlusItem;

public class AreaResult implements SpinnerPlusItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("adType")
    @Expose
    private Integer adType;
    @SerializedName("childCount")
    @Expose
    private Integer childCount;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("province")
    @Expose
    private Boolean province;
    @SerializedName("adTypeName")
    @Expose
    private String adTypeName;
    @SerializedName("parentId")
    @Expose
    private Integer parentId;
    @SerializedName("parentName")
    @Expose
    private String parentName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAdType() {
        return adType;
    }

    public void setAdType(Integer adType) {
        this.adType = adType;
    }

    public Integer getChildCount() {
        return childCount;
    }

    public void setChildCount(Integer childCount) {
        this.childCount = childCount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getProvince() {
        return province;
    }

    public void setProvince(Boolean province) {
        this.province = province;
    }

    public String getAdTypeName() {
        return adTypeName;
    }

    public void setAdTypeName(String adTypeName) {
        this.adTypeName = adTypeName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String getFriendlyName() {
        return getName();
    }

    @Override
    public int getItemId() {
        return getId();
    }
}
