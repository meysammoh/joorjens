package ir.joorjens.run;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.background.Calculator;
import ir.joorjens.background.LogTask;
import ir.joorjens.background.PositionTask;
import ir.joorjens.background.StocksUpdater;
import ir.joorjens.common.ThreadFactory;
import ir.joorjens.common.Utility;
import ir.joorjens.controller.*;
import ir.joorjens.dao.interfaces.*;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.TypeEnumeration;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static spark.Spark.*;

public class JoorJens {

    //---------------------------------------------------------------------------------------------------
    private static final Logger logger = LoggerFactory.getLogger(JoorJens.class);
    //---------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        Config.loadConfig();
        readArgs(args);

        initDB();
        runService();
        loadModules();
        runBackgroundTasks();

        logger.info(String.format("JoorJens is ready now: Port(%s)", Config.API_PORT));
    }

    //---------------------------------------------------------------------------------------------------

    public static void runService() {

        port(Config.API_PORT);
        threadPool(Config.API_THREAD_MAX, Config.API_THREAD_MIN, Config.API_TIMEOUT);

        options("*", (request, response) -> "{}");
        before(ApiLog::beforeApiStarted);
        after(ApiLog::afterApiFinished);
        exception(Exception.class, ApiLog::exception);
    }

    //---------------------------------------------------------------------------------------------------

    public static void loadModules() {
        new AdvertisingController();
        new AreaController();
        new BannerController();
        new BonusCardController();
        new BonusCardDetailController();
        new CartController();
        new CartDistributorController();
        new CartableController();
        new ColorTypeController();
        new ConfigEnumController();
        new ConfigFieldController();
        new CustomerController();
        new CustomerLoginController();
        new CustomerSessionController();
        new DictionaryController();
        new DistributorController();
        new DistributorDelivererController();
        new DistributorPackageController();
        new DistributorDiscontentTypeController();
        new DistributorEmployeeController();
        new DistributorRateDiscontentController();
        new DistributorProductController();
        new DistributorPromotionController();
        new DistributorProductPriceHistoryController();
        new JoorJensEmployeeController();
        new LogController();
        new MessageController();
        new OrderStatusTypeController();
        new PermissionController();
        new PositionController();
        new ProductBrandTypeController();
        new ProductController();
        new ProductPriceHistoryController();
        new ProductCategoryTypeController();
        new ProductDetailTypeController();
        new ProductReturnTypeController();
        new PromotionController();
        new RoleController();
        new StoreController();
        new VehicleBrandTypeController();
        new VehicleController();
        new DashboardController();
        new TransactionController();
    }

    public static void initDB() {
        try {
            final int currentTime = Utility.getCurrentTime();

            //---------------------------------------------------------------------------------
            final AreaRepository areaRepo = (AreaRepository) RepositoryManager.getByEntity(Area.class);
            final Optional<Area> areaOptional = areaRepo.getByKey(Area.getFakeId());
            if (!areaOptional.isPresent()) {
                TypeEnumeration fake = Area.getFake();
                String query = String.format("INSERT INTO `area`(`id`,`createdTime`" +
                        ",`updatedTime`,`adType`,`name`,`note`,`id_parent`) " +
                        " VALUES (%d,%d,%d,%d,'%s','%s',%s)"
                        , fake.getId(), currentTime, currentTime, fake.getId()
                        , fake.getFa(), fake.getEn(), "null");
                areaRepo.executeNative(query);
            }
            //---------------------------------------------------------------------------------
            final ProductDetailTypeRepository pdTypeRepo = (ProductDetailTypeRepository) RepositoryManager.getByEntity(ProductDetailType.class);
            final Optional<ProductDetailType> pdTypeOptional = pdTypeRepo.getByKey(ProductDetailType.getFakeId());
            if (!pdTypeOptional.isPresent()) {
                TypeEnumeration fake = ProductDetailType.getFake();
                String query = String.format("INSERT INTO `product_detail_type`(`id`,`createdTime`" +
                        ",`updatedTime`,`name`,`note`,`id_parent`) " +
                        " VALUES (%d,%d,%d,'%s','%s',%s)"
                        , fake.getId(), currentTime, currentTime, fake.getFa(), fake.getEn(), "null");
                pdTypeRepo.executeNative(query);
            }
            //---------------------------------------------------------------------------------
            final ProductCategoryTypeRepository pcTypeRepo = (ProductCategoryTypeRepository) RepositoryManager.getByEntity(ProductCategoryType.class);
            final Optional<ProductCategoryType> pcTypeOptional = pcTypeRepo.getByKey(ProductCategoryType.getFakeId());
            if (!pcTypeOptional.isPresent()) {
                TypeEnumeration fake = ProductCategoryType.getFake();
                String query = String.format("INSERT INTO `product_category_type`(`id`,`createdTime`" +
                        ",`updatedTime`,`name`,`note`,`id_parent`) " +
                        " VALUES (%d,%d,%d,'%s','%s',%s)"
                        , fake.getId(), currentTime, currentTime, fake.getFa(), fake.getEn(), "null");
                pcTypeRepo.executeNative(query);
            }
            //---------------------------------------------------------------------------------
            final ProductBrandTypeRepository pbtRepo = (ProductBrandTypeRepository) RepositoryManager.getByEntity(ProductBrandType.class);
            final Optional<ProductBrandType> pbtOptional = pbtRepo.getByKey(ProductBrandType.getFakeId());
            if (!pbtOptional.isPresent()) {
                TypeEnumeration fake = ProductBrandType.getFake();
                String query = String.format("INSERT INTO `product_brand_type`(`id`,`createdTime`" +
                        ",`updatedTime`,`pbType`,`name`,`note`,`id_parent`) " +
                        " VALUES (%d,%d,%d,%d,'%s','%s',%s)"
                        , fake.getId(), currentTime, currentTime, fake.getId()
                        , fake.getFa(), fake.getEn(), "null");
                pbtRepo.executeNative(query);
            }
            //---------------------------------------------------------------------------------
            final RoleRepository roleRepo = (RoleRepository) RepositoryManager.getByEntity(Role.class);
            Optional<Role> roleOptional;
            boolean isAdded = false;
            final StringBuilder roleQuery = new StringBuilder("INSERT INTO `role`(`id`,`createdTime`" +
                    ",`updatedTime`,`name`,`note`) VALUES ");
            for (TypeEnumeration t : TypeEnumeration.getList(TypeEnumeration.USER_ROLE.getId())) {
                roleOptional = roleRepo.getByKey((long) t.getId());
                if (!roleOptional.isPresent()) {
                    if (isAdded) {
                        roleQuery.append(',');
                    } else {
                        isAdded = true;
                    }
                    roleQuery.append(String.format("(%d,%d,%d,'%s','%s')",
                            t.getId(), currentTime, currentTime, t.getFa(), t.getEn()));
                }
            }
            if (isAdded) {
                roleRepo.executeNative(roleQuery.toString());
            }
            //---------------------------------------------------------------------------------
            final OrderStatusTypeRepository orderRepo = (OrderStatusTypeRepository) RepositoryManager.getByEntity(OrderStatusType.class);
            Optional<OrderStatusType> orderOptional;
            isAdded = false;
            final StringBuilder orderQuery = new StringBuilder("INSERT INTO `order_status_type`(`id`,`createdTime`" +
                    ",`updatedTime`,`name`,`note`) VALUES ");
            for (TypeEnumeration t : TypeEnumeration.getList(TypeEnumeration.ORDER_STATUS.getId())) {
                orderOptional = orderRepo.getByKey((long) t.getId());
                if (!orderOptional.isPresent()) {
                    if (isAdded) {
                        orderQuery.append(',');
                    } else {
                        isAdded = true;
                    }
                    orderQuery.append(String.format("(%d,%d,%d,'%s','%s')",
                            t.getId(), currentTime, currentTime, t.getFa(), t.getEn()));
                }
            }
            if (isAdded) {
                orderRepo.executeNative(orderQuery.toString());
            }
            //---------------------------------------------------------------------------------
            final ConfigFieldRepository configRepo = (ConfigFieldRepository) RepositoryManager.getByEntity(ConfigField.class);
            Optional<ConfigField> configOptional;
            isAdded = false;
            final StringBuilder configQuery = new StringBuilder("INSERT INTO `config_field`(`id`,`createdTime`" +
                    ",`updatedTime`,`name`,`note`,`value`) VALUES ");
            for (TypeEnumeration t : TypeEnumeration.getList(TypeEnumeration.CONFIG.getId())) {
                configOptional = configRepo.getByKey((long) t.getId());
                if (!configOptional.isPresent()) {
                    if (isAdded) {
                        configQuery.append(',');
                    } else {
                        isAdded = true;
                    }
                    configQuery.append(String.format("(%d,%d,%d,'%s','%s','%s')",
                            t.getId(), currentTime, currentTime, t.getFa(), t.getEn(), t.getDef()));
                }
            }
            if (isAdded) {
                configRepo.executeNative(configQuery.toString());
            }
            //---------------------------------------------------------------------------------
            final PermissionRepository permissionRepo = (PermissionRepository) RepositoryManager.getByEntity(Permission.class);
            Optional<Permission> permissionOptional;
            isAdded = false;
            boolean isAddedRP = false;
            final StringBuilder permissionQuery = new StringBuilder("INSERT INTO `permission`(`id`,`createdTime`" +
                    ",`updatedTime`,`key_`,`name`,`justLogin`,`noLogin`,`url`) VALUES ");
            final StringBuilder rolePermissionQuery = new StringBuilder("INSERT INTO `role__permission`(`id_permission`,`id_role`) VALUES ");
            for (UrlRolesType t : UrlRolesType.values()) {
                if (t.isNoLogin()) {
                    AAA.addPublicUrl(t.getUrl());
                    continue;
                }
                if (t.isJustLogin()) {
                    continue;
                }
                permissionOptional = permissionRepo.getByKey((long) t.getId());
                if (!permissionOptional.isPresent()) {
                    if (isAdded) {
                        permissionQuery.append(',');
                    } else {
                        isAdded = true;
                    }
                    permissionQuery.append(String.format("(%d,%d,%d,'%s','%s',%s,%s,",
                            t.getId(), currentTime, currentTime, t, t.getNameFa(), t.isJustLogin(), t.isNoLogin()));
                    if (!t.isMenu()) {
                        permissionQuery.append('\'');
                    }
                    permissionQuery.append(String.format("%s", t.getUrl()));
                    if (!t.isMenu()) {
                        permissionQuery.append('\'');
                    }
                    permissionQuery.append(')');

                    for (int roleId : t.getRoles()) {
                        if (isAddedRP) {
                            rolePermissionQuery.append(',');
                        } else {
                            isAddedRP = true;
                        }
                        rolePermissionQuery.append(String.format("(%d,%d)", t.getId(), roleId));
                    }
                }
            }
            if (isAdded) {
                permissionRepo.executeNative(permissionQuery.toString());
                roleRepo.executeNative(rolePermissionQuery.toString());
            }
            //---------------------------------------------------------------------------------
        } catch (Exception e) {
            logger.error(String.format("Exception@initDB. Message: %s", e.getMessage()));
        }
    }

    private static void runBackgroundTasks() {
        final Thread logThread = new Thread(new LogTask(), ThreadFactory.getThreadName(LogTask.class));
        logThread.setPriority(Thread.MAX_PRIORITY);
        logThread.start();

        final Thread positionThread = new Thread(new PositionTask(), ThreadFactory.getThreadName(PositionTask.class));
        positionThread.setPriority(Thread.MAX_PRIORITY);
        positionThread.start();

        final Thread stocksThread = new Thread(new StocksUpdater(), ThreadFactory.getThreadName(StocksUpdater.class));
        stocksThread.setPriority(Thread.MAX_PRIORITY);
        stocksThread.start();

        final Thread calculatorThread = new Thread(new Calculator(), ThreadFactory.getThreadName(Calculator.class));
        calculatorThread.setPriority(Thread.MAX_PRIORITY);
        calculatorThread.start();
    }

    //---------------------------------------------------------------------------------------------------

    private static void readArgs(String[] args) {
        if (args == null || args.length == 0) {
            return;
        }

        Options options = new Options();
        options.addOption(new Option("p", "port", true, "rest service port"));
        options.addOption(new Option("a", "pagination", true, "max pagination in api"));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);

            if (cmd.hasOption("p")) {
                Config.setApiPort(Integer.parseInt(cmd.getOptionValue("p")));
            }
            if (cmd.hasOption("a")) {
                Config.apiPaginationMax = Integer.parseInt(cmd.getOptionValue("a"));
            }
        } catch (ParseException e) {
            logger.info("ParseException@ readArgs. Message: " + e.getMessage());
            formatter.printHelp(JoorJens.class.getSimpleName(), options);
            System.exit(1);
        }
    }

    //---------------------------------------------------------------------------------------------------
}