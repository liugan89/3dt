package com.takumi.wms.model.dto.production;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractProductionLineOutputCommandDto extends AbstractCommand
{
    /**
     * Production Line Output Seq Id
     */
    private String productionLineOutputSeqId;

    public String getProductionLineOutputSeqId()
    {
        return this.productionLineOutputSeqId;
    }

    public void setProductionLineOutputSeqId(String productionLineOutputSeqId)
    {
        this.productionLineOutputSeqId = productionLineOutputSeqId;
    }


}
