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
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_category_type", uniqueConstraints =
@UniqueConstraint(columnNames = {"id_parent", "name"}, name = "UK_PRODUCT_CATEGORY_TYPE__parent_name"))
public class ProductCategoryType extends AbstractModel implements ComparatorInterface {
    private static final long serialVersionUID = 1395L;
    private static final TypeEnumeration FAKE = TypeEnumeration.PRODUCT_CATEGORY_TYPE;

    //------------------------------------------------
    public static String getEN() {
        return FAKE.getFa();
    }

    public String getEntityName() {
        return getEN();
    }

    public static TypeEnumeration getFake() {
        return FAKE;
    }

    public static long getFakeId() {
        return FAKE.getId();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;
    private String note;
    @Column(columnDefinition = "int(11) default 0")
    private int childCount;
    @Column(columnDefinition = "int(11) default 0")
    private int weight;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_parent", foreignKey = @ForeignKey(name = "FK_PRODUCT_CAT_TYPE__PRODUCT_CAT_TYPE__parent"))
    @JsonIgnore
    private ProductCategoryType parent;
    //------------------------------------------------
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "productCategoryType", cascade = CascadeType.ALL, orphanRemoval = true)
    //@JsonIgnore no need to Ignore because ProductCategoryTypeDetailTypes is OK!
    private Set<ProductCategoryDetailType> productDetailTypes = new HashSet<>();

    //------------------------------------------------

    public ProductCategoryType() {
    }

    public ProductCategoryType(long id) {
        this.id = id;
    }

    public ProductCategoryType(String name, ProductCategoryType parent) {
        this.name = name;
        this.parent = parent;
    }

    //------------------------------------------------

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public ProductCategoryType getParent() {
        return parent;
    }

    public void setParent(ProductCategoryType parent) {
        this.parent = parent;
    }

    public Set<ProductCategoryDetailType> getProductDetailTypes() {
        return productDetailTypes;
    }

    public void setProductDetailTypes(Set<ProductCategoryDetailType> productDetailTypes) {
        this.productDetailTypes = productDetailTypes;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(ProductCategoryType productCategoryType) {
        super.setEdit(productCategoryType);
        setChildCount(productCategoryType.getChildCount());
        setParent(productCategoryType.getParent());
        //setProductDetailTypes(productCategoryType.getProductDetailTypes());
    }

    public boolean isFistLevel() {
        return this.parent == null || this.parent.getId() == getFakeId();
    }

    public void addProductDetailType(ProductCategoryDetailType categoryDetailType) {
        this.productDetailTypes.add(categoryDetailType);
    }

    @Override
    public Double getOrder1() {
        return (double) this.weight;
    }

    @Override
    public Double getOrder2() {
        return !Utility.isEmpty(this.name) ? (double) this.name.charAt(0) : 0;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getParentId() {
        return (this.parent != null && this.parent.getId() != getFakeId()) ? parent.getId() : 0;
    }

    @JsonGetter
    public String getParentName() {
        return (this.parent != null && this.parent.getId() != getFakeId()) ? parent.getName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setParentId(long id) {
        if (id > 0) {
            this.parent = new ProductCategoryType(id);
        }
    }

    @JsonIgnore
    public void setParentFake() {
        this.parent = new ProductCategoryType(getFakeId());
    }

    @JsonIgnore
    public boolean isFake() {
        return this.id == getFakeId();
    }

    //-----------------------------------------------------------------------------------------------------------
}