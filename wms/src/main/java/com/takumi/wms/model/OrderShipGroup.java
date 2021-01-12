package com.takumi.wms.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderShipGroup {

    @SerializedName(value = "id", alternate = {"shipGroupSeqId"})
    private String id;
    private String contractNo;
    private String shipmentId;
    @SerializedName(value = "orderShipGroupLine", alternate = {"noticeInformationLine"})
    private List<OrderShipGroupLineBean> orderShipGroupLine;
    private String name;
    private String phoneNo;
    private String plateNumber;
    private String instructions;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public List<OrderShipGroupLineBean> getOrderShipGroupLine() {
        return orderShipGroupLine;
    }

    public void setOrderShipGroupLine(List<OrderShipGroupLineBean> orderShipGroupLine) {
        this.orderShipGroupLine = orderShipGroupLine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public static class OrderShipGroupLineBean {

        private String orderItemSeqId;
        private String productId;
        private String orderId;
        private Double planQuantity;
        private Double quantity;


        public String getOrderItemSeqId() {
            return orderItemSeqId;
        }

        public void setOrderItemSeqId(String orderItemSeqId) {
            this.orderItemSeqId = orderItemSeqId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public Double getPlanQuantity() {
            return planQuantity;
        }

        public void setPlanQuantity(Double planQuantity) {
            this.planQuantity = planQuantity;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }
    }
}
