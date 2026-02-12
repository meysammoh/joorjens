package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.ComparatorInterface;

import javax.persistence.*;

@Entity
@Table(name = "distributor_product_package", uniqueConstraints = @UniqueConstraint
        (columnNames = {"id_distributor_product", "countFrom"}, name = "UK_DISTRIBUTOR_PRODUCT_PACKAGE__product_count"))
public class DistributorProductPackage extends AbstractModel implements ComparatorInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "بسته کالای تامین‌کننده";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int countFrom, countTo;
    private float percent;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor_product", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PRODUCT_PRICE__DP__distributorProduct"))
    @JsonIgnore
    private DistributorProduct distributorProduct;
    //------------------------------------------------

    public DistributorProductPackage() {
    }

    public DistributorProductPackage(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCountFrom() {
        return countFrom;
    }

    public void setCountFrom(int countFrom) {
        this.countFrom = countFrom;
    }

    public int getCountTo() {
        return countTo;
    }

    public void setCountTo(int countTo) {
        this.countTo = countTo;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public DistributorProduct getDistributorProduct() {
        return distributorProduct;
    }

    public void setDistributorProduct(DistributorProduct distributorProduct) {
        this.distributorProduct = distributorProduct;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        if(this.countFrom <= 0 || this.countFrom > this.countTo) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "تعدادها", DistributorProductPackage.getEN());
        }
        if(this.percent <= 0 || this.percent > 100) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "درصدها", DistributorProductPackage.getEN());
        }

        final boolean OK = this.distributorProduct != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setFields(final DistributorProductPackage dpp, final DistributorProduct dp) {
        setEdit(dpp);
        setDistributorProduct(dp);
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    @JsonIgnore
    public Double getOrder1() {
        return (double) this.countFrom;
    }

    @Override
    @JsonIgnore
    public Double getOrder2() {
        return (double) this.countTo;
    }

    @JsonIgnore
    public Distributor getDistributor() {
        return (this.distributorProduct != null) ? distributorProduct.getDistributor() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public int getPriceConsumerWithDiscount() {
        return (int) Utility.getPrice(this.getProductPriceConsumer(), this.percent);
    }

    @JsonGetter
    public int getPriceDistributorWithDiscount() {
        return (int) Utility.getPrice(this.getDistributorProductPrice(), this.percent);
    }

    @JsonGetter
    public long getDistributorProductId() {
        return (this.distributorProduct != null) ? distributorProduct.getId() : 0;
    }

    @JsonGetter
    public int getDistributorProductPrice() {
        return (this.distributorProduct != null) ? distributorProduct.getPrice() : 0;
    }

    @JsonGetter
    public long getProductId() {
        return (this.distributorProduct != null) ? distributorProduct.getId() : 0;
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

    @JsonSetter
    public void setDistributorProductId(long id) {
        if (id > 0) {
            this.distributorProduct = new DistributorProduct(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}