package com.takumi.wms.model.dto.production;

import java.io.Serializable;

public class ProductionLineOutputId implements Serializable
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

    public ProductionLineOutputId()
    {
    }

    public ProductionLineOutputId(String productionId, String productionLineLineNumber, String productionLineOutputSeqId)
    {
        this.productionId = productionId;
        this.productionLineLineNumber = productionLineLineNumber;
        this.productionLineOutputSeqId = productionLineOutputSeqId;
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

        ProductionLineOutputId other = (ProductionLineOutputId)obj;
        return true 
            && (productionId == other.productionId || (productionId != null && productionId.equals(other.productionId)))
            && (productionLineLineNumber == other.productionLineLineNumber || (productionLineLineNumber != null && productionLineLineNumber.equals(other.productionLineLineNumber)))
            && (productionLineOutputSeqId == other.productionLineOutputSeqId || (productionLineOutputSeqId != null && productionLineOutputSeqId.equals(other.productionLineOutputSeqId)))
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
        return hash;
    }


}

