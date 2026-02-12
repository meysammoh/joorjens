package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartDistributorProduct implements Serializable {
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("timeFinished")
    @Expose
    private Integer timeFinished;
    @SerializedName("cartPrice")
    @Expose
    private CartPrice cartPrice;
    @SerializedName("buyByCheck")
    @Expose
    private Boolean buyByCheck;
    @SerializedName("serial")
    @Expose
    private String serial;
    @SerializedName("productId")
    @Expose
    private Integer productId;
    @SerializedName("distributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("distributorName")
    @Expose
    private String distributorName;
    @SerializedName("distributorProductId")
    @Expose
    private Integer distributorProductId;
    @SerializedName("finished")
    @Expose
    private Boolean finished;
    @SerializedName("orderStatusTypeId")
    @Expose
    private Integer orderStatusTypeId;
    @SerializedName("orderStatusTypeName")
    @Expose
    private String orderStatusTypeName;
    @SerializedName("distributorPackageId")
    @Expose
    private Integer distributorPackageId;
    @SerializedName("distributorProductName")
    @Expose
    private String distributorProductName;
    @SerializedName("distributorProductFeature")
    @Expose
    private String distributorProductFeature;
    @SerializedName("typeName")
    @Expose
    private String typeName;


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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(Integer timeFinished) {
        this.timeFinished = timeFinished;
    }

    public CartPrice getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(CartPrice cartPrice) {
        this.cartPrice = cartPrice;
    }

    public Boolean getBuyByCheck() {
        return buyByCheck;
    }

    public void setBuyByCheck(Boolean buyByCheck) {
        this.buyByCheck = buyByCheck;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public Integer getDistributorProductId() {
        return distributorProductId;
    }

    public void setDistributorProductId(Integer distributorProductId) {
        this.distributorProductId = distributorProductId;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public Integer getOrderStatusTypeId() {
        return orderStatusTypeId;
    }

    public void setOrderStatusTypeId(Integer orderStatusTypeId) {
        this.orderStatusTypeId = orderStatusTypeId;
    }

    public String getOrderStatusTypeName() {
        return orderStatusTypeName;
    }

    public void setOrderStatusTypeName(String orderStatusTypeName) {
        this.orderStatusTypeName = orderStatusTypeName;
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

    public String getDistributorProductFeature() {
        return distributorProductFeature;
    }

    public void setDistributorProductFeature(String distributorProductFeature) {
        this.distributorProductFeature = distributorProductFeature;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
