package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Lot implements Parcelable {


    private String lotId;
    private double quantity;
    private Date expirationDate;
    private Boolean active;
    private Integer version;
    private String commandId;

    public Lot() {
    }

    protected Lot(Parcel in) {
        lotId = in.readString();
        quantity = in.readDouble();
        expirationDate = (Date) in.readSerializable();
        byte tmpActive = in.readByte();
        active = tmpActive == 0 ? null : tmpActive == 1;
        if (in.readByte() == 0) {
            version = null;
        } else {
            version = in.readInt();
        }
        commandId = in.readString();
    }

    public static final Creator<Lot> CREATOR = new Creator<Lot>() {
        @Override
        public Lot createFromParcel(Parcel in) {
            return new Lot(in);
        }

        @Override
        public Lot[] newArray(int size) {
            return new Lot[size];
        }
    };

    public String getCommandId() {
        return this.commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public String getLotId() {
        return lotId;
    }

    public void setLotId(String lotId) {
        this.lotId = lotId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(lotId);
        dest.writeDouble(quantity);
        dest.writeSerializable(expirationDate);
        dest.writeByte((byte) (active == null ? 0 : active ? 1 : 2));
        if (version == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(version);
        }
        dest.writeString(commandId);
    }
}
