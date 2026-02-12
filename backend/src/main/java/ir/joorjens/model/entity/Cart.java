package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.CartPrice;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "cart", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"serial"}, name = "UK_CART__serial")})
public class Cart extends AbstractModel {
    private static final long serialVersionUID = 1395L;
//------------------------------------------------

    public static String getEN() {
        return "سبد خرید";
    }

    @Override
    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "serial", nullable = false, length = 30)
    private String serial;
    @Column(columnDefinition = "int(11) default 0")
    private int promotionCredit = 0;
    @Column(columnDefinition = "int(11) default 0")
    private int timeFinished = 0;
    @Column(columnDefinition = "int(11) default 0")
    @JsonIgnore
    private int timeDay = 0, timeWeek = 0, timeMonth = 0;
    CartPrice cartPrice = new CartPrice();

    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_store", foreignKey = @ForeignKey(name = "FK_CART__STORE__store"))
    @JsonIgnore
    private Store store;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartDistributor> distributorSet = new HashSet<>();

    //------------------------------------------------
    @Transient
    private String promotionMessage = null;
    //------------------------------------------------

    public Cart() {
    }

    public Cart(final long id) {
        this.id = id;
    }

    public Cart(final Store store) {
        this.store = store;
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTimeFinished() {
        return timeFinished;
    }

    public void setTimeFinished(int timeFinished) {
        this.timeFinished = timeFinished;
    }

    @JsonIgnore
    public boolean setFinished() {
        boolean isFinished = true;
        for(CartDistributor dist: this.getDistributorSet()) {
            if(!dist.isFinished()) {
                isFinished = false;
            }
        }
        final boolean pre = this.timeFinished > 0;
        if(!pre && isFinished) {
            this.timeFinished = Utility.getCurrentTime();
        } else {
            this.timeFinished = 0;
        }
        return pre;
    }

    public int getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(int timeDay) {
        this.timeDay = timeDay;
    }

    public int getTimeWeek() {
        return timeWeek;
    }

    public void setTimeWeek(int timeWeek) {
        this.timeWeek = timeWeek;
    }

    public int getTimeMonth() {
        return timeMonth;
    }

    public void setTimeMonth(int timeMonth) {
        this.timeMonth = timeMonth;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public int getPromotionCredit() {
        return promotionCredit;
    }

    public void setPromotionCredit(int promotionCredit) {
        this.promotionCredit = promotionCredit;
    }

    public CartPrice getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(CartPrice cartPrice) {
        this.cartPrice = cartPrice;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    @JsonGetter
    public List<CartDistributor> getDistributorSet() {
        return Utility.sortList(distributorSet, true, 1);
    }

    public void setDistributorSet(Set<CartDistributor> distributorList) {
        this.distributorSet = distributorList;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    public String getPromotionMessage() {
        return promotionMessage;
    }

    public void setPromotionMessage(String promotionMessage) {
        this.promotionMessage = promotionMessage;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonIgnore
    public void setKey() {
        this.serial = Utility.randomNumber(Config.CART_SERIAL_LEN) + "";
                //+ "-" + Utility.randomNumber(2 * Config.CART_SERIAL_LEN);
        final int time = Utility.getCurrentTime();
        this.timeDay = Utility.getTimeStamp(time, TypeEnumeration.TS_DAY, true);
        this.timeWeek = Utility.getTimeStamp(time, TypeEnumeration.TS_WEEK, true);
        this.timeMonth = Utility.getTimeStamp(time, TypeEnumeration.TS_MONTH, true);
    }

    public synchronized int update(final CartDistributorPackduct cdp, final boolean add) throws JoorJensException {
        CartDistributor cartDistributor = null;
        for (CartDistributor dist : this.distributorSet) {
            if (dist.getDistributorId() == cdp.getDistributorId()) {
                cartDistributor = dist;
                break;
            }
        }

        if (add) {
            if (cartDistributor == null) {
                cartDistributor = new CartDistributor(cdp.getDistributor());
                this.distributorSet.add(cartDistributor);
            }
        } else {
            if (cartDistributor == null) {
                throw new JoorJensException(ExceptionCode.CART_NOT_FOUND_PRODUCT);
            }
        }
        final int distributorPackageSize = cartDistributor.update(this, cdp, add);
        if (distributorPackageSize == 0) {
            this.distributorSet.remove(cartDistributor);
        }

        return this.distributorSet.size();
    }

    public Map<Long, Integer> getOrderStatusMap() {
        final Map<Long, Integer> statusMap = new HashMap<>();
        for(CartDistributor pack: this.distributorSet) {
            for(Map.Entry<Long, Integer> entry: pack.getOrderStatusMap().entrySet()) {
                Integer count = statusMap.get(entry.getKey());
                if(count == null) {
                    count = 0;
                }
                statusMap.put(entry.getKey(), count + entry.getValue());
            }
        }
        return statusMap;
    }

    public boolean containsDistributor(final long distributorId) {
        for(CartDistributor pack: this.distributorSet) {
            if(pack.getDistributorId() == distributorId) {
                return true;
            }
        }
        return false;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public int getCreatedTime() {
        return super.getCreatedTime();
    }

    @JsonGetter
    public boolean isFinished() {
        for (CartDistributor pack : this.distributorSet) {
            if (!pack.isFinished()) {
                return false;
            }
        }
        return true;
    }

    @JsonGetter
    public List<Third<Long, String, Integer>> getOrderStatus() {
        final List<Third<Long, String, Integer>> statusList = new ArrayList<>();
        statusList.addAll(getOrderStatusMap().entrySet().stream()
                .map(entry -> new Third<>(entry.getKey(), TypeEnumeration.getFa(entry.getKey().intValue()), entry.getValue()))
                .collect(Collectors.toList()));
        return statusList;
    }

    @JsonGetter
    public int getDistributorSize() {
        return this.distributorSet.size();
    }

    @JsonGetter
    public long getPackageSize() {
        int count = 0;
        for (CartDistributor cd : this.distributorSet) {
            count += cd.getPackageSize();
        }
        return count;
    }

    @JsonGetter
    public long getStoreId() {
        return (this.store != null) ? store.getId() : 0;
    }

    @JsonGetter
    public String getStoreName() {
        return (this.store != null) ? store.getName() : null;
    }

    @JsonGetter
    public String getStoreAddress() {
        return (this.store != null) ? store.getAddressFull() : null;
    }

    @JsonGetter
    public long getStoreManagerId() {
        return (this.store != null) ? store.getManagerId() : 0;
    }

    @JsonGetter
    public String getStoreManagerName() {
        return (this.store != null) ? store.getManagerName() : null;
    }

    @JsonGetter
    public String getStoreManagerMobile() {
        return (this.store != null) ? store.getManagerMobile() : null;
    }

    @JsonGetter
    public long getStoreAreaZoneId() {
        return (this.store != null) ? store.getAreaZoneId() : 0;
    }

    @JsonGetter
    public String getStoreAreaZoneName() {
        return (this.store != null) ? store.getAreaZoneName() : null;
    }
    //-----------------------------------------------------------------------------------------------------------

}