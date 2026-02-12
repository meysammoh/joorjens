package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bonus_card")
public class BonusCard extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "کارت جایزه";
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
    private int count, price, digit;
    @Column(name = "fromTime")
    private int from;
    @Column(name = "toTime")
    private int to;
    private String note;
    //------------------------------------------------
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "bonusCard", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BonusCardDetail> bonusCardDetails = new HashSet<>();
    //------------------------------------------------

    public BonusCard() {
    }

    public BonusCard(long id) {
        this.id = id;
    }

    public void setEdit(final BonusCard bc) {
        super.setEdit(bc);
        this.bonusCardDetails = bc.bonusCardDetails;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDigit() {
        return digit;
    }

    public void setDigit(int digit) {
        this.digit = digit;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Set<BonusCardDetail> getBonusCardDetails() {
        return bonusCardDetails;
    }

    public void setBonusCardDetails(Set<BonusCardDetail> bonusCardDetails) {
        this.bonusCardDetails = bonusCardDetails;
    }

    public void addBonusCardDetails(final BonusCardDetail bonusCardDetail) {
        this.bonusCardDetails.add(bonusCardDetail);
    }

    public void removeBonusCardDetails(final long bcdId) {
        for (BonusCardDetail bcd: bonusCardDetails) {
            if(bcdId == bcd.getId()) {
                this.bonusCardDetails.remove(bcd);
                break;
            }
        }
    }

    public void clearBonusCardDetails() {
        this.bonusCardDetails.clear();
    }

    @Override
    public boolean isValid() throws JoorJensException {
        if (from == 0 || to <= from) {
            throw new JoorJensException(ExceptionCode.INVALID_TIME);
        }
        if (Utility.isEmpty(name)) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, "نام");
        }

        boolean OK = count > 0 && (digit > 0 && digit < 17) && price > 0;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_PARAM_PARAM, "ارقام");
        }
        return true;
    }
}