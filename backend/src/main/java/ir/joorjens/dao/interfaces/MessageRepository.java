package ir.joorjens.dao.interfaces;

import ir.joorjens.common.response.FetchResult;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.manager.Repository;
import ir.joorjens.model.entity.Message;

import java.util.Set;

public interface MessageRepository extends Repository<Long, Message> {

    int updateReceiverSeen(Set<Long> messageIds, Set<Long> messageReceiverIds) throws JoorJensException;

    int deleteReceiver(Set<Long> messageIds, Set<Long> messageReceiverIds) throws JoorJensException;

    FetchResult<Message> search(long id, String title, String text, int timeFrom, int timeTo
            , long senderId, long senderDistributorId, long senderStoreId
            , long receiverId, long receiverDistributorId, long receiverStoreId
            , boolean send, Boolean receiverSeen, boolean like, int max, int offset) throws JoorJensException;

}