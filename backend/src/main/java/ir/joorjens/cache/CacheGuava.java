package ir.joorjens.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import ir.joorjens.aaa.AAA;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.controller.ConfigFieldController;
import ir.joorjens.controller.DistributorController;
import ir.joorjens.model.entity.Cart;
import ir.joorjens.model.entity.ConfigField;
import ir.joorjens.model.entity.Distributor;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;

import java.util.concurrent.TimeUnit;

public abstract class CacheGuava {

    //-----------------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheGuava.class);
    //-----------------------------------------------------------------------------------------------------------

    //-----------------------------------------------------------------------------------------------------------
    private static final LoadingCache<TypeEnumeration, String> CONFIG = CacheBuilder.newBuilder()
            .maximumSize(1_000)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(new CacheLoader<TypeEnumeration, String>() {
                @Override
                public String load(final TypeEnumeration type) throws JoorJensException {
                    ConfigField configField = ConfigFieldController.getConfigField(type.getId());
                    if (configField != null) {
                        return configField.getValue();
                    }
                    return null;
                }
            });

    public static void invalidateConfig(final TypeEnumeration t) {
        CONFIG.invalidate(t);
    }

    public static String getConfigValueStr(final TypeEnumeration t) {
        String value = null;
        if (t != null) {
            try {
                value = CONFIG.get(t);
            } catch (Exception e) {
                LOGGER.debug(String.format("Exception@getConfigValueStr(%s). Message: %s", t, e.getMessage()));
            }
        }
        return value;
    }

    public static double getConfigValue(final TypeEnumeration t) {
        Double value = null;
        final String valueStr = getConfigValueStr(t);
        if (valueStr != null) {
            try {
                value = Double.valueOf(getConfigValueStr(t));
            } catch (Exception e) {
                LOGGER.warn(String.format("Exception@getConfigValue(%s). valueStr: %s, Message: %s", t, valueStr, e.getMessage()));
            }
        }
        if (value == null) {
            value = 0d;
        }
        return value;
    }

    //-----------------------------------------------------------------------------------------------------------

    private static final LoadingCache<Long, Distributor> DISTRIBUTORS = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(new CacheLoader<Long, Distributor>() {
                @Override
                public Distributor load(Long id) throws JoorJensException {
                    return DistributorController.getDistributor(id);
                }
            });

    public static Distributor getDistributor(long distributorId) {
        try {
            return DISTRIBUTORS.get(distributorId);
        } catch (Exception e) {
            LOGGER.error(String.format("Distributor(%d) not found! Message: %s", distributorId, e.getMessage()));
            return null;
        }
    }

    //---------------------------------------------------------------------------------------------------

    private static final LoadingCache<Long, Cart> CART = CacheBuilder.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(60, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, Cart>() {
                @Override
                public Cart load(final Long customerId) throws JoorJensException {
                    return null;
                }
            });

    public static Cart getCart(final Request req) throws JoorJensException {
        final long customerId = AAA.getCustomerId(req);
        return CART.getIfPresent(customerId);
    }

    public static void putCart(final Request req, final Cart cart) throws JoorJensException {
        final long customerId = AAA.getCustomerId(req);
        CART.put(customerId, cart);
    }


    public static void invalidateCart(final Request req) {
        try {
            final long customerId = AAA.getCustomerId(req);
            CART.invalidate(customerId);
        } catch (Exception e) {
            LOGGER.debug(String.format("Exception@invalidateCart. Message: %s", e.getMessage()));
        }
    }
    //-----------------------------------------------------------------------------------------------------------
}