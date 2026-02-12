package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;

@Entity
@Table(name = "distributor_product_price_history")
public class DistributorProductPriceHistory extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "تاریخچه قیمت تامین‌کننده";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private final int price;
    private final int priceMin;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_distributor_product", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PRODUCT_PRICE_HIS__DIS_PRO__dp"))
    @JsonIgnore
    private final DistributorProduct distributorProduct;
    //------------------------------------------------

    public DistributorProductPriceHistory() {
        this(0,0,0);
    }

    public DistributorProductPriceHistory(long distributorProductId, int price, int priceMin) {
        this.distributorProduct = new DistributorProduct(distributorProductId);
        this.price = price;
        this.priceMin = priceMin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public DistributorProduct getDistributorProduct() {
        return distributorProduct;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.distributorProduct != null && this.price > 0 && this.priceMin > 0;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public int getTime() {
        return getCreatedTime();
    }

    @JsonGetter
    public long getProductId() {
        return (this.distributorProduct != null) ? distributorProduct.getProductId() : 0;
    }

    @JsonGetter
    public int getProductPriceConsumer() {
        return (this.distributorProduct != null) ? distributorProduct.getProductPriceConsumer() : 0;
    }

    @JsonGetter
    public String getProductBarcode() {
        return (this.distributorProduct != null) ? distributorProduct.getProductBarcode() : null;
    }

    @JsonGetter
    public String getProductName() {
        return (this.distributorProduct != null) ? distributorProduct.getProductName() : null;
    }

    @JsonGetter
    public long getDistributorId() {
        return (this.distributorProduct != null) ? distributorProduct.getDistributorId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        return (this.distributorProduct != null) ? distributorProduct.getDistributorName() : null;
    }
    //-----------------------------------------------------------------------------------------------------------
}