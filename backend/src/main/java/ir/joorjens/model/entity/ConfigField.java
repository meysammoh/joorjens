package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "config_field", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"name"}, name = "UK_CONFIG_FIELD__name")})
public class ConfigField extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "تنظیمات";
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
    @Column(nullable = false)
    private String value;
    private String note;

    public ConfigField() {
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

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.id > 0 && !Utility.isEmpty(this.value) && !Utility.isEmpty(this.name);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //---------------------------------------------------------------------------------------------

    @JsonIgnore
    public TypeEnumeration getType() {
        return TypeEnumeration.get((int) this.id);
    }

    //---------------------------------------------------------------------------------------------
}