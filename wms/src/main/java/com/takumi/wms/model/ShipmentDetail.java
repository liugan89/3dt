package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

public class ShipmentDetail implements Parcelable {


    private String shipmentId;
    private String shipmentTypeId;
    private String statusId;
    private String destinationFacilityId;
    private int version;
    private Date createdAt;
    private Date updatedAt;
    private String primaryOrderId;
    private String primaryShipGroupSeqId;
    private String vehicleId;
    private List<OssImage> shipmentImages;
    private List<ShipmentItem> shipmentItems;
    private List<ShipmentReceipt> shipmentReceipts;

    public ShipmentDetail() {
    }

    protected ShipmentDetail(Parcel in) {
        shipmentId = in.readString();
        shipmentTypeId = in.readString();
        statusId = in.readString();
        destinationFacilityId = in.readString();
        version = in.readInt();
        createdAt = (Date) in.readSerializable();
        updatedAt = (Date) in.readSerializable();
        primaryOrderId = in.readString();
        primaryShipGroupSeqId = in.readString();
        vehicleId = in.readString();
        shipmentImages = in.createTypedArrayList(OssImage.CREATOR);
        shipmentItems = in.createTypedArrayList(ShipmentItem.CREATOR);
        shipmentReceipts = in.createTypedArrayList(ShipmentReceipt.CREATOR);
    }

    public static final Creator<ShipmentDetail> CREATOR = new Creator<ShipmentDetail>() {
        @Override
        public ShipmentDetail createFromParcel(Parcel in) {
            return new ShipmentDetail(in);
        }

        @Override
        public ShipmentDetail[] newArray(int size) {
            return new ShipmentDetail[size];
        }
    };

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentTypeId() {
        return shipmentTypeId;
    }

    public void setShipmentTypeId(String shipmentTypeId) {
        this.shipmentTypeId = shipmentTypeId;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getDestinationFacilityId() {
        return destinationFacilityId;
    }

    public void setDestinationFacilityId(String destinationFacilityId) {
        this.destinationFacilityId = destinationFacilityId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPrimaryOrderId() {
        return primaryOrderId;
    }

    public String getPrimaryShipGroupSeqId() {
        return primaryShipGroupSeqId;
    }

    public void setPrimaryShipGroupSeqId(String primaryShipGroupSeqId) {
        this.primaryShipGroupSeqId = primaryShipGroupSeqId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setPrimaryOrderId(String primaryOrderId) {
        this.primaryOrderId = primaryOrderId;
    }

    public List<OssImage> getShipmentImages() {
        return shipmentImages;
    }

    public void setShipmentImages(List<OssImage> shipmentImages) {
        this.shipmentImages = shipmentImages;
    }

    public List<ShipmentItem> getShipmentItems() {
        return shipmentItems;
    }

    public void setShipmentItems(List<ShipmentItem> shipmentItems) {
        this.shipmentItems = shipmentItems;
    }

    public List<ShipmentReceipt> getShipmentReceipts() {
        return shipmentReceipts;
    }

    public void setShipmentReceipts(List<ShipmentReceipt> shipmentReceipts) {
        this.shipmentReceipts = shipmentReceipts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shipmentId);
        dest.writeString(shipmentTypeId);
        dest.writeString(statusId);
        dest.writeString(destinationFacilityId);
        dest.writeInt(version);
        dest.writeSerializable(createdAt);
        dest.writeSerializable(updatedAt);
        dest.writeString(primaryOrderId);
        dest.writeString(primaryShipGroupSeqId);
        dest.writeString(vehicleId);
        dest.writeTypedList(shipmentImages);
        dest.writeTypedList(shipmentItems);
        dest.writeTypedList(shipmentReceipts);
    }

    public static class ShipmentReceipt implements Parcelable {

        private String receiptSeqId;
        private String productId;
        private String attributeSetInstanceId;
        private String shipmentItemSeqId;
        private double acceptedQuantity;
        private double rejectedQuantity;
        private double damagedQuantity;
        private int version;
        private String shipmentId;
        private Date createdAt;
        private List<OssImage> shipmentReceiptImages;

        protected ShipmentReceipt(Parcel in) {
            receiptSeqId = in.readString();
            productId = in.readString();
            attributeSetInstanceId = in.readString();
            shipmentItemSeqId = in.readString();
            acceptedQuantity = in.readDouble();
            rejectedQuantity = in.readDouble();
            damagedQuantity = in.readDouble();
            version = in.readInt();
            shipmentId = in.readString();
            createdAt = (Date) in.readSerializable();
            shipmentReceiptImages = in.createTypedArrayList(OssImage.CREATOR);
        }

        public static final Creator<ShipmentReceipt> CREATOR = new Creator<ShipmentReceipt>() {
            @Override
            public ShipmentReceipt createFromParcel(Parcel in) {
                return new ShipmentReceipt(in);
            }

            @Override
            public ShipmentReceipt[] newArray(int size) {
                return new ShipmentReceipt[size];
            }
        };

        public String getReceiptSeqId() {
            return receiptSeqId;
        }

        public void setReceiptSeqId(String receiptSeqId) {
            this.receiptSeqId = receiptSeqId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getAttributeSetInstanceId() {
            return attributeSetInstanceId;
        }

        public void setAttributeSetInstanceId(String attributeSetInstanceId) {
            this.attributeSetInstanceId = attributeSetInstanceId;
        }

        public String getShipmentItemSeqId() {
            return shipmentItemSeqId;
        }

        public void setShipmentItemSeqId(String shipmentItemSeqId) {
            this.shipmentItemSeqId = shipmentItemSeqId;
        }

        public double getAcceptedQuantity() {
            return acceptedQuantity;
        }

        public void setAcceptedQuantity(double acceptedQuantity) {
            this.acceptedQuantity = acceptedQuantity;
        }

        public double getRejectedQuantity() {
            return rejectedQuantity;
        }

        public void setRejectedQuantity(double rejectedQuantity) {
            this.rejectedQuantity = rejectedQuantity;
        }

        public double getDamagedQuantity() {
            return damagedQuantity;
        }

        public void setDamagedQuantity(double damagedQuantity) {
            this.damagedQuantity = damagedQuantity;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getShipmentId() {
            return shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }

        public List<OssImage> getShipmentReceiptImages() {
            return shipmentReceiptImages;
        }

        public void setShipmentReceiptImages(List<OssImage> shipmentReceiptImages) {
            this.shipmentReceiptImages = shipmentReceiptImages;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(receiptSeqId);
            dest.writeString(productId);
            dest.writeString(attributeSetInstanceId);
            dest.writeString(shipmentItemSeqId);
            dest.writeDouble(acceptedQuantity);
            dest.writeDouble(rejectedQuantity);
            dest.writeDouble(damagedQuantity);
            dest.writeInt(version);
            dest.writeString(shipmentId);
            dest.writeSerializable(createdAt);
            dest.writeTypedList(shipmentReceiptImages);
        }
    }

    public static class ShipmentItem implements Parcelable {

        private String shipmentItemSeqId;
        private String productId;
        private String attributeSetInstanceId;
        private double quantity;
        private boolean active;
        private int version;
        private String shipmentId;
        private Date createdAt;

        protected ShipmentItem(Parcel in) {
            shipmentItemSeqId = in.readString();
            productId = in.readString();
            attributeSetInstanceId = in.readString();
            quantity = in.readDouble();
            active = in.readByte() != 0;
            version = in.readInt();
            shipmentId = in.readString();
            createdAt = (Date) in.readSerializable();
        }

        public static final Creator<ShipmentItem> CREATOR = new Creator<ShipmentItem>() {
            @Override
            public ShipmentItem createFromParcel(Parcel in) {
                return new ShipmentItem(in);
            }

            @Override
            public ShipmentItem[] newArray(int size) {
                return new ShipmentItem[size];
            }
        };

        public String getShipmentItemSeqId() {
            return shipmentItemSeqId;
        }

        public void setShipmentItemSeqId(String shipmentItemSeqId) {
            this.shipmentItemSeqId = shipmentItemSeqId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getAttributeSetInstanceId() {
            return attributeSetInstanceId;
        }

        public void setAttributeSetInstanceId(String attributeSetInstanceId) {
            this.attributeSetInstanceId = attributeSetInstanceId;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
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

        public String getShipmentId() {
            return shipmentId;
        }

        public void setShipmentId(String shipmentId) {
            this.shipmentId = shipmentId;
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
            dest.writeString(shipmentItemSeqId);
            dest.writeString(productId);
            dest.writeString(attributeSetInstanceId);
            dest.writeDouble(quantity);
            dest.writeByte((byte) (active ? 1 : 0));
            dest.writeInt(version);
            dest.writeString(shipmentId);
            dest.writeSerializable(createdAt);
        }
    }
}
