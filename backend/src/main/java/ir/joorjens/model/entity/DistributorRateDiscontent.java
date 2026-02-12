package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "distributor_rate_discontent", uniqueConstraints =
@UniqueConstraint(columnNames = {"id_cart", "id_distributor"}, name = "UK_DISTRIBUTOR_RATE__cart_distributor"))
public class DistributorRateDiscontent extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "امتیازها و نارضایتی از شرکت‌پخش‌ها";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    private float rate;
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_RATE_DISCONTENT__DISTRIBUTOR__distributor"))
    @JsonIgnore
    private Distributor distributor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cart", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_RATE_DISCONTENT__cart__cart"))
    @JsonIgnore
    private Cart cart;
    //------------------------------------------------
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(name = "distributor_rate_dis__dis_type",
            joinColumns = @JoinColumn(name = "id_distributor_rate_discontent", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_R_D__DISTRIBUTOR_D_T__distributorRateDis")),
            inverseJoinColumns = @JoinColumn(name = "id_distributor_discontent_type", foreignKey = @ForeignKey(name = "FK_DISTRIBUTOR_R_D__DISTRIBUTOR_D_T__distributorDisType")))
    //@JsonIgnore no need to Ignore
    private Set<DistributorDiscontentType> distributorDiscontentTypes = new HashSet<>();

    //------------------------------------------------
    public DistributorRateDiscontent() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Set<DistributorDiscontentType> getDistributorDiscontentTypes() {
        return distributorDiscontentTypes;
    }

    public void setDistributorDiscontentTypes(Set<DistributorDiscontentType> distributorDiscontentTypes) {
        this.distributorDiscontentTypes = distributorDiscontentTypes;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.rate >= 0 && this.rate <= 5 && this.distributor != null && this.cart != null;
        if (OK && this.rate < 5) {
            OK = this.distributorDiscontentTypes.size() > 0 || !Utility.isEmpty(this.comment);
        }
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
    public long getCartId() {
        return (this.cart != null) ? cart.getId() : 0;
    }

    @JsonGetter
    public String getCartSerial() {
        return (this.distributor != null) ? cart.getSerial() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setDistributorId(long id) {
        if (id > 0) {
            this.distributor = new Distributor(id);
        }
    }

    @JsonSetter
    public void setCartId(long id) {
        if (id > 0) {
            this.cart = new Cart(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}