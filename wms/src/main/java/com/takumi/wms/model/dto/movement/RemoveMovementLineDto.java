package com.takumi.wms.model.dto.movement;


public class RemoveMovementLineDto extends CreateOrMergePatchMovementLineDto {

    public RemoveMovementLineDto() {
        setCommandType(COMMAND_TYPE_REMOVE);
    }

    @Override
    public String getCommandType() {
        return COMMAND_TYPE_REMOVE;
    }

}

