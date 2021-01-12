package com.takumi.wms.model.dto.shipment;


public class RemoveShipmentImageDto extends CreateOrMergePatchShipmentImageDto
{

    public RemoveShipmentImageDto() {
        this.commandType = COMMAND_TYPE_REMOVE;
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

}

