package com.takumi.wms.model.dto.shipment;

public class CreateOrMergePatchItemIssuanceDto extends AbstractItemIssuanceCommandDto
{
    /**
     * Order Id
     */
    private String orderId;

    public String getOrderId()
    {
        return this.orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    /**
     * Order Item Seq Id
     */
    private String orderItemSeqId;

    public String getOrderItemSeqId()
    {
        return this.orderItemSeqId;
    }

    public void setOrderItemSeqId(String orderItemSeqId)
    {
        this.orderItemSeqId = orderItemSeqId;
    }

    /**
     * Ship Group Seq Id
     */
    private Long shipGroupSeqId;

    public Long getShipGroupSeqId()
    {
        return this.shipGroupSeqId;
    }

    public void setShipGroupSeqId(Long shipGroupSeqId)
    {
        this.shipGroupSeqId = shipGroupSeqId;
    }

    /**
     * Product Id
     */
    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    /**
     * Locator Id
     */
    private String locatorId;

    public String getLocatorId()
    {
        return this.locatorId;
    }

    public void setLocatorId(String locatorId)
    {
        this.locatorId = locatorId;
    }

    /**
     * Attribute Set Instance Id
     */
    private String attributeSetInstanceId;

    public String getAttributeSetInstanceId()
    {
        return this.attributeSetInstanceId;
    }

    public void setAttributeSetInstanceId(String attributeSetInstanceId)
    {
        this.attributeSetInstanceId = attributeSetInstanceId;
    }

    /**
     * IncomingShipment Item Seq Id
     */
    private String shipmentItemSeqId;

    public String getShipmentItemSeqId()
    {
        return this.shipmentItemSeqId;
    }

    public void setShipmentItemSeqId(String shipmentItemSeqId)
    {
        this.shipmentItemSeqId = shipmentItemSeqId;
    }

    /**
     * Fixed Asset Id
     */
    private String fixedAssetId;

    public String getFixedAssetId()
    {
        return this.fixedAssetId;
    }

    public void setFixedAssetId(String fixedAssetId)
    {
        this.fixedAssetId = fixedAssetId;
    }

    /**
     * Maint Hist Seq Id
     */
    private String maintHistSeqId;

    public String getMaintHistSeqId()
    {
        return this.maintHistSeqId;
    }

    public void setMaintHistSeqId(String maintHistSeqId)
    {
        this.maintHistSeqId = maintHistSeqId;
    }

    /**
     * Issued Date Time
     */
    private java.sql.Timestamp issuedDateTime;

    public java.sql.Timestamp getIssuedDateTime()
    {
        return this.issuedDateTime;
    }

    public void setIssuedDateTime(java.sql.Timestamp issuedDateTime)
    {
        this.issuedDateTime = issuedDateTime;
    }

    /**
     * Issued By User Login Id
     */
    private String issuedByUserLoginId;

    public String getIssuedByUserLoginId()
    {
        return this.issuedByUserLoginId;
    }

    public void setIssuedByUserLoginId(String issuedByUserLoginId)
    {
        this.issuedByUserLoginId = issuedByUserLoginId;
    }

    /**
     * Quantity
     */
    private java.math.BigDecimal quantity;

    public java.math.BigDecimal getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Cancel Quantity
     */
    private java.math.BigDecimal cancelQuantity;

    public java.math.BigDecimal getCancelQuantity()
    {
        return this.cancelQuantity;
    }

    public void setCancelQuantity(java.math.BigDecimal cancelQuantity)
    {
        this.cancelQuantity = cancelQuantity;
    }

    /**
     * Active
     */
    private Boolean active;

    public Boolean getActive()
    {
        return this.active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

    private Boolean isPropertyOrderIdRemoved;

    public Boolean getIsPropertyOrderIdRemoved()
    {
        return this.isPropertyOrderIdRemoved;
    }

    public void setIsPropertyOrderIdRemoved(Boolean removed)
    {
        this.isPropertyOrderIdRemoved = removed;
    }

    private Boolean isPropertyOrderItemSeqIdRemoved;

    public Boolean getIsPropertyOrderItemSeqIdRemoved()
    {
        return this.isPropertyOrderItemSeqIdRemoved;
    }

    public void setIsPropertyOrderItemSeqIdRemoved(Boolean removed)
    {
        this.isPropertyOrderItemSeqIdRemoved = removed;
    }

    private Boolean isPropertyShipGroupSeqIdRemoved;

    public Boolean getIsPropertyShipGroupSeqIdRemoved()
    {
        return this.isPropertyShipGroupSeqIdRemoved;
    }

    public void setIsPropertyShipGroupSeqIdRemoved(Boolean removed)
    {
        this.isPropertyShipGroupSeqIdRemoved = removed;
    }

    private Boolean isPropertyProductIdRemoved;

    public Boolean getIsPropertyProductIdRemoved()
    {
        return this.isPropertyProductIdRemoved;
    }

    public void setIsPropertyProductIdRemoved(Boolean removed)
    {
        this.isPropertyProductIdRemoved = removed;
    }

    private Boolean isPropertyLocatorIdRemoved;

    public Boolean getIsPropertyLocatorIdRemoved()
    {
        return this.isPropertyLocatorIdRemoved;
    }

    public void setIsPropertyLocatorIdRemoved(Boolean removed)
    {
        this.isPropertyLocatorIdRemoved = removed;
    }

    private Boolean isPropertyAttributeSetInstanceIdRemoved;

    public Boolean getIsPropertyAttributeSetInstanceIdRemoved()
    {
        return this.isPropertyAttributeSetInstanceIdRemoved;
    }

    public void setIsPropertyAttributeSetInstanceIdRemoved(Boolean removed)
    {
        this.isPropertyAttributeSetInstanceIdRemoved = removed;
    }

    private Boolean isPropertyShipmentItemSeqIdRemoved;

    public Boolean getIsPropertyShipmentItemSeqIdRemoved()
    {
        return this.isPropertyShipmentItemSeqIdRemoved;
    }

    public void setIsPropertyShipmentItemSeqIdRemoved(Boolean removed)
    {
        this.isPropertyShipmentItemSeqIdRemoved = removed;
    }

    private Boolean isPropertyFixedAssetIdRemoved;

    public Boolean getIsPropertyFixedAssetIdRemoved()
    {
        return this.isPropertyFixedAssetIdRemoved;
    }

    public void setIsPropertyFixedAssetIdRemoved(Boolean removed)
    {
        this.isPropertyFixedAssetIdRemoved = removed;
    }

    private Boolean isPropertyMaintHistSeqIdRemoved;

    public Boolean getIsPropertyMaintHistSeqIdRemoved()
    {
        return this.isPropertyMaintHistSeqIdRemoved;
    }

    public void setIsPropertyMaintHistSeqIdRemoved(Boolean removed)
    {
        this.isPropertyMaintHistSeqIdRemoved = removed;
    }

    private Boolean isPropertyIssuedDateTimeRemoved;

    public Boolean getIsPropertyIssuedDateTimeRemoved()
    {
        return this.isPropertyIssuedDateTimeRemoved;
    }

    public void setIsPropertyIssuedDateTimeRemoved(Boolean removed)
    {
        this.isPropertyIssuedDateTimeRemoved = removed;
    }

    private Boolean isPropertyIssuedByUserLoginIdRemoved;

    public Boolean getIsPropertyIssuedByUserLoginIdRemoved()
    {
        return this.isPropertyIssuedByUserLoginIdRemoved;
    }

    public void setIsPropertyIssuedByUserLoginIdRemoved(Boolean removed)
    {
        this.isPropertyIssuedByUserLoginIdRemoved = removed;
    }

    private Boolean isPropertyQuantityRemoved;

    public Boolean getIsPropertyQuantityRemoved()
    {
        return this.isPropertyQuantityRemoved;
    }

    public void setIsPropertyQuantityRemoved(Boolean removed)
    {
        this.isPropertyQuantityRemoved = removed;
    }

    private Boolean isPropertyCancelQuantityRemoved;

    public Boolean getIsPropertyCancelQuantityRemoved()
    {
        return this.isPropertyCancelQuantityRemoved;
    }

    public void setIsPropertyCancelQuantityRemoved(Boolean removed)
    {
        this.isPropertyCancelQuantityRemoved = removed;
    }

    private Boolean isPropertyActiveRemoved;

    public Boolean getIsPropertyActiveRemoved()
    {
        return this.isPropertyActiveRemoved;
    }

    public void setIsPropertyActiveRemoved(Boolean removed)
    {
        this.isPropertyActiveRemoved = removed;
    }

    public static class CreateItemIssuanceDto extends CreateOrMergePatchItemIssuanceDto
    {
        public CreateItemIssuanceDto() {
            this.commandType = COMMAND_TYPE_CREATE;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_CREATE;
        }

    }

    public static class MergePatchItemIssuanceDto extends CreateOrMergePatchItemIssuanceDto
    {
        public MergePatchItemIssuanceDto() {
            this.commandType = COMMAND_TYPE_MERGE_PATCH;
        }

        @Override
        public String getCommandType() {
            return COMMAND_TYPE_MERGE_PATCH;
        }

    }

}

