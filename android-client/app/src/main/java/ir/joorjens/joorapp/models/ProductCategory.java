package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by meysammoh on 15.11.17.
 */

public class ProductCategory extends AbstractModel{
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("childCount")
    @Expose
    private int childCount;
    @SerializedName("parentId")
    @Expose
    private long parentId;
    @SerializedName("parentName")
    @Expose
    private String parentName;
    @SerializedName("firstLevel")
    @Expose
    private boolean firstLevel;
    @SerializedName("productDetailTypes")
    @Expose
    private List<ProductDetailType> productDetailTypes = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public boolean isFirstLevel() {
        return firstLevel;
    }

    public void setFirstLevel(boolean firstLevel) {
        this.firstLevel = firstLevel;
    }

    public List<ProductDetailType> getProductDetailTypes() {
        return productDetailTypes;
    }

    public void setProductDetailTypes(List<ProductDetailType> productDetailTypes) {
        this.productDetailTypes = productDetailTypes;
    }
}
