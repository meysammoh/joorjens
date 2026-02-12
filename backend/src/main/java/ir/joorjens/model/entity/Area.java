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
@Table(name = "area", uniqueConstraints =
@UniqueConstraint(columnNames = {"id_parent", "adType", "name"}, name = "UK_AREA__parent_ad_name"))
public class Area extends AbstractModel {
    private static final long serialVersionUID = 1395L;
    private static final TypeEnumeration FAKE = TypeEnumeration.AD;

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
    private int adType;
    @Column(columnDefinition = "int(11) default 0")
    private int childCount;
    private String note;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_parent", foreignKey = @ForeignKey(name = "FK_AREA__AREA__parent"))
    @JsonIgnore
    private Area parent;

    public Area() {
    }

    public Area(long id) {
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

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        final TypeEnumeration adT = TypeEnumeration.get(adType);
        if (adT != null && TypeEnumeration.AD == adT.getParent()) {
            this.adType = adType;
        }
    }

    public Area getParent() {
        return parent;
    }

    public void setParent(Area parent) {
        this.parent = parent;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        final TypeEnumeration adT = TypeEnumeration.get(this.adType);
        boolean OK = adT != null && TypeEnumeration.AD == adT.getParent()
                && !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(Area area) {
        super.setEdit(area);
        setChildCount(area.getChildCount());
        setParent(area.getParent()); //you can comment this
    }

    public boolean isProvince() {
        return this.adType == TypeEnumeration.AD_PROVINCE.getId();
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public String getAdTypeName() {
        final TypeEnumeration adT = TypeEnumeration.get(this.adType);
        return (adT != null) ? adT.getFa() : null;
    }

    @JsonGetter
    public long getParentId() {
        return (this.parent != null && this.parent.getAdType() != getFakeId()) ? parent.getId() : 0;
    }

    @JsonGetter
    public String getParentName() {
        return (this.parent != null && this.parent.getAdType() != getFakeId()) ? parent.getName() : null;
    }

    @JsonGetter
    public long getGrandId() {
        return (this.parent != null && this.parent.getAdType() != getFakeId()) ? parent.getParentId() : 0;
    }

    @JsonGetter
    public String getGrandName() {
        return (this.parent != null && this.parent.getAdType() != getFakeId()) ? parent.getParentName() : null;
    }

    @JsonGetter
    public long getAncestorId() {
        return (this.parent != null && this.parent.getAdType() != getFakeId()) ? parent.getGrandId() : 0;
    }

    @JsonGetter
    public String getAncestorName() {
        return (this.parent != null && this.parent.getAdType() != getFakeId()) ? parent.getGrandName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setParentId(long id) {
        if (id > 0) {
            this.parent = new Area(id);
        }
    }

    @JsonIgnore
    public void setParentFake() {
        this.parent = new Area(getFakeId());
    }

    //-----------------------------------------------------------------------------------------------------------
}