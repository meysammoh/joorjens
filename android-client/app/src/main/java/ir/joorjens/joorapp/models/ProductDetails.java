package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by batman on 11/6/2017.
 */

public class ProductDetails {
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("productDetailTypeId")
    @Expose
    private Integer productDetailTypeId;
    @SerializedName("productDetailTypeName")
    @Expose
    private String productDetailTypeName;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productBarcode")
    @Expose
    private String productBarcode;
    @SerializedName("productName")
    @Expose
    private String productName;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getProductDetailTypeId() {
        return productDetailTypeId;
    }

    public void setProductDetailTypeId(Integer productDetailTypeId) {
        this.productDetailTypeId = productDetailTypeId;
    }

    public String getProductDetailTypeName() {
        return productDetailTypeName;
    }

    public void setProductDetailTypeName(String productDetailTypeName) {
        this.productDetailTypeName = productDetailTypeName;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
