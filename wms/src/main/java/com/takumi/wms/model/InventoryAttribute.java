package com.takumi.wms.model;

import java.util.Date;
import java.util.List;

public class InventoryAttribute {

    private String attributeSetId;
    private int version;
    private String attributeSetName;
    private String organizationId;
    private String description;
    private String referenceId;
    private String createdBy;
    private String updatedBy;
    private boolean active;
    private boolean deleted;
    private boolean mandatory;
    private boolean instanceAttributeSet;
    private Date createdAt;
    private Date updatedAt;
    private List<AttributesItem> attributes;

    public String getAttributeSetId() {
        return attributeSetId;
    }

    public void setAttributeSetId(String attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getAttributeSetName() {
        return attributeSetName;
    }

    public void setAttributeSetName(String attributeSetName) {
        this.attributeSetName = attributeSetName;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isInstanceAttributeSet() {
        return instanceAttributeSet;
    }

    public void setInstanceAttributeSet(boolean instanceAttributeSet) {
        this.instanceAttributeSet = instanceAttributeSet;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<AttributesItem> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributesItem> attributes) {
        this.attributes = attributes;
    }

    public static class AttributesItem {

        private String attributeId;
        private String attributeName;
        private String description;
        private String attributeValueType;
        private int attributeValueLength;
        private String fieldName;
        private boolean list;
        private List attributeValues;
        private String value;
        private Boolean mandatory;

        public Boolean getMandatory() {
            return mandatory;
        }

        public void setMandatory(Boolean mandatory) {
            this.mandatory = mandatory;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAttributeValueType() {
            return attributeValueType;
        }

        public void setAttributeValueType(String attributeValueType) {
            this.attributeValueType = attributeValueType;
        }

        public int getAttributeValueLength() {
            return attributeValueLength;
        }

        public void setAttributeValueLength(int attributeValueLength) {
            this.attributeValueLength = attributeValueLength;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public boolean isList() {
            return list;
        }

        public void setList(boolean list) {
            this.list = list;
        }

        public List getAttributeValues() {
            return attributeValues;
        }

        public void setAttributeValues(List attributeValues) {
            this.attributeValues = attributeValues;
        }
    }
}
