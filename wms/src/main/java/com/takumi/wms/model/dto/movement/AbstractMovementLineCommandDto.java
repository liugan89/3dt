package com.takumi.wms.model.dto.movement;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractMovementLineCommandDto extends AbstractCommand
{
    private String lineNumber;

    public String getLineNumber()
    {
        return this.lineNumber;
    }

    public void setLineNumber(String lineNumber)
    {
        this.lineNumber = lineNumber;
    }


    public void copyTo(AbstractMovementLineCommand command)
    {
        command.setLineNumber(this.getLineNumber());
        
        command.setRequesterId(this.getRequesterId());
        command.setCommandId(this.getCommandId());
    }

}
