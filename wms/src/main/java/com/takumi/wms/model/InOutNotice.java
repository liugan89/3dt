package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class InOutNotice implements Parcelable {


    private String inOutNoticeId;
    private String inOutNoticeType;
    private String trackingNumber;
    private String contactPartyId;
    private String estimatedDeliveryDate;
    private String statusId;
    private boolean active;
    private int version;
    private String createdBy;
    private Date createdAt;

    protected InOutNotice(Parcel in) {
        inOutNoticeId = in.readString();
        inOutNoticeType = in.readString();
        trackingNumber = in.readString();
        contactPartyId = in.readString();
        estimatedDeliveryDate = in.readString();
        statusId = in.readString();
        active = in.readByte() != 0;
        version = in.readInt();
        createdBy = in.readString();
        createdAt = (Date) in.readSerializable();
    }

    public static final Creator<InOutNotice> CREATOR = new Creator<InOutNotice>() {
        @Override
        public InOutNotice createFromParcel(Parcel in) {
            return new InOutNotice(in);
        }

        @Override
        public InOutNotice[] newArray(int size) {
            return new InOutNotice[size];
        }
    };

    public String getInOutNoticeId() {
        return inOutNoticeId;
    }

    public void setInOutNoticeId(String inOutNoticeId) {
        this.inOutNoticeId = inOutNoticeId;
    }

    public String getInOutNoticeType() {
        return inOutNoticeType;
    }

    public void setInOutNoticeType(String inOutNoticeType) {
        this.inOutNoticeType = inOutNoticeType;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getContactPartyId() {
        return contactPartyId;
    }

    public void setContactPartyId(String contactPartyId) {
        this.contactPartyId = contactPartyId;
    }

    public String getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(String estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inOutNoticeId);
        dest.writeString(inOutNoticeType);
        dest.writeString(trackingNumber);
        dest.writeString(contactPartyId);
        dest.writeString(estimatedDeliveryDate);
        dest.writeString(statusId);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeInt(version);
        dest.writeString(createdBy);
        dest.writeSerializable(createdAt);
    }
}
