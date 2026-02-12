package ir.joorjens.joorapp.models;

/**
 * Created by Mohsen on 12/15/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DistributorPackage {

    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("bundlingOrDiscount")
    @Expose
    private Boolean bundlingOrDiscount;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("from")
    @Expose
    private Integer from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("packageProducts")
    @Expose
    private List<PackageProduct> packageProducts = null;
    @SerializedName("distributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("distributorName")
    @Expose
    private String distributorName;
    @SerializedName("allCount")
    @Expose
    private Integer allCount;
    @SerializedName("allPriceConsumer")
    @Expose
    private Integer allPriceConsumer;
    @SerializedName("allPrice")
    @Expose
    private Integer allPrice;
    @SerializedName("allPriceWithDiscount")
    @Expose
    private Integer allPriceWithDiscount;
    @SerializedName("allDiscountPercent")
    @Expose
    private Double allDiscountPercent;
    @SerializedName("distributorTypeName")
    @Expose
    private String distributorTypeName;
    @SerializedName("distributorManagerId")
    @Expose
    private Integer distributorManagerId;

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

    public Boolean getBundlingOrDiscount() {
        return bundlingOrDiscount;
    }

    public void setBundlingOrDiscount(Boolean bundlingOrDiscount) {
        this.bundlingOrDiscount = bundlingOrDiscount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PackageProduct> getPackageProducts() {
        return packageProducts;
    }

    public void setPackageProducts(List<PackageProduct> packageProducts) {
        this.packageProducts = packageProducts;
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

    public Integer getAllCount() {
        return allCount;
    }

    public void setAllCount(Integer allCount) {
        this.allCount = allCount;
    }

    public Integer getAllPriceConsumer() {
        return allPriceConsumer;
    }

    public void setAllPriceConsumer(Integer allPriceConsumer) {
        this.allPriceConsumer = allPriceConsumer;
    }

    public Integer getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(Integer allPrice) {
        this.allPrice = allPrice;
    }

    public Integer getAllPriceWithDiscount() {
        return allPriceWithDiscount;
    }

    public void setAllPriceWithDiscount(Integer allPriceWithDiscount) {
        this.allPriceWithDiscount = allPriceWithDiscount;
    }

    public Double getAllDiscountPercent() {
        return allDiscountPercent;
    }

    public void setAllDiscountPercent(Double allDiscountPercent) {
        this.allDiscountPercent = allDiscountPercent;
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

}
