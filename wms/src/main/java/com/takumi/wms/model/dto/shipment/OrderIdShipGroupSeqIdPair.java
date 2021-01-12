package com.takumi.wms.model.dto.shipment;

import java.io.Serializable;

public class OrderIdShipGroupSeqIdPair implements Serializable
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

    public OrderIdShipGroupSeqIdPair()
    {
    }

    public OrderIdShipGroupSeqIdPair(String orderId, String shipGroupSeqId)
    {
        this.orderId = orderId;
        this.shipGroupSeqId = shipGroupSeqId;
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

        OrderIdShipGroupSeqIdPair other = (OrderIdShipGroupSeqIdPair)obj;
        return true 
            && (orderId == other.orderId || (orderId != null && orderId.equals(other.orderId)))
            && (shipGroupSeqId == other.shipGroupSeqId || (shipGroupSeqId != null && shipGroupSeqId.equals(other.shipGroupSeqId)))
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
        return hash;
    }



}

