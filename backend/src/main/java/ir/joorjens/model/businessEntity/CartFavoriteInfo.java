package ir.joorjens.model.businessEntity;

import java.io.Serializable;

public class CartFavoriteInfo implements Serializable {

    public final long distributorProductId, distributorId, productId;
    public final String distributorName, productName, productBarcode, productImage;
    public final long amount;

    public CartFavoriteInfo() {
        distributorProductId = distributorId = productId = 0;
        distributorName = productName = productBarcode = productImage = null;
        amount = 0;
    }

    public CartFavoriteInfo(final Object row) {
        int i = -1;
        final Object[] fields = (Object[]) row;
        this.distributorProductId = (long) fields[++i];
        this.distributorId = (long) fields[++i];
        this.productId = (long) fields[++i];
        this.distributorName = (String) fields[++i];
        this.productName = (String) fields[++i];
        this.productBarcode = (String) fields[++i];
        this.productImage = (String) fields[++i];
        this.amount = (long) fields[++i];
    }

    // -------------------------------------------------------------------------------------

    public long getDistributorProductId() {
        return distributorProductId;
    }

    public long getDistributorId() {
        return distributorId;
    }

    public long getProductId() {
        return productId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public String getProductImage() {
        return productImage;
    }

    public long getAmount() {
        return amount;
    }

    // -------------------------------------------------------------------------------------
}
