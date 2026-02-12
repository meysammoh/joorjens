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

@Entity
@Table(name = "product_detail_type", uniqueConstraints =
@UniqueConstraint(columnNames = {"id_parent", "name"}, name = "UK_PRODUCT_DETAIL_TYPE__parent_name"))
public class ProductDetailType extends AbstractModel {
    private static final long serialVersionUID = 1395L;
    private static final TypeEnumeration FAKE = TypeEnumeration.PRODUCT_DETAIL_TYPE;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_parent", foreignKey = @ForeignKey(name = "FK_PRODUCT_DETAIL_TYPE__PRODUCT_DETAIL_TYPE__parent"))
    @JsonIgnore
    private ProductDetailType parent;

    public ProductDetailType() {
    }

    public ProductDetailType(long id) {
        this.id = id;
    }

    public ProductDetailType(String name, int weight, ProductDetailType parent) {
        this.name = name;
        this.weight = weight;
        this.parent = parent;
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

    public ProductDetailType getParent() {
        return parent;
    }

    public void setParent(ProductDetailType parent) {
        this.parent = parent;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(ProductDetailType productDetailType) {
        super.setEdit(productDetailType);
        setChildCount(productDetailType.getChildCount());
        setParent(productDetailType.getParent());
    }

    public boolean isFistLevel() {
        return this.parent == null || this.parent.getId() == getFakeId();
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
            this.parent = new ProductDetailType(id);
        }
    }

    @JsonIgnore
    public void setParentFake() {
        this.parent = new ProductDetailType(getFakeId());
    }

    //-----------------------------------------------------------------------------------------------------------
}