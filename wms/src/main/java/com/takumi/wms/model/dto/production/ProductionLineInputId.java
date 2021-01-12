package com.takumi.wms.model.dto.production;

import java.io.Serializable;

public class ProductionLineInputId implements Serializable
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

    public ProductionLineInputId()
    {
    }

    public ProductionLineInputId(String productionId, String productionLineLineNumber, String productionLineInputSeqId)
    {
        this.productionId = productionId;
        this.productionLineLineNumber = productionLineLineNumber;
        this.productionLineInputSeqId = productionLineInputSeqId;
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

        ProductionLineInputId other = (ProductionLineInputId)obj;
        return true 
            && (productionId == other.productionId || (productionId != null && productionId.equals(other.productionId)))
            && (productionLineLineNumber == other.productionLineLineNumber || (productionLineLineNumber != null && productionLineLineNumber.equals(other.productionLineLineNumber)))
            && (productionLineInputSeqId == other.productionLineInputSeqId || (productionLineInputSeqId != null && productionLineInputSeqId.equals(other.productionLineInputSeqId)))
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
        return hash;
    }

}

