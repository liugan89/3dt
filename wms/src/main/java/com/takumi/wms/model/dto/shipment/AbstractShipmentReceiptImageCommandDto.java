package com.takumi.wms.model.dto.shipment;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractShipmentReceiptImageCommandDto extends AbstractCommand
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
