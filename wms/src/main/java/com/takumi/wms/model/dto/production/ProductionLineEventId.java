package com.takumi.wms.model.dto.production;

import java.io.Serializable;

public class ProductionLineEventId implements Serializable
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

    private Long productionVersion;

    public Long getProductionVersion()
    {
        return this.productionVersion;
    }

    public void setProductionVersion(Long productionVersion)
    {
        this.productionVersion = productionVersion;
    }

    public ProductionLineEventId()
    {
    }

    public ProductionLineEventId(String productionId, String lineNumber, Long productionVersion)
    {
        this.productionId = productionId;
        this.lineNumber = lineNumber;
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

        ProductionLineEventId other = (ProductionLineEventId)obj;
        return true 
            && (productionId == other.productionId || (productionId != null && productionId.equals(other.productionId)))
            && (lineNumber == other.lineNumber || (lineNumber != null && lineNumber.equals(other.lineNumber)))
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
        if (this.lineNumber != null) {
            hash += 13 * this.lineNumber.hashCode();
        }
        if (this.productionVersion != null) {
            hash += 13 * this.productionVersion.hashCode();
        }
        return hash;
    }



}

