package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "vehicle", uniqueConstraints = {@UniqueConstraint(
        columnNames = {"licensePlate"}, name = "UK_VEHICLE__licensePlate")})
public class Vehicle extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "وسیله نقلیه";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false, length = 14)
    private String licensePlate;         //۱۲#الف#۳۴۵#۱۱۱
    private int manufactureYear;
    private String note;

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehicle_brand_type", foreignKey = @ForeignKey(name = "FK_VEHICLE__VEHICLE_BRAND_TYPE__vehicleBrandType"))
    @JsonIgnore
    private VehicleBrandType vehicleBrandType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_colorType", foreignKey = @ForeignKey(name = "FK_VEHICLE__COLOR_TYPE__colorType"))
    @JsonIgnore
    private ColorType colorType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_VEHICLE__DISTRIBUTOR__distributor"))
    @JsonIgnore
    private Distributor distributor;
    //------------------------------------------------
    @Transient
    private int licensePlateCode, licensePlateFirst, licensePlateSecond;
    @Transient
    private String licensePlateLetter;
    //------------------------------------------------

    public Vehicle() {
    }

    public Vehicle(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ColorType getColorType() {
        return colorType;
    }

    public void setColorType(ColorType colorType) {
        this.colorType = colorType;
    }

    public int getManufactureYear() {
        return manufactureYear;
    }

    public void setManufactureYear(int manufactureYear) {
        this.manufactureYear = manufactureYear;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public VehicleBrandType getVehicleBrandType() {
        return vehicleBrandType;
    }

    public void setVehicleBrandType(VehicleBrandType vehicleBrandType) {
        this.vehicleBrandType = vehicleBrandType;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        setLicensePlateDetails();
        boolean OK = !Utility.isEmpty(this.licensePlate) && this.colorType != null
                && this.vehicleBrandType != null && this.distributor != null;
        if (OK) {
            final String[] split = this.licensePlate.split("" + Config.CHAR_SPLITTER);
            OK = (split.length == 4);
        }
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------
    @JsonGetter
    public String getLicensePlate() {
        setLicensePlateDetails();
        return this.licensePlate;
    }

    @JsonSetter
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
        setLicensePlateDetails();
    }

    @JsonGetter
    public int getLicensePlateFirst() {
        if (licensePlateFirst == 0) {
            setLicensePlateDetails();
        }
        return licensePlateFirst;
    }

    @JsonGetter
    public String getLicensePlatePersian() {
        return getLicensePlateCode() + "-" + getLicensePlateSecond() + getLicensePlateLetter() + getLicensePlateFirst();
    }

    @JsonSetter
    public void setLicensePlateFirst(int licensePlateFirst) {
        this.licensePlateFirst = licensePlateFirst;
    }

    @JsonGetter
    public String getLicensePlateLetter() {
        if (Utility.isEmpty(licensePlateLetter)) {
            setLicensePlateDetails();
        }
        return licensePlateLetter;
    }

    @JsonSetter
    public void setLicensePlateLetter(String licensePlateLetter) {
        this.licensePlateLetter = licensePlateLetter;
    }

    @JsonGetter
    public int getLicensePlateSecond() {
        if (licensePlateSecond == 0) {
            setLicensePlateDetails();
        }
        return licensePlateSecond;
    }

    @JsonSetter
    public void setLicensePlateSecond(int licensePlateSecond) {
        this.licensePlateSecond = licensePlateSecond;
    }

    @JsonGetter
    public int getLicensePlateCode() {
        if (licensePlateCode == 0) {
            setLicensePlateDetails();
        }
        return licensePlateCode;
    }

    @JsonSetter
    public void setLicensePlateCode(int licensePlateCode) {
        this.licensePlateCode = licensePlateCode;
    }

    @JsonGetter
    public boolean isLicensePlateEven() {
        if (this.licensePlateSecond == 0) {
            setLicensePlateDetails();
        }
        return this.licensePlateSecond % 2 == 0;
    }

    @JsonSetter
    public void setLicensePlateDetails() {
        if (this.licensePlate != null) {
            if (this.licensePlate.indexOf(Config.CHAR_SPLITTER) > 0) {
                if (this.licensePlateFirst == 0) {
                    String[] split = this.licensePlate.split("" + Config.CHAR_SPLITTER);
                    if (split.length == 4) {
                        this.licensePlateFirst = Integer.parseInt(split[0]);
                        this.licensePlateLetter = split[1];
                        this.licensePlateSecond = Integer.parseInt(split[2]);
                        this.licensePlateCode = Integer.parseInt(split[3]);
                    }
                }
            } else {
                this.licensePlate = null;
            }
        }
        if (this.licensePlate == null && this.licensePlateFirst > 9 &&
                this.licensePlateSecond > 99 && this.licensePlateCode > 9 && !Utility.isEmpty(this.licensePlateLetter)) {
            this.licensePlate = getLicensePlate(this.licensePlateFirst, this.licensePlateLetter
                    , this.licensePlateSecond, this.licensePlateCode);
        }
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public long getColorTypeId() {
        return colorType != null ? colorType.getId() : 0;
    }

    @JsonGetter
    public String getColorTypeName() {
        return colorType != null ? colorType.getName() : null;
    }

    @JsonGetter
    public String getColorTypeCode() {
        return colorType != null ? colorType.getCode() : null;
    }

    @JsonGetter
    public long getVehicleBrandTypeId() {
        return (this.vehicleBrandType != null) ? vehicleBrandType.getId() : 0;
    }

    @JsonGetter
    public String getVehicleBrandTypeName() {
        return (this.vehicleBrandType != null) ? vehicleBrandType.getName() : null;
    }

    @JsonGetter
    public int getVehicleBrandTypeCapacity() {
        return (this.vehicleBrandType != null) ? vehicleBrandType.getCapacity() : 0;
    }

    @JsonGetter
    public long getDistributorId() {
        return (this.distributor != null) ? this.distributor.getId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        return (this.distributor != null) ? this.distributor.getName() : null;
    }

    @JsonGetter
    public String getDistributorAreaCityName() {
        return (this.distributor != null) ? this.distributor.getAreaCityName() : null;
    }

    @JsonGetter
    public String getDistributorSerial() {
        return (this.distributor != null) ? this.distributor.getSerial() : null;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setVehicleBrandTypeId(long id) {
        if (id > 0) {
            this.vehicleBrandType = new VehicleBrandType(id);
        }
    }

    @JsonSetter
    public void setDistributorId(long id) {
        if (id > 0) {
            this.distributor = new Distributor(id);
        }
    }

    @JsonSetter
    public void setColorTypeId(long id) {
        if (id > 0) {
            this.colorType = new ColorType(id);
        }
    }

    //-----------------------------------------------------------------------------------------------------------
    public static String getLicensePlate(int licensePlateFirst, String licensePlateLetter
            , int licensePlateSecond, int licensePlateCode) {
        return "" + licensePlateFirst
                + Config.CHAR_SPLITTER + licensePlateLetter
                + Config.CHAR_SPLITTER + licensePlateSecond
                + Config.CHAR_SPLITTER + licensePlateCode;
    }
    //-----------------------------------------------------------------------------------------------------------
}