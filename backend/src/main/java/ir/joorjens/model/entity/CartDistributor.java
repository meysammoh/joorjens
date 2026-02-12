package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.clazz.Third;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.interfaces.ComparatorInterface;
import ir.joorjens.model.util.CartPrice;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "cart_distributor")
public class CartDistributor extends AbstractModel implements ComparatorInterface {
    private static final long serialVersionUID = 1395L;
    //------------------------------------------------

    public static String getEN() {
        return "مشخصات توزیع کننده در سبد خرید";
    }

    @Override
    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    CartPrice cartPrice = new CartPrice();
    @Column(columnDefinition = "int(11) default 0")
    private int timeFinished = 0;
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_distributor", foreignKey = @ForeignKey(name = "FK_CART_DIST__DISTRIBUTOR__distributor"))
    @JsonIgnore
    private Distributor distributor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "id_cart", foreignKey = @ForeignKey(name = "FK_CART_DIST__CART__cart"))
    @JsonIgnore
    private Cart cart;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "cartDistributor", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartDistributorPackduct> packageSet = new HashSet<>();
    //------------------------------------------------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_distributor_deliverer", foreignKey = @ForeignKey(name = "FK_CART_DIST__DISTRIBUTOR_DEL__deliverer"))
    @JsonIgnore
    private DistributorDeliverer deliverer;
    //------------------------------------------------

    public CartDistributor() {
    }

    public CartDistributor(Distributor distributor) {
        this.distributor = distributor;
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
        for(CartDistributorPackduct pack: this.packageSet) {
            if(!pack.isFinished()) {
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

    public CartPrice getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(CartPrice cartPrice) {
        this.cartPrice = cartPrice;
    }

    public Distributor getDistributor() {
        return distributor;
    }

    public void setDistributor(Distributor distributor) {
        this.distributor = distributor;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @JsonGetter
    public List<CartDistributorPackduct> getPackageSet() {
        return Utility.sortList(packageSet, true, 1);
    }

    public void setPackageSet(Set<CartDistributorPackduct> packageList) {
        this.packageSet = packageList;
    }

    public DistributorDeliverer getDeliverer() {
        return deliverer;
    }

    public void setDeliverer(DistributorDeliverer distributorDeliverer) {
        this.deliverer = distributorDeliverer;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    synchronized int update(final Cart cart, final CartDistributorPackduct cdp, final boolean add) {
        CartDistributorPackduct cdpPre = null;
        for (CartDistributorPackduct pack : this.packageSet) {
            if (pack.getKey().equals(cdp.getKey())) {
                cdpPre = pack;
                this.packageSet.remove(pack);
                break;
            }
        }

        if(add) {
            cdp.setCartDistributor(this);
            this.setCart(cart);
            this.packageSet.add(cdp);
            update(cart, this, cdp, cdpPre);
        } else {
            update(cart, this, null, cdpPre);
        }

        return this.packageSet.size();
    }

    private void update(final Cart cart, final CartDistributor cartDistributor
            , final CartDistributorPackduct cdp, final CartDistributorPackduct cdpPre) {
        if (cdpPre != null) {
            cartDistributor.cartPrice.setFields(cdpPre.getCartPrice(), false, cdpPre.isBuyByCheck());
            cart.cartPrice.setFields(cdpPre.getCartPrice(), false, cdpPre.isBuyByCheck());
        }
        if(cdp != null) { //in remove mode
            cdp.setFields();
            cartDistributor.cartPrice.setFields(cdp.getCartPrice(), true, cdp.isBuyByCheck());
            cart.cartPrice.setFields(cdp.getCartPrice(), true, cdp.isBuyByCheck());
        }
    }

    @JsonIgnore
    public Map<Long, Integer> getOrderStatusMap() {
        final Map<Long, Integer> statusMap = new HashMap<>();
        for(CartDistributorPackduct pack: this.packageSet) {
            final long id = pack.getOrderStatusTypeId();
            Integer count = statusMap.get(id);
            if(count == null) {
                count = 0;
            }
            statusMap.put(id, ++count);
        }
        return statusMap;
    }

    @Override
    public Double getOrder1() {
        return (double) getDistributorId();
    }

    @Override
    public Double getOrder2() {
        return 0.0;
    }
    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public boolean isFinished() {
        return timeFinished > 0;
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
    public String getSerial() {
        final String serial;
        if (cart != null && distributor != null && !Utility.isEmpty(cart.getSerial())) {
            serial = cart.getSerial() + Config.SERIAL_SPLITTER + distributor.getSerial();
        } else if (cart != null && !Utility.isEmpty(cart.getSerial())) {
            serial = cart.getSerial();
        } else {
            serial = null;
        }
        return serial;
    }

    @JsonGetter
    public long getCartId() {
        return cart != null ? cart.getId() : 0;
    }

    @JsonGetter
    public long getStoreId() {
        return (this.cart != null) ? cart.getStoreId() : 0;
    }

    @JsonGetter
    public String getStoreName() {
        return cart != null ? cart.getStoreName() : null;
    }

    @JsonGetter
    public String getStoreAddress() {
        return cart != null ? cart.getStoreAddress() : null;
    }

    @JsonGetter
    public long getStoreManagerId() {
        return (this.cart != null) ? cart.getStoreManagerId() : 0;
    }

    @JsonGetter
    public String getStoreManagerName() {
        return (this.cart != null) ? cart.getStoreManagerName() : null;
    }

    @JsonGetter
    public String getStoreManagerMobile() {
        return (this.cart != null) ? cart.getStoreManagerMobile() : null;
    }

    @JsonGetter
    public long getStoreAreaZoneId() {
        return (this.cart != null) ? cart.getStoreAreaZoneId() : 0;
    }

    @JsonGetter
    public String getStoreAreaZoneName() {
        return (this.cart != null) ? cart.getStoreAreaZoneName() : null;
    }

    @JsonGetter
    public long getDistributorId() {
        return distributor != null ? distributor.getId() : 0;
    }

    @JsonGetter
    public String getDistributorName() {
        return distributor != null ? distributor.getName() : null;
    }

    @JsonGetter
    public long getDelivererId() {
        return deliverer != null ? deliverer.getId() : 0;
    }

    @JsonGetter
    public String getDelivererName() {
        return deliverer != null ? deliverer.getName() : null;
    }

    @JsonGetter
    public String getDelivererMobile() {
        return deliverer != null ? deliverer.getMobileNumber() : null;
    }

    @JsonGetter
    public int getPackageSize() {
        return this.packageSet.size();
    }

    //-----------------------------------------------------------------------------------------------------------

}