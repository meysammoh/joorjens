package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 5/31/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("barcode")
    @Expose
    private String barcode;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("priceConsumer")
    @Expose
    private Integer priceConsumer;
    @SerializedName("productDetails")
    @Expose
    private List<ProductDetails> productDetails = null;
    @SerializedName("productBrandTypeId")
    @Expose
    private Integer productBrandTypeId;
    @SerializedName("productBrandTypeName")
    @Expose
    private String productBrandTypeName;
    @SerializedName("productBrandTypeParentId")
    @Expose
    private Integer productBrandTypeParentId;
    @SerializedName("productBrandTypeParentName")
    @Expose
    private String productBrandTypeParentName;
    @SerializedName("productCategoryTypeId")
    @Expose
    private Integer productCategoryTypeId;
    @SerializedName("productCategoryTypeName")
    @Expose
    private String productCategoryTypeName;
    @SerializedName("productCategoryTypeParentId")
    @Expose
    private Integer productCategoryTypeParentId;

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getPriceConsumer() {
        return priceConsumer;
    }

    public void setPriceConsumer(Integer priceConsumer) {
        this.priceConsumer = priceConsumer;
    }

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    public Integer getProductBrandTypeId() {
        return productBrandTypeId;
    }

    public void setProductBrandTypeId(Integer productBrandTypeId) {
        this.productBrandTypeId = productBrandTypeId;
    }

    public String getProductBrandTypeName() {
        return productBrandTypeName;
    }

    public void setProductBrandTypeName(String productBrandTypeName) {
        this.productBrandTypeName = productBrandTypeName;
    }

    public Integer getProductBrandTypeParentId() {
        return productBrandTypeParentId;
    }

    public void setProductBrandTypeParentId(Integer productBrandTypeParentId) {
        this.productBrandTypeParentId = productBrandTypeParentId;
    }

    public String getProductBrandTypeParentName() {
        return productBrandTypeParentName;
    }

    public void setProductBrandTypeParentName(String productBrandTypeParentName) {
        this.productBrandTypeParentName = productBrandTypeParentName;
    }

    public Integer getProductCategoryTypeId() {
        return productCategoryTypeId;
    }

    public void setProductCategoryTypeId(Integer productCategoryTypeId) {
        this.productCategoryTypeId = productCategoryTypeId;
    }

    public String getProductCategoryTypeName() {
        return productCategoryTypeName;
    }

    public void setProductCategoryTypeName(String productCategoryTypeName) {
        this.productCategoryTypeName = productCategoryTypeName;
    }

    public Integer getProductCategoryTypeParentId() {
        return productCategoryTypeParentId;
    }

    public void setProductCategoryTypeParentId(Integer productCategoryTypeParentId) {
        this.productCategoryTypeParentId = productCategoryTypeParentId;
    }

}