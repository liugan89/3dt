package com.takumi.wms.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class OutInventoryAtt {


    private String productId;
    private String locatorId;
    private double onHandQuantity;
    private double count;
    private String attributeSetInstanceId;
    private LinkedHashMap<String, Object> attributes;
    private List<StatusItem> damageStatuss = new ArrayList<>();


    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public double getOnHandQuantity() {
        return onHandQuantity;
    }

    public void setOnHandQuantity(double onHandQuantity) {
        this.onHandQuantity = onHandQuantity;
    }

    public double getCount() {
        return count;
    }

    public void setCount(double count) {
        this.count = count;
    }

    public String getAttributeSetInstanceId() {
        return attributeSetInstanceId;
    }

    public void setAttributeSetInstanceId(String attributeSetInstanceId) {
        this.attributeSetInstanceId = attributeSetInstanceId;
    }

    public LinkedHashMap<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(LinkedHashMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    public List<StatusItem> getDamageStatuss() {
        return damageStatuss;
    }

    public void setDamageStatuss(List<StatusItem> damageStatuss) {
        this.damageStatuss = damageStatuss;
    }
}
