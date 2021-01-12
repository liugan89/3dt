package com.takumi.wms.model.dto.inout;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractInOutLineImageCommandDto extends AbstractCommand
{
    /**
     * Sequence Id
     */
    private String sequenceId;

    public String getSequenceId()
    {
        return this.sequenceId;
    }

    public void setSequenceId(String sequenceId)
    {
        this.sequenceId = sequenceId;
    }


}
