package com.takumi.wms.model;

/**
 * 库存单元信息。
 */
public class InventoryItemInfo {
    private String productId;
    /**
     * 行数。
     */
    private int count;
    private double quantity;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
