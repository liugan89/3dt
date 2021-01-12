package com.takumi.wms.model.dto.movement;

import java.math.BigDecimal;
import java.util.Map;

public class MovementCommandDtos {
    private MovementCommandDtos() {
    }

    public static class AddLineRequestContent {

        public String getCommandType() {
            return "AddLine";
        }

        /**
         * Line Number
         */
        private String lineNumber;

        public String getLineNumber() {
            return this.lineNumber;
        }

        public void setLineNumber(String lineNumber) {
            this.lineNumber = lineNumber;
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
         * Locator Id From
         */
        private String locatorIdFrom;

        public String getLocatorIdFrom() {
            return this.locatorIdFrom;
        }

        public void setLocatorIdFrom(String locatorIdFrom) {
            this.locatorIdFrom = locatorIdFrom;
        }

        /**
         * Locator Id To
         */
        private String locatorIdTo;

        public String getLocatorIdTo() {
            return this.locatorIdTo;
        }

        public void setLocatorIdTo(String locatorIdTo) {
            this.locatorIdTo = locatorIdTo;
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
         * Description
         */
        private String description;

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        /**
         * Quantity Uom Id
         */
        private String quantityUomId;

        public String getQuantityUomId() {
            return this.quantityUomId;
        }

        public void setQuantityUomId(String quantityUomId) {
            this.quantityUomId = quantityUomId;
        }

        /**
         * Movement Quantity
         */
        private BigDecimal movementQuantity;

        public BigDecimal getMovementQuantity() {
            return this.movementQuantity;
        }

        public void setMovementQuantity(BigDecimal movementQuantity) {
            this.movementQuantity = movementQuantity;
        }

        /**
         * Document Number
         */
        private String documentNumber;

        public String getDocumentNumber() {
            return this.documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
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

    public static class DocumentActionRequestContent {

        private String commandType = "DocumentAction";

        public String getCommandType() {
            return "DocumentAction";
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
         * Document Number
         */
        private String documentNumber;

        public String getDocumentNumber() {
            return this.documentNumber;
        }

        public void setDocumentNumber(String documentNumber) {
            this.documentNumber = documentNumber;
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

