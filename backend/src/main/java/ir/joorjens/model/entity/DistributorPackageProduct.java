package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;

@Entity
@Table(name = "distributor_package_product")
public class DistributorPackageProduct extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "کالای بسته‌های تامین‌کننده";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int count;
    private float discountPercent;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor_package", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PACKAGE_PRO__DP__distributorPackage"))
    @JsonIgnore
    private DistributorPackage distributorPackage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor_product", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PACKAGE_PRO__DP__distributorProduct"))
    @JsonIgnore
    private DistributorProduct distributorProduct;
    //------------------------------------------------

    public DistributorPackageProduct() {
    }

    public DistributorPackageProduct(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public DistributorPackage getDistributorPackage() {
        return distributorPackage;
    }

    public void setDistributorPackage(DistributorPackage distributorPackage) {
        this.distributorPackage = distributorPackage;
    }

    public DistributorProduct getDistributorProduct() {
        return distributorProduct;
    }

    public void setDistributorProduct(DistributorProduct distributorProduct) {
        this.distributorProduct = distributorProduct;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = distributorProduct != null && distributorPackage != null && count > 0;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        if(!distributorPackage.isBundlingOrDiscount() && (this.discountPercent <= 0 || this.discountPercent > 100)) {
            throw new JoorJensException(ExceptionCode.PACKAGE_PRODUCT_INVALID_DISCOUNT);
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getDistributorPackageId() {
        return (this.distributorPackage != null) ? distributorPackage.getId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        return (this.distributorPackage != null) ? distributorPackage.getDistributorName() : null;
    }

    @JsonGetter
    public long getProductId() {
        return (this.distributorProduct != null) ? distributorProduct.getProductId() : 0;
    }

    @JsonGetter
    public long getDistributorProductId() {
        return (this.distributorProduct != null) ? distributorProduct.getId() : 0;
    }

    @JsonGetter
    public int getDistributorProductPriceConsumer() {
        return this.count * ((this.distributorProduct != null) ? distributorProduct.getProductPriceConsumer() : 0);
    }

    @JsonGetter
    public int getDistributorProductPrice() {
        return this.count * ((this.distributorProduct != null) ? distributorProduct.getPrice() : 0);
    }

    @JsonGetter
    public int getDistributorProductPriceWithDiscount() {
        final int price = getDistributorProductPrice();
        final float off = price * (this.discountPercent / 100);
        return (int) (price - off);
    }

    @JsonGetter
    public int getYourProfit() {
        return getDistributorProductPrice() - getDistributorProductPriceWithDiscount();
    }

    @JsonGetter
    public String getDistributorProductName() {
        return (this.distributorProduct != null) ? distributorProduct.getProductName() : null;
    }

    @JsonGetter
    public String getDistributorProductBarcode() {
        return (this.distributorProduct != null) ? distributorProduct.getProductBarcode() : null;
    }

    @JsonGetter
    public boolean isDistributorProductOnlyBundling() {
        return (this.distributorProduct != null) && distributorProduct.isOnlyBundling();
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setDistributorPackageId(long id) {
        if (id > 0) {
            this.distributorPackage = new DistributorPackage(id);
        }
    }


    @JsonSetter
    public void setDistributorProductId(long id) {
        if (id > 0) {
            this.distributorProduct = new DistributorProduct(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}