package ir.joorjens.controller;

import ir.joorjens.aaa.AAA;
import ir.joorjens.aaa.UrlRolesType;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.common.response.ResponseCode;
import ir.joorjens.common.response.ResponseMessage;
import ir.joorjens.dao.interfaces.MessageRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.*;
import ir.joorjens.model.util.FormFactory;
import ir.joorjens.model.util.TypeEnumeration;
import spark.Request;

import java.util.*;

import static ir.joorjens.common.Utility.json;
import static spark.Spark.*;

public class MessageController {

    private static final MessageRepository REPO_TYPE = (MessageRepository) RepositoryManager.getByEntity(Message.class);
    private static final FormFactory<Message> FORM = new FormFactory<>(Message.class);
    private static final FormFactory<Long[][]> FORM_RECEIVERS = new FormFactory<>(Long[][].class);

    public MessageController() {

        post(Config.API_PREFIX + UrlRolesType.MESSAGE_INSERT.getUrl(), (req, res) -> upsert(req, false), json());

        post(Config.API_PREFIX + UrlRolesType.MESSAGE_UPDATE.getUrl(), (req, res) -> upsert(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.MESSAGE_DELETE.getUrl() + ":id/", (req, res) -> {
            final long id = Long.parseLong(req.params(":id"));
            return AbstractController.delete(req, Message.class, id);
        }, json());

        get(Config.API_PREFIX + UrlRolesType.MESSAGE_SEARCH.getUrl(), (req, res) -> search(req, false), json());

        get(Config.API_PREFIX + UrlRolesType.MESSAGE_VIEW.getUrl(), (req, res) -> search(req, true), json());

        post(Config.API_PREFIX + UrlRolesType.MESSAGE_SEEN_RECEIVER.getUrl(), (req, res) -> changeReceiver(req, true), json());

        delete(Config.API_PREFIX + UrlRolesType.MESSAGE_DELETE_RECEIVER.getUrl(), (req, res) -> changeReceiver(req, false), json());

    }

    private static Message upsert(final Request req, final boolean update) throws JoorJensException {
        final Message message = FORM.get(req.body());
        final Map<Long, MessageReceiver> messageReceiverMap = new HashMap<>();
        if (update && message.getId() > 0) { //update
            final Message messagePre = getMessage(message.getId());
            message.setEdit(messagePre);
            for (MessageReceiver receiver : messagePre.getMessageReceivers()) {
                messageReceiverMap.put(receiver.getId(), receiver);
            }
        }
        final Customer customer = AAA.getCustomer(req);
        message.setSender(customer);
        final TypeEnumeration role = customer.getRoleType();
        if(TypeEnumeration.isCentralEmployee(role)) {
            message.setFromSystem(true);
        } else if(TypeEnumeration.isDistributorEmployee(role)) {
            final DistributorEmployee de = DistributorEmployeeController.getDistributorEmployee(customer.getId());
            message.setSenderDistributor(de.getDistributor());
        } else if(TypeEnumeration.isStoreEmployee(role)) {
            final Store sm = StoreController.getStoreByManager(customer.getId());
            message.setSenderStore(sm);
        }

        for (MessageReceiver receiver : message.getMessageReceivers()) {
            if (update && messageReceiverMap.containsKey(receiver.getId())) {
                receiver.setEdit(messageReceiverMap.get(receiver.getId()));
            } else if(!update) {
                receiver.setCreatedTime();
            }
            receiver.setMessage(message);
            receiver.isValid();
        }
        AbstractController.upsert(req, ColorType.class, message, update);
        return message;
    }

    private static ResponseMessage changeReceiver(final Request req, boolean updateOrDelete) throws JoorJensException {
        final long customerId = getCustomerId(req);
        final Set<Long> messageIds = new HashSet<>();
        final Set<Long> receiverIds = new HashSet<>();
        final Long[][] messageReceiverIds = FORM_RECEIVERS.get(req.body());
        for (Long[] messageReceiverId : messageReceiverIds) {
            messageIds.add(messageReceiverId[0]);
            receiverIds.add(customerId == 0 ? messageReceiverId[1] : customerId);
        }
        final int changeCount;
        final TypeEnumeration action;
        if (updateOrDelete) {
            changeCount = REPO_TYPE.updateReceiverSeen(messageIds, receiverIds);
            action = TypeEnumeration.ACTION_UPDATE;
        } else {
            changeCount = REPO_TYPE.deleteReceiver(messageIds, receiverIds);
            action = TypeEnumeration.ACTION_DELETE;
        }
        return new ResponseMessage(ResponseCode.getMessage(changeCount, Message.getEN(), action));
    }

    private static Object search(final Request req, final boolean view) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final TypeEnumeration role = customer.getRoleType();
        final Map<String, String> queryMap = AbstractController.getQueryMap(req, false);
        final long id = (queryMap.containsKey("id")) ? Long.parseLong(queryMap.get("id")) : 0;
        if (view) {
            return getMessage(id);
        }
        final boolean send = !queryMap.containsKey("send") || Boolean.parseBoolean(queryMap.get("send"));
        long senderId = (queryMap.containsKey("senderid")) ? Long.parseLong(queryMap.get("senderid")) : 0;
        long senderDistributorId = (queryMap.containsKey("senderdistributorid")) ? Long.parseLong(queryMap.get("senderdistributorid")) : 0;
        long senderStoreId = (queryMap.containsKey("senderstoreid")) ? Long.parseLong(queryMap.get("senderstoreid")) : 0;
        long receiverId = (queryMap.containsKey("receiverid")) ? Long.parseLong(queryMap.get("receiverid")) : 0;
        long receiverDistributorId = (queryMap.containsKey("receiverdistributorid")) ? Long.parseLong(queryMap.get("receiverdistributorid")) : 0;
        long receiverStoreId = (queryMap.containsKey("receiverstoreid")) ? Long.parseLong(queryMap.get("receiverstoreid")) : 0;
        if (TypeEnumeration.isDistributorEmployee(role)) {
            senderId = receiverId = customer.getId();
            final DistributorEmployee de = DistributorEmployeeController.getDistributorEmployee(customer.getId());
            senderDistributorId = receiverDistributorId = de.getDistributorId();
            senderStoreId = receiverStoreId = 0;
        } else if (TypeEnumeration.isStoreEmployee(role)) {
            senderId = receiverId = customer.getId();
            senderDistributorId = receiverDistributorId = 0;
            final Store sm = StoreController.getStoreByManager(customer.getId());
            senderStoreId = receiverStoreId = sm.getId();
        }

        final String title = queryMap.get("title");
        final String text = queryMap.get("text");
        final int timeFrom = (queryMap.containsKey("timefrom")) ? Integer.parseInt(queryMap.get("timefrom")) : 0;
        final int timeTo = (queryMap.containsKey("timeto")) ? Integer.parseInt(queryMap.get("timeto")) : 0;
        final Boolean receiverSeen = queryMap.containsKey("receiverseen") ? Boolean.parseBoolean(queryMap.get("receiverseen")) : null;
        final boolean like = (queryMap.containsKey("like")) && Boolean.parseBoolean(queryMap.get("like"));
        final int max = (queryMap.containsKey("max")) ? Integer.parseInt(queryMap.get("max")) : Config.apiPaginationMax;
        final int offset = (queryMap.containsKey("offset")) ? Integer.parseInt(queryMap.get("offset")) : 0;
        return REPO_TYPE.search(id, title, text, timeFrom, timeTo, senderId, senderDistributorId, senderStoreId
                , receiverId, receiverDistributorId, receiverStoreId, send, receiverSeen, like, max, offset);
    }

    //-------------------------------------------------------------------------------------------------
    static Message getMessage(final long id) throws JoorJensException {
        final Optional<Message> messageOptional = REPO_TYPE.getByKey(id);
        if (!messageOptional.isPresent()) {
            throw new JoorJensException(ExceptionCode.NOT_FOUND_PARAM, Message.getEN());
        }
        return messageOptional.get();
    }

    /**
     * @param req request
     * @return if user is admin 0 else customer.id
     * @throws JoorJensException
     */
    private static long getCustomerId(final Request req) throws JoorJensException {
        final Customer customer = AAA.getCustomer(req);
        final long id;
        switch (customer.getRoleType()) {
            case UR_CENTRAL_ADMIN:
            case UR_DISTRIBUTION_ADMIN:
                id = 0;
                break;
            default:
                id = customer.getId();
                break;
        }
        return id;
    }
    //-------------------------------------------------------------------------------------------------
}