package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

import static ir.joorjens.common.Utility.getCurrentTime;

@Entity
@Table(name = "position", indexes =
        {@Index(columnList = "createdTime", name = "IDX_LOG__createdTime")})
public class Position implements Serializable {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "موقعیت‌ها";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int createdTime;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_customer", nullable = false, foreignKey = @ForeignKey(name = "FK_POSITION__CUSTOMER__customer"))
    @JsonIgnore
    private Customer customer;
    //------------------------------------------------

    public Position() {
        createdTime = getCurrentTime();
    }

    public Position(long customerId, double latitude, double longitude) {
        this();
        this.customer = new Customer(customerId);
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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
    public String getAddress() {
        //todo get reverse from google
        return null;
    }

    //-----------------------------------------------------------------------------------------------------------

}