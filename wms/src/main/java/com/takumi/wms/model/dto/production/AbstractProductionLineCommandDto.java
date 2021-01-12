package com.takumi.wms.model.dto.production;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractProductionLineCommandDto extends AbstractCommand
{
    /**
     * Line Number
     */
    private String lineNumber;

    public String getLineNumber()
    {
        return this.lineNumber;
    }

    public void setLineNumber(String lineNumber)
    {
        this.lineNumber = lineNumber;
    }


}
