package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.DistributorPackductInterface;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "distributor_product", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"id_distributor", "id_product"}, name = "UK_DISTRIBUTOR_PRODUCT__id_dis_product")})
public class DistributorProduct extends AbstractModel implements DistributorPackductInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "کالای تامین‌کننده";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private boolean onlyBundling;
    private int price, priceMin;
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PRODUCT__DISTRIBUTOR__distributor"))
    @JsonIgnore
    private Distributor distributor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PRODUCT__PRODUCT__product"))
    @JsonIgnore
    private Product product;
    //------------------------------------------------
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "distributorProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DistributorProductPackage> packages = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "distributorProduct", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DistributorProductDiscount> discounts = new HashSet<>();
    //------------------------------------------------

    public DistributorProduct() {
    }

    public DistributorProduct(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isOnlyBundling() {
        return onlyBundling;
    }

    public void setOnlyBundling(boolean onlyBundling) {
        this.onlyBundling = onlyBundling;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(int priceMin) {
        this.priceMin = priceMin;
    }

    public float getSettlementPercent() {
        return settlementPercent;
    }

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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @JsonGetter
    public List<DistributorProductPackage> getPackages() {
        return Utility.sortList(packages, true, 1);
    }

    public void setPackages(Set<DistributorProductPackage> packages) {
        this.packages = packages;
    }

    @JsonGetter
    public List<DistributorProductDiscount> getDiscounts() {
        return Utility.sortList(discounts, true, 1);
    }

    public void setDiscounts(Set<DistributorProductDiscount> discounts) {
        this.discounts = discounts;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = distributor != null && product != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        if (!Utility.isFromToOk(this.packages)) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, "بازه‌ها", DistributorProductPackage.getEN());
        }
        //times of this.discounts ----------------------------------
        final Map<Integer, List<DistributorProductDiscount>> dpdMap = new HashMap<>();
        for (DistributorProductDiscount dpd : this.discounts) {
            if (!dpdMap.containsKey(dpd.getType())) {
                dpdMap.put(dpd.getType(), new ArrayList<>());
            }
            dpdMap.get(dpd.getType()).add(dpd);
        }
        for (Map.Entry<Integer, List<DistributorProductDiscount>> entry : dpdMap.entrySet()) {
            if (!Utility.isFromToOk(entry.getValue())) {
                final String name = "بازه های " + TypeEnumeration.get(entry.getKey()).getFa();
                throw new JoorJensException(ExceptionCode.INVALID_PARAM_OBJECT, name, DistributorProductDiscount.getEN());
            }
        }
        //----------------------------------------------------------
        this.priceMin = calculatePriceMin(this.price);
        return true;
    }


    public void setEdit(final DistributorProduct distributorProduct) {
        super.setEdit(distributorProduct);
        setSaleCount(distributorProduct.saleCount);
    }

    public int calculatePriceMin(int price) {
        int priceMin = price;
        if (this.packages.size() > 0) {
            final List<DistributorProductPackage> list = Utility.sortList(this.packages, false, 1);
            final DistributorProductPackage dpp = list.get(0);
            priceMin = dpp.getPriceDistributorWithDiscount(); // * this.minOrder;
        }
        return priceMin;
    }
    //-----------------------------------------------------------------------------------------------------------

    public int getPriceWithDiscount(final int count, final boolean buyCheck) {
        float percent = 0.0f;
        if(this.packages.size() > 0) {
            for(DistributorProductPackage tmp: this.packages) {
                if(count >= tmp.getCountFrom() && count <= tmp.getCountTo()) {
                    percent = tmp.getPercent();
                    break;
                }
            }
            for(DistributorProductDiscount tmp: this.discounts) {
                if (tmp.isValidTime()) {
                    if (tmp.isType(TypeEnumeration.DT_SPECIAL)) {
                        percent += tmp.getPercent();
                    } else if (!buyCheck && tmp.isType(TypeEnumeration.DT_CACHE)) {
                        percent += tmp.getPercent();
                    }
                }
            }
            if(percent > 100) {
                percent = 100;
            }
        }
        return (int) Utility.getPrice(this.price, percent);
    }

    public int getOfferCount(final int count) {
        int offerCount = 0;
        for(DistributorProductDiscount tmp: this.discounts) {
            if (tmp.isType(TypeEnumeration.DT_OFFER) && count >= tmp.getBuyCount()
                    && offerCount < tmp.getOfferCount() && tmp.isValidTime()) {
                offerCount = tmp.getOfferCount();
            }
        }
        return offerCount;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getProductId() {
        return (this.product != null) ? product.getId() : 0;
    }

    @JsonGetter
    public int getProductPriceConsumer() {
        return (this.product != null) ? product.getPriceConsumer() : 0;
    }

    @JsonGetter
    public String getProductBarcode() {
        return (this.product != null) ? product.getBarcode() : null;
    }

    @JsonGetter
    public String getProductName() {
        return (this.product != null) ? product.getName() : null;
    }

    @JsonGetter
    public String getProductNote() {
        return (this.product != null) ? product.getNote() : null;
    }

    @JsonGetter
    public float getProductRate() {
        return (this.product != null) ? product.getRate() : 0.0f;
    }

    @JsonGetter
    public String getProductImage() {
        return (this.product != null) ? product.getImage() : null;
    }

    @JsonGetter
    public long getProductCategoryTypeId() {
        return (this.product != null) ? product.getProductCategoryTypeId() : 0;
    }

    @JsonGetter
    public String getProductCategoryTypeName() {
        return (this.product != null) ? product.getProductCategoryTypeName() : null;
    }

    @JsonGetter
    public long getProductCategoryTypeParentId() {
        return (this.product != null) ? product.getProductCategoryTypeParentId() : 0;
    }

    @JsonGetter
    public String getProductCategoryTypeParentName() {
        return (this.product != null) ? product.getProductCategoryTypeParentName() : null;
    }

    @JsonGetter
    public long getProductBrandTypeId() {
        return (this.product != null) ? product.getProductBrandTypeId() : 0;
    }

    @JsonGetter
    public String getProductBrandTypeName() {
        return (this.product != null) ? product.getProductBrandTypeName() : null;
    }

    @JsonGetter
    public long getProductBrandTypeParentId() {
        return (this.product != null) ? product.getProductBrandTypeParentId() : 0;
    }

    @JsonGetter
    public String getProductBrandTypeParentName() {
        return (this.product != null) ? product.getProductBrandTypeParentName() : null;
    }

    @JsonGetter
    public Set<ProductDetail> getProductDetails() {
        return (this.product != null) ? product.getProductDetails() : new HashSet<>();
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
    public float getDistributorRate() {
        return (this.distributor != null) ? distributor.getRate() : 0.0f;
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
    public void setProductId(long id) {
        if (id > 0) {
            this.product = new Product(id);
        }
    }

    @JsonSetter
    public void setDistributorId(long id) {
        if (id > 0) {
            this.distributor = new Distributor(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}