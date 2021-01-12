package com.takumi.wms.model.dto;

public abstract class AbstractCommand implements Command {

    protected String commandType;

    public String getCommandType() {
        return this.commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    private String commandId;

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    private String requesterId;

    public String getRequesterId() {
        return this.requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

}
