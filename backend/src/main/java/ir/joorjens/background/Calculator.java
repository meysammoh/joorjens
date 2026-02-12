package ir.joorjens.background;

import ir.joorjens.cache.CacheGuava;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.*;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.CartPrice;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Calculator implements Runnable {

    //--------------------------------------------------------------------------------------------------
    private static final Logger LOGGER = LoggerFactory.getLogger(Calculator.class);
    //--------------------------------------------------------------------------------------------------
    private static final MessageRepository REPO_MESSAGE = (MessageRepository) RepositoryManager.getByEntity(Message.class);
    private static final CartRepository REPO_CART = (CartRepository) RepositoryManager.getByEntity(Cart.class);
    private static final DistributorRepository REPO_DIS = (DistributorRepository) RepositoryManager.getByEntity(Distributor.class);
    //---------------------------------------------------------------------------------------------------

    public void run() {

        int timeUntilNextDay;
        do {
            timeUntilNextDay = Utility.getTimeUntilNextDay() + Config.TIME_HOUR;
            try {
                Thread.sleep(timeUntilNextDay * 1000L);
            } catch (InterruptedException e) {
                LOGGER.error(String.format("InterruptedException. Message: %s", e.getMessage()));
            }

            try {
                analyseBandedDistributor();
            } catch (JoorJensException e) {
                LOGGER.error(String.format("JoorJensException@analyseBandedDistributor. Message: %s", e.getMessage()));
            }

            if (Utility.isFirstOfMonth(true)) {
                LOGGER.info("First of month ...");
                //todo transactions
            }

        } while (true);
    } // end run

    //---------------------------------------------------------------------------------------------------

    private static void analyseBandedDistributor() throws JoorJensException {
        final Map<Long, Long> distributorsHasOpenCart = getDistributorsHasOpenCart();
        final Set<Long> bandedDistributorIds = new HashSet<>();
        LOGGER.debug(String.format("analysing %d Distributor ... ", distributorsHasOpenCart.size()));
        final List<Message> messages = new ArrayList<>();
        Distributor distributor;
        Boolean blockOrWarn;
        for (Map.Entry<Long, Long> entry : distributorsHasOpenCart.entrySet()) {
            distributor = CacheGuava.getDistributor(entry.getKey());
            if (distributor == null || distributor.isBlock()) {
                continue;
            }
            blockOrWarn = null;
            if (distributor.getOpenCartsBlock() < entry.getValue()) {
                LOGGER.debug(String.format("Distributor will be block: id=%d, count=%d", entry.getKey(), entry.getValue()));
                blockOrWarn = true;
                bandedDistributorIds.add(entry.getKey());
            } else if (distributor.getOpenCartsWarn() < entry.getValue()) {
                LOGGER.debug(String.format("Distributor reach warn: id=%d, count=%d", entry.getKey(), entry.getValue()));
                blockOrWarn = false;
            } else {
                LOGGER.debug(String.format("Distributor nothing: id=%d, count=%d", entry.getKey(), entry.getValue()));
            }
            if (blockOrWarn != null) {
                messages.add(sendMessage(distributor, blockOrWarn));
            }
        }

        LOGGER.debug(String.format("blocking %d Distributor ...", bandedDistributorIds.size()));
        if (bandedDistributorIds.size() > 0) {
            try {
                REPO_DIS.update(bandedDistributorIds, true);
                LOGGER.debug(String.format("block %d Distributor.", bandedDistributorIds.size()));
            } catch (JoorJensException e) {
                LOGGER.error(String.format("JoorJensException@blocking. size: %d, Message: %s"
                        , bandedDistributorIds.size(), e.getMessage()));
            }
        }

        LOGGER.debug(String.format("sending %d messages for Distributor ...", messages.size()));
        if (messages.size() > 0) {
            try {
                REPO_MESSAGE.persistBatch(messages);
                LOGGER.debug(String.format("sent %d messages for Distributor.", messages.size()));
            } catch (JoorJensException e) {
                LOGGER.error(String.format("JoorJensException@sendMessage. MessageSize: %d, Message: %s"
                        , messages.size(), e.getMessage()));
            }
        }
        LOGGER.debug(String.format("analysed %d Distributor.", distributorsHasOpenCart.size()));
    }

    private static Map<Long, Long> getDistributorsHasOpenCart() throws JoorJensException {
        final Map<Long, Long> distributorIds = new HashMap<>();
        final int max = Config.apiPaginationMax;
        int total = 1;
        FetchResult<CartPrice> result;
        for (int offset = 0; offset * max < total; offset++) {
            result = REPO_CART.dashboardSales(0, 0, 0, 0, 0, false, false, true, null, 0, null
                    , 0, 0, 0, 0, 0, null, max, offset);
            if (total == 1) {
                total = result.getTotal();
            }
            for (CartPrice r : result.getResult()) {
                distributorIds.put(r.getDistributorId(), r.getTotal());
            }
        }
        return distributorIds;
    }

    private static Message sendMessage(final Distributor distributor, boolean blockOrWarn) {
        final Message message = new Message();
        final MessageReceiver messageReceiver = new MessageReceiver();
        message.setFromSystem(true);
        TypeEnumeration title, desc;
        if (blockOrWarn) {
            title = TypeEnumeration.CONFIG_DISTRIBUTOR_CART_BLOCK_TITLE;
            desc = TypeEnumeration.CONFIG_DISTRIBUTOR_CART_BLOCK_DESC;
        } else {
            title = TypeEnumeration.CONFIG_DISTRIBUTOR_CART_WARN_TITLE;
            desc = TypeEnumeration.CONFIG_DISTRIBUTOR_CART_WARN_DESC;
        }
        message.setTitle(CacheGuava.getConfigValueStr(title));
        message.setText(CacheGuava.getConfigValueStr(desc));
        messageReceiver.setReceiverDistributorId(distributor.getId());
        messageReceiver.setMessage(message);
        message.addMessageReceiver(messageReceiver);
        return message;
    }

    //---------------------------------------------------------------------------------------------------

}