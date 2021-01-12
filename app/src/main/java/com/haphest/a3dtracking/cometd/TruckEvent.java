package com.haphest.a3dtracking.cometd;

import java.time.ZonedDateTime;
import java.util.Date;

public class TruckEvent {
    public static final String EVENT_TYPE_CLAMPED = "CLAMPED";
    public static final String EVENT_TYPE_RELEASED = "RELEASED";

    private String eventId;
    private String truckId;
    private String eventType;
    private Point3D goodLocation;
    private Date occurredAt;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTruckId() {
        return truckId;
    }

    public void setTruckId(String truckId) {
        this.truckId = truckId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Point3D getGoodLocation() {
        return goodLocation;
    }

    public void setGoodLocation(Point3D goodLocation) {
        this.goodLocation = goodLocation;
    }

    public Date getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Date occurredAt) {
        this.occurredAt = occurredAt;
    }

}
