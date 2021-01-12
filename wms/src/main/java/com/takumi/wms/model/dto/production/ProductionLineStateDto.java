package com.takumi.wms.model.dto.production;

import java.util.*;
import java.util.Date;


public class ProductionLineStateDto
{

    private String lineNumber;

    public String getLineNumber()
    {
        return this.lineNumber;
    }

    public void setLineNumber(String lineNumber)
    {
        this.lineNumber = lineNumber;
    }

    private java.math.BigDecimal processingCharge;

    public java.math.BigDecimal getProcessingCharge()
    {
        return this.processingCharge;
    }

    public void setProcessingCharge(java.math.BigDecimal processingCharge)
    {
        this.processingCharge = processingCharge;
    }

    private Boolean active;

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
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

    private String productionId;

    public String getProductionId()
    {
        return this.productionId;
    }

    public void setProductionId(String productionId)
    {
        this.productionId = productionId;
    }

    private String createdBy;

    public String getCreatedBy()
    {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }

    private Date createdAt;

    public Date getCreatedAt()
    {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    private String updatedBy;

    public String getUpdatedBy()
    {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }

    private Date updatedAt;

    public Date getUpdatedAt()
    {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    private ProductionLineInputStateDto[] productionLineInputs;

    public ProductionLineInputStateDto[] getProductionLineInputs()
    {
        return this.productionLineInputs;
    }	

    public void setProductionLineInputs(ProductionLineInputStateDto[] productionLineInputs)
    {
        this.productionLineInputs = productionLineInputs;
    }

    private ProductionLineOutputStateDto[] productionLineOutputs;

    public ProductionLineOutputStateDto[] getProductionLineOutputs()
    {
        return this.productionLineOutputs;
    }	

    public void setProductionLineOutputs(ProductionLineOutputStateDto[] productionLineOutputs)
    {
        this.productionLineOutputs = productionLineOutputs;
    }

}

