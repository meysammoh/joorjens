package ir.joorjens.joorapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Message {
    @SerializedName("createdTime")
    @Expose
    private Integer createdTime;
    @SerializedName("updatedTime")
    @Expose
    private Integer updatedTime;
    @SerializedName("block")
    @Expose
    private Boolean block;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("fromSystem")
    @Expose
    private Boolean fromSystem;
    @SerializedName("messageReceivers")
    @Expose
    private List<MessageReceiver> messageReceivers = null;
    @SerializedName("senderId")
    @Expose
    private Integer senderId;
    @SerializedName("senderName")
    @Expose
    private String senderName;
    @SerializedName("senderType")
    @Expose
    private String senderType;
    @SerializedName("messageReceiversOrdinal")
    @Expose
    private String messageReceiversOrdinal;
    @SerializedName("messageReceiversCC")
    @Expose
    private String messageReceiversCC;
    @SerializedName("messageReceiversBCC")
    @Expose
    private String messageReceiversBCC;

    public Integer getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Integer createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Integer updatedTime) {
        this.updatedTime = updatedTime;
    }

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

    public Boolean getFromSystem() {
        return fromSystem;
    }

    public void setFromSystem(Boolean fromSystem) {
        this.fromSystem = fromSystem;
    }

    public List<MessageReceiver> getMessageReceivers() {
        return messageReceivers;
    }

    public void setMessageReceivers(List<MessageReceiver> messageReceivers) {
        this.messageReceivers = messageReceivers;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getMessageReceiversOrdinal() {
        return messageReceiversOrdinal;
    }

    public void setMessageReceiversOrdinal(String messageReceiversOrdinal) {
        this.messageReceiversOrdinal = messageReceiversOrdinal;
    }

    public String getMessageReceiversCC() {
        return messageReceiversCC;
    }

    public void setMessageReceiversCC(String messageReceiversCC) {
        this.messageReceiversCC = messageReceiversCC;
    }

    public String getMessageReceiversBCC() {
        return messageReceiversBCC;
    }

    public void setMessageReceiversBCC(String messageReceiversBCC) {
        this.messageReceiversBCC = messageReceiversBCC;
    }
}
