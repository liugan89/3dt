package com.takumi.wms.model.dto.inout;

import java.io.Serializable;
import java.math.BigDecimal;

public class ImportingInOutLine implements Serializable
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

    private String locatorId;

    public String getLocatorId()
    {
        return this.locatorId;
    }

    public void setLocatorId(String locatorId)
    {
        this.locatorId = locatorId;
    }

    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private java.util.Map<String, Object> attributeSetInstance;

    public java.util.Map<String, Object> getAttributeSetInstance()
    {
        return this.attributeSetInstance;
    }

    public void setAttributeSetInstance(java.util.Map<String, Object> attributeSetInstance)
    {
        this.attributeSetInstance = attributeSetInstance;
    }

    private String description;

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    private String quantityUomId;

    public String getQuantityUomId()
    {
        return this.quantityUomId;
    }

    public void setQuantityUomId(String quantityUomId)
    {
        this.quantityUomId = quantityUomId;
    }

    private BigDecimal movementQuantity;

    public BigDecimal getMovementQuantity()
    {
        return this.movementQuantity;
    }

    public void setMovementQuantity(BigDecimal movementQuantity)
    {
        this.movementQuantity = movementQuantity;
    }

    public ImportingInOutLine()
    {
    }

    public ImportingInOutLine(String lineNumber, String locatorId, String productId, java.util.Map<String, Object> attributeSetInstance, String description, String quantityUomId, BigDecimal movementQuantity)
    {
        this.lineNumber = lineNumber;
        this.locatorId = locatorId;
        this.productId = productId;
        this.attributeSetInstance = attributeSetInstance;
        this.description = description;
        this.quantityUomId = quantityUomId;
        this.movementQuantity = movementQuantity;
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

        ImportingInOutLine other = (ImportingInOutLine)obj;
        return true 
            && (lineNumber == other.lineNumber || (lineNumber != null && lineNumber.equals(other.lineNumber)))
            && (locatorId == other.locatorId || (locatorId != null && locatorId.equals(other.locatorId)))
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (attributeSetInstance == other.attributeSetInstance || (attributeSetInstance != null && attributeSetInstance.equals(other.attributeSetInstance)))
            && (description == other.description || (description != null && description.equals(other.description)))
            && (quantityUomId == other.quantityUomId || (quantityUomId != null && quantityUomId.equals(other.quantityUomId)))
            && (movementQuantity == other.movementQuantity || (movementQuantity != null && movementQuantity.equals(other.movementQuantity)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.lineNumber != null) {
            hash += 13 * this.lineNumber.hashCode();
        }
        if (this.locatorId != null) {
            hash += 13 * this.locatorId.hashCode();
        }
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.attributeSetInstance != null) {
            hash += 13 * this.attributeSetInstance.hashCode();
        }
        if (this.description != null) {
            hash += 13 * this.description.hashCode();
        }
        if (this.quantityUomId != null) {
            hash += 13 * this.quantityUomId.hashCode();
        }
        if (this.movementQuantity != null) {
            hash += 13 * this.movementQuantity.hashCode();
        }
        return hash;
    }



}

