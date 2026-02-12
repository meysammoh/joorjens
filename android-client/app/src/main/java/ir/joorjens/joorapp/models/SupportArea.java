package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meysam on 14/04/2018.
 */

public class SupportArea {

    @SerializedName("block")
    @Expose
    private Boolean block;
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
    @SerializedName("grandId")
    @Expose
    private Integer grandId;

    @SerializedName("grandName")
    @Expose
    private String grandName;
    @SerializedName("ancestorId")
    @Expose
    private Integer ancestorId;

    @SerializedName("ancestorName")
    @Expose
    private String ancestorName;



    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

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

    public Integer getGrandId() {
        return grandId;
    }

    public void setGrandId(Integer grandId) {
        this.grandId = grandId;
    }

    public String getGrandName() {
        return grandName;
    }

    public void setGrandName(String grandName) {
        this.grandName = grandName;
    }

    public Integer getAncestorId() {
        return ancestorId;
    }

    public void setAncestorId(Integer ancestorId) {
        this.ancestorId = ancestorId;
    }

    public String getAncestorName() {
        return ancestorName;
    }

    public void setAncestorName(String ancestorName) {
        this.ancestorName = ancestorName;
    }
}
