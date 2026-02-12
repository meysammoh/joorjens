package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "dictionary", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}, name = "UK_DICTIONARY__name")})
public class Dictionary extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "واژگان";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String name, note;
    //------------------------------------------------

    public Dictionary() {
    }

    public Dictionary(long id) {
        this.id = id;
    }

    //-----------------------------------------------------------------------------------------------------------

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
        boolean OK = !Utility.isEmpty(this.name) && !Utility.isEmpty(this.note);
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }
}