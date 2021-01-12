package com.takumi.wms.model.dto.inout;


public class RemoveInOutLineDto extends CreateOrMergePatchInOutLineDto
{

    public RemoveInOutLineDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

}

