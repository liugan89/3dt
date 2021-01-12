package com.takumi.wms.model.dto.production;

import com.takumi.wms.model.dto.AbstractCommand;

import java.util.*;

public class ProductionCommandDtos
{
    private ProductionCommandDtos() {
    }

    public static class AddProductionLineInputRequestContent extends AbstractCommand {

        public String getCommandType() {
            return "AddProductionLineInput";
        }

        /**
         * Production Id
         */
        private String productionId;

        public String getProductionId() {
            return this.productionId;
        }

        public void setProductionId(String productionId) {
            this.productionId = productionId;
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
         * Production Line Input Seq Id
         */
        private String productionLineInputSeqId;

        public String getProductionLineInputSeqId() {
            return this.productionLineInputSeqId;
        }

        public void setProductionLineInputSeqId(String productionLineInputSeqId) {
            this.productionLineInputSeqId = productionLineInputSeqId;
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
         * Actual Quantity
         */
        private java.math.BigDecimal actualQuantity;

        public java.math.BigDecimal getActualQuantity() {
            return this.actualQuantity;
        }

        public void setActualQuantity(java.math.BigDecimal actualQuantity) {
            this.actualQuantity = actualQuantity;
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
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

    }

    public static class AddProductionLineOutputRequestContent extends AbstractCommand {

        public String getCommandType() {
            return "AddProductionLineOutput";
        }

        /**
         * Production Id
         */
        private String productionId;

        public String getProductionId() {
            return this.productionId;
        }

        public void setProductionId(String productionId) {
            this.productionId = productionId;
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
         * Production Line Output Seq Id
         */
        private String productionLineOutputSeqId;

        public String getProductionLineOutputSeqId() {
            return this.productionLineOutputSeqId;
        }

        public void setProductionLineOutputSeqId(String productionLineOutputSeqId) {
            this.productionLineOutputSeqId = productionLineOutputSeqId;
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
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

    }

    public static class DocumentActionRequestContent extends AbstractCommand {

        public String getCommandType() {
            return "DocumentAction";
        }

        /**
         * Production Id
         */
        private String productionId;

        public String getProductionId() {
            return this.productionId;
        }

        public void setProductionId(String productionId) {
            this.productionId = productionId;
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
         * Version
         */
        private Long version;

        public Long getVersion() {
            return this.version;
        }

        public void setVersion(Long version) {
            this.version = version;
        }

    }

}

