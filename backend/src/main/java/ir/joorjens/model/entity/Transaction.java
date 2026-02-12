package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "transaction")
public class Transaction extends AbstractModel {

    private static final long serialVersionUID = 1395L;
    //------------------------------------------------

    public static String getEN() {
        return "تراکنش مالی";
    }

    @Override
    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private final long amount;
    @Column(columnDefinition = "boolean default false", nullable = false)
    private final boolean sheba, credit; //true: credit(increase), false: debate(decrease&settlement)
    @Column(columnDefinition = "int(11) default 0")
    private int state;
    private boolean success;
    private String referenceId;
    @JsonIgnore
    private String invoiceDateBank, creditNumber;
    @Column(columnDefinition = "int(11) default 0")
    private int invoiceTime;
    private String note, backUrl;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_customer")
    @JsonIgnore
    private final Customer customer;

    @ManyToOne(fetch = FetchType.EAGER) // for customer credit
    @JoinColumn(name = "id_cart")
    @JsonIgnore
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER) // for distributor hesabketab! or credit of distributor
    @JoinColumn(name = "id_distributor")
    @JsonIgnore
    private Distributor distributor;
    //اگر هر سه فیلد بالا پر باشد یعنی توزیع کننده به مشتری برای یک فاکتور اعتبار داده است

    public Transaction() {
        this.amount = 0;
        this.customer = null;
        this.state = TypeEnumeration.TRS_NEW.getId();
        this.credit = this.sheba = false;
    }

    /**
     * for increasing/decreasing credit in buying
     */
    public Transaction(final long amount, final Cart cart, final boolean credit) {
        this.amount = amount;
        this.cart = cart;
        this.customer = cart.getStore().getManager();
        this.sheba = false;
        this.credit = credit; // true credit
        this.state = TypeEnumeration.TRS_COMPLETED.getId();
        this.referenceId = "JoorJens";
        this.invoiceTime = Utility.getCurrentTime();
        this.note = String.format("%s اعتبار توسط جورجنس برای خرید %s", (credit ? "افزایش" : "کاهش"), cart.getSerial());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isSheba() {
        return sheba;
    }

    public boolean isCredit() {
        return credit;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getInvoiceDateBank() {
        return invoiceDateBank;
    }

    public void setInvoiceDateBank(String invoiceDateBank) {
        this.invoiceDateBank = invoiceDateBank;
    }

    public String getCreditNumber() {
        return creditNumber;
    }

    public void setCreditNumber(String creditNumber) {
        this.creditNumber = creditNumber;
    }

    public int getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(int invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    public boolean isValid() {
        if (!this.sheba) {
            this.success = TypeEnumeration.TRS_COMPLETED.getId() == this.state;
        } else {
            this.success = TypeEnumeration.SHS_SUCCESS.getId() == this.state
                    || TypeEnumeration.SHS_SUCCESS_BANK.getId() == this.state;
        }

        return true;
    }

    @JsonIgnore
    public void updateCartNote() {
        if (cart != null) {
            this.note = String.format("%s اعتبار توسط %s برای خرید %s", (credit ? "افزایش" : "کاهش")
                    , (distributor != null ? Distributor.getEN() : "جورجنس")
                    , cart.getSerial());
        }
    }
    //----------------------------------------------------------------------------------------------

    @JsonGetter
    private String getSign() {
        return this.credit ? "+" : "-";
    }

    @JsonGetter
    public String getAmountStr() {
        return Utility.getMoneyTomanStr(this.amount);
    }

    @JsonGetter
    private long getCartId() {
        return this.cart != null ? this.cart.getId() : 0;
    }

    @JsonGetter
    private String getCartSerial() {
        return this.cart != null ? this.cart.getSerial() : null;
    }

    @JsonGetter
    private long getCustomerId() {
        return this.customer != null ? this.customer.getId() : 0;
    }

    @JsonGetter
    private String getCustomerName() {
        return this.customer != null ? this.customer.getName() : null;
    }

    @JsonGetter
    private String getCustomerMobileNumber() {
        return this.customer != null ? this.customer.getMobileNumber() : null;
    }

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
}