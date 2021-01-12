package com.takumi.wms.model.dto.shipment;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractShipmentReceiptCommandDto extends AbstractCommand
{
    /**
     * Receipt Seq Id
     */
    private String receiptSeqId;

    public String getReceiptSeqId()
    {
        return this.receiptSeqId;
    }

    public void setReceiptSeqId(String receiptSeqId)
    {
        this.receiptSeqId = receiptSeqId;
    }


}
