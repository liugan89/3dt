package com.takumi.wms.model.dto.movement;

import java.math.BigDecimal;

public class CreateOrMergePatchMovementLineDto extends AbstractMovementLineCommandDto
{
    private BigDecimal movementQuantity;

    public BigDecimal getMovementQuantity()
    {
        return this.movementQuantity;
    }

    public void setMovementQuantity(BigDecimal movementQuantity)
    {
        this.movementQuantity = movementQuantity;
    }

    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private String locatorIdFrom;

    public String getLocatorIdFrom()
    {
        return this.locatorIdFrom;
    }

    public void setLocatorIdFrom(String locatorIdFrom)
    {
        this.locatorIdFrom = locatorIdFrom;
    }

    private String locatorIdTo;

    public String getLocatorIdTo()
    {
        return this.locatorIdTo;
    }

    public void setLocatorIdTo(String locatorIdTo)
    {
        this.locatorIdTo = locatorIdTo;
    }

    private String attributeSetInstanceId;

    public String getAttributeSetInstanceId()
    {
        return this.attributeSetInstanceId;
    }

    public void setAttributeSetInstanceId(String attributeSetInstanceId)
    {
        this.attributeSetInstanceId = attributeSetInstanceId;
    }

    private Boolean processed;

    public Boolean getProcessed()
    {
        return this.processed;
    }

    public void setProcessed(Boolean processed)
    {
        this.processed = processed;
    }

    private String reversalLineNumber;

    public String getReversalLineNumber()
    {
        return this.reversalLineNumber;
    }

    public void setReversalLineNumber(String reversalLineNumber)
    {
        this.reversalLineNumber = reversalLineNumber;
    }

    private Boolean active;

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    private Boolean isPropertyMovementQuantityRemoved;

    public Boolean getIsPropertyMovementQuantityRemoved()
    {
        return this.isPropertyMovementQuantityRemoved;
    }

    public void setIsPropertyMovementQuantityRemoved(Boolean removed)
    {
        this.isPropertyMovementQuantityRemoved = removed;
    }

    private Boolean isPropertyProductIdRemoved;

    public Boolean getIsPropertyProductIdRemoved()
    {
        return this.isPropertyProductIdRemoved;
    }

    public void setIsPropertyProductIdRemoved(Boolean removed)
    {
        this.isPropertyProductIdRemoved = removed;
    }

    private Boolean isPropertyLocatorIdFromRemoved;

    public Boolean getIsPropertyLocatorIdFromRemoved()
    {
        return this.isPropertyLocatorIdFromRemoved;
    }

    public void setIsPropertyLocatorIdFromRemoved(Boolean removed)
    {
        this.isPropertyLocatorIdFromRemoved = removed;
    }

    private Boolean isPropertyLocatorIdToRemoved;

    public Boolean getIsPropertyLocatorIdToRemoved()
    {
        return this.isPropertyLocatorIdToRemoved;
    }

    public void setIsPropertyLocatorIdToRemoved(Boolean removed)
    {
        this.isPropertyLocatorIdToRemoved = removed;
    }

    private Boolean isPropertyAttributeSetInstanceIdRemoved;

    public Boolean getIsPropertyAttributeSetInstanceIdRemoved()
    {
        return this.isPropertyAttributeSetInstanceIdRemoved;
    }

    public void setIsPropertyAttributeSetInstanceIdRemoved(Boolean removed)
    {
        this.isPropertyAttributeSetInstanceIdRemoved = removed;
    }

    private Boolean isPropertyProcessedRemoved;

    public Boolean getIsPropertyProcessedRemoved()
    {
        return this.isPropertyProcessedRemoved;
    }

    public void setIsPropertyProcessedRemoved(Boolean removed)
    {
        this.isPropertyProcessedRemoved = removed;
    }

    private Boolean isPropertyReversalLineNumberRemoved;

    public Boolean getIsPropertyReversalLineNumberRemoved()
    {
        return this.isPropertyReversalLineNumberRemoved;
    }

    public void setIsPropertyReversalLineNumberRemoved(Boolean removed)
    {
        this.isPropertyReversalLineNumberRemoved = removed;
    }

    private Boolean isPropertyActiveRemoved;

    public Boolean getIsPropertyActiveRemoved()
    {
        return this.isPropertyActiveRemoved;
    }

    public void setIsPropertyActiveRemoved(Boolean removed)
    {
        this.isPropertyActiveRemoved = removed;
    }

    public void copyTo(AbstractMovementLineCommand.AbstractCreateOrMergePatchMovementLine command)
    {
        ((AbstractMovementLineCommandDto) this).copyTo(command);
        command.setMovementQuantity(this.getMovementQuantity());
        command.setProductId(this.getProductId());
        command.setLocatorIdFrom(this.getLocatorIdFrom());
        command.setLocatorIdTo(this.getLocatorIdTo());
        command.setAttributeSetInstanceId(this.getAttributeSetInstanceId());
        command.setProcessed(this.getProcessed());
        command.setReversalLineNumber(this.getReversalLineNumber());
        command.setActive(this.getActive());
    }

    public MovementLineCommand toCommand()
    {
        if (getCommandType() == null) {
            setCommandType(COMMAND_TYPE_MERGE_PATCH);
        }
        if (COMMAND_TYPE_CREATE.equals(getCommandType())) {
            AbstractMovementLineCommand.SimpleCreateMovementLine command = new AbstractMovementLineCommand.SimpleCreateMovementLine();
            copyTo((AbstractMovementLineCommand.AbstractCreateMovementLine) command);
            return command;
        } else if (COMMAND_TYPE_MERGE_PATCH.equals(getCommandType())) {
            AbstractMovementLineCommand.SimpleMergePatchMovementLine command = new AbstractMovementLineCommand.SimpleMergePatchMovementLine();
            copyTo((AbstractMovementLineCommand.SimpleMergePatchMovementLine) command);
            return command;
        } 
        else if (COMMAND_TYPE_REMOVE.equals(getCommandType())) {
            AbstractMovementLineCommand.SimpleRemoveMovementLine command = new AbstractMovementLineCommand.SimpleRemoveMovementLine();
            ((AbstractMovementLineCommandDto) this).copyTo(command);
            return command;
        }
        throw new IllegalStateException("Unknown command type:" + getCommandType());
    }

    public void copyTo(AbstractMovementLineCommand.AbstractCreateMovementLine command)
    {
        copyTo((AbstractMovementLineCommand.AbstractCreateOrMergePatchMovementLine) command);
    }

    public void copyTo(AbstractMovementLineCommand.AbstractMergePatchMovementLine command)
    {
        copyTo((AbstractMovementLineCommand.AbstractCreateOrMergePatchMovementLine) command);
        command.setIsPropertyMovementQuantityRemoved(this.getIsPropertyMovementQuantityRemoved());
        command.setIsPropertyProductIdRemoved(this.getIsPropertyProductIdRemoved());
        command.setIsPropertyLocatorIdFromRemoved(this.getIsPropertyLocatorIdFromRemoved());
        command.setIsPropertyLocatorIdToRemoved(this.getIsPropertyLocatorIdToRemoved());
        command.setIsPropertyAttributeSetInstanceIdRemoved(this.getIsPropertyAttributeSetInstanceIdRemoved());
        command.setIsPropertyProcessedRemoved(this.getIsPropertyProcessedRemoved());
        command.setIsPropertyReversalLineNumberRemoved(this.getIsPropertyReversalLineNumberRemoved());
        command.setIsPropertyActiveRemoved(this.getIsPropertyActiveRemoved());
    }

    public static class CreateMovementLineDto extends CreateOrMergePatchMovementLineDto
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }
        public MovementLineCommand.CreateMovementLine toCreateMovementLine()
        {
            return (MovementLineCommand.CreateMovementLine) toCommand();
        }

    }

    public static class MergePatchMovementLineDto extends CreateOrMergePatchMovementLineDto
    {
        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }
        public MovementLineCommand.MergePatchMovementLine toMergePatchMovementLine()
        {
            return (MovementLineCommand.MergePatchMovementLine) toCommand();
        }

    }

}

