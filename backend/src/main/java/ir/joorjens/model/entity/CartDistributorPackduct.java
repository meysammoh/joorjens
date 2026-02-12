package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.ComparatorInterface;
import ir.joorjens.model.util.CartPrice;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "cart_distributor_packduct", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_cart_distributor", "id_distributor_package"}, name = "UK_CART_DIST_PACKDUCT__package"),
        @UniqueConstraint(columnNames = {"id_cart_distributor", "id_distributor_product"}, name = "UK_CART_DIST_PACKDUCT__product")})
public class CartDistributorPackduct extends AbstractModel implements ComparatorInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------

    public static String getEN() {
        return "مشخصات محصول در سبد خرید";
    }

    @Override
    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int type;
    @Column(columnDefinition = "int(11) default 0")
    private int timeFinished = 0;
    private CartPrice cartPrice = new CartPrice();
    //------------------------------------------------
    private boolean buyByCheck = false;
    //------------------------------------------------

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_orderStatusType", foreignKey = @ForeignKey(name = "FK_CART_DIST_PACK__OSTYPE__orderStatusType"))
    @JsonIgnore
    private OrderStatusType orderStatusType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor_product", foreignKey = @ForeignKey(name = "FK_CART_DIST_PACK__DP__distributorProduct"))
    @JsonIgnore
    private DistributorProduct distributorProduct;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor_package", foreignKey = @ForeignKey(name = "FK_CART_DIST_PACK__DP__distributorPackage"))
    @JsonIgnore
    private DistributorPackage distributorPackage;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_cart_distributor", foreignKey = @ForeignKey(name = "FK_CART_DIST_PACK__CD__cartDistributor"))
    @JsonIgnore
    private CartDistributor cartDistributor;
    //------------------------------------------------

    public CartDistributorPackduct() {
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(int timeFinished) {
        this.timeFinished = timeFinished;
    }

    public CartPrice getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(CartPrice cartPrice) {
        this.cartPrice = cartPrice;
    }

    public boolean isBuyByCheck() {
        return buyByCheck;
    }

    public void setBuyByCheck(boolean buyByCheck) {
        this.buyByCheck = buyByCheck;
    }

    public OrderStatusType getOrderStatusType() {
        return orderStatusType;
    }

    public void setOrderStatusType(OrderStatusType orderStatusType) {
        this.orderStatusType = orderStatusType;
    }

    public DistributorProduct getDistributorProduct() {
        return distributorProduct;
    }

    public void setDistributorProduct(DistributorProduct distributorProduct) {
        this.distributorProduct = distributorProduct;
    }

    public DistributorPackage getDistributorPackage() {
        return distributorPackage;
    }

    public void setDistributorPackage(DistributorPackage distributorPackage) {
        this.distributorPackage = distributorPackage;
    }

    public CartDistributor getCartDistributor() {
        return cartDistributor;
    }

    public void setCartDistributor(CartDistributor cartDistributor) {
        this.cartDistributor = cartDistributor;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        if (!isOrdinal() && !isPackage()) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "نوع", getEN());
        }
        if (this.cartPrice.getCount() <= 0) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "تعداد", getEN());
        }
        if ((isOrdinal() && distributorProduct == null)
                || (isPackage() && distributorPackage == null)) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }

        if(this.orderStatusType == null || this.orderStatusType.getId() == 0) {
            this.orderStatusType = new OrderStatusType(TypeEnumeration.OS_RECEIVED.getId());
        }
        if(timeFinished == 0 && TypeEnumeration.OS_DELIVERED.getId() == this.orderStatusType.getId()) {
            timeFinished = Utility.getCurrentTime();
        } else {
            timeFinished = 0;
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonIgnore
    public String getKey() {
        final long id, disId;
        if(this.isPackage() && this.distributorPackage != null) {
            id = distributorPackage.getId();
            disId = distributorPackage.getDistributorId();
        } else if(this.isOrdinal() && this.distributorProduct != null) {
            id = distributorProduct.getId();
            disId = distributorProduct.getDistributorId();
        } else {
            id = disId = 0;
        }
        return disId + "-" + type + "-" + id;
    }

    @JsonIgnore
    public boolean isOrdinal() {
        return TypeEnumeration.CPT_PRODUCT.getId() == this.type;
    }

    @JsonIgnore
    public boolean isPackage() {
        return TypeEnumeration.CPT_PACKAGE.getId() == this.type;
    }

    @JsonIgnore
    void setFields() {
        final long packCount, packPriceConsumer, packPrice, packPriceDiscount;
        final float settlementPercent;
        if (isOrdinal() && distributorProduct != null) {
            distributorPackage = null;
            packCount = 1;
            packPriceConsumer = distributorProduct.getProductPriceConsumer();
            packPrice = distributorProduct.getPrice();
            packPriceDiscount = distributorProduct.getPriceWithDiscount((int) this.cartPrice.getCount(), buyByCheck);
            settlementPercent = distributorProduct.getSettlementPercent();
        } else if (isPackage() && distributorPackage != null) {
            distributorProduct = null;
            packCount = distributorPackage.getAllCount();
            packPriceConsumer = distributorPackage.getAllPriceConsumer();
            packPrice = distributorPackage.getAllPrice();
            packPriceDiscount = distributorPackage.getAllPriceWithDiscount();
            settlementPercent = distributorPackage.getSettlementPercent();
        } else {
            packCount = packPriceConsumer = packPrice = packPriceDiscount = 0;
            settlementPercent = 0;
        }
        this.cartPrice.initFields(packCount, packPriceConsumer, packPrice, packPriceDiscount, buyByCheck, settlementPercent);
    }

    @JsonIgnore
    public Distributor getDistributor() {
        final Distributor distributor;
        if (this.isOrdinal() && this.distributorProduct != null) {
            distributor = distributorProduct.getDistributor();
        } else if (this.isPackage() && this.distributorPackage != null) {
            distributor = distributorPackage.getDistributor();
        } else {
            distributor = null;
        }
        return distributor;
    }

    @Override
    public Double getOrder1() {
        double tmp = 0;
        if (this.isPackage() && this.distributorPackage != null) {
            tmp = distributorPackage.getId();
        } else if (this.isOrdinal() && this.distributorProduct != null) {
            tmp = distributorProduct.getId();
        }
        return tmp;
    }

    @Override
    public Double getOrder2() {
        return 0.0;
    }
    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public boolean isFinished() {
        return timeFinished > 0;
    }

    @JsonGetter
    public String getSerial() {
        return cartDistributor != null ? cartDistributor.getSerial() : null;
    }

    @JsonGetter
    public String getTypeName() {
        return TypeEnumeration.contains(this.type) ? TypeEnumeration.get(this.type).getFa() : null;
    }

    @JsonGetter
    public long getOrderStatusTypeId() {
        return (this.orderStatusType != null) ? this.orderStatusType.getId() : 0;
    }

    @JsonGetter
    public String getOrderStatusTypeName() {
        return (this.orderStatusType != null) ? this.orderStatusType.getName() : null;
    }

    @JsonGetter
    public long getDistributorId() {
        final Distributor distributor = getDistributor();
        return distributor != null ? distributor.getId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        final Distributor distributor = getDistributor();
        return distributor != null ? distributor.getName() : null;
    }

    @JsonGetter
    public long getDistributorPackageId() {
        return this.distributorPackage != null ? this.distributorPackage.getId() : 0;
    }

    @JsonGetter
    public String getDistributorPackageName() {
        return this.distributorPackage != null ? this.distributorPackage.getName() : null;
    }

    @JsonGetter
    public long getProductId() {
        return this.distributorProduct != null ? this.distributorProduct.getProductId() : 0;
    }

    @JsonGetter
    public long getDistributorProductId() {
        return this.distributorProduct != null ? this.distributorProduct.getId() : 0;
    }

    @JsonGetter
    public String getDistributorProductName() {
        return this.distributorProduct != null ? this.distributorProduct.getProductName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setOrderStatusTypeId(long id) {
        if (id > 0) {
            this.orderStatusType = new OrderStatusType(id);
        }
    }

    @JsonSetter
    public void setDistributorProductId(long id) {
        if (id > 0) {
            this.distributorProduct = new DistributorProduct(id);
        }
    }

    @JsonSetter
    public void setDistributorPackageId(long id) {
        if (id > 0) {
            this.distributorPackage = new DistributorPackage(id);
        }
    }

    @JsonSetter
    public void setCount(int count) {
        if (count > 0) {
            this.cartPrice.setCount(count);
        }
    }
    //-----------------------------------------------------------------------------------------------------------

}