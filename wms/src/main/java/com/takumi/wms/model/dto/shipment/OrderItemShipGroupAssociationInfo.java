package com.takumi.wms.model.dto.shipment;

import java.io.Serializable;

public class OrderItemShipGroupAssociationInfo implements Serializable
{
    private String orderId;

    public String getOrderId()
    {
        return this.orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    private String shipGroupSeqId;

    public String getShipGroupSeqId()
    {
        return this.shipGroupSeqId;
    }

    public void setShipGroupSeqId(String shipGroupSeqId)
    {
        this.shipGroupSeqId = shipGroupSeqId;
    }

    private String orderItemSeqId;

    public String getOrderItemSeqId()
    {
        return this.orderItemSeqId;
    }

    public void setOrderItemSeqId(String orderItemSeqId)
    {
        this.orderItemSeqId = orderItemSeqId;
    }

    private String productId;

    public String getProductId()
    {
        return this.productId;
    }

    public void setProductId(String productId)
    {
        this.productId = productId;
    }

    private java.math.BigDecimal quantity;

    public java.math.BigDecimal getQuantity()
    {
        return this.quantity;
    }

    public void setQuantity(java.math.BigDecimal quantity)
    {
        this.quantity = quantity;
    }

    private Integer numberOfPackages;

    public Integer getNumberOfPackages()
    {
        return this.numberOfPackages;
    }

    public void setNumberOfPackages(Integer numberOfPackages)
    {
        this.numberOfPackages = numberOfPackages;
    }

    private Integer numberOfContainers;

    public Integer getNumberOfContainers()
    {
        return this.numberOfContainers;
    }

    public void setNumberOfContainers(Integer numberOfContainers)
    {
        this.numberOfContainers = numberOfContainers;
    }

    private Integer numberOfPakagesPerContainer;

    public Integer getNumberOfPakagesPerContainer()
    {
        return this.numberOfPakagesPerContainer;
    }

    public void setNumberOfPakagesPerContainer(Integer numberOfPakagesPerContainer)
    {
        this.numberOfPakagesPerContainer = numberOfPakagesPerContainer;
    }

    public OrderItemShipGroupAssociationInfo()
    {
    }

    public OrderItemShipGroupAssociationInfo(String orderId, String shipGroupSeqId, String orderItemSeqId, String productId, java.math.BigDecimal quantity, Integer numberOfPackages, Integer numberOfContainers, Integer numberOfPakagesPerContainer)
    {
        this.orderId = orderId;
        this.shipGroupSeqId = shipGroupSeqId;
        this.orderItemSeqId = orderItemSeqId;
        this.productId = productId;
        this.quantity = quantity;
        this.numberOfPackages = numberOfPackages;
        this.numberOfContainers = numberOfContainers;
        this.numberOfPakagesPerContainer = numberOfPakagesPerContainer;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        OrderItemShipGroupAssociationInfo other = (OrderItemShipGroupAssociationInfo)obj;
        return true 
            && (orderId == other.orderId || (orderId != null && orderId.equals(other.orderId)))
            && (shipGroupSeqId == other.shipGroupSeqId || (shipGroupSeqId != null && shipGroupSeqId.equals(other.shipGroupSeqId)))
            && (orderItemSeqId == other.orderItemSeqId || (orderItemSeqId != null && orderItemSeqId.equals(other.orderItemSeqId)))
            && (productId == other.productId || (productId != null && productId.equals(other.productId)))
            && (quantity == other.quantity || (quantity != null && quantity.equals(other.quantity)))
            && (numberOfPackages == other.numberOfPackages || (numberOfPackages != null && numberOfPackages.equals(other.numberOfPackages)))
            && (numberOfContainers == other.numberOfContainers || (numberOfContainers != null && numberOfContainers.equals(other.numberOfContainers)))
            && (numberOfPakagesPerContainer == other.numberOfPakagesPerContainer || (numberOfPakagesPerContainer != null && numberOfPakagesPerContainer.equals(other.numberOfPakagesPerContainer)))
            ;
    }

    @Override
    public int hashCode()
    {
        int hash = 0;
        if (this.orderId != null) {
            hash += 13 * this.orderId.hashCode();
        }
        if (this.shipGroupSeqId != null) {
            hash += 13 * this.shipGroupSeqId.hashCode();
        }
        if (this.orderItemSeqId != null) {
            hash += 13 * this.orderItemSeqId.hashCode();
        }
        if (this.productId != null) {
            hash += 13 * this.productId.hashCode();
        }
        if (this.quantity != null) {
            hash += 13 * this.quantity.hashCode();
        }
        if (this.numberOfPackages != null) {
            hash += 13 * this.numberOfPackages.hashCode();
        }
        if (this.numberOfContainers != null) {
            hash += 13 * this.numberOfContainers.hashCode();
        }
        if (this.numberOfPakagesPerContainer != null) {
            hash += 13 * this.numberOfPakagesPerContainer.hashCode();
        }
        return hash;
    }



}

