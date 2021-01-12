package com.takumi.wms.model.dto.production;

import java.io.Serializable;

public class ProductionLineInputEventId implements Serializable
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

    private String productionLineInputSeqId;

    public String getProductionLineInputSeqId()
    {
        return this.productionLineInputSeqId;
    }

    public void setProductionLineInputSeqId(String productionLineInputSeqId)
    {
        this.productionLineInputSeqId = productionLineInputSeqId;
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

    public ProductionLineInputEventId()
    {
    }

    public ProductionLineInputEventId(String productionId, String productionLineLineNumber, String productionLineInputSeqId, Long productionVersion)
    {
        this.productionId = productionId;
        this.productionLineLineNumber = productionLineLineNumber;
        this.productionLineInputSeqId = productionLineInputSeqId;
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

        ProductionLineInputEventId other = (ProductionLineInputEventId)obj;
        return true 
            && (productionId == other.productionId || (productionId != null && productionId.equals(other.productionId)))
            && (productionLineLineNumber == other.productionLineLineNumber || (productionLineLineNumber != null && productionLineLineNumber.equals(other.productionLineLineNumber)))
            && (productionLineInputSeqId == other.productionLineInputSeqId || (productionLineInputSeqId != null && productionLineInputSeqId.equals(other.productionLineInputSeqId)))
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
        if (this.productionLineInputSeqId != null) {
            hash += 13 * this.productionLineInputSeqId.hashCode();
        }
        if (this.productionVersion != null) {
            hash += 13 * this.productionVersion.hashCode();
        }
        return hash;
    }

}

