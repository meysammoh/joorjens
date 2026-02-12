package ir.joorjens.dao.repositories;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.MessageRepository;
import ir.joorjens.dao.manager.RepositoryImpAbstract;
import ir.joorjens.model.entity.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageRepositoryImpl
        extends RepositoryImpAbstract<Long, Message>
        implements MessageRepository {

    @Override
    public int updateReceiverSeen(final Set<Long> messageIds, final Set<Long> messageReceiverIds) throws JoorJensException {
        if(messageIds.size() == 0 || messageReceiverIds.size() == 0) {
            return 0;
        }
        final String update = "UPDATE MessageReceiver t SET t.seenTime=:current"
                + " WHERE t.message.id in :messageIds AND t.id in :ids AND t.seenTime=:zero";
        final Map<String, Object> map = new HashMap<>();
        map.put("current", Utility.getCurrentTime());
        map.put("messageIds", messageIds);
        map.put("ids", messageReceiverIds);
        map.put("zero", 0);
        return update(update, map);
    }

    @Override
    public int deleteReceiver(final Set<Long> messageIds, final Set<Long> messageReceiverIds) throws JoorJensException {
        if(messageIds.size() == 0 || messageReceiverIds.size() == 0) {
            return 0;
        }
        final String update = "DELETE FROM MessageReceiver t"
                + " WHERE t.message.id in :messageIds AND t.id in :ids";
        final Map<String, Object> map = new HashMap<>();
        map.put("messageIds", messageIds);
        map.put("ids", messageReceiverIds);
        return update(update, map);
    }

    @Override
    public FetchResult<Message> search(long id, String title, String text, int timeFrom, int timeTo
            , long senderId, long senderDistributorId, long senderStoreId
            , long receiverId, long receiverDistributorId, long receiverStoreId
            , boolean send, Boolean receiverSeen, boolean like, int max, int offset) throws JoorJensException {
        final StringBuilder query = new StringBuilder("SELECT %s FROM Message t WHERE 1=1");
        final Map<String, Object> map = new HashMap<>();
        if (id > 0) {
            query.append(" and t.id=:id");
            map.put("id", id);
        }
        if (!Utility.isEmpty(title)) {
            query.append(" and t.title").append(like ? " like " : "=").append(":title");
            map.put("title", like ? '%' + title + '%' : title);

        }
        if (!Utility.isEmpty(text)) {
            query.append(" and t.text").append(like ? " like " : "=").append(":text");
            map.put("text", like ? '%' + text + '%' : text);
        }
        if (timeFrom > 0) {
            query.append(" and t.createdTime>=:timeFrom");
            map.put("timeFrom", timeFrom);
        }
        if (timeTo > 0 && timeTo >= timeFrom) {
            query.append(" and t.createdTime<=:timeTo");
            map.put("timeTo", timeTo);
        }


        if (send) {
            if(senderId > 0 || senderDistributorId > 0 || senderStoreId > 0) {
                query.append(" and (t.sender.id=:senderId" +
                        " or t.senderDistributor.id=:senderDistributorId" +
                        " or t.senderStore.id=:senderStoreId)");
                map.put("senderId", senderId);
                map.put("senderDistributorId", senderDistributorId);
                map.put("senderStoreId", senderStoreId);
            }
        } else { // receiver
            if (receiverId > 0 || receiverDistributorId > 0 || receiverStoreId > 0) {
                query.append(" and t.id in (select DISTINCT mr.message.id from MessageReceiver mr where mr.receiver.id=:receiverId" +
                        " or mr.receiverDistributor.id=:receiverDistributorId" +
                        " or mr.receiverStore.id=:receiverStoreId");
                if (receiverSeen != null) {
                    query.append(String.format(" and mr.seenTime%s0", (receiverSeen ? ">" : "=")));
                }
                query.append(')');
                map.put("receiverId", receiverId);
                map.put("receiverDistributorId", receiverDistributorId);
                map.put("receiverStoreId", receiverStoreId);
            }
        }
        return search(query.toString(), map, max, offset);
    }
}