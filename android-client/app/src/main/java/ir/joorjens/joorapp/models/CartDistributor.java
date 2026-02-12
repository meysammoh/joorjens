package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CartDistributor implements Serializable{
    @SerializedName("createdTime")
    @Expose
    private Integer createdTime;
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cartPrice")
    @Expose
    private CartPrice cartPrice;
    @SerializedName("timeFinished")
    @Expose
    private Integer timeFinished;
    @SerializedName("packageSet")
    @Expose
    private List<CartDistributorProduct> packageSet = null;
    @SerializedName("serial")
    @Expose
    private String serial;
    @SerializedName("distributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("distributorName")
    @Expose
    private String distributorName;
    @SerializedName("cartId")
    @Expose
    private Integer cartId;
    @SerializedName("finished")
    @Expose
    private Boolean finished;
    @SerializedName("orderStatus")
    @Expose
    private List<OrderStatus> orderStatus = null;
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
    private String storeAreaZoneName;
    @SerializedName("delivererId")
    @Expose
    private Integer delivererId;
    @SerializedName("delivererName")
    @Expose
    private String delivererName;
    @SerializedName("delivererMobile")
    @Expose
    private String delivererMobile;

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

    public CartPrice getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(CartPrice cartPrice) {
        this.cartPrice = cartPrice;
    }

    public Integer getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(Integer timeFinished) {
        this.timeFinished = timeFinished;
    }

    public List<CartDistributorProduct> getPackageSet() {
        return packageSet;
    }

    public void setPackageSet(List<CartDistributorProduct> packageSet) {
        this.packageSet = packageSet;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
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

    public String getStoreAreaZoneName() {
        return storeAreaZoneName;
    }

    public void setStoreAreaZoneName(String storeAreaZoneName) {
        this.storeAreaZoneName = storeAreaZoneName;
    }

    public Integer getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(Integer delivererId) {
        this.delivererId = delivererId;
    }
}
