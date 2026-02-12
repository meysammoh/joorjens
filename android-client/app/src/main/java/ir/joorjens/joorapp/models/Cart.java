package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by meysammoh on 15.03.18.
 */

public class Cart {

    @SerializedName("createdTime")
    @Expose
    private Integer createdTime;
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("serial")
    @Expose
    private String serial;
    @SerializedName("promotionCredit")
    @Expose
    private Integer promotionCredit;
    @SerializedName("timeFinished")
    @Expose
    private Integer timeFinished;
    @SerializedName("cartPrice")
    @Expose
    private CartPrice cartPrice;
    @SerializedName("distributorSet")
    @Expose
    private List<CartDistributor> distributorSet = null;
    @SerializedName("finished")
    @Expose
    private Boolean finished;
    @SerializedName("orderStatus")
    @Expose
    private List<OrderStatus> orderStatus;
    @SerializedName("distributorSize")
    @Expose
    private Integer distributorSize;
    @SerializedName("packageSize")
    @Expose
    private Integer packageSize;
    @SerializedName("storeId")
    @Expose
    private Integer storeId;
    @SerializedName("storeName")
    @Expose
    private String storeName;
    @SerializedName("storeAddress")
    @Expose
    private String storeAddress;
    @SerializedName("storeManagerId")
    @Expose
    private Integer storeManagerId;
    @SerializedName("storeManagerName")
    @Expose
    private String storeManagerName;
    @SerializedName("storeManagerMobile")
    @Expose
    private String storeManagerMobile;
    @SerializedName("storeAreaZoneId")
    @Expose
    private Integer storeAreaZoneId;
    @SerializedName("storeAreaZoneName")
    @Expose
    private Integer storeAreaZoneName;


    public Integer getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

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

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Integer getPromotionCredit() {
        return promotionCredit;
    }

    public void setPromotionCredit(Integer promotionCredit) {
        this.promotionCredit = promotionCredit;
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

    public List<CartDistributor> getDistributorSet() {
        return distributorSet;
    }

    public void setDistributorSet(List<CartDistributor> distributorSet) {
        this.distributorSet = distributorSet;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public List<OrderStatus> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<OrderStatus> orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getDistributorSize() {
        return distributorSize;
    }

    public void setDistributorSize(Integer distributorSize) {
        this.distributorSize = distributorSize;
    }

    public Integer getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(Integer packageSize) {
        this.packageSize = packageSize;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public Integer getStoreManagerId() {
        return storeManagerId;
    }

    public void setStoreManagerId(Integer storeManagerId) {
        this.storeManagerId = storeManagerId;
    }

    public String getStoreManagerName() {
        return storeManagerName;
    }

    public void setStoreManagerName(String storeManagerName) {
        this.storeManagerName = storeManagerName;
    }

    public String getStoreManagerMobile() {
        return storeManagerMobile;
    }

    public void setStoreManagerMobile(String storeManagerMobile) {
        this.storeManagerMobile = storeManagerMobile;
    }

    public Integer getStoreAreaZoneId() {
        return storeAreaZoneId;
    }

    public void setStoreAreaZoneId(Integer storeAreaZoneId) {
        this.storeAreaZoneId = storeAreaZoneId;
    }

    public Integer getStoreAreaZoneName() {
        return storeAreaZoneName;
    }

    public void setStoreAreaZoneName(Integer storeAreaZoneName) {
        this.storeAreaZoneName = storeAreaZoneName;
    }
}

