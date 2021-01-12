package com.takumi.wms.model.dto.shipment;

import java.io.Serializable;

public class ImportingShipmentItem implements Serializable
{
    private String shipmentItemSeqId;

    public String getShipmentItemSeqId()
    {
        return this.shipmentItemSeqId;
    }

    public void setShipmentItemSeqId(String shipmentItemSeqId)
    {
        this.shipmentItemSeqId = shipmentItemSeqId;
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

    private java.math.BigDecimal quantity;

    public java.math.BigDecimal getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    private String shipmentContentDescription;

    public String getShipmentContentDescription()
    {
        return this.shipmentContentDescription;
    }

    public void setShipmentContentDescription(String shipmentContentDescription)
    {
        this.shipmentContentDescription = shipmentContentDescription;
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

    public ImportingShipmentItem()
    {
    }

    public ImportingShipmentItem(String shipmentItemSeqId, String productId, java.math.BigDecimal quantity, String shipmentContentDescription, java.util.Map<String, Object> attributeSetInstance)
    {
        this.shipmentItemSeqId = shipmentItemSeqId;
        this.productId = productId;
        this.quantity = quantity;
        this.shipmentContentDescription = shipmentContentDescription;
        this.attributeSetInstance = attributeSetInstance;
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

        ImportingShipmentItem other = (ImportingShipmentItem)obj;
        return true 
            && (shipmentItemSeqId == other.shipmentItemSeqId || (shipmentItemSeqId != null && shipmentItemSeqId.equals(other.shipmentItemSeqId)))
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (quantity == other.quantity || (quantity != null && quantity.equals(other.quantity)))
            && (shipmentContentDescription == other.shipmentContentDescription || (shipmentContentDescription != null && shipmentContentDescription.equals(other.shipmentContentDescription)))
            && (attributeSetInstance == other.attributeSetInstance || (attributeSetInstance != null && attributeSetInstance.equals(other.attributeSetInstance)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.shipmentItemSeqId != null) {
            hash += 13 * this.shipmentItemSeqId.hashCode();
        }
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.quantity != null) {
            hash += 13 * this.quantity.hashCode();
        }
        if (this.shipmentContentDescription != null) {
            hash += 13 * this.shipmentContentDescription.hashCode();
        }
        if (this.attributeSetInstance != null) {
            hash += 13 * this.attributeSetInstance.hashCode();
        }
        return hash;
    }



}

