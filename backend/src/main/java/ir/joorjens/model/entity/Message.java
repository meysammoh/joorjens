package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "message")
public class Message extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "پیام";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(nullable = false)
    private String title;
    private String text;

    //------------------------------------------------
    @Column(columnDefinition = "boolean default false")
    private boolean fromSystem = false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sender", foreignKey = @ForeignKey(name = "FK_MESSAGE__CUSTOMER__sender"))
    @JsonIgnore
    private Customer sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_senderDistributor", foreignKey = @ForeignKey(name = "FK_MESSAGE__DISTRIBUTOR__sender"))
    @JsonIgnore
    private Distributor senderDistributor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_senderStore", foreignKey = @ForeignKey(name = "FK_MESSAGE__STORE__sender"))
    @JsonIgnore
    private Store senderStore;
    //------------------------------------------------
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MessageReceiver> messageReceivers = new HashSet<>();
    //------------------------------------------------

    public Message() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(boolean fromSystem) {
        this.fromSystem = fromSystem;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }

    public Distributor getSenderDistributor() {
        return senderDistributor;
    }

    public void setSenderDistributor(Distributor senderDistributor) {
        this.senderDistributor = senderDistributor;
    }

    public Store getSenderStore() {
        return senderStore;
    }

    public void setSenderStore(Store senderStore) {
        this.senderStore = senderStore;
    }

    public Set<MessageReceiver> getMessageReceivers() {
        return messageReceivers;
    }

    public void setMessageReceivers(Set<MessageReceiver> messageReceivers) {
        this.messageReceivers = messageReceivers;
    }

    public void addMessageReceiver(MessageReceiver messageReceiver) {
        this.messageReceivers.add(messageReceiver);
    }

    @Override
    public boolean isValid() throws JoorJensException {
        final boolean OK = !Utility.isEmpty(title) && messageReceivers.size() > 0;
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public int getCreatedTime() {
        return super.getCreatedTime();
    }

    @JsonGetter
    public int getUpdatedTime() {
        return super.getUpdatedTime();
    }

    @JsonGetter
    public long getSenderId() {
        if (senderDistributor != null) {
            return senderDistributor.getId();
        }
        if (senderStore != null) {
            return senderStore.getId();
        }
        if (!fromSystem && sender != null) {
            return sender.getId();
        }
        return 0;
    }

    @JsonGetter
    public String getSenderName() {
        if (senderDistributor != null) {
            return senderDistributor.getName();
        }
        if (senderStore != null) {
            return senderStore.getName();
        }
        if (!fromSystem && sender != null) {
            return sender.getName();
        }
        return TypeEnumeration.JOORJENS.getFa();
    }

    @JsonGetter
    public String getSenderType() {
        if (senderDistributor != null) {
            return Distributor.getEN();
        }
        if (senderStore != null) {
            return Store.getEN();
        }
        if (!fromSystem && sender != null) {
            return Customer.getEN();
        }
        return TypeEnumeration.JOORJENS.getFa();
    }

    @JsonGetter
    public String getMessageReceiversOrdinal() {
        final StringBuilder receiver = new StringBuilder();
        int counter = 0;
        for (MessageReceiver messageReceiver : messageReceivers) {
            if (TypeEnumeration.MTT_ORDINAL.getId() == messageReceiver.getMessageType()) {
                if (++counter > 1) {
                    receiver.append("، ");
                }
                receiver.append(messageReceiver.getReceiverName());
            }
        }
        return receiver.toString();
    }

    @JsonGetter
    public String getMessageReceiversCC() {
        final StringBuilder receiver = new StringBuilder();
        int counter = 0;
        for (MessageReceiver messageReceiver : messageReceivers) {
            if (TypeEnumeration.MTT_CC.getId() == messageReceiver.getMessageType()) {
                if (++counter > 1) {
                    receiver.append("، ");
                }
                receiver.append(messageReceiver.getReceiverName());
            }
        }
        return receiver.toString();
    }

    @JsonGetter
    public String getMessageReceiversBCC() {
        final StringBuilder receiver = new StringBuilder();
        int counter = 0;
        for (MessageReceiver messageReceiver : messageReceivers) {
            if (TypeEnumeration.MTT_BCC.getId() == messageReceiver.getMessageType()) {
                if (++counter > 1) {
                    receiver.append("، ");
                }
                receiver.append(messageReceiver.getReceiverName());
            }
        }
        return receiver.toString();
    }

    //-----------------------------------------------------------------------------------------------------------

}