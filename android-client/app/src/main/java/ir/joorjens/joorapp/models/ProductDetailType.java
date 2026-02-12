package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meysammoh on 15.11.17.
 */

public class ProductDetailType extends AbstractModel{
    @SerializedName("productDetailTypeId")
    @Expose
    private long productDetailTypeId;
    @SerializedName("productDetailTypeName")
    @Expose
    private String productDetailTypeName;
    @SerializedName("mandatory")
    @Expose
    private boolean mandatory;

    public long getProductDetailTypeId() {
        return productDetailTypeId;
    }

    public void setProductDetailTypeId(long productDetailTypeId) {
        this.productDetailTypeId = productDetailTypeId;
    }

    public String getProductDetailTypeName() {
        return productDetailTypeName;
    }

    public void setProductDetailTypeName(String productDetailTypeName) {
        this.productDetailTypeName = productDetailTypeName;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }
}
