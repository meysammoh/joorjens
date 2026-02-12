package ir.joorjens.joorapp.models;

/**
 * Created by ZM on 7/1/2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardSale {

    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("packCount")
    @Expose
    private Integer packCount;
    @SerializedName("packPriceConsumer")
    @Expose
    private long packPriceConsumer;
    @SerializedName("packPrice")
    @Expose
    private long packPrice;
    @SerializedName("packPriceDiscount")
    @Expose
    private long packPriceDiscount;
    @SerializedName("amountCheck")
    @Expose
    private long amountCheck;
    @SerializedName("amountCache")
    @Expose
    private long amountCache;
    @SerializedName("amountCredit")
    @Expose
    private long amountCredit;
    @SerializedName("settlementPercent")
    @Expose
    private Integer settlementPercent;
    @SerializedName("joorJensShare")
    @Expose
    private double joorJensShare;
    @SerializedName("allPriceConsumer")
    @Expose
    private long allPriceConsumer;
    @SerializedName("allPrice")
    @Expose
    private long allPrice;
    @SerializedName("yourProfit")
    @Expose
    private long yourProfit;
    @SerializedName("allPriceDiscount")
    @Expose
    private long allPriceDiscount;
    @SerializedName("amountMustPay")
    @Expose
    private long amountMustPay;
    @SerializedName("areaZoneId")
    @Expose
    private Integer areaZoneId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getPackCount() {
        return packCount;
    }

    public void setPackCount(Integer packCount) {
        this.packCount = packCount;
    }

    public long getPackPriceConsumer() {
        return packPriceConsumer;
    }

    public void setPackPriceConsumer(long packPriceConsumer) {
        this.packPriceConsumer = packPriceConsumer;
    }

    public long getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(long packPrice) {
        this.packPrice = packPrice;
    }

    public long getPackPriceDiscount() {
        return packPriceDiscount;
    }

    public void setPackPriceDiscount(long packPriceDiscount) {
        this.packPriceDiscount = packPriceDiscount;
    }

    public long getAmountCheck() {
        return amountCheck;
    }

    public void setAmountCheck(long amountCheck) {
        this.amountCheck = amountCheck;
    }

    public long getAmountCache() {
        return amountCache;
    }

    public void setAmountCache(long amountCache) {
        this.amountCache = amountCache;
    }

    public long getAmountCredit() {
        return amountCredit;
    }

    public void setAmountCredit(long amountCredit) {
        this.amountCredit = amountCredit;
    }

    public Integer getSettlementPercent() {
        return settlementPercent;
    }

    public void setSettlementPercent(Integer settlementPercent) {
        this.settlementPercent = settlementPercent;
    }

    public double getJoorJensShare() {
        return joorJensShare;
    }

    public void setJoorJensShare(double joorJensShare) {
        this.joorJensShare = joorJensShare;
    }

    public long getAllPriceConsumer() {
        return allPriceConsumer;
    }

    public void setAllPriceConsumer(long allPriceConsumer) {
        this.allPriceConsumer = allPriceConsumer;
    }

    public long getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(long allPrice) {
        this.allPrice = allPrice;
    }

    public long getYourProfit() {
        return yourProfit;
    }

    public void setYourProfit(long yourProfit) {
        this.yourProfit = yourProfit;
    }

    public long getAllPriceDiscount() {
        return allPriceDiscount;
    }

    public void setAllPriceDiscount(long allPriceDiscount) {
        this.allPriceDiscount = allPriceDiscount;
    }

    public long getAmountMustPay() {
        return amountMustPay;
    }

    public void setAmountMustPay(long amountMustPay) {
        this.amountMustPay = amountMustPay;
    }

    public Integer getAreaZoneId() {
        return areaZoneId;
    }

    public void setAreaZoneId(Integer areaZoneId) {
        this.areaZoneId = areaZoneId;
    }
}
