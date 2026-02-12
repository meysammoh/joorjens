package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 12/15/2017.
 */

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.zxing.client.android.Intents;

public class DistributorProduct implements Serializable {
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("onlyBundling")
    @Expose
    private Boolean onlyBundling;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("priceMin")
    @Expose
    private Integer priceMin;
    @SerializedName("stock")
    @Expose
    private Integer stock;
    @SerializedName("stockWarn")
    @Expose
    private Integer stockWarn;
    @SerializedName("minOrder")
    @Expose
    private Integer minOrder;
    @SerializedName("settlementPercent")
    @Expose
    private double settlementPercent;
    @SerializedName("packages")
    @Expose
    private List<Packaging> packages = null;

    @SerializedName("supportCheck")
    @Expose
    private Boolean supportCheck;
    @SerializedName("discounts")
    @Expose
    private List<Discount> discounts = null;
    @SerializedName("distributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("distributorName")
    @Expose
    private String distributorName;
    @SerializedName("distributorTypeName")
    @Expose
    private String distributorTypeName;
    @SerializedName("distributorManagerId")
    @Expose
    private Integer distributorManagerId;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("productBarcode")
    @Expose
    private String productBarcode;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productImage")
    @Expose
    private String productImage;

    @SerializedName("productCategoryTypeName")
    @Expose
    private String productCategoryTypeName;

    public Integer getProductCategoryTypeId() {
        return productCategoryTypeId;
    }

    public void setProductCategoryTypeId(Integer productCategoryTypeId) {
        this.productCategoryTypeId = productCategoryTypeId;
    }

    @SerializedName("productCategoryTypeId")
    @Expose
    private Integer productCategoryTypeId;

    @SerializedName("productBrandTypeName")
    @Expose
    private String productBrandTypeName;

    public String getProductNote() {
        return productNote;
    }

    public void setProductNote(String productNote) {
        this.productNote = productNote;
    }

    @SerializedName("productNote")
    @Expose
    private String productNote;

    @SerializedName("productPriceConsumer")
    @Expose
    private Integer productPriceConsumer;

    @SerializedName("productDetails")
    @Expose
    private List<ProductDetails> productDetails = null;

    @SerializedName("distributorRate")
    @Expose
    private double distributorRate;
    @SerializedName("maxDelivery")
    @Expose
    private Integer maxDelivery;


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

    public Boolean getOnlyBundling() {
        return onlyBundling;
    }

    public void setOnlyBundling(Boolean onlyBundling) {
        this.onlyBundling = onlyBundling;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public double getSettlementPercent() {
        return settlementPercent;
    }

    public void setSettlementPercent(Integer settlementPercent) {
        this.settlementPercent = settlementPercent;
    }
    public Integer getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(Integer priceMin) {
        this.priceMin = priceMin;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockWarn() {
        return stockWarn;
    }

    public void setStockWarn(Integer stockWarn) {
        this.stockWarn = stockWarn;
    }

    public Integer getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(Integer minOrder) {
        this.minOrder = minOrder;
    }

    public void setSettlementPercent(double settlementPercent) {
        this.settlementPercent = settlementPercent;
    }

    public List<Packaging> getPackages() {
        return packages;
    }

    public void setPackages(List<Packaging> packages) {
        this.packages = packages;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
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

    public String getDistributorTypeName() {
        return distributorTypeName;
    }

    public void setDistributorTypeName(String distributorTypeName) {
        this.distributorTypeName = distributorTypeName;
    }

    public Integer getDistributorManagerId() {
        return distributorManagerId;
    }

    public void setDistributorManagerId(Integer distributorManagerId) {
        this.distributorManagerId = distributorManagerId;
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
    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
    public Integer getProductPriceConsumer() {
        return productPriceConsumer;
    }

    public void setProductPriceConsumer(Integer productPriceConsumer) {
        this.productPriceConsumer = productPriceConsumer;
    }

    public List<ProductDetails> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(List<ProductDetails> productDetails) {
        this.productDetails = productDetails;
    }

    public double getDistributorRate() {
        return distributorRate;
    }

    public void setDistributorRate(double distributorRate) {
        this.distributorRate = distributorRate;
    }

    public Integer getMaxDelivery() {
        return maxDelivery;
    }

    public void setMaxDelivery(Integer maxDelivery) {
        this.maxDelivery = maxDelivery;
    }

    public String getProductCategoryTypeName() {
        return productCategoryTypeName;
    }

    public void setProductCategoryTypeName(String productCategoryTypeName) {
        this.productCategoryTypeName = productCategoryTypeName;
    }

    public String getProductBrandTypeName() {
        return productBrandTypeName;
    }

    public void setProductBrandTypeName(String productBrandTypeName) {
        this.productBrandTypeName = productBrandTypeName;
    }

    public Boolean getSupportCheck() {
        return supportCheck;
    }

    public void setSupportCheck(Boolean supportCheck) {
        this.supportCheck = supportCheck;
    }
}
