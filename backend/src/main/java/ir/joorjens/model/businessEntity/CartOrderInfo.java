package ir.joorjens.model.businessEntity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Pair;
import ir.joorjens.model.entity.Area;
import ir.joorjens.model.entity.OrderStatusType;
import ir.joorjens.model.util.TypeEnumeration;

import java.io.Serializable;

public class CartOrderInfo implements Serializable {

    private final long time;
    private final int timeFrom, timeTo;
    private final long countCart, count;
    @JsonIgnore
    private final Area areaZone;
    private final long distributorId;
    @JsonIgnore
    private final OrderStatusType orderStatusType;

    public CartOrderInfo() {
        this.time = this.countCart = this.count = this.timeFrom = this.timeTo = 0;
        this.areaZone = null;
        this.distributorId = 0;
        this.orderStatusType = null;
    }

    public CartOrderInfo(final Object row, boolean hasTime, boolean hasArea, boolean hasDistributor, TypeEnumeration timeStamp) {
        int i = -1;
        final Object[] columns = (Object[]) row;
        this.orderStatusType = (OrderStatusType) columns[++i];
        this.time = hasTime ? (int) columns[++i] : 0;
        this.areaZone = hasArea ? (Area) columns[++i] : null;
        this.distributorId = hasDistributor ? (long) columns[++i] : 0;
        this.count = (long) columns[++i];
        this.countCart = (long) columns[++i];

        if (this.time > 0) {
            final Pair<Integer, Integer> timeFromTo = Utility.getTimeFromTo(String.valueOf(time), timeStamp, true);
            this.timeFrom = timeFromTo.getFirst();
            this.timeTo = timeFromTo.getSecond();
        } else {
            this.timeFrom = this.timeTo = 0;
        }
    }

    // -------------------------------------------------------------------------------------

    public long getTime() {
        return time;
    }

    public int getTimeFrom() {
        return timeFrom;
    }

    public int getTimeTo() {
        return timeTo;
    }

    public long getCount() {
        return count;
    }

    public long getCountCart() {
        return countCart;
    }

    public Area getAreaZone() {
        return areaZone;
    }

    public long getDistributorId() {
        return distributorId;
    }

    public OrderStatusType getOrderStatusType() {
        return orderStatusType;
    }

    // -------------------------------------------------------------------------------------

    @JsonGetter
    public long getOrderStatusTypeId() {
        return (orderStatusType != null) ? orderStatusType.getId() : 0;
    }

    @JsonGetter
    public String getOrderStatusTypeName() {
        return (orderStatusType != null) ? orderStatusType.getName() : null;
    }

    @JsonGetter
    public long getAreaZoneId() {
        return (areaZone != null) ? areaZone.getId() : 0;
    }

    @JsonGetter
    public String getAreaZoneName() {
        return (areaZone != null) ? areaZone.getName() : null;
    }

    // -------------------------------------------------------------------------------------
}