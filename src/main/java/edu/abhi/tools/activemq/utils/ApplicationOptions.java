package edu.abhi.tools.activemq.utils;

import edu.abhi.tools.activemq.constants.ActionType;

import java.io.File;

public class ApplicationOptions {

    private String Queue1Name, Queue2Name;
    private String actionType, messageType;
    private String folderLocation;
    private String messageFormat, firstLineStarts, lastLineStarts;
    private String formattedDate;
    private boolean multipleMessage, messageStore;

    private int msgCopies;


    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public boolean isMultipleMessage() {
        return multipleMessage;
    }

    public void setMultipleMessage(boolean multipleMessage) {
        this.multipleMessage = multipleMessage;
    }

    public ActionType getActionTypeEnum() {
        return actionTypeEnum;
    }

    public void setActionTypeEnum(ActionType actionTypeEnum) {
        this.actionTypeEnum = actionTypeEnum;
    }

    private ActionType actionTypeEnum;

    public String getQueue1Name() {
        return Queue1Name;
    }

    public void setQueue1Name(String queue1Name) {
        Queue1Name = queue1Name;
    }

    public String getQueue2Name() {
        return Queue2Name;
    }

    public void setQueue2Name(String queue2Name) {
        Queue2Name = queue2Name;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    public String getMessageFormat() {
        return messageFormat;
    }

    public void setMessageFormat(String messageFormat) {
        this.messageFormat = messageFormat;
    }

    public String getFirstLineStarts() {
        return firstLineStarts;
    }

    public void setFirstLineStarts(String firstLineStarts) {
        this.firstLineStarts = firstLineStarts;
    }

    public String getLastLineStarts() {
        return lastLineStarts;
    }

    public void setLastLineStarts(String lastLineStarts) {
        this.lastLineStarts = lastLineStarts;
    }

    public int getMsgCopies() {
        return msgCopies;
    }

    public void setMsgCopies(int msgCopies) {
        this.msgCopies = msgCopies;
    }

    public String getDownloadFileName() {
        return getFolderLocation() + File.separator + String.format("%s-Msg-%s-%s-",
                getMessageType(), getQueue1Name(), formattedDate);
    }

    public boolean isMessageStore() {
        return messageStore;
    }

    public void setMessageStore(boolean messageStore) {
        this.messageStore = messageStore;
    }
}
