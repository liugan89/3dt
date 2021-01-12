package com.takumi.wms.model.dto.production;


public class DeleteProductionDto extends AbstractProductionCommandDto
{

    public DeleteProductionDto() {
        this.commandType = COMMAND_TYPE_DELETE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_DELETE;
    }

}

