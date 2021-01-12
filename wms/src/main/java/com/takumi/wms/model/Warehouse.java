package com.takumi.wms.model;

import java.util.Date;

/**
 * 仓库
 */
public class Warehouse {
    /**
     * warehouseId : TEST
     * warehouseName : null
     * description : null
     * isInTransit : null
     * active : true
     * version : 0
     * createdBy : 111111
     * createdAt : 1531478927000
     * updatedBy : null
     * updatedAt : null
     */

    private String warehouseId;
    private String warehouseName;
    private String longitude;
    private String latitude;
    private int scope;
    private boolean limit;


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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public boolean isLimit() {
        return limit;
    }

    public void setLimit(boolean limit) {
        this.limit = limit;
    }
}
