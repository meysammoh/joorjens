package ir.joorjens.model.interfaces;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;

import javax.persistence.*;

@MappedSuperclass
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public abstract class AbstractPromotion extends AbstractModel implements ComparatorInterface {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "ترویج";
    }

    public String getEntityName() {
        return getEN();
    }
    //------------------------------------------------
    private int fromPrice;
    private int toPrice;
    private int toPercent; //from:100 - to:200 - percent:10
    private int credit; //credit for next buying

    //------------------------------------------------

    public int getFromPrice() {
        return fromPrice;
    }

    public void setFromPrice(int from) {
        this.fromPrice = from;
    }

    public int getToPrice() {
        return toPrice;
    }

    public void setToPrice(int to) {
        this.toPrice = to;
    }

    public int getToPercent() {
        return toPercent;
    }

    public void setToPercent(int toPercent) {
        this.toPercent = toPercent;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.fromPrice > 0 && this.toPrice > this.fromPrice && this.credit > 0
                && this.toPercent >= 0 && this.toPercent <= 100;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    @Override
    @JsonIgnore
    public Double getOrder1() {
        return (double) this.fromPrice;
    }

    @Override
    @JsonIgnore
    public Double getOrder2() {
        return (double) this.toPrice;
    }

    //------------------------------------------------

    public boolean isShow(int buyingAmount) {
        return !isBlock() && buyingAmount >= getToPriceWithPercent();
    }

    //------------------------------------------------

    @JsonGetter
    public int getToPriceWithPercent() {
        return (int) (this.toPrice - (((100.0f - this.toPercent) / 100.0f) * this.toPrice));
    }

    //------------------------------------------------

}