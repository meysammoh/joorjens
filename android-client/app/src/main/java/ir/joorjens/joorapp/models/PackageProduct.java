package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 12/15/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageProduct {

    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("discountPercent")
    @Expose
    private double discountPercent;
    @SerializedName("distributorName")
    @Expose
    private String distributorName;
    @SerializedName("distributorProductOnlyBundling")
    @Expose
    private Boolean distributorProductOnlyBundling;
    @SerializedName("distributorProductPriceConsumer")
    @Expose
    private Integer distributorProductPriceConsumer;
    @SerializedName("distributorProductPrice")
    @Expose
    private Integer distributorProductPrice;
    @SerializedName("distributorProductId")
    @Expose
    private Integer distributorProductId;
    @SerializedName("distributorPackageId")
    @Expose
    private Integer distributorPackageId;
    @SerializedName("distributorProductName")
    @Expose
    private String distributorProductName;
    @SerializedName("distributorProductBarcode")
    @Expose
    private String distributorProductBarcode;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public Boolean getDistributorProductOnlyBundling() {
        return distributorProductOnlyBundling;
    }

    public void setDistributorProductOnlyBundling(Boolean distributorProductOnlyBundling) {
        this.distributorProductOnlyBundling = distributorProductOnlyBundling;
    }

    public Integer getDistributorProductPriceConsumer() {
        return distributorProductPriceConsumer;
    }

    public void setDistributorProductPriceConsumer(Integer distributorProductPriceConsumer) {
        this.distributorProductPriceConsumer = distributorProductPriceConsumer;
    }

    public Integer getDistributorProductPrice() {
        return distributorProductPrice;
    }

    public void setDistributorProductPrice(Integer distributorProductPrice) {
        this.distributorProductPrice = distributorProductPrice;
    }

    public Integer getDistributorProductId() {
        return distributorProductId;
    }

    public void setDistributorProductId(Integer distributorProductId) {
        this.distributorProductId = distributorProductId;
    }

    public Integer getDistributorPackageId() {
        return distributorPackageId;
    }

    public void setDistributorPackageId(Integer distributorPackageId) {
        this.distributorPackageId = distributorPackageId;
    }

    public String getDistributorProductName() {
        return distributorProductName;
    }

    public void setDistributorProductName(String distributorProductName) {
        this.distributorProductName = distributorProductName;
    }

    public String getDistributorProductBarcode() {
        return distributorProductBarcode;
    }

    public void setDistributorProductBarcode(String distributorProductBarcode) {
        this.distributorProductBarcode = distributorProductBarcode;
    }

}
