package ir.joorjens.background;

import ir.joorjens.common.ThreadFactory;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.DistributorPackageRepository;
import ir.joorjens.dao.interfaces.DistributorProductRepository;
import ir.joorjens.dao.interfaces.MessageRepository;
import ir.joorjens.dao.interfaces.ProductRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class StocksUpdater implements Runnable {

    //--------------------------------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(StocksUpdater.class);
    private static final MessageRepository REPO_MESSAGE = (MessageRepository) RepositoryManager.getByEntity(Message.class);
    private static final ProductRepository REPO_PRODUCT = (ProductRepository) RepositoryManager.getByEntity(Product.class);
    private static final DistributorProductRepository REPO_DIS_PRODUCT = (DistributorProductRepository) RepositoryManager.getByEntity(DistributorProduct.class);
    private static final DistributorPackageRepository REPO_DIS_PACKAGE = (DistributorPackageRepository) RepositoryManager.getByEntity(DistributorPackage.class);

    //---------------------------------------------------------------------------------------------------

    private static final Object LOCK_MAP = new Object();
    private static final Map<Long, Long> MAP_PRODUCT = new HashMap<>() //
            , MAP_DIS_PRODUCT = new HashMap<>() //
            , MAP_DIS_PACKAGE = new HashMap<>();

    /**tableName, QUERY_COLUMN, ids*/
    private static final String QUERY = "UPDATE %s SET %s WHERE id IN (%s);";
    /**colName, colName (+|-), QUERY_WHEN*/
    private static final String QUERY_COLUMN = "%s = %s CASE id %s ELSE 0 END";
    /**id, value*/
    private static final String QUERY_WHEN = "WHEN %d THEN %d";

    /*
    UPDATE table SET Col1 = CASE id
        WHEN 1 THEN 1
        WHEN 2 THEN 2
        WHEN 4 THEN 10
        ELSE Col1 END
    , Col2 = CASE id
        WHEN 3 THEN 3
        WHEN 4 THEN 12
        ELSE Col2 END
    WHERE id IN (1, 2, 3, 4);
    */
    // ---------------------------------------------- QUEUE ---------------------------------------------
    private static final BlockingQueue<Cart> QUEUE = new LinkedBlockingQueue<>();

    public static boolean addToQueue(final Cart cart) {
        boolean OK = false;
        if (cart != null && cart.getDistributorSize() > 0) {
            try {
                QUEUE.put(cart);
                OK = true;
            } catch (Exception e) {
                LOGGER.error(String.format("Exception@addToQueue(%s). Message: %s", cart.getSerial(), e.getMessage()));
            }
        } else {
            LOGGER.error("@addToQueue. cart is not valid!");
        }
        return OK;
    }

    // ----------------------------------------- Worker Management -----------------------------------------

    private static int threadsActive = 0;
    private static final int THREADS_COUNT = 5;
    private static final Object LOCK_THREADS = new Object();
    private static final ExecutorService THREAD_POOL = Executors.
            newFixedThreadPool(THREADS_COUNT, new ThreadFactory(StocksUpdater.class.getSimpleName()));

    public void run() {

        final DbUpdater dbUpdater = new DbUpdater();
        final Thread dbUpdaterThread = new Thread(dbUpdater, ThreadFactory.getThreadName(DbUpdater.class));
        dbUpdaterThread.setPriority(Thread.MAX_PRIORITY);
        dbUpdaterThread.start();

        Cart cart;
        do {
            try {
                cart = QUEUE.take();
            } catch (Exception intEx) {
                LOGGER.warn("InterruptedException. Message: " + intEx.getMessage());
                continue;
            }

            synchronized (LOCK_THREADS) {
                if (threadsActive == THREADS_COUNT) {
                    try {
                        LOGGER.warn(String.format("@run: threadsActive(%d) is equal to max!.", threadsActive));
                        LOCK_THREADS.wait();
                    } catch (InterruptedException e) {
                        LOGGER.error(String.format("InterruptedException@run. Message: %s", e.getMessage()));
                    }
                }
                ++threadsActive;
            }
            THREAD_POOL.execute(new MapUpdater(cart));
        } while (true);
    } // end run

    // --------------------------------------------- Worker ---------------------------------------------

    private class MapUpdater implements Runnable {
        private final Cart cart;

        private MapUpdater(Cart cart) {
            this.cart = cart;
        }

        @Override
        public void run() {
            if (this.cart != null) {
                long time = System.currentTimeMillis();
                //--------------------------------------------------------------------------------------------------
                final Map<Long, Long> distributorPackageMap = new HashMap<>();
                final Map<Long, Long> distributorProductMap = new HashMap<>();
                final Map<Long, Long> productMap = new HashMap<>();

                long count;
                DistributorProduct dProduct;
                DistributorPackage dPack;
                final Map<Long, DistributorProduct> dProductWarn = new HashMap<>();
                final Map<Long, DistributorPackage> dPackWarn = new HashMap<>();
                Long value;
                for (CartDistributor cd : this.cart.getDistributorSet()) {
                    for (CartDistributorPackduct pack : cd.getPackageSet()) {
                        count = pack.getCartPrice().getCount();

                        if (pack.isOrdinal()) {
                            //disProduct
                            dProduct = pack.getDistributorProduct();
                            value = distributorProductMap.get(dProduct.getId());
                            if (value == null) {
                                value = 0L;
                            }
                            value += count;
                            distributorProductMap.put(dProduct.getId(), value);
                            if (value >= dProduct.getStockWarn() && !dProductWarn.containsKey(dProduct.getId())) {
                                dProductWarn.put(dProduct.getId(), dProduct);
                            }

                            //Product
                            value = productMap.get(dProduct.getProductId());
                            if (value == null) {
                                value = 0L;
                            }
                            value += count;
                            productMap.put(dProduct.getProductId(), value);

                        } else if (pack.isPackage()) {
                            //disPackage
                            dPack = pack.getDistributorPackage();
                            value = distributorPackageMap.get(dPack.getId());
                            if (value == null) {
                                value = 0L;
                            }
                            value += count;
                            distributorPackageMap.put(dPack.getId(), value);
                            if (value >= dPack.getStockWarn() && !dPackWarn.containsKey(dPack.getId())) {
                                dPackWarn.put(dPack.getId(), dPack);
                            }

                            for (DistributorPackageProduct dpp : pack.getDistributorPackage().getPackageProducts()) {
                                //disProduct
                                dProduct = dpp.getDistributorProduct();
                                value = distributorProductMap.get(dProduct.getId());
                                if (value == null) {
                                    value = 0L;
                                }
                                value += (count * dpp.getCount());
                                distributorProductMap.put(dProduct.getId(), value);
                                if (value >= dProduct.getStockWarn() && !dProductWarn.containsKey(dProduct.getId())) {
                                    dProductWarn.put(dProduct.getId(), dProduct);
                                }

                                //Product
                                value = productMap.get(dProduct.getProductId());
                                if (value == null) {
                                    value = 0L;
                                }
                                value += (count * dpp.getCount());
                                productMap.put(dProduct.getProductId(), value);
                            }

                        } //end elseIf
                    } // end inner for
                } // end first for

                synchronized (LOCK_MAP) {
                    MAP_PRODUCT.putAll(productMap);
                    MAP_DIS_PRODUCT.putAll(distributorProductMap);
                    MAP_DIS_PACKAGE.putAll(distributorPackageMap);
                }

                if (dPackWarn.size() > 0 || dProductWarn.size() > 0) {
                    LOGGER.info(String.format("packageWarn(%d) || productWarn(%d)!"
                            , dPackWarn.size(), dProductWarn.size()));
                    updateDb();

                    final List<Message> messages = new ArrayList<>();
                    messages.addAll(dProductWarn.values().stream().map(dp -> sendMessage(dp, null)).collect(Collectors.toList()));
                    messages.addAll(dPackWarn.values().stream().map(dp -> sendMessage(null, dp)).collect(Collectors.toList()));
                    try {
                        REPO_MESSAGE.persistBatch(messages);
                    } catch (JoorJensException e) {
                        LOGGER.error(String.format("JoorJensException@sendMessage. MessageSize: %d, Message: %s"
                                , messages.size(), e.getMessage()));
                    }
                }
                //--------------------------------------------------------------------------------------------------
                time = System.currentTimeMillis() - time;
                LOGGER.info(String.format("filling maps for key(%s) was successfully Done in %d (ms)!", this.cart.getSerial(), time));
            }

            synchronized (LOCK_THREADS) {
                if (--threadsActive < THREADS_COUNT) {
                    LOCK_THREADS.notify();
                }
            }
        }
    } //end private class

    private class DbUpdater implements Runnable {

        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(1000l * Utility.getTimeUntilNextHour());
                } catch (InterruptedException e) {
                    LOGGER.error(String.format("InterruptedException@DbUpdater. Message: %s", e.getMessage()));
                }

                updateDb();

            } // end while
        } //end run
    } //end dbUpdater class

    private static void updateDb() {

        final Map<Long, Long> distributorPackageMap = new HashMap<>();
        final Map<Long, Long> distributorProductMap = new HashMap<>();
        final Map<Long, Long> productMap = new HashMap<>();

        synchronized (LOCK_MAP) {
            productMap.putAll(MAP_PRODUCT);
            MAP_PRODUCT.clear();
            distributorProductMap.putAll(MAP_DIS_PRODUCT);
            MAP_DIS_PRODUCT.clear();
            distributorPackageMap.putAll(MAP_DIS_PACKAGE);
            MAP_DIS_PACKAGE.clear();
        }
        LOGGER.info(String.format("DbUpdater started with product(%d), disProduct(%d), disPackage(%d)."
                , productMap.size(), distributorProductMap.size(), distributorPackageMap.size()));

        if (productMap.size() > 0) {
            final StringBuilder ids = new StringBuilder(), whens = new StringBuilder();
            int index = 0;
            for (Map.Entry<Long, Long> entry : productMap.entrySet()) {
                if (++index > 1) {
                    ids.append(',');
                    whens.append(' ');
                }
                ids.append(entry.getKey());
                whens.append(String.format(QUERY_WHEN, entry.getKey(), entry.getValue()));
            }
            final String queryColumn = String.format(QUERY_COLUMN, "saleCount", "saleCount+", whens.toString());
            final String query = String.format(QUERY, "product", queryColumn, ids.toString());
            try {
                final int rowAffected = REPO_PRODUCT.executeNative(query);
                LOGGER.info(String.format("DbUpdater: product(%d), rowAffected(%d).", productMap.size(), rowAffected));
            } catch (JoorJensException e) {
                LOGGER.error(String.format("JoorJensException@DbUpdater. productMap(%d), Message: %s", productMap.size(), e.getMessage()));
            }
        }

        if (distributorProductMap.size() > 0) {
            final StringBuilder ids = new StringBuilder(), whens = new StringBuilder();
            int index = 0;
            for (Map.Entry<Long, Long> entry : distributorProductMap.entrySet()) {
                if (++index > 1) {
                    ids.append(',');
                    whens.append(' ');
                }
                ids.append(entry.getKey());
                whens.append(String.format(QUERY_WHEN, entry.getKey(), entry.getValue()));
            }
            final String queryColumn = String.format(QUERY_COLUMN, "saleCount", "saleCount+", whens.toString())
                    + ',' + String.format(QUERY_COLUMN, "stock", "stock-", whens.toString());
            final String query = String.format(QUERY, "distributor_product", queryColumn, ids.toString());
            try {
                final int rowAffected = REPO_DIS_PRODUCT.executeNative(query);
                LOGGER.info(String.format("DbUpdater: distributorProductMap(%d), rowAffected(%d).", distributorProductMap.size(), rowAffected));
            } catch (JoorJensException e) {
                LOGGER.error(String.format("JoorJensException@DbUpdater. distributorProductMap(%d), Message: %s", distributorProductMap.size(), e.getMessage()));
            }
        }

        if (distributorPackageMap.size() > 0) {
            final StringBuilder ids = new StringBuilder(), whens = new StringBuilder();
            int index = 0;
            for (Map.Entry<Long, Long> entry : distributorPackageMap.entrySet()) {
                if (++index > 1) {
                    ids.append(',');
                    whens.append(' ');
                }
                ids.append(entry.getKey());
                whens.append(String.format(QUERY_WHEN, entry.getKey(), entry.getValue()));
            }
            final String queryColumn = String.format(QUERY_COLUMN, "saleCount", "saleCount+", whens.toString())
                    + ',' + String.format(QUERY_COLUMN, "stock", "stock-", whens.toString());
            final String query = String.format(QUERY, "distributor_package", queryColumn, ids.toString());
            try {
                final int rowAffected = REPO_DIS_PACKAGE.executeNative(query);
                LOGGER.info(String.format("DbUpdater: distributorPackageMap(%d), rowAffected(%d).", distributorPackageMap.size(), rowAffected));
            } catch (JoorJensException e) {
                LOGGER.error(String.format("JoorJensException@DbUpdater. distributorPackageMap(%d), Message: %s", distributorPackageMap.size(), e.getMessage()));
            }
        }

    } //end function

    private static Message sendMessage(final DistributorProduct dProductWarn, final DistributorPackage dPackWarn) {
        final String name, type;
        final long receiverId;
        if(dProductWarn != null) {
            type = DistributorProduct.getEN();
            name = dProductWarn.getProductName();
            receiverId = dProductWarn.getDistributorId();
        } else if(dPackWarn != null) {
            type = DistributorPackage.getEN();
            name = dPackWarn.getName();
            receiverId = dPackWarn.getDistributorId();
        } else {
          return null;
        }
        final Message message = new Message();
        final MessageReceiver messageReceiver = new MessageReceiver();
        message.setFromSystem(true);
        message.setTitle("هشدار موجودی");
        message.setText(String.format("%s(%s) به هشدار موجودی رسیده است.", type, name));
        messageReceiver.setReceiverDistributorId(receiverId);
        messageReceiver.setMessage(message);
        message.addMessageReceiver(messageReceiver);
        return message;
    }

    public static void main(String[] args) {
        MAP_PRODUCT.put(1L, 3L);
        MAP_PRODUCT.put(2L, 4L);
        MAP_DIS_PRODUCT.put(5L, 6L);
        MAP_DIS_PRODUCT.put(6L, 7L);
        MAP_DIS_PACKAGE.put(8L, 11L);
        MAP_DIS_PACKAGE.put(9L, 12L);
        updateDb();
    }
}