package com.takumi.wms.model.dto.production;


public class RemoveProductionLineOutputDto extends CreateOrMergePatchProductionLineOutputDto
{

    public RemoveProductionLineOutputDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

}

