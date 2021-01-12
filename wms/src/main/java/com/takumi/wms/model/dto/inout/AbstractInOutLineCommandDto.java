package com.takumi.wms.model.dto.inout;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractInOutLineCommandDto extends AbstractCommand
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
