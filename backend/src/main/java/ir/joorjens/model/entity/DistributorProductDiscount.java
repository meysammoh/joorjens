package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.ComparatorInterface;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "distributor_product_discount", uniqueConstraints = @UniqueConstraint
        (columnNames = {"id_distributor_product", "type"}, name = "UK_DISTRIBUTOR_PRODUCT_DISCOUNT__product_type"))
public class DistributorProductDiscount extends AbstractModel implements ComparatorInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "تخفیف کالای تامین‌کننده";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    @Column(nullable = false)
    private int type = TypeEnumeration.DISCOUNT_TYPE.getId();
    private int buyCount;
    private int offerCount;
    private float percent;
    @Column(name = "fromTime")
    private int from;
    @Column(name = "toTime")
    private int to;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor_product", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PRODUCT_DISCOUNT__DP__distributorProduct"))
    @JsonIgnore
    private DistributorProduct distributorProduct;
    //------------------------------------------------

    public DistributorProductDiscount() {
    }

    public DistributorProductDiscount(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        final TypeEnumeration t = TypeEnumeration.get(type);
        if (t != null && TypeEnumeration.DISCOUNT_TYPE == t.getParent()) {
            this.type = type;
        }
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public int getOfferCount() {
        return offerCount;
    }

    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public DistributorProduct getDistributorProduct() {
        return distributorProduct;
    }

    public void setDistributorProduct(DistributorProduct distributorProduct) {
        this.distributorProduct = distributorProduct;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        final TypeEnumeration type = TypeEnumeration.get(this.type);
        switch (type) {
            case DT_OFFER:
                if(this.buyCount <= 0 || this.offerCount <= 0) {
                    throw new JoorJensException(ExceptionCode.DISCOUNT_BOTH_INT);
                }
                break;
            case DT_CACHE:
            case DT_SPECIAL:
                if(this.percent <= 0 || this.percent > 100) {
                    throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "درصدها", DistributorProductDiscount.getEN());
                }
                break;
            default:
                throw new JoorJensException(ExceptionCode.DISCOUNT_UNKNOWN);
        }
        if(this.from <= 0 || this.from > this.to) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "بازه ها", DistributorProductDiscount.getEN());
        }

        boolean OK = this.distributorProduct != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    @Override
    @JsonIgnore
    public Double getOrder1() {
        return (double) this.from;
    }

    @Override
    @JsonIgnore
    public Double getOrder2() {
        return (double) this.to;
    }

    public boolean isType(final TypeEnumeration type) {
        return type.getId() == this.type;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public boolean isValidTime() {
        final int currentTime = Utility.getCurrentTime();
        return from <= currentTime && currentTime <= to;
    }

    @JsonGetter
    public String getTypeName() {
        final TypeEnumeration t = TypeEnumeration.get(this.type);
        return (t != null) ? t.getFa() : null;
    }

    @JsonGetter
    public long getDistributorProductId() {
        return (this.distributorProduct != null) ? distributorProduct.getId() : 0;
    }

    @JsonGetter
    public long getProductId() {
        return (this.distributorProduct != null) ? distributorProduct.getId() : 0;
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

    @JsonGetter
    public String getDiscountStr() {
        final TypeEnumeration t = TypeEnumeration.get(this.type);
        String name;
        switch (t) {
            case DT_CACHE:
            case DT_SPECIAL:
                name = ((Utility.isEmpty(this.name)) ? t.getFa() : this.name) + " (" + percent + "%)";
                break;
            case DT_OFFER:
                name = this.buyCount + " تا بخر " + this.offerCount + " تا ببر!";
                break;
            default:
                name = null;
                break;
        }
        return name;
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