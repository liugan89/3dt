package com.takumi.wms.model;

public enum Function {
    In("其他入库", "In", false),
    Out("其他出库", "Out", true),
    Movement("移动库存", "Movement", true),
    SwitchWarehouse("切换仓库", "SwitchWarehouse", false),
    PhysicalInventory("盘点", "PhysicalInventory", false),
    IncomingShipment("入库", "IncomingShipment", false),
    OutgoingShipment("出库","OutgoingShipment", true),
    InventoryItemQuery("盘点查询","InventoryItemQuery", false),
    OutNotice("出库通知单","OutNotice", false);


    private final String name;
    private final String value;
    private final boolean isOut;

    Function(String name, String value, boolean isOut) {
        this.name = name;
        this.value = value;
        this.isOut = isOut;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public boolean isOut(){
        return isOut;
    }
}