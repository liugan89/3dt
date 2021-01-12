package com.takumi.wms.model.dto.production;

import java.io.Serializable;

public class ProductionEventId implements Serializable
{
    private String productionId;

    public String getProductionId()
    {
        return this.productionId;
    }

    public void setProductionId(String productionId)
    {
        this.productionId = productionId;
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

    public ProductionEventId()
    {
    }

    public ProductionEventId(String productionId, Long version)
    {
        this.productionId = productionId;
        this.version = version;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ProductionEventId other = (ProductionEventId)obj;
        return true 
            && (productionId == other.productionId || (productionId != null && productionId.equals(other.productionId)))
            && (version == other.version || (version != null && version.equals(other.version)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.productionId != null) {
            hash += 13 * this.productionId.hashCode();
        }
        if (this.version != null) {
            hash += 13 * this.version.hashCode();
        }
        return hash;
    }



}

