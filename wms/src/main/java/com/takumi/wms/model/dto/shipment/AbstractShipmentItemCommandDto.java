package com.takumi.wms.model.dto.shipment;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractShipmentItemCommandDto extends AbstractCommand {
    /**
     * IncomingShipment Item Seq Id
     */
    private String shipmentItemSeqId;

    public String getShipmentItemSeqId() {
        return this.shipmentItemSeqId;
    }

    public void setShipmentItemSeqId(String shipmentItemSeqId) {
        this.shipmentItemSeqId = shipmentItemSeqId;
    }


}
