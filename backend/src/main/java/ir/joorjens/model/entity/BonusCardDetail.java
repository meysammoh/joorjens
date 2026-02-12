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
@Table(name = "bonus_card_detail", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"number"}, name = "UK_BONUS_CARD_DETAIL__number")})
public class BonusCardDetail extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "جزئیات کارت جایزه";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, length = 16)
    private String number;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bonus_card", foreignKey = @ForeignKey(name = "FK_BONUS_CARD_DETAIL__BC__bonusCard"))
    @JsonIgnore
    private BonusCard bonusCard;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer", foreignKey = @ForeignKey(name = "FK_BONUS_CARD_DETAIL__CUSTOMER__customer"))
    @JsonIgnore
    private Customer customer;
    //------------------------------------------------

    public BonusCardDetail() {
    }

    public BonusCardDetail(long id) {
        this.id = id;
    }

    public BonusCardDetail(final BonusCard bonusCard) {
        this.bonusCard = bonusCard;
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public BonusCard getBonusCard() {
        return bonusCard;
    }

    public void setBonusCard(BonusCard bonusCard) {
        this.bonusCard = bonusCard;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = !Utility.isEmpty(number) && bonusCard != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        if (number.length() != bonusCard.getDigit()) {
            throw new JoorJensException(ExceptionCode.BONUS_CARD_DETAIL_DIGIT, "" + bonusCard.getDigit());
        }
        return true;
    }

    @Override
    public boolean isBlock() {
        return super.isBlock() || bonusCard.isBlock();
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonIgnore
    public TypeEnumeration getStatus() {
        final TypeEnumeration status;
        if (customer != null) {
            status = TypeEnumeration.CS_USED;
        } else if (getBonusCardTo() < Utility.getCurrentTime()) {
            status = TypeEnumeration.CS_EXPIRED;
        } else {
            status = TypeEnumeration.CS_NEW;
        }
        return status;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public int getStatusId() {
        return getStatus().getId();
    }

    @JsonGetter
    public String getStatusName() {
        return getStatus().getFa();
    }

    @JsonGetter
    public long getBonusCardId() {
        return (this.bonusCard != null) ? bonusCard.getId() : 0;
    }

    @JsonGetter
    public String getBonusCardName() {
        return (this.bonusCard != null) ? bonusCard.getName() : null;
    }

    @JsonGetter
    public int getBonusCardPrice() {
        return (this.bonusCard != null) ? bonusCard.getPrice() : 0;
    }

    @JsonGetter
    public int getBonusCardFrom() {
        return (this.bonusCard != null) ? bonusCard.getFrom() : 0;
    }

    @JsonGetter
    public int getBonusCardTo() {
        return (this.bonusCard != null) ? bonusCard.getTo() : 0;
    }

    @JsonGetter
    public long getCustomerId() {
        return (this.customer != null) ? customer.getId() : 0;
    }

    @JsonGetter
    public String getCustomerName() {
        return (this.customer != null) ? customer.getName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setBonusCardId(long id) {
        if (id > 0) {
            this.bonusCard = new BonusCard(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
}