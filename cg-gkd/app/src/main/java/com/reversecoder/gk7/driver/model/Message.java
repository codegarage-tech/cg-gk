package com.reversecoder.gk7.driver.model;

import com.google.gson.Gson;

/**
 * Created by rashed on 3/2/16.
 */
public class Message {

    private String id = "";
    private String order_id = "";
    private String parent_id = "";
    private String sender_type = "";
    private String receiver_type = "";
    private String sender = "";
    private String receiver = "";
    private String message = "";
    private String is_read = "";
    private String delete_by_receiver = "";
    private String delete_by_sender = "";
    private String status = "";
    private String created = "";
    private String modified = "";
    private String created_by = "";
    private String modified_by = "";
    private String senderName = "";
    private String receiverName = "";
    private String senderEmail = "";
    private String receiverEmail = "";
    private String jobReference = "";

    public String getModified_by() {
        return modified_by;
    }

    public void setModified_by(String modified_by) {
        this.modified_by = modified_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getSender_type() {
        return sender_type;
    }

    public void setSender_type(String sender_type) {
        this.sender_type = sender_type;
    }

    public String getSenderTypeName() {
        if (getSender_type().equalsIgnoreCase("1")) {
            return "Customer";
        } else if (getSender_type().equalsIgnoreCase("2")) {
            return "Driver";
        } else if (getSender_type().equalsIgnoreCase("3")) {
            return "Admin";
        }
        return "";
    }

    public String getReceiver_type() {
        return receiver_type;
    }

    public void setReceiver_type(String receiver_type) {
        this.receiver_type = receiver_type;
    }

    public String getReceiverTypeName() {
        if (getReceiver_type().equalsIgnoreCase("1")) {
            return "Customer";
        } else if (getReceiver_type().equalsIgnoreCase("2")) {
            return "Driver";
        } else if (getReceiver_type().equalsIgnoreCase("3")) {
            return "Admin";
        }
        return "";
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getDelete_by_receiver() {
        return delete_by_receiver;
    }

    public void setDelete_by_receiver(String delete_by_receiver) {
        this.delete_by_receiver = delete_by_receiver;
    }

    public String getDelete_by_sender() {
        return delete_by_sender;
    }

    public void setDelete_by_sender(String delete_by_sender) {
        this.delete_by_sender = delete_by_sender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getJobReference() {
        return jobReference;
    }

    public void setJobReference(String jobReference) {
        this.jobReference = jobReference;
    }

    public static Message getMessageObject(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, Message.class);
    }

    public String toStringObject() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", order_id='" + order_id + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", sender_type='" + sender_type + '\'' +
                ", receiver_type='" + receiver_type + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", message='" + message + '\'' +
                ", is_read='" + is_read + '\'' +
                ", delete_by_receiver='" + delete_by_receiver + '\'' +
                ", delete_by_sender='" + delete_by_sender + '\'' +
                ", status='" + status + '\'' +
                ", created='" + created + '\'' +
                ", modified='" + modified + '\'' +
                ", created_by='" + created_by + '\'' +
                ", modified_by='" + modified_by + '\'' +
                ", senderName='" + senderName + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", jobReference='" + jobReference + '\'' +
                '}';
    }
}
