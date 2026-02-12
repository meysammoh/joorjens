package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;

@Entity
@Table(name = "product_detail", uniqueConstraints = @UniqueConstraint
        (columnNames = {"id_product", "id_product_detail_type"}, name = "UK_PRODUCT_DETAILS__product_type"))
public class ProductDetail extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "جزئیات کالا";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 1000)
    private String value;
    private String note;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product_detail_type", foreignKey = @ForeignKey(name = "FK_PRODUCT_DETAILS__DETAIL_TYPE__productDetail"))
    @JsonIgnore
    private ProductDetailType productDetailType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "FK_PRODUCT_DETAILS__PRODUCT__product"))
    @JsonIgnore
    private Product product;
    //------------------------------------------------

    public ProductDetail() {
    }

    public ProductDetail(long id) {
        this.id = id;
    }

    public ProductDetail(String value, ProductDetailType productDetailType, Product product) {
        this.value = value;
        this.productDetailType = productDetailType;
        this.product = product;
    }

    //------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ProductDetailType getProductDetailType() {
        return productDetailType;
    }

    public void setProductDetailType(ProductDetailType productDetailType) {
        this.productDetailType = productDetailType;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.product != null && this.productDetailType != null
                && !Utility.isEmpty(this.value);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setFields(Product product) {
        this.product = product;
        setUpdatedTime(Utility.getCurrentTime());
        if (id == 0) {
            setCreatedTime(Utility.getCurrentTime());
        }
    }
    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getProductId() {
        return (this.product != null) ? product.getId() : 0;
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
    public long getProductDetailTypeId() {
        return (this.productDetailType != null) ? productDetailType.getId() : 0;
    }

    @JsonGetter
    public String getProductDetailTypeName() {
        return (this.productDetailType != null) ? productDetailType.getName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setProductId(long id) {
        if (id > 0) {
            this.product = new Product(id);
        }
    }

    @JsonSetter
    public void setProductDetailTypeId(long id) {
        if (id > 0) {
            this.productDetailType = new ProductDetailType(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}