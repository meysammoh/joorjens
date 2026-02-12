package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"barcode"}, name = "UK_PRODUCT__barcode")})
public class Product extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "کالا";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "barcode", nullable = false, length = 30)
    private String barcode;

    @Column(nullable = false)
    private String name;
    private String image;
    @Column(length = 2000)
    private String note;
    @Column(columnDefinition = "int(11) default 0")
    private int priceConsumer;
    @Column(columnDefinition = "int(11) default 0")
    private int position = 0;
    //------------------------------------------------
    @Column(columnDefinition = "bigint(20) default 0")
    @JsonIgnore
    private long saleCount = 0;
    @Column(columnDefinition = "float default '5.00'")
    @JsonIgnore
    private float rateSum = 5;
    @Column(columnDefinition = "int(11) default 1")
    @JsonIgnore
    private int rateCount = 1;
    //------------------------------------------------
    private String productBrandTypeNameTyped;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product_brand_type", foreignKey = @ForeignKey(name = "FK_PRODUCT__BRANT_TYPE__pbt"))
    @JsonIgnore
    private ProductBrandType productBrandType;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_product_category_type", foreignKey = @ForeignKey(name = "FK_PRODUCT__CATEGORY_TYPE__pct"))
    @JsonIgnore
    private ProductCategoryType productCategoryType;
    //------------------------------------------------
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductDetail> productDetails = new HashSet<>();
    //------------------------------------------------

    public Product() {
    }

    public Product(long id) {
        this.id = id;
    }

    public Product(String barcode, String name, int priceConsumer) {
        this.barcode = barcode;
        this.name = name;
        this.priceConsumer = priceConsumer;
    }

    //------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getPriceConsumer() {
        return priceConsumer;
    }

    public void setPriceConsumer(int priceConsumer) {
        this.priceConsumer = priceConsumer;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(long saleCount) {
        this.saleCount = saleCount;
    }

    public float getRateSum() {
        return rateSum;
    }

    public void setRateSum(float rateSum) {
        this.rateSum = rateSum;
    }

    public int getRateCount() {
        return rateCount;
    }

    public void setRateCount(int rateCount) {
        this.rateCount = rateCount;
    }

    public String getProductBrandTypeNameTyped() {
        return productBrandTypeNameTyped;
    }

    public void setProductBrandTypeNameTyped(String productBrandTypeNameTyped) {
        this.productBrandTypeNameTyped = productBrandTypeNameTyped;
    }

    public ProductBrandType getProductBrandType() {
        return productBrandType;
    }

    public void setProductBrandType(ProductBrandType productBrandType) {
        this.productBrandType = productBrandType;
    }

    public ProductCategoryType getProductCategoryType() {
        return productCategoryType;
    }

    public void setProductCategoryType(ProductCategoryType productCategoryType) {
        this.productCategoryType = productCategoryType;
    }

    public Set<ProductDetail> getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(Set<ProductDetail> productDetails) {
        this.productDetails = productDetails;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        final boolean OK = !Utility.isEmpty(this.name) && !Utility.isEmpty(this.barcode)
                && priceConsumer > 0 && productCategoryType != null
                && (productBrandType != null || !Utility.isEmpty(productBrandTypeNameTyped));
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(Product product) {
        super.setEdit(product);
        setSaleCount(product.saleCount);
        setRateSum(product.rateSum);
        setRateCount(product.rateCount);
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public float getRate() {
        return (float) Utility.round((rateSum / rateCount), 2);
    }

    @JsonGetter
    public long getProductBrandTypeId() {
        return (this.productBrandType != null) ? productBrandType.getId() : 0;
    }

    @JsonGetter
    public String getProductBrandTypeName() {
        return (this.productBrandType != null) ? productBrandType.getName() : null;
    }

    @JsonGetter
    public long getProductBrandTypeParentId() {
        return (this.productBrandType != null) ? productBrandType.getParentId() : 0;
    }

    @JsonGetter
    public String getProductBrandTypeParentName() {
        return (this.productBrandType != null) ? productBrandType.getParentName() : null;
    }

    @JsonGetter
    public long getProductCategoryTypeId() {
        return (this.productCategoryType != null) ? productCategoryType.getId() : 0;
    }

    @JsonGetter
    public String getProductCategoryTypeName() {
        return (this.productCategoryType != null) ? productCategoryType.getName() : null;
    }

    @JsonGetter
    public long getProductCategoryTypeParentId() {
        return (this.productCategoryType != null) ? productCategoryType.getParentId() : 0;
    }

    @JsonGetter
    public String getProductCategoryTypeParentName() {
        return (this.productCategoryType != null) ? productCategoryType.getParentName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setProductBrandTypeId(long id) {
        if (id > 0) {
            this.productBrandType = new ProductBrandType(id);
        }
    }

    @JsonSetter
    public void setProductCategoryTypeId(long id) {
        if (id > 0) {
            this.productCategoryType = new ProductCategoryType(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}