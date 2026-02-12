package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageReceiver {
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("seenTime")
    @Expose
    private Integer seenTime;
    @SerializedName("messageType")
    @Expose
    private Integer messageType;
    @SerializedName("toSystem")
    @Expose
    private Boolean toSystem;
    @SerializedName("messageTypeName")
    @Expose
    private String messageTypeName;
    @SerializedName("seen")
    @Expose
    private Boolean seen;
    @SerializedName("messageId")
    @Expose
    private Integer messageId;
    @SerializedName("receiverId")
    @Expose
    private Integer receiverId;
    @SerializedName("receiverName")
    @Expose
    private String receiverName;
    @SerializedName("receiverType")
    @Expose
    private String receiverType;

    public Boolean getBlock() {
        return block;
    }

    public void setBlock(Boolean block) {
        this.block = block;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSeenTime() {
        return seenTime;
    }

    public void setSeenTime(Integer seenTime) {
        this.seenTime = seenTime;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Boolean getToSystem() {
        return toSystem;
    }

    public void setToSystem(Boolean toSystem) {
        this.toSystem = toSystem;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public void setMessageTypeName(String messageTypeName) {
        this.messageTypeName = messageTypeName;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(String receiverType) {
        this.receiverType = receiverType;
    }
}
