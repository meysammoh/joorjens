package ir.joorjens.model.entity;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;

@Entity
@Table(name = "vehicle_brand_type", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"name"}, name = "UK_VEHICLE_BRAND_TYPE__name")})
public class VehicleBrandType extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "نوع برند وسیله نقلیه";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name;
    private String note;
    private int capacity; // in KG

    public VehicleBrandType() {
    }

    public VehicleBrandType(long id) {
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.capacity > 0 && !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }
}