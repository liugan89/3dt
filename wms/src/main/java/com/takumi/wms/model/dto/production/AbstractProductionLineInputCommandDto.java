package com.takumi.wms.model.dto.production;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractProductionLineInputCommandDto extends AbstractCommand
{
    /**
     * Production Line Input Seq Id
     */
    private String productionLineInputSeqId;

    public String getProductionLineInputSeqId()
    {
        return this.productionLineInputSeqId;
    }

    public void setProductionLineInputSeqId(String productionLineInputSeqId)
    {
        this.productionLineInputSeqId = productionLineInputSeqId;
    }


}
