package ir.joorjens.model.businessEntity;

import ir.joorjens.common.Utility;

import java.io.Serializable;

public class DashboardDistributorProductInfo implements Serializable {

    public final long count // کل کالا
            , saleCount // کل تعداد فروش
            , stock // کل تعداد موجودی
            , stockProduct // تعداد کالاهایی که موجود هستند
            , stockProductWithoutWarn //تعداد کالاهایی که بدون هشدار هستند
            , price //قیمت انبارمون
            , priceMin;
    public final double settlementPercent;

    public DashboardDistributorProductInfo() {
        count = saleCount = stock = price = priceMin = stockProduct = stockProductWithoutWarn = 0;
        settlementPercent = 0;
    }

    public DashboardDistributorProductInfo(final Object row) {
        int i = -1;
        final Object[] columns = (Object[]) row;
        this.count = (long) columns[++i];
        this.saleCount = (long) columns[++i];
        this.stock = (long) columns[++i];
        this.stockProduct = (long) columns[++i];
        this.stockProductWithoutWarn = (long) columns[++i];
        this.price = (long) columns[++i];
        this.priceMin = (long) columns[++i];
        this.settlementPercent = (double) columns[++i];
    }

    // -------------------------------------------------------------------------------------

    public long getCount() {
        return count;
    }

    public long getSaleCount() {
        return saleCount;
    }

    public long getStock() {
        return stock;
    }

    public long getStockProduct() {
        return stockProduct;
    }

    public long getStockProductWithoutWarn() {
        return stockProductWithoutWarn;
    }

    public long getPrice() {
        return price;
    }

    public long getPriceMin() {
        return priceMin;
    }

    public double getSettlementPercent() {
        return Utility.round(settlementPercent, 2);
    }

    // -------------------------------------------------------------------------------------

}