package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartPrice implements Serializable {
    @SerializedName("time")
    @Expose
    private Integer time;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("timeFrom")
    @Expose
    private Integer timeFrom;
    @SerializedName("timeTo")
    @Expose
    private Integer timeTo;
    @SerializedName("distributorId")
    @Expose
    private Integer distributorId;
    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("packCount")
    @Expose
    private Long packCount;
    @SerializedName("allPrice")
    @Expose
    private Long allPrice;
    @SerializedName("allPriceConsumer")
    @Expose
    private Long allPriceConsumer;
    @SerializedName("allPriceDiscount")
    @Expose
    private Long allPriceDiscount;
    @SerializedName("amountCheck")
    @Expose
    private Long amountCheck;
    @SerializedName("amountCache")
    @Expose
    private Long amountCache;
    @SerializedName("amountCredit")
    @Expose
    private Long amountCredit;
    @SerializedName("settlementPercent")
    @Expose
    private Double settlementPercent;
    @SerializedName("joorJensShare")
    @Expose
    private Double joorJensShare;
    @SerializedName("yourProfit")
    @Expose
    private Long yourProfit;
    @SerializedName("packPrice")
    @Expose
    private Long packPrice;
    @SerializedName("packPriceConsumer")
    @Expose
    private Long packPriceConsumer;
    @SerializedName("packPriceDiscount")
    @Expose
    private Long packPriceDiscount;
    @SerializedName("amountMustPay")
    @Expose
    private Integer amountMustPay;
    @SerializedName("areaZoneId")
    @Expose
    private Integer areaZoneId;


    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Integer timeFrom) {
        this.timeFrom = timeFrom;
    }

    public Integer getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Integer timeTo) {
        this.timeTo = timeTo;
    }

    public Integer getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(Integer distributorId) {
        this.distributorId = distributorId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getPackCount() {
        return packCount;
    }

    public void setPackCount(Long packCount) {
        this.packCount = packCount;
    }

    public Long getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(Long allPrice) {
        this.allPrice = allPrice;
    }

    public Long getAllPriceConsumer() {
        return allPriceConsumer;
    }

    public void setAllPriceConsumer(Long allPriceConsumer) {
        this.allPriceConsumer = allPriceConsumer;
    }

    public Long getAllPriceDiscount() {
        return allPriceDiscount;
    }

    public void setAllPriceDiscount(Long allPriceDiscount) {
        this.allPriceDiscount = allPriceDiscount;
    }

    public Long getAmountCheck() {
        return amountCheck;
    }

    public void setAmountCheck(Long amountCheck) {
        this.amountCheck = amountCheck;
    }

    public Long getAmountCache() {
        return amountCache;
    }

    public void setAmountCache(Long amountCache) {
        this.amountCache = amountCache;
    }

    public Long getAmountCredit() {
        return amountCredit;
    }

    public void setAmountCredit(Long amountCredit) {
        this.amountCredit = amountCredit;
    }

    public Double getSettlementPercent() {
        return settlementPercent;
    }

    public void setSettlementPercent(Double settlementPercent) {
        this.settlementPercent = settlementPercent;
    }

    public Double getJoorJensShare() {
        return joorJensShare;
    }

    public void setJoorJensShare(Double joorJensShare) {
        this.joorJensShare = joorJensShare;
    }

    public Long getYourProfit() {
        return yourProfit;
    }

    public void setYourProfit(Long yourProfit) {
        this.yourProfit = yourProfit;
    }

    public Long getPackPrice() {
        return packPrice;
    }

    public void setPackPrice(Long packPrice) {
        this.packPrice = packPrice;
    }

    public Long getPackPriceConsumer() {
        return packPriceConsumer;
    }

    public void setPackPriceConsumer(Long packPriceConsumer) {
        this.packPriceConsumer = packPriceConsumer;
    }

    public Long getPackPriceDiscount() {
        return packPriceDiscount;
    }

    public void setPackPriceDiscount(Long packPriceDiscount) {
        this.packPriceDiscount = packPriceDiscount;
    }

    public Integer getAmountMustPay() {
        return amountMustPay;
    }

    public void setAmountMustPay(Integer amountMustPay) {
        this.amountMustPay = amountMustPay;
    }

    public Integer getAreaZoneId() {
        return areaZoneId;
    }

    public void setAreaZoneId(Integer areaZoneId) {
        this.areaZoneId = areaZoneId;
    }
}
