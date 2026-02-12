package ir.joorjens.model.entity;

import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractPromotion;

import javax.persistence.*;

@Entity
@Table(name = "promotion")
public class Promotion extends AbstractPromotion {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return AbstractPromotion.getEN();
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //------------------------------------------------

    public Promotion() {
    }

    public Promotion(long id) {
        this.id = id;
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = super.isValid();
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

}