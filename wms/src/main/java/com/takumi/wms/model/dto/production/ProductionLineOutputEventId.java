package com.takumi.wms.model.dto.production;

import java.io.Serializable;

public class ProductionLineOutputEventId implements Serializable
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

    private String productionLineLineNumber;

    public String getProductionLineLineNumber()
    {
        return this.productionLineLineNumber;
    }

    public void setProductionLineLineNumber(String productionLineLineNumber)
    {
        this.productionLineLineNumber = productionLineLineNumber;
    }

    private String productionLineOutputSeqId;

    public String getProductionLineOutputSeqId()
    {
        return this.productionLineOutputSeqId;
    }

    public void setProductionLineOutputSeqId(String productionLineOutputSeqId)
    {
        this.productionLineOutputSeqId = productionLineOutputSeqId;
    }

    private Long productionVersion;

    public Long getProductionVersion()
    {
        return this.productionVersion;
    }

    public void setProductionVersion(Long productionVersion)
    {
        this.productionVersion = productionVersion;
    }

    public ProductionLineOutputEventId()
    {
    }

    public ProductionLineOutputEventId(String productionId, String productionLineLineNumber, String productionLineOutputSeqId, Long productionVersion)
    {
        this.productionId = productionId;
        this.productionLineLineNumber = productionLineLineNumber;
        this.productionLineOutputSeqId = productionLineOutputSeqId;
        this.productionVersion = productionVersion;
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

        ProductionLineOutputEventId other = (ProductionLineOutputEventId)obj;
        return true 
            && (productionId == other.productionId || (productionId != null && productionId.equals(other.productionId)))
            && (productionLineLineNumber == other.productionLineLineNumber || (productionLineLineNumber != null && productionLineLineNumber.equals(other.productionLineLineNumber)))
            && (productionLineOutputSeqId == other.productionLineOutputSeqId || (productionLineOutputSeqId != null && productionLineOutputSeqId.equals(other.productionLineOutputSeqId)))
            && (productionVersion == other.productionVersion || (productionVersion != null && productionVersion.equals(other.productionVersion)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.productionId != null) {
            hash += 13 * this.productionId.hashCode();
        }
        if (this.productionLineLineNumber != null) {
            hash += 13 * this.productionLineLineNumber.hashCode();
        }
        if (this.productionLineOutputSeqId != null) {
            hash += 13 * this.productionLineOutputSeqId.hashCode();
        }
        if (this.productionVersion != null) {
            hash += 13 * this.productionVersion.hashCode();
        }
        return hash;
    }


}

