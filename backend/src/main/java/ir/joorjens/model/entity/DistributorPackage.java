package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.cache.CacheGuava;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.DistributorPackductInterface;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "distributor_package")
public class DistributorPackage extends AbstractModel implements DistributorPackductInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "بسته‌های تامین‌کننده";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private boolean bundlingOrDiscount;
    private String name;
    @Column(length = 2000)
    private String note;
    @Column(name = "fromTime")
    private int from;
    @Column(name = "toTime")
    private int to;
    private String image;
    @JsonIgnore
    private UUID uuid = Utility.getUUID();
    //------------------------------------------------
    @Column(columnDefinition = "float default '0.00'")
    private float settlementPercent;
    @Column(columnDefinition = "int(11) default 100")
    private int stock;
    @Column(columnDefinition = "int(11) default 10")
    private int stockWarn;
    @Column(columnDefinition = "int(11) default 1")
    private int minOrder = 1;
    @Column(columnDefinition = "int(11) default 20")
    private int maxOrder;
    @Column(columnDefinition = "int(11) default 48")
    private int maxDelivery;
    private int position;
    @Column(columnDefinition = "boolean default false")
    private boolean supportCheck = false;
    //------------------------------------------------
    @Column(columnDefinition = "bigint(20) default 0")
    @JsonIgnore
    private long saleCount = 0;
    //------------------------------------------------
    @Column(columnDefinition = "int(11) default 0")
    private int allCount, allPriceConsumer, allPrice//
            , allPriceWithDiscount, allDiscountPercent;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PACKAGE__DIS__distributor"))
    @JsonIgnore
    private Distributor distributor;
    //------------------------------------------------
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "distributorPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DistributorPackageProduct> packageProducts = new HashSet<>();
    //------------------------------------------------

    public DistributorPackage() {
    }

    public DistributorPackage(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isBundlingOrDiscount() {
        return bundlingOrDiscount;
    }

    public void setBundlingOrDiscount(boolean bundlingOrDiscount) {
        this.bundlingOrDiscount = bundlingOrDiscount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public float getSettlementPercent() {
        return settlementPercent;
    }

    @Override
    public void setSettlementPercent(float settlementPercent) {
        this.settlementPercent = settlementPercent;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStockWarn() {
        return stockWarn;
    }

    public void setStockWarn(int stockWarn) {
        this.stockWarn = stockWarn;
    }

    public int getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(int minOrder) {
        this.minOrder = minOrder;
    }

    public int getMaxOrder() {
        return maxOrder;
    }

    public void setMaxOrder(int maxOrder) {
        this.maxOrder = maxOrder;
    }

    public int getMaxDelivery() {
        return maxDelivery;
    }

    public void setMaxDelivery(int maxDelivery) {
        this.maxDelivery = maxDelivery;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isSupportCheck() {
        return supportCheck;
    }

    public void setSupportCheck(boolean supportCheck) {
        this.supportCheck = supportCheck;
    }

    public long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(long saleCount) {
        this.saleCount = saleCount;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Set<DistributorPackageProduct> getPackageProducts() {
        return packageProducts;
    }

    public void setPackageProducts(Set<DistributorPackageProduct> packageProducts) {
        this.packageProducts = packageProducts;
    }

    public int getAllCount() {
        return allCount;
    }

    public int getAllPriceConsumer() {
        return allPriceConsumer;
    }

    public int getAllPrice() {
        return allPrice;
    }

    public int getAllPriceWithDiscount() {
        return allPriceWithDiscount;
    }

    public int getAllDiscountPercent() {
        return allDiscountPercent;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = distributor != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        if (from == 0 || to <= from) {
            throw new JoorJensException(ExceptionCode.INVALID_TIME);
        }
        final double allSize = CacheGuava.getConfigValue(TypeEnumeration.CONFIG_DISTRIBUTOR_PACK_SIZE_ALL);
        if (packageProducts.size() < allSize) {
            throw new JoorJensException(ExceptionCode.INVALID_CHILD_SIZE
                    , DistributorPackageProduct.getEN(), allSize);
        }
        if (bundlingOrDiscount) {
            int count = 0;
            for (DistributorPackageProduct dpp : this.packageProducts) {
                if (dpp.isDistributorProductOnlyBundling()) {
                    ++count;
                }
            }
            final double bundlingSize = CacheGuava.getConfigValue(TypeEnumeration.CONFIG_DISTRIBUTOR_PACK_SIZE_BUNDLING);
            if (count < bundlingSize) {
                throw new JoorJensException(ExceptionCode.INVALID_CHILD_SIZE, "باندلینگ در " +
                        DistributorPackageProduct.getEN(), bundlingSize);
            }
        }
        setAllPriceFields();
        return true;
    }

    public void setEdit(final DistributorPackage distributorPackage) {
        super.setEdit(distributorPackage);
        setSaleCount(distributorPackage.saleCount);
    }

    @Override
    public boolean isBlock() {
        return super.isBlock() || isExpired();
    }

    @JsonIgnore
    private void setAllPriceFields() {
        this.allCount = this.allPriceConsumer = this.allPrice = this.allPriceWithDiscount = 0;
        for (DistributorPackageProduct pp : this.packageProducts) {
            this.allCount += pp.getCount();
            this.allPriceConsumer += pp.getDistributorProductPriceConsumer();
            this.allPrice += pp.getDistributorProductPrice();
            this.allPriceWithDiscount += pp.getDistributorProductPriceWithDiscount();
        }

        final float percent = 100 - (100 * (this.allPriceWithDiscount / (this.allPrice * 1.0f)));
        this.allDiscountPercent = (int) Utility.round(percent, 2);
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public boolean isValidTime() {
        final int currentTime = Utility.getCurrentTime();
        return from <= currentTime && currentTime <= to;
    }

    @JsonGetter
    public boolean isExpired() {
        return Utility.getCurrentTime() > to;
    }

    @JsonGetter
    public int getYourProfit() {
        return this.allPrice - this.allPriceWithDiscount;
    }

    @JsonGetter
    public long getDistributorId() {
        return (this.distributor != null) ? distributor.getId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        return (this.distributor != null) ? distributor.getName() : null;
    }

    @JsonGetter
    public String getDistributorTypeName() {
        return (this.distributor != null) ? distributor.getTypeName() : null;
    }

    @JsonGetter
    public long getDistributorManagerId() {
        return (this.distributor != null) ? distributor.getManagerId() : 0;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setDistributorId(long id) {
        if (id > 0) {
            this.distributor = new Distributor(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}