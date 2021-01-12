package com.takumi.wms.model.dto.production;


public class RemoveProductionLineInputDto extends CreateOrMergePatchProductionLineInputDto
{

    public RemoveProductionLineInputDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

}

