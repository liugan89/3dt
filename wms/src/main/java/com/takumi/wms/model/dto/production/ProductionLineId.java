package com.takumi.wms.model.dto.production;

import java.io.Serializable;

public class ProductionLineId implements Serializable
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

    private String lineNumber;

    public String getLineNumber()
    {
        return this.lineNumber;
    }

    public void setLineNumber(String lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    public ProductionLineId()
    {
    }

    public ProductionLineId(String productionId, String lineNumber)
    {
        this.productionId = productionId;
        this.lineNumber = lineNumber;
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

        ProductionLineId other = (ProductionLineId)obj;
        return true 
            && (productionId == other.productionId || (productionId != null && productionId.equals(other.productionId)))
            && (lineNumber == other.lineNumber || (lineNumber != null && lineNumber.equals(other.lineNumber)))
            ;
    }





}

