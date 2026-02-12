package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "order_status_type", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "UK_ORDER_STATUS_TYPE__name")})
public class OrderStatusType extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "نوع حالت سفارش";
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

    public OrderStatusType() {
    }

    public OrderStatusType(long id) {
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

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.id > 0 && !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //---------------------------------------------------------------------------------------

    @JsonIgnore
    public TypeEnumeration getOrderStatusType() {
        return TypeEnumeration.get((int) this.id);
    }

    //---------------------------------------------------------------------------------------
}