package com.takumi.wms.model.dto.shipment;


import com.takumi.wms.model.dto.AbstractCommand;

public abstract class AbstractShipmentCommandDto extends AbstractCommand
{
    /**
     * IncomingShipment Id
     */
    private String shipmentId;

    public String getShipmentId()
    {
        return this.shipmentId;
    }

    public void setShipmentId(String shipmentId)
    {
        this.shipmentId = shipmentId;
    }

    /**
     * Version
     */
    private Long version;

    public Long getVersion()
    {
        return this.version;
    }

    public void setVersion(Long version)
    {
        this.version = version;
    }


}
