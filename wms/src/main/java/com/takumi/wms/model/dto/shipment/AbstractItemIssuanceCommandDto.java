package com.takumi.wms.model.dto.shipment;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractItemIssuanceCommandDto extends AbstractCommand
{
    /**
     * Item Issuance Seq Id
     */
    private String itemIssuanceSeqId;

    public String getItemIssuanceSeqId()
    {
        return this.itemIssuanceSeqId;
    }

    public void setItemIssuanceSeqId(String itemIssuanceSeqId)
    {
        this.itemIssuanceSeqId = itemIssuanceSeqId;
    }


}
