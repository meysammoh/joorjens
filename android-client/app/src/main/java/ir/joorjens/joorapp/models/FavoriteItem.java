package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by meysammoh on 04.05.18.
 */

public class FavoriteItem {
    @SerializedName("distributorProductId")
    @Expose
    private Long distributorProductId;
    @SerializedName("distributorId")
    @Expose
    private Long distributorId;
    @SerializedName("productId")
    @Expose
    private Long productId;
    @SerializedName("distributorName")
    @Expose
    private String distributorName;
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("productBarcode")
    @Expose
    private String productBarcode;
    @SerializedName("productImage")
    @Expose
    private String productImage;
    @SerializedName("amount")
    @Expose
    private Long amount;

    public Long getDistributorProductId() {
        return distributorProductId;
    }

    public void setDistributorProductId(Long distributorProductId) {
        this.distributorProductId = distributorProductId;
    }

    public Long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Long distributorId) {
        this.distributorId = distributorId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
