package ir.joorjens.dao.manager;

import ir.joorjens.dao.repositories.*;
import ir.joorjens.model.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public enum RepositoryManager {
    INSTANCE;
    private final EntityManagerFactory emFactory;

    RepositoryManager() {
        //persistence-unit
        this.emFactory = Persistence.createEntityManagerFactory("JoorJensPersistenceUnit");
    }

    public EntityManager getEntityManager() {
        return this.emFactory.createEntityManager();
    }

    public void close() {
        this.emFactory.close();
    }

    //----------------------------------------------------------------------------------------------

    private static final Map<Class, RepositoryImpAbstract> ENTITY_REPO_MAP = new HashMap<>();

    static {
        ENTITY_REPO_MAP.put(Advertising.class, new AdvertisingRepositoryImpl());
        ENTITY_REPO_MAP.put(Area.class, new AreaRepositoryImpl());
        ENTITY_REPO_MAP.put(Banner.class, new BannerRepositoryImpl());
        ENTITY_REPO_MAP.put(BonusCard.class, new BonusCardRepositoryImpl());
        ENTITY_REPO_MAP.put(BonusCardDetail.class, new BonusCardDetailRepositoryImpl());
        ENTITY_REPO_MAP.put(Cart.class, new CartRepositoryImpl());
        ENTITY_REPO_MAP.put(CartDistributor.class, new CartDistributorRepositoryImpl());
        ENTITY_REPO_MAP.put(CartDistributorPackduct.class, new CartDistributorPackductRepositoryImpl());
        ENTITY_REPO_MAP.put(Cartable.class, new CartableRepositoryImpl());
        ENTITY_REPO_MAP.put(ColorType.class, new ColorTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(ConfigField.class, new ConfigFieldRepositoryImpl());
        ENTITY_REPO_MAP.put(CustomerLogin.class, new CustomerLoginRepositoryImpl());
        ENTITY_REPO_MAP.put(Customer.class, new CustomerRepositoryImpl());
        ENTITY_REPO_MAP.put(CustomerSession.class, new CustomerSessionRepositoryImpl());
        ENTITY_REPO_MAP.put(Dictionary.class, new DictionaryRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorDeliverer.class, new DistributorDelivererRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorDiscontentType.class, new DistributorDiscontentTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorPackage.class, new DistributorPackageRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorEmployee.class, new DistributorEmployeeRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorProduct.class, new DistributorProductRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorProductPackage.class, new DistributorProductPackageRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorProductPriceHistory.class, new DistributorProductPriceHistoryRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorPromotion.class, new DistributorPromotionRepositoryImpl());
        ENTITY_REPO_MAP.put(DistributorRateDiscontent.class, new DistributorRateDiscontentRepositoryImpl());
        ENTITY_REPO_MAP.put(Distributor.class, new DistributorRepositoryImpl());
        ENTITY_REPO_MAP.put(JoorJensEmployee.class, new JoorJensEmployeeRepositoryImpl());
        ENTITY_REPO_MAP.put(Log.class, new LogRepositoryImpl());
        ENTITY_REPO_MAP.put(Message.class, new MessageRepositoryImpl());
        ENTITY_REPO_MAP.put(OrderStatusType.class, new OrderStatusTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(Permission.class, new PermissionRepositoryImpl());
        ENTITY_REPO_MAP.put(Position.class, new PositionRepositoryImpl());
        ENTITY_REPO_MAP.put(Product.class, new ProductRepositoryImpl());
        ENTITY_REPO_MAP.put(ProductBrandType.class, new ProductBrandTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(ProductPriceHistory.class, new ProductPriceHistoryRepositoryImpl());
        ENTITY_REPO_MAP.put(ProductCategoryType.class, new ProductCategoryTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(ProductDetailType.class, new ProductDetailTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(ProductReturnType.class, new ProductReturnTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(Promotion.class, new PromotionRepositoryImpl());
        ENTITY_REPO_MAP.put(Role.class, new RoleRepositoryImpl());
        ENTITY_REPO_MAP.put(Store.class, new StoreRepositoryImpl());
        ENTITY_REPO_MAP.put(ValidIP.class, new ValidIPRepositoryImpl());
        ENTITY_REPO_MAP.put(VehicleBrandType.class, new VehicleBrandTypeRepositoryImpl());
        ENTITY_REPO_MAP.put(Vehicle.class, new VehicleRepositoryImpl());
        ENTITY_REPO_MAP.put(Transaction.class, new TransactionRepositoryImpl());
    }

    public static RepositoryImpAbstract getByEntity(Class clazz) {
        return ENTITY_REPO_MAP.get(clazz);
    }
}
