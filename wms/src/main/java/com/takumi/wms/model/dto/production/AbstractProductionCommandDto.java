package com.takumi.wms.model.dto.production;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractProductionCommandDto extends AbstractCommand
{
    /**
     * Production Id
     */
    private String productionId;

    public String getProductionId()
    {
        return this.productionId;
    }

    public void setProductionId(String productionId)
    {
        this.productionId = productionId;
    }

    /**
     * Version
     */
    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


}
