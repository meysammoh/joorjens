package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.io.Serializable;

import static ir.joorjens.common.Utility.getCurrentTime;

@Entity
@Table(name = "log", indexes =
        {@Index(columnList = "createdTime", name = "IDX_LOG__createdTime")})
public class Log implements Serializable {
    private static final long serialVersionUID = 1395L;
    private static final String PACKAGE_NAME = Log.class.getPackage().getName();

    //------------------------------------------------
    public static String getEN() {
        return "لاگ";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int createdTime;
    private int actionType;    //save, delete, update, search
    private int resultType;
    private String className;
    private String note;
    @Column(length = 2_000)
    @JsonIgnore
    private String params;
    //@Column(length = 10_000)
    @Column(columnDefinition = "TEXT")
    @JsonIgnore
    private String objectJson;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer", foreignKey = @ForeignKey(name = "FK_LOG__CUSTOMER__customer"))
    @JsonIgnore
    private Customer customer;
    //------------------------------------------------

    public Log() {
        createdTime = getCurrentTime();
    }

    public Log(long customerId, int actionType, int resultType
            , String className, String params, String objectJson) {
        this();
        this.actionType = actionType;
        this.resultType = resultType;
        this.className = className;
        this.params = params;
        this.objectJson = objectJson;
        this.note = getActionTypeName() + " " + getClassNameFa();
        this.customer = (customerId > 0) ? new Customer(customerId) : null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(int createdTime) {
        this.createdTime = createdTime;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getResultType() {
        return resultType;
    }

    public void setResultType(int resultType) {
        this.resultType = resultType;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getObjectJson() {
        return objectJson;
    }

    public void setObjectJson(String objectJson) {
        this.objectJson = objectJson;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getCustomerId() {
        return (this.customer != null) ? customer.getId() : 0;
    }

    @JsonGetter
    public String getCustomerName() {
        return (this.customer != null) ? customer.getName() : null;
    }

    @JsonGetter
    public String getCustomerMobile() {
        return (this.customer != null) ? customer.getMobileNumber() : null;
    }

    @JsonGetter
    public String getActionTypeName() {
        TypeEnumeration at = TypeEnumeration.get(this.actionType);
        return (at != null) ? at.getFa() : null;
    }

    @JsonGetter
    public String getResultTypeName() {
        TypeEnumeration rt = TypeEnumeration.get(this.resultType);
        return (rt != null) ? rt.getFa() : null;
    }

    @JsonGetter
    public String getClassNameFa() {
        if (!Utility.isEmpty(this.className)) {
            try {
                Class clazz = Class.forName(String.format("%s.%s", PACKAGE_NAME, this.className));
                return ((AbstractModel) clazz.newInstance()).getEntityName();
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    //-----------------------------------------------------------------------------------------------------------
}