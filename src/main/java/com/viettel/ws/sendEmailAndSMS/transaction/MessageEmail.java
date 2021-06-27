package com.viettel.ws.sendEmailAndSMS.transaction;

/**
 *
 * @author HiepND8
 */
public class MessageEmail {

    public MessageEmail() {
    }

    private int messageID; // id cua email
    private String sendTime; // thoi gian gui email
    private int isSent; // duoc gui hay chua
    private String errorMsg; // noi dung thong bao loi
    private String sentTimeReq; // thoi gian tin nhan duoc them vao csdl
    private int msgType; // dang tin nhan
    private String msgTypeDetails;
    private String receiveEmail; // dia chi email gui
    private int senderID;
    private String content; // noi dung
    private int sendCount; // so lan gui

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getIsSent() {
        return isSent;
    }

    public void setIsSent(int isSent) {
        this.isSent = isSent;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSentTimeReq() {
        return sentTimeReq;
    }

    public void setSentTimeReq(String sentTimeReq) {
        this.sentTimeReq = sentTimeReq;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getMsgTypeDetails() {
        return msgTypeDetails;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;

        switch (msgType) {
            case 0:
                this.msgTypeDetails = "Thông báo đã tiếp nhận hồ sơ";
                break;
            case 1:
                this.msgTypeDetails = "Thông báo kết quả tiếp nhận hồ sơ";
                break;
        }
    }

    public String getReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(String receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }
}
