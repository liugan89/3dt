package com.takumi.wms.model.dto.shipment;

import java.math.BigDecimal;
import java.util.Map;

public class ShipmentCommandDtos {
    private ShipmentCommandDtos() {
    }

    public static class ImportRequestContent {

        public String getCommandType() {
            return "Import";
        }

        /**
         * IncomingShipment Type Id
         */
        private String shipmentTypeId;

        public String getShipmentTypeId() {
            return this.shipmentTypeId;
        }

        public void setShipmentTypeId(String shipmentTypeId) {
            this.shipmentTypeId = shipmentTypeId;
        }

        /**
         * Primary Order Id
         */
        private String primaryOrderId;

        public String getPrimaryOrderId() {
            return this.primaryOrderId;
        }

        public void setPrimaryOrderId(String primaryOrderId) {
            this.primaryOrderId = primaryOrderId;
        }

        /**
         * Primary Return Id
         */
        private String primaryReturnId;

        public String getPrimaryReturnId() {
            return this.primaryReturnId;
        }

        public void setPrimaryReturnId(String primaryReturnId) {
            this.primaryReturnId = primaryReturnId;
        }

        /**
         * Only One Order
         */
        private Boolean onlyOneOrder;

        public Boolean getOnlyOneOrder() {
            return this.onlyOneOrder;
        }

        public void setOnlyOneOrder(Boolean onlyOneOrder) {
            this.onlyOneOrder = onlyOneOrder;
        }

        /**
         * Only One Order Ship Group
         */
        private Boolean onlyOneOrderShipGroup;

        public Boolean getOnlyOneOrderShipGroup() {
            return this.onlyOneOrderShipGroup;
        }

        public void setOnlyOneOrderShipGroup(Boolean onlyOneOrderShipGroup) {
            this.onlyOneOrderShipGroup = onlyOneOrderShipGroup;
        }

        /**
         * Bill of lading
         */
        private String bolNumber;

        public String getBolNumber() {
            return this.bolNumber;
        }

        public void setBolNumber(String bolNumber) {
            this.bolNumber = bolNumber;
        }

        /**
         * Vehicle Id
         */
        private String vehicleId;

        public String getVehicleId() {
            return this.vehicleId;
        }

        public void setVehicleId(String vehicleId) {
            this.vehicleId = vehicleId;
        }

        /**
         * Seal Number
         */
        private String sealNumber;

        public String getSealNumber() {
            return this.sealNumber;
        }

        public void setSealNumber(String sealNumber) {
            this.sealNumber = sealNumber;
        }

        /**
         * External Order Number
         */
        private String externalOrderNumber;

        public String getExternalOrderNumber() {
            return this.externalOrderNumber;
        }

        public void setExternalOrderNumber(String externalOrderNumber) {
            this.externalOrderNumber = externalOrderNumber;
        }

        /**
         * Carrier
         */
        private String carrier;

        public String getCarrier() {
            return this.carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        /**
         * Date Shipped
         */
        private java.sql.Timestamp dateShipped;

        public java.sql.Timestamp getDateShipped() {
            return this.dateShipped;
        }

        public void setDateShipped(java.sql.Timestamp dateShipped) {
            this.dateShipped = dateShipped;
        }

        /**
         * Estimated Ready Date
         */
        private java.sql.Timestamp estimatedReadyDate;

        public java.sql.Timestamp getEstimatedReadyDate() {
            return this.estimatedReadyDate;
        }

        public void setEstimatedReadyDate(java.sql.Timestamp estimatedReadyDate) {
            this.estimatedReadyDate = estimatedReadyDate;
        }

        /**
         * Estimated Ship Date
         */
        private java.sql.Timestamp estimatedShipDate;

        public java.sql.Timestamp getEstimatedShipDate() {
            return this.estimatedShipDate;
        }

        public void setEstimatedShipDate(java.sql.Timestamp estimatedShipDate) {
            this.estimatedShipDate = estimatedShipDate;
        }

        /**
         * Estimated Arrival Date
         */
        private java.sql.Timestamp estimatedArrivalDate;

        public java.sql.Timestamp getEstimatedArrivalDate() {
            return this.estimatedArrivalDate;
        }

        public void setEstimatedArrivalDate(java.sql.Timestamp estimatedArrivalDate) {
            this.estimatedArrivalDate = estimatedArrivalDate;
        }

        /**
         * Latest Cancel Date
         */
        private java.sql.Timestamp latestCancelDate;

        public java.sql.Timestamp getLatestCancelDate() {
            return this.latestCancelDate;
        }

        public void setLatestCancelDate(java.sql.Timestamp latestCancelDate) {
            this.latestCancelDate = latestCancelDate;
        }

        /**
         * Estimated Ship Cost
         */
        private java.math.BigDecimal estimatedShipCost;

        public java.math.BigDecimal getEstimatedShipCost() {
            return this.estimatedShipCost;
        }

        public void setEstimatedShipCost(java.math.BigDecimal estimatedShipCost) {
            this.estimatedShipCost = estimatedShipCost;
        }

        /**
         * Currency Uom Id
         */
        private String currencyUomId;

        public String getCurrencyUomId() {
            return this.currencyUomId;
        }

        public void setCurrencyUomId(String currencyUomId) {
            this.currencyUomId = currencyUomId;
        }

        /**
         * Handling Instructions
         */
        private String handlingInstructions;

        public String getHandlingInstructions() {
            return this.handlingInstructions;
        }

        public void setHandlingInstructions(String handlingInstructions) {
            this.handlingInstructions = handlingInstructions;
        }

        /**
         * Origin Facility Id
         */
        private String originFacilityId;

        public String getOriginFacilityId() {
            return this.originFacilityId;
        }

        public void setOriginFacilityId(String originFacilityId) {
            this.originFacilityId = originFacilityId;
        }

        /**
         * Destination Facility Id
         */
        private String destinationFacilityId;

        public String getDestinationFacilityId() {
            return this.destinationFacilityId;
        }

        public void setDestinationFacilityId(String destinationFacilityId) {
            this.destinationFacilityId = destinationFacilityId;
        }

        /**
         * Party Id To
         */
        private String partyIdTo;

        public String getPartyIdTo() {
            return this.partyIdTo;
        }

        public void setPartyIdTo(String partyIdTo) {
            this.partyIdTo = partyIdTo;
        }

        /**
         * Party Id From
         */
        private String partyIdFrom;

        public String getPartyIdFrom() {
            return this.partyIdFrom;
        }

        public void setPartyIdFrom(String partyIdFrom) {
            this.partyIdFrom = partyIdFrom;
        }

        /**
         * Additional Shipping Charge
         */
        private java.math.BigDecimal additionalShippingCharge;

        public java.math.BigDecimal getAdditionalShippingCharge() {
            return this.additionalShippingCharge;
        }

        public void setAdditionalShippingCharge(java.math.BigDecimal additionalShippingCharge) {
            this.additionalShippingCharge = additionalShippingCharge;
        }

        /**
         * Addtl Shipping Charge Desc
         */
        private String addtlShippingChargeDesc;

        public String getAddtlShippingChargeDesc() {
            return this.addtlShippingChargeDesc;
        }

        public void setAddtlShippingChargeDesc(String addtlShippingChargeDesc) {
            this.addtlShippingChargeDesc = addtlShippingChargeDesc;
        }

        /**
         * IncomingShipment Items
         */
        private ImportingShipmentItem[] shipmentItems;

        public ImportingShipmentItem[] getShipmentItems() {
            return this.shipmentItems;
        }

        public void setShipmentItems(ImportingShipmentItem[] shipmentItems) {
            this.shipmentItems = shipmentItems;
        }

        /**
         * IncomingShipment Id
         */
        private String shipmentId;

        public String getShipmentId() {
            return this.shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

    public static class ReceiveItemRequestContent {

        public String getCommandType() {
            return "ReceiveItem";
        }

        /**
         * Receipt Seq Id
         */
        private String receiptSeqId;

        public String getReceiptSeqId() {
            return this.receiptSeqId;
        }

        public void setReceiptSeqId(String receiptSeqId) {
            this.receiptSeqId = receiptSeqId;
        }

        /**
         * IncomingShipment Item Seq Id
         */
        private String shipmentItemSeqId;

        public String getShipmentItemSeqId() {
            return this.shipmentItemSeqId;
        }

        public void setShipmentItemSeqId(String shipmentItemSeqId) {
            this.shipmentItemSeqId = shipmentItemSeqId;
        }

        /**
         * Order Id
         */
        private String orderId;

        public String getOrderId() {
            return this.orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        /**
         * Order Item Seq Id
         */
        private String orderItemSeqId;

        public String getOrderItemSeqId() {
            return this.orderItemSeqId;
        }

        public void setOrderItemSeqId(String orderItemSeqId) {
            this.orderItemSeqId = orderItemSeqId;
        }

        /**
         * Locator Id
         */
        private String locatorId;

        public String getLocatorId() {
            return this.locatorId;
        }

        public void setLocatorId(String locatorId) {
            this.locatorId = locatorId;
        }

        /**
         * Attribute Set Instance
         */
        private Map<String, Object> attributeSetInstance;

        public Map<String, Object> getAttributeSetInstance() {
            return this.attributeSetInstance;
        }

        public void setAttributeSetInstance(Map<String, Object> attributeSetInstance) {
            this.attributeSetInstance = attributeSetInstance;
        }

        /**
         * Rejection Reason Id
         */
        private String rejectionReasonId;

        public String getRejectionReasonId() {
            return this.rejectionReasonId;
        }

        public void setRejectionReasonId(String rejectionReasonId) {
            this.rejectionReasonId = rejectionReasonId;
        }

        /**
         * Damage Status Ids
         */
        private String[] damageStatusIds;

        public String[] getDamageStatusIds() {
            return this.damageStatusIds;
        }

        public void setDamageStatusIds(String[] damageStatusIds) {
            this.damageStatusIds = damageStatusIds;
        }

        /**
         * Damage Reason Id
         */
        private String damageReasonId;

        public String getDamageReasonId() {
            return this.damageReasonId;
        }

        public void setDamageReasonId(String damageReasonId) {
            this.damageReasonId = damageReasonId;
        }

        /**
         * Accepted Quantity
         */
        private java.math.BigDecimal acceptedQuantity;

        public java.math.BigDecimal getAcceptedQuantity() {
            return this.acceptedQuantity;
        }

        public void setAcceptedQuantity(java.math.BigDecimal acceptedQuantity) {
            this.acceptedQuantity = acceptedQuantity;
        }

        /**
         * Rejected Quantity
         */
        private java.math.BigDecimal rejectedQuantity;

        public java.math.BigDecimal getRejectedQuantity() {
            return this.rejectedQuantity;
        }

        public void setRejectedQuantity(java.math.BigDecimal rejectedQuantity) {
            this.rejectedQuantity = rejectedQuantity;
        }

        /**
         * Damaged Quantity
         */
        private java.math.BigDecimal damagedQuantity;

        public java.math.BigDecimal getDamagedQuantity() {
            return this.damagedQuantity;
        }

        public void setDamagedQuantity(java.math.BigDecimal damagedQuantity) {
            this.damagedQuantity = damagedQuantity;
        }

        /**
         * Item Description
         */
        private String itemDescription;

        public String getItemDescription() {
            return this.itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        /**
         * IncomingShipment Id
         */
        private String shipmentId;

        public String getShipmentId() {
            return this.shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

    public static class IssueItemRequestContent {

        public String getCommandType() {
            return "IssueItem";
        }

        private BigDecimal f_D_1_;

        public BigDecimal getF_D_1_() {
            return f_D_1_;
        }

        public void setF_D_1_(BigDecimal f_D_1_) {
            this.f_D_1_ = f_D_1_;
        }

        /**
         * Item Issuance Seq Id
         */
        private String itemIssuanceSeqId;

        public String getItemIssuanceSeqId() {
            return this.itemIssuanceSeqId;
        }

        public void setItemIssuanceSeqId(String itemIssuanceSeqId) {
            this.itemIssuanceSeqId = itemIssuanceSeqId;
        }

        /**
         * IncomingShipment Item Seq Id
         */
        private String shipmentItemSeqId;

        public String getShipmentItemSeqId() {
            return this.shipmentItemSeqId;
        }

        public void setShipmentItemSeqId(String shipmentItemSeqId) {
            this.shipmentItemSeqId = shipmentItemSeqId;
        }

        /**
         * Order Id
         */
        private String orderId;

        public String getOrderId() {
            return this.orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        /**
         * Order Item Seq Id
         */
        private String orderItemSeqId;

        public String getOrderItemSeqId() {
            return this.orderItemSeqId;
        }

        public void setOrderItemSeqId(String orderItemSeqId) {
            this.orderItemSeqId = orderItemSeqId;
        }

        /**
         * Ship Group Seq Id
         */
        private String shipGroupSeqId;

        public String getShipGroupSeqId() {
            return this.shipGroupSeqId;
        }

        public void setShipGroupSeqId(String shipGroupSeqId) {
            this.shipGroupSeqId = shipGroupSeqId;
        }

        /**
         * Product Id
         */
        private String productId;

        public String getProductId() {
            return this.productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        /**
         * Locator Id
         */
        private String locatorId;

        public String getLocatorId() {
            return this.locatorId;
        }

        public void setLocatorId(String locatorId) {
            this.locatorId = locatorId;
        }

        /**
         * Attribute Set Instance
         */
        private Map<String, Object> attributeSetInstance;

        public Map<String, Object> getAttributeSetInstance() {
            return this.attributeSetInstance;
        }

        public void setAttributeSetInstance(Map<String, Object> attributeSetInstance) {
            this.attributeSetInstance = attributeSetInstance;
        }

        /**
         * Quantity
         */
        private java.math.BigDecimal quantity;

        public java.math.BigDecimal getQuantity() {
            return this.quantity;
        }

        public void setQuantity(java.math.BigDecimal quantity) {
            this.quantity = quantity;
        }

        /**
         * Cancel Quantity
         */
        private java.math.BigDecimal cancelQuantity;

        public java.math.BigDecimal getCancelQuantity() {
            return this.cancelQuantity;
        }

        public void setCancelQuantity(java.math.BigDecimal cancelQuantity) {
            this.cancelQuantity = cancelQuantity;
        }

        /**
         * Item Description
         */
        private String itemDescription;

        public String getItemDescription() {
            return this.itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        /**
         * IncomingShipment Id
         */
        private String shipmentId;

        public String getShipmentId() {
            return this.shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

    public static class ConfirmAllItemsReceivedRequestContent {

        public String getCommandType() {
            return "ConfirmAllItemsReceived";
        }

        /**
         * Destination Locator Id
         */
        private String destinationLocatorId;

        public String getDestinationLocatorId() {
            return this.destinationLocatorId;
        }

        public void setDestinationLocatorId(String destinationLocatorId) {
            this.destinationLocatorId = destinationLocatorId;
        }

        /**
         * IncomingShipment Id
         */
        private String shipmentId;

        public String getShipmentId() {
            return this.shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

    public static class ConfirmAllItemsIssuedRequestContent {

        public String getCommandType() {
            return "ConfirmAllItemsIssued";
        }

        /**
         * IncomingShipment Id
         */
        private String shipmentId;

        public String getShipmentId() {
            return this.shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

    public static class PurchaseShipmentActionRequestContent {

        private String commandType = "PurchaseShipmentAction";

        public String getCommandType() {
            return "PurchaseShipmentAction";
        }

        /**
         * Value
         */
        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * IncomingShipment Id
         */
        private String shipmentId;

        public String getShipmentId() {
            return this.shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

    public static class SalesShipmentActionRequestContent {

        public String getCommandType() {
            return "SalesShipmentAction";
        }

        /**
         * Value
         */
        private String value;

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        /**
         * IncomingShipment Id
         */
        private String shipmentId;

        public String getShipmentId() {
            return this.shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        /**
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

        /**
         * Command Id
         */
        private String commandId;

        public String getCommandId() {
            return this.commandId;
        }

        public void setCommandId(String commandId) {
            this.commandId = commandId;
        }

        /**
         * Requester Id
         */
        private String requesterId;

        public String getRequesterId() {
            return this.requesterId;
        }

        public void setRequesterId(String requesterId) {
            this.requesterId = requesterId;
        }

    }

}

