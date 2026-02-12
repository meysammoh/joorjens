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
@Table(name = "cartable", indexes = {@Index(columnList = "status", name = "IDX_CARTABLE__status")
        , @Index(columnList = "type", name = "IDX_CARTABLE__type")
        , @Index(columnList = "key_", name = "IDX_CARTABLE__key")}
)
public class Cartable extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "وظیفه";
    }

    public String getEntityName() {
        return getEN();
    }
    //------------------------------------------------

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = true)
    private int doneTime;
    private int status;
    private int type;
    private int priority;
    private String params;
    @Column(columnDefinition = "TEXT")
    private String body;
    @Column(length = 500)
    private String note;
    @Column(name = "key_", length = 50)
    private String key;

    //------------------------------------------------

    @Column(columnDefinition = "boolean default false")
    private boolean toJoorJens;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_from_customer", nullable = false
            , foreignKey = @ForeignKey(name = "FK_CARTABLE__CUSTOMER__fromCustomer"))
    @JsonIgnore
    private Customer fromCustomer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_to_customer", foreignKey = @ForeignKey(name = "FK_CARTABLE__CUSTOMER__toCustomer"))
    @JsonIgnore
    private Customer toCustomer; //assign

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_donner_customer", foreignKey = @ForeignKey(name = "FK_CARTABLE__CUSTOMER__donnerCustomer"))
    @JsonIgnore
    private Customer donnerCustomer; //assign

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_to_distributor", foreignKey = @ForeignKey(name = "FK_CARTABLE__DISTRIBUTOR__toDistributor"))
    @JsonIgnore
    private Distributor toDistributor;
    //------------------------------------------------

    public Cartable() {
        this.status = TypeEnumeration.TS_NEW.getId();
        this.doneTime = 0;
    }

    public Cartable(int type, int priority, String params, String body, String note, boolean toJoorJens
            , Customer fromCustomer, Customer toCustomer, Distributor toDistributor) {
        this();
        this.type = type;
        this.priority = priority;
        this.params = params;
        this.body = body;
        this.note = note;
        this.toJoorJens = toJoorJens;
        this.fromCustomer = fromCustomer;
        this.toCustomer = toCustomer;
        this.toDistributor = toDistributor;
    }

    /**
     * toJoorJens = true
     *
     * @param type         type
     * @param priority     priority
     * @param params       params
     * @param body         json body
     * @param note         note
     * @param fromCustomer from
     */
    public Cartable(int type, int priority, String params, String body, String note, Customer fromCustomer) {
        this(type, priority, params, body, note, true, fromCustomer, null, null);
    }

    public Cartable(String key, int type, int priority, String params, String body, String note, Customer fromCustomer) {
        this(type, priority, params, body, note, true, fromCustomer, null, null);
        setKey(key);
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    public int getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(int doneTime) {
        this.doneTime = doneTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isToJoorJens() {
        return toJoorJens;
    }

    public void setToJoorJens(boolean toJoorJens) {
        this.toJoorJens = toJoorJens;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Customer getFromCustomer() {
        return fromCustomer;
    }

    public void setFromCustomer(Customer fromCustomer) {
        this.fromCustomer = fromCustomer;
    }

    public Customer getToCustomer() {
        return toCustomer;
    }

    public void setToCustomer(Customer toCustomer) {
        this.toCustomer = toCustomer;
    }

    public Customer getDonnerCustomer() {
        return donnerCustomer;
    }

    public void setDonnerCustomer(Customer donnerCustomer) {
        this.donnerCustomer = donnerCustomer;
    }

    public Distributor getToDistributor() {
        return toDistributor;
    }

    public void setToDistributor(Distributor toDistributor) {
        this.toDistributor = toDistributor;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        boolean OK = status > 0 && this.type > 0 && this.priority > 0
                && !Utility.isEmpty(this.body) && this.fromCustomer != null;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    public void setEdit(Cartable cartable) {
        super.setEdit(cartable);
        setFromCustomer(cartable.getFromCustomer());
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public String getDuration() {
        return (this.doneTime > 0) ? Utility.getTime(this.doneTime - this.getCreatedTime()) : "-";
    }

    @JsonGetter
    public String getSubject() {
        return (this.type > 0) ? (TypeEnumeration.get(this.type) != null ? TypeEnumeration.get(this.type).getFa() : "") : "";
    }

    @JsonGetter
    public String getStatusName() {
        return (TypeEnumeration.get(this.status) != null) ? TypeEnumeration.get(this.status).getFa() : "";
    }

    @JsonGetter
    public String getPriorityName() {
        return (TypeEnumeration.get(this.priority) != null) ? TypeEnumeration.get(this.priority).getFa() : "";
    }

    @JsonGetter
    public long getFromCustomerId() {
        return (fromCustomer != null) ? fromCustomer.getId() : 0;
    }

    @JsonGetter
    public String getFromCustomerName() {
        return (fromCustomer != null) ? fromCustomer.getName() : null;
    }

    @JsonGetter
    public String getFromCustomerMobile() {
        return (fromCustomer != null) ? fromCustomer.getMobileNumber() : null;
    }

    @JsonGetter
    public long getToCustomerId() {
        return (toCustomer != null) ? toCustomer.getId() : 0;
    }

    @JsonGetter
    public String getToCustomerName() {
        return (toCustomer != null) ? toCustomer.getName() : null;
    }

    @JsonGetter
    public String getToCustomerMobile() {
        return (toCustomer != null) ? toCustomer.getMobileNumber() : null;
    }

    @JsonGetter
    public long getDonnerCustomerId() {
        return (donnerCustomer != null) ? donnerCustomer.getId() : 0;
    }

    @JsonGetter
    public String getDonnerCustomerName() {
        return (donnerCustomer != null) ? donnerCustomer.getName() : null;
    }

    @JsonGetter
    public String getDonnerCustomerMobile() {
        return (donnerCustomer != null) ? donnerCustomer.getMobileNumber() : null;
    }

    @JsonGetter
    public long getToDistributorId() {
        return (toDistributor != null) ? toDistributor.getId() : 0;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setToCustomerId(long id) {
        if (id > 0) {
            this.toCustomer = new Customer(id);
        }
    }

    @JsonSetter
    public void setToDistributorId(long id) {
        if (id > 0) {
            this.toDistributor = new Distributor(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    @JsonIgnore
    public TypeEnumeration getTaskType() {
        return TypeEnumeration.get(this.type);
    }

    @JsonIgnore
    public TypeEnumeration getStatusType() {
        return TypeEnumeration.get(this.status);
    }
    //-----------------------------------------------------------------------------------------------------------
}