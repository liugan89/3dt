package com.takumi.wms.model.dto.production;


public class RemoveProductionLineDto extends CreateOrMergePatchProductionLineDto
{

    public RemoveProductionLineDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

}

