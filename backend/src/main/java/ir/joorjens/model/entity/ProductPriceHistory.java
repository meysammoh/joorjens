package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;

import javax.persistence.*;

@Entity
@Table(name = "product_price_history")
public class ProductPriceHistory extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "تاریخچه قیمت‌ مصرف کننده";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private final int price;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_product", foreignKey = @ForeignKey(name = "FK_PRODUCT_PRICE_HIS__PRODUCT__product"))
    @JsonIgnore
    private final Product product;
    //------------------------------------------------

    public ProductPriceHistory() {
        this(0,0);
    }

    public ProductPriceHistory(long productId, int price) {
        this.product = new Product(productId);
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = this.product != null && this.price > 0;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public int getTime() {
        return getCreatedTime();
    }

    @JsonGetter
    public long getProductId() {
        return (this.product != null) ? product.getId() : 0;
    }

    @JsonGetter
    public String getProductBarcode() {
        return (this.product != null) ? product.getBarcode() : null;
    }

    @JsonGetter
    public String getProductName() {
        return (this.product != null) ? product.getName() : null;
    }

    //-----------------------------------------------------------------------------------------------------------
}