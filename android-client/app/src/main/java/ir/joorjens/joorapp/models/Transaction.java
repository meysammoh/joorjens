package ir.joorjens.joorapp.models;

/**
 * Created by batman on 5/4/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Transaction {

    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("sheba")
    @Expose
    private Boolean sheba;
    @SerializedName("credit")
    @Expose
    private Boolean credit;
    @SerializedName("state")
    @Expose
    private Integer state;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("referenceId")
    @Expose
    private String referenceId;
    @SerializedName("invoiceTime")
    @Expose
    private Integer invoiceTime;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("sign")
    @Expose
    private String sign;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("amountStr")
    @Expose
    private String amountStr;
    @SerializedName("cartId")
    @Expose
    private Integer cartId;
    @SerializedName("cartSerial")
    @Expose
    private String cartSerial;
    @SerializedName("customerMobileNumber")
    @Expose
    private String customerMobileNumber;

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Boolean getSheba() {
        return sheba;
    }

    public void setSheba(Boolean sheba) {
        this.sheba = sheba;
    }

    public Boolean getCredit() {
        return credit;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Integer getInvoiceTime() {
        return invoiceTime;
    }

    public void setInvoiceTime(Integer invoiceTime) {
        this.invoiceTime = invoiceTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAmountStr() {
        return amountStr;
    }

    public void setAmountStr(String amountStr) {
        this.amountStr = amountStr;
    }

    public Integer getCartId() {
        return cartId;
    }

    public void setCartId(Integer cartId) {
        this.cartId = cartId;
    }

    public String getCartSerial() {
        return cartSerial;
    }

    public void setCartSerial(String cartSerial) {
        this.cartSerial = cartSerial;
    }

    public String getCustomerMobileNumber() {
        return customerMobileNumber;
    }

    public void setCustomerMobileNumber(String customerMobileNumber) {
        this.customerMobileNumber = customerMobileNumber;
    }

}
