package com.haphest.a3dtracking.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * 仓库
 */
public class Warehouse {

    private String warehouseId;
    private String warehouseName;
    private String description;
    private boolean isInTransit;
    private boolean active;
    private long version;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String facilityId;
    private String facilityTypeId;
    private String ownerPartyId;
    private String facilityName;
    private String facilityTypeClass;


    public Warehouse() {
    }

    private static Warehouse house = new Warehouse();

    public static Warehouse getHouse() {
        return house;
    }

    public static void setHouse(Warehouse house1) {
        house = house1;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInTransit() {
        return isInTransit;
    }

    public void setInTransit(boolean inTransit) {
        isInTransit = inTransit;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getFacilityTypeId() {
        return facilityTypeId;
    }

    public void setFacilityTypeId(String facilityTypeId) {
        this.facilityTypeId = facilityTypeId;
    }

    public String getOwnerPartyId() {
        return ownerPartyId;
    }

    public void setOwnerPartyId(String ownerPartyId) {
        this.ownerPartyId = ownerPartyId;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getFacilityTypeClass() {
        return facilityTypeClass;
    }

    public void setFacilityTypeClass(String facilityTypeClass) {
        this.facilityTypeClass = facilityTypeClass;
    }

}
