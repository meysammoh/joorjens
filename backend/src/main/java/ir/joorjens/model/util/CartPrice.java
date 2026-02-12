package ir.joorjens.model.util;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.model.entity.Area;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.io.Serializable;

@Embeddable
public class CartPrice implements Serializable {
    private static final long serialVersionUID = 1395L;

    @Transient
    private long time, total; //تعداد فاکتور
    @Transient
    private int timeFrom, timeTo;
    @Transient
    @JsonIgnore
    private Area areaZone;
    @Transient
    private long distributorId;
    private long count;
    private long packCount, packPriceConsumer, packPrice, packPriceDiscount;
    private long amountCheck, amountCache, amountCredit = 0;
    @Column(columnDefinition = "double default '0.00'")
    private double settlementPercent = 0, joorJensShare = 0;

    //-----------------------------------------------------------------------------------------------------------

    public CartPrice() {
    }

    public CartPrice(final Object row, boolean hasTime, boolean hasArea, boolean hasDistributor
            , TypeEnumeration timeStamp) {
        int i = -1;
        final Object[] columns = (Object[]) row;
        this.time = hasTime ? (int) columns[++i] : 0;
        this.areaZone = hasArea ? (Area) columns[++i] : null;
        this.distributorId = hasDistributor ? (long) columns[++i] : 0;
        this.total = (long) columns[++i];
        this.count = (long) columns[++i];
        this.packCount = (long) columns[++i];
        this.packPriceConsumer = (long) columns[++i];
        this.packPrice = (long) columns[++i];
        this.packPriceDiscount = (long) columns[++i];
        this.amountCheck = (long) columns[++i];
        this.amountCache = (long) columns[++i];
        this.amountCredit = (long) columns[++i];
        this.settlementPercent = (double) columns[++i];
        this.joorJensShare = (double) columns[++i];

        if (this.time > 0) {
            final Pair<Integer, Integer> timeFromTo = Utility.getTimeFromTo(String.valueOf(time), timeStamp, true);
            this.timeFrom = timeFromTo.getFirst();
            this.timeTo = timeFromTo.getSecond();
        } else {
            this.timeFrom = this.timeTo = 0;
        }
    }

    public synchronized void initFields(long packCount, long packPriceConsumer, long packPrice, long packPriceDiscount, boolean buyCache, float settlementPercent) {
        this.packCount = packCount;
        this.packPriceConsumer = packPriceConsumer;
        this.packPrice = packPrice;
        this.packPriceDiscount = packPriceDiscount;
        this.settlementPercent = settlementPercent;
        final long price = getAllPriceDiscount();
        this.joorJensShare = Utility.getShare(price, settlementPercent);
        if (buyCache) {
            this.amountCache = price;
        } else {
            this.amountCheck = price;
        }
    }

    public synchronized void setFields(final CartPrice cartPrice, final boolean add, boolean buyCheck) {
        final long price = cartPrice.getAllPriceDiscount();
        if (add) {
            count += cartPrice.getCount();
            packCount += cartPrice.getPackCount();
            packPriceConsumer += cartPrice.getPackPriceConsumer();
            packPrice += cartPrice.getPackPrice();
            packPriceDiscount += cartPrice.getPackPriceDiscount();
            joorJensShare += cartPrice.getJoorJensShare();
            if(buyCheck) {
                this.amountCheck += price;
            } else {
                this.amountCache += price;
            }
        } else {
            count -= cartPrice.getCount();
            packCount -= cartPrice.getPackCount();
            packPriceConsumer -= cartPrice.getPackPriceConsumer();
            packPrice -= cartPrice.getPackPrice();
            packPriceDiscount -= cartPrice.getPackPriceDiscount();
            joorJensShare -= cartPrice.getJoorJensShare();
            if(buyCheck) {
                this.amountCheck -= price;
            } else {
                this.amountCache -= price;
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(int timeFrom) {
        this.timeFrom = timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(int timeTo) {
        this.timeTo = timeTo;
    }

    public Area getAreaZone() {
        return areaZone;
    }

    public void setAreaZone(Area areaZone) {
        this.areaZone = areaZone;
    }

    public long getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(long distributorId) {
        this.distributorId = distributorId;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getPackCount() {
        return packCount;
    }

    public void setPackCount(long packCount) {
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

    public double getSettlementPercent() {
        return settlementPercent;
    }

    public void setSettlementPercent(double settlementPercent) {
        this.settlementPercent = settlementPercent;
    }

    public double getJoorJensShare() {
        return joorJensShare;
    }

    public void setJoorJensShare(double joorjensShare) {
        this.joorJensShare = joorjensShare;
    }

    //-----------------------------------------------------------------------------------------------------------
    @JsonGetter
    public long getAllPrice() {
        return this.count * this.getPackPrice();
    }

    @JsonGetter
    public long getAllPriceDiscount() {
        return this.count * this.getPackPriceDiscount();
    }

    @JsonGetter
    public long getAllPriceConsumer() {
        return this.count * this.getPackPriceConsumer();
    }

    @JsonGetter
    public long getYourProfit() {
        return this.getAllPrice() - this.getAllPriceDiscount();
    }

    @JsonGetter
    public long getAmountMustPay() {
        return getAllPriceDiscount() - this.amountCredit;
    }

    @JsonGetter
    public long getAreaZoneId() {
        return (areaZone != null) ? areaZone.getId() : 0;
    }

    @JsonGetter
    public String getAreaZoneName() {
        return (areaZone != null) ? areaZone.getName() : null;
    }
    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setAllPrice(long a) {
    }

    @JsonSetter
    public void setAllPriceDiscount(long a) {
    }

    @JsonSetter
    public void setAllPriceConsumer(long a) {
    }

    @JsonSetter
    public void setYourProfit(long a) {
    }

    @JsonSetter
    public void setAmountMustPay(long a) {
    }

    @JsonSetter
    public void setAreaZoneId(long a) {
    }

    @JsonSetter
    public void setAreaZoneName(String a) {
    }
    //-----------------------------------------------------------------------------------------------------------

}