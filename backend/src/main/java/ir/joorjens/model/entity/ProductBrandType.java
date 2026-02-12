package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "product_brand_type", uniqueConstraints =
@UniqueConstraint(columnNames = {"id_parent", "pbType", "name"}, name = "UK_PRODUCT_BRAND__parent_pb_name"))
public class ProductBrandType extends AbstractModel {
    private static final long serialVersionUID = 1395L;
    private static final TypeEnumeration FAKE = TypeEnumeration.PRODUCT_BRAND_TYPE;

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
    @Column(nullable = false)
    private int pbType;
    @Column(columnDefinition = "int(11) default 0")
    private int childCount;
    @Column(columnDefinition = "int(11) default 0")
    private int weight;
    private String note;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_parent", foreignKey = @ForeignKey(name = "FK_PRODUCT_BRAND__PRODUCT_BRAND__parent"))
    @JsonIgnore
    private ProductBrandType parent;
    //------------------------------------------------
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "product_brand_category_type",
            joinColumns = @JoinColumn(name = "id_product_brand_type", foreignKey = @ForeignKey(name = "FK_PRODUCT_BRAND__CATEGORY_TYPE__brand")),
            inverseJoinColumns = @JoinColumn(name = "id_product_category_type", foreignKey = @ForeignKey(name = "FK_PRODUCT_BRAND__CATEGORY_TYPE__category")))
    private Set<ProductCategoryType> productCategoryTypes = new HashSet<>();
    //------------------------------------------------

    public ProductBrandType() {
    }

    public ProductBrandType(String name, int weigth, int pbType, ProductBrandType parent) {
        this.name = name;
        this.weight = weigth;
        this.pbType = pbType;
        this.parent = parent;
    }

    public ProductBrandType(long id) {
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPbType() {
        return pbType;
    }

    public void setPbType(int pbType) {
        final TypeEnumeration pdT = TypeEnumeration.get(pbType);
        if (pdT != null && TypeEnumeration.PRODUCT_BRAND_TYPE == pdT.getParent()) {
            this.pbType = pbType;
        }
    }

    public ProductBrandType getParent() {
        return parent;
    }

    public void setParent(ProductBrandType parent) {
        this.parent = parent;
    }

    public List<ProductCategoryType> getProductCategoryTypes() {
        return Utility.sortList(productCategoryTypes, false, 1);
    }

    public void setProductCategoryTypes(Set<ProductCategoryType> productCategoryTypes) {
        this.productCategoryTypes = productCategoryTypes;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        final TypeEnumeration pbT = TypeEnumeration.get(this.pbType);
        boolean OK = pbT != null && TypeEnumeration.PRODUCT_BRAND_TYPE == pbT.getParent()
                && !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(ProductBrandType pbt) {
        super.setEdit(pbt);
        setChildCount(pbt.getChildCount());
        setParent(pbt.getParent()); //you can comment this
    }

    public boolean isMain() {
        return this.pbType == TypeEnumeration.PBT_MAIN.getId();
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public String getPbTypeName() {
        final TypeEnumeration pbT = TypeEnumeration.get(this.pbType);
        return (pbT != null) ? pbT.getFa() : null;
    }

    @JsonGetter
    public long getParentId() {
        return (this.parent != null && this.parent.getPbType() != getFakeId()) ? parent.getId() : 0;
    }

    @JsonGetter
    public String getParentName() {
        return (this.parent != null && this.parent.getPbType() != getFakeId()) ? parent.getName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setParentId(long id) {
        if (id > 0) {
            this.parent = new ProductBrandType(id);
        }
    }

    @JsonIgnore
    public void setParentFake() {
        this.parent = new ProductBrandType(getFakeId());
    }

    //-----------------------------------------------------------------------------------------------------------
}