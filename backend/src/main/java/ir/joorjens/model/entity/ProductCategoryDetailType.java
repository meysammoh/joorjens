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
@Table(name = "product_category_detail_type", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"id_product_category_type", "id_product_detail_type"}, name = "UK_PRODUCT_CATEGORY_DETAIL_TYPE__foreignKeys")})
public class ProductCategoryDetailType extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "انواع جزئیات یک نوع دسته‌بندی";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(columnDefinition = "boolean default false")
    private boolean mandatory = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product_category_type", foreignKey = @ForeignKey(name = "FK_PRODUCT_CAT_DET_TYPE__CAT_TYPE__ProductCategoryType"))
    @JsonIgnore
    private ProductCategoryType productCategoryType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_product_detail_type", foreignKey = @ForeignKey(name = "FK_PRODUCT_CAT_DET_TYPE__DET_TYPE__ProductCategoryDetailType"))
    @JsonIgnore
    private ProductDetailType productDetailType;

    public ProductCategoryDetailType() {
    }

    public ProductCategoryDetailType(ProductCategoryType productCategoryType, ProductDetailType productDetailType) {
        this.productCategoryType = productCategoryType;
        this.productDetailType = productDetailType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public ProductCategoryType getProductCategoryType() {
        return productCategoryType;
    }

    public void setProductCategoryType(ProductCategoryType productCategoryType) {
        this.productCategoryType = productCategoryType;
    }

    public ProductDetailType getProductDetailType() {
        return productDetailType;
    }

    public void setProductDetailType(ProductDetailType productDetailType) {
        this.productDetailType = productDetailType;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = productCategoryType != null && productDetailType != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setFields(ProductCategoryType productCategoryType) {
        this.productCategoryType = productCategoryType;
        setUpdatedTime(Utility.getCurrentTime());
        if (id == 0) {
            setCreatedTime(Utility.getCurrentTime());
        }
    }

    //-----------------------------------------------------------------------------------------------------------

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
    public void setProductDetailTypeId(long id) {
        if (id > 0) {
            this.productDetailType = new ProductDetailType(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}