package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 12/15/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Packaging {

    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("countFrom")
    @Expose
    private Integer countFrom;
    @SerializedName("countTo")
    @Expose
    private Integer countTo;
    @SerializedName("percent")
    @Expose
    private Integer percent;
    @SerializedName("distributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("distributorName")
    @Expose
    private String distributorName;
    @SerializedName("distributorProductPrice")
    @Expose
    private Integer distributorProductPrice;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productPriceConsumer")
    @Expose
    private Integer productPriceConsumer;
    @SerializedName("productBarcode")
    @Expose
    private String productBarcode;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("priceDistributorWithDiscount")
    @Expose
    private Integer priceDistributorWithDiscount;
    @SerializedName("distributorProductId")
    @Expose
    private Integer distributorProductId;
    @SerializedName("priceConsumerWithDiscount")
    @Expose
    private Integer priceConsumerWithDiscount;

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

    public Integer getPercent() {
        return percent;
    }

    public void setPercent(Integer percent) {
        this.percent = percent;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public Integer getDistributorProductPrice() {
        return distributorProductPrice;
    }

    public void setDistributorProductPrice(Integer distributorProductPrice) {
        this.distributorProductPrice = distributorProductPrice;
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

    public Integer getDistributorProductId() {
        return distributorProductId;
    }

    public void setDistributorProductId(Integer distributorProductId) {
        this.distributorProductId = distributorProductId;
    }

    public Integer getProductPriceConsumer() {
        return productPriceConsumer;
    }

    public void setProductPriceConsumer(Integer productPriceConsumer) {
        this.productPriceConsumer = productPriceConsumer;
    }

    public Integer getCountFrom() {
        return countFrom;
    }

    public void setCountFrom(Integer countFrom) {
        this.countFrom = countFrom;
    }

    public Integer getCountTo() {
        return countTo;
    }

    public void setCountTo(Integer countTo) {
        this.countTo = countTo;
    }

    public Integer getPriceDistributorWithDiscount() {
        return priceDistributorWithDiscount;
    }

    public void setPriceDistributorWithDiscount(Integer priceDistributorWithDiscount) {
        this.priceDistributorWithDiscount = priceDistributorWithDiscount;
    }

    public Integer getPriceConsumerWithDiscount() {
        return priceConsumerWithDiscount;
    }

    public void setPriceConsumerWithDiscount(Integer priceConsumerWithDiscount) {
        this.priceConsumerWithDiscount = priceConsumerWithDiscount;
    }
}
