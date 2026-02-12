package ir.joorjens.model.interfaces;

import ir.joorjens.model.entity.Distributor;

public interface DistributorPackductInterface {

    long getId();

    void setId(long id);

    float getSettlementPercent();

    void setSettlementPercent(float settlementPercent);

    int getStock();

    void setStock(int stock);

    int getStockWarn();

    void setStockWarn(int stockWarn);

    int getMinOrder();

    void setMinOrder(int minOrder);

    int getMaxOrder();

    void setMaxOrder(int maxOrder);

    int getMaxDelivery();

    void setMaxDelivery(int maxDelivery);

    int getPosition();

    void setPosition(int position);

    boolean isSupportCheck();

    void setSupportCheck(boolean supportCheck);

    long getSaleCount();

    void setSaleCount(long saleCount);

    Distributor getDistributor();

    void setDistributor(Distributor distributor);
}