package ir.joorjens.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import ir.joorjens.common.response.ExceptionCode;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.model.interfaces.AbstractModel;
import ir.joorjens.model.util.TypeEnumeration;

import javax.persistence.*;

@Entity
@Table(name = "message_receiver")
public class MessageReceiver extends AbstractModel {
    private static final long serialVersionUID = 1395L;

    //------------------------------------------------
    public static String getEN() {
        return "گیرنده پیام";
    }

    public String getEntityName() {
        return getEN();
    }

    //------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int seenTime = 0;
    private int messageType = TypeEnumeration.MTT_ORDINAL.getId();

    //------------------------------------------------
    @Column(columnDefinition = "boolean default false")
    private boolean toSystem = false;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_receiver", foreignKey = @ForeignKey(name = "FK_MESSAGE_RECEIVER__CUSTOMER__receiver"))
    @JsonIgnore
    private Customer receiver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_receiverDistributor", foreignKey = @ForeignKey(name = "FK_MESSAGE_RECEIVER__DISTRIBUTOR__receiver"))
    @JsonIgnore
    private Distributor receiverDistributor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_receiverStore", foreignKey = @ForeignKey(name = "FK_MESSAGE_RECEIVER__STORE__receiver"))
    @JsonIgnore
    private Store receiverStore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_message", foreignKey = @ForeignKey(name = "FK_MESSAGE_RECEIVER__MESSAGE__message"))
    @JsonIgnore
    private Message message;
    //------------------------------------------------

    public MessageReceiver() {
    }

    public void setEdit(final MessageReceiver receiver) {
        super.setEdit(receiver);
        this.setSeenTime(receiver.seenTime);
    }

    //-----------------------------------------------------------------------------------------------------------

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSeenTime() {
        return seenTime;
    }

    public void setSeenTime(int seenTime) {
        this.seenTime = seenTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        final TypeEnumeration mt = TypeEnumeration.get(messageType);
        if (mt != null && TypeEnumeration.MESSAGE_TO_TYPE == mt.getParent()) {
            this.messageType = messageType;
        }
    }

    public boolean isToSystem() {
        return toSystem;
    }

    public void setToSystem(boolean toSystem) {
        this.toSystem = toSystem;
    }

    public Customer getReceiver() {
        return receiver;
    }

    public void setReceiver(Customer receiver) {
        this.receiver = receiver;
    }

    public Distributor getReceiverDistributor() {
        return receiverDistributor;
    }

    public void setReceiverDistributor(Distributor receiverDistributor) {
        this.receiverDistributor = receiverDistributor;
    }

    public Store getReceiverStore() {
        return receiverStore;
    }

    public void setReceiverStore(Store receiverStore) {
        this.receiverStore = receiverStore;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public boolean isValid() throws JoorJensException {
        final boolean OK = message != null && messageType > 0;
        if(receiver == null && receiverDistributor == null && receiverStore == null) {
            toSystem = true;
        }
        if (!OK) {
            throw new JoorJensException(ExceptionCode.INVALID_OBJECT_PARAM, getEN());
        }
        return true;
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonGetter
    public String getMessageTypeName() {
        TypeEnumeration mt = TypeEnumeration.get(this.messageType);
        return (mt != null) ? mt.getEn() : null;
    }

    @JsonGetter
    public boolean isSeen() {
        return seenTime > 0;
    }

    @JsonGetter
    public long getMessageId() {
        return (this.message != null) ? this.message.getId() : 0;
    }

    @JsonGetter
    public long getReceiverId() {
        if (receiverDistributor != null) {
            return receiverDistributor.getId();
        }
        if (receiverStore != null) {
            return receiverStore.getId();
        }
        if (!toSystem && receiver != null) {
            return receiver.getId();
        }
        return 0;
    }

    @JsonGetter
    public String getReceiverName() {
        if (receiverDistributor != null) {
            return receiverDistributor.getName();
        }
        if (receiverStore != null) {
            return receiverStore.getName();
        }
        if (!toSystem && receiver != null) {
            return receiver.getName();
        }
        return TypeEnumeration.JOORJENS.getFa();
    }

    @JsonGetter
    public String getReceiverType() {
        if (receiverDistributor != null) {
            return Distributor.getEN();
        }
        if (receiverStore != null) {
            return Store.getEN();
        }
        if (!toSystem && receiver != null) {
            return Customer.getEN();
        }
        return TypeEnumeration.JOORJENS.getFa();
    }

    //-----------------------------------------------------------------------------------------------------------

    @JsonSetter
    public void setReceiverId(long id) {
        if (id > 0) {
            this.receiver = new Customer(id);
        }
    }

    @JsonSetter
    public void setReceiverDistributorId(long id) {
        if (id > 0) {
            this.receiverDistributor = new Distributor(id);
        }
    }

    @JsonSetter
    public void setReceiverStoreId(long id) {
        if (id > 0) {
            this.receiverStore = new Store(id);
        }
    }
    //-----------------------------------------------------------------------------------------------------------

}