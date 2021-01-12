package com.takumi.wms.model.dto.movement;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractMovementCommandDto extends AbstractCommand
{
    private String documentNumber;

    public String getDocumentNumber()
    {
        return this.documentNumber;
    }

    public void setDocumentNumber(String documentNumber)
    {
        this.documentNumber = documentNumber;
    }

    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


    public void copyTo(AbstractMovementCommand command)
    {
        command.setDocumentNumber(this.getDocumentNumber());
        command.setVersion(this.getVersion());
        
        command.setRequesterId(this.getRequesterId());
        command.setCommandId(this.getCommandId());
    }

}
