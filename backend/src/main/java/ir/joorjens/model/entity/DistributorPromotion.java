package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractPromotion;

import javax.persistence.*;

@Entity
@Table(name = "distributor_promotion")
public class DistributorPromotion extends AbstractPromotion {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return AbstractPromotion.getEN() + " " + Distributor.getEN();
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_PROMOTION__DISTRIBUTOR__distributor"))
    @JsonIgnore
    private Distributor distributor;
    //------------------------------------------------

    public DistributorPromotion() {
    }

    public DistributorPromotion(long id) {
        this.id = id;
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    //-----------------------------------------------------------------------------------------------------------

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = super.isValid() && distributor != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getDistributorId() {
        return (this.distributor != null) ? distributor.getId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        return (this.distributor != null) ? distributor.getName() : null;
    }

    @JsonGetter
    public float getDistributorRate() {
        return (this.distributor != null) ? distributor.getRate() : 0.0f;
    }

    @JsonGetter
    public String getDistributorTypeName() {
        return (this.distributor != null) ? distributor.getTypeName() : null;
    }

    @JsonGetter
    public long getDistributorManagerId() {
        return (this.distributor != null) ? distributor.getManagerId() : 0;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setDistributorId(long id) {
        if (id > 0) {
            this.distributor = new Distributor(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}