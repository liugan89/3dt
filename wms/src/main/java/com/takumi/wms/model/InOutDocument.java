package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * gson自动根据json生成的实体类，单子
 */
public class InOutDocument implements Parcelable {


    private String documentNumber;
    private String documentStatusId;
    private String posted;
    private String processed;
    private String processing;
    private String documentTypeId;
    private String description;
    private String orderId;
    private String dateOrdered;
    private String isPrinted;
    private String movementTypeId;
    private Date movementDate;
    private String businessPartnerId;
    private String warehouseId;
    private String freightAmount;
    private String shipperId;
    private String chargeAmount;
    private String datePrinted;
    private String createdFrom;
    private String salesRepresentativeId;
    private String numberOfPackages;
    private String pickDate;
    private String shipDate;
    private String trackingNumber;
    private String dateReceived;
    private String isInTransit;
    private String isApproved;
    private String isInDispute;
    private String rmaDocumentNumber;
    private String reversalDocumentNumber;
    private String active;
    private long version;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String poreference;
    private String approvalAmount;
    private String warehouseIdFrom;
    private String warehouseIdTo;
    private List<LinesBean> inOutLines = new ArrayList<>();
    private String productionId;
    private int lineNumber;

    public InOutDocument() {
    }

    protected InOutDocument(Parcel in) {
        documentNumber = in.readString();
        documentStatusId = in.readString();
        posted = in.readString();
        processed = in.readString();
        processing = in.readString();
        documentTypeId = in.readString();
        description = in.readString();
        orderId = in.readString();
        dateOrdered = in.readString();
        isPrinted = in.readString();
        movementTypeId = in.readString();
        movementDate = (Date) in.readSerializable();
        businessPartnerId = in.readString();
        warehouseId = in.readString();
        freightAmount = in.readString();
        shipperId = in.readString();
        chargeAmount = in.readString();
        datePrinted = in.readString();
        createdFrom = in.readString();
        salesRepresentativeId = in.readString();
        numberOfPackages = in.readString();
        pickDate = in.readString();
        shipDate = in.readString();
        trackingNumber = in.readString();
        dateReceived = in.readString();
        isInTransit = in.readString();
        isApproved = in.readString();
        isInDispute = in.readString();
        rmaDocumentNumber = in.readString();
        reversalDocumentNumber = in.readString();
        active = in.readString();
        version = in.readLong();
        createdBy = in.readString();
        createdAt = (Date) in.readSerializable();
        updatedBy = in.readString();
        updatedAt = (Date) in.readSerializable();
        poreference = in.readString();
        approvalAmount = in.readString();
        warehouseIdFrom = in.readString();
        warehouseIdTo = in.readString();
        inOutLines = in.createTypedArrayList(LinesBean.CREATOR);
        productionId = in.readString();
        lineNumber = in.readInt();
    }

    public static final Creator<InOutDocument> CREATOR = new Creator<InOutDocument>() {
        @Override
        public InOutDocument createFromParcel(Parcel in) {
            return new InOutDocument(in);
        }

        @Override
        public InOutDocument[] newArray(int size) {
            return new InOutDocument[size];
        }
    };

    public String getApprovalAmount() {
        return approvalAmount;
    }

    public void setApprovalAmount(String approvalAmount) {
        this.approvalAmount = approvalAmount;
    }

    public String getWarehouseIdFrom() {
        return warehouseIdFrom;
    }

    public void setWarehouseIdFrom(String warehouseIdFrom) {
        this.warehouseIdFrom = warehouseIdFrom;
    }

    public String getWarehouseIdTo() {
        return warehouseIdTo;
    }

    public void setWarehouseIdTo(String warehouseIdTo) {
        this.warehouseIdTo = warehouseIdTo;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentStatusId() {
        return documentStatusId;
    }

    public void setDocumentStatusId(String documentStatusId) {
        this.documentStatusId = documentStatusId;
    }

    public String getPosted() {
        return posted;
    }

    public void setPosted(String posted) {
        this.posted = posted;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

    public String getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(String documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(String dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public String getIsPrinted() {
        return isPrinted;
    }

    public void setIsPrinted(String isPrinted) {
        this.isPrinted = isPrinted;
    }

    public String getMovementTypeId() {
        return movementTypeId;
    }

    public void setMovementTypeId(String movementTypeId) {
        this.movementTypeId = movementTypeId;
    }

    public Date getMovementDate() {
        return movementDate;
    }

    public void setMovementDate(Date movementDate) {
        this.movementDate = movementDate;
    }

    public String getBusinessPartnerId() {
        return businessPartnerId;
    }

    public void setBusinessPartnerId(String businessPartnerId) {
        this.businessPartnerId = businessPartnerId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(String freightAmount) {
        this.freightAmount = freightAmount;
    }

    public String getShipperId() {
        return shipperId;
    }

    public void setShipperId(String shipperId) {
        this.shipperId = shipperId;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getDatePrinted() {
        return datePrinted;
    }

    public void setDatePrinted(String datePrinted) {
        this.datePrinted = datePrinted;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public String getSalesRepresentativeId() {
        return salesRepresentativeId;
    }

    public void setSalesRepresentativeId(String salesRepresentativeId) {
        this.salesRepresentativeId = salesRepresentativeId;
    }

    public String getNumberOfPackages() {
        return numberOfPackages;
    }

    public void setNumberOfPackages(String numberOfPackages) {
        this.numberOfPackages = numberOfPackages;
    }

    public String getPickDate() {
        return pickDate;
    }

    public void setPickDate(String pickDate) {
        this.pickDate = pickDate;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    public String getIsInTransit() {
        return isInTransit;
    }

    public void setIsInTransit(String isInTransit) {
        this.isInTransit = isInTransit;
    }

    public String getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(String isApproved) {
        this.isApproved = isApproved;
    }

    public String getIsInDispute() {
        return isInDispute;
    }

    public void setIsInDispute(String isInDispute) {
        this.isInDispute = isInDispute;
    }

    public String getRmaDocumentNumber() {
        return rmaDocumentNumber;
    }

    public void setRmaDocumentNumber(String rmaDocumentNumber) {
        this.rmaDocumentNumber = rmaDocumentNumber;
    }

    public String getReversalDocumentNumber() {
        return reversalDocumentNumber;
    }

    public void setReversalDocumentNumber(String reversalDocumentNumber) {
        this.reversalDocumentNumber = reversalDocumentNumber;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
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

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPoreference() {
        return poreference;
    }

    public void setPoreference(String poreference) {
        this.poreference = poreference;
    }

    public List<LinesBean> getInOutLines() {
        return inOutLines;
    }

    public void setInOutLines(List<LinesBean> inOutLines) {
        this.inOutLines = inOutLines;
    }

    public String getProductionId() {
        return productionId;
    }

    public void setProductionId(String productionId) {
        this.productionId = productionId;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentNumber);
        dest.writeString(documentStatusId);
        dest.writeString(posted);
        dest.writeString(processed);
        dest.writeString(processing);
        dest.writeString(documentTypeId);
        dest.writeString(description);
        dest.writeString(orderId);
        dest.writeString(dateOrdered);
        dest.writeString(isPrinted);
        dest.writeString(movementTypeId);
        dest.writeSerializable(movementDate);
        dest.writeString(businessPartnerId);
        dest.writeString(warehouseId);
        dest.writeString(freightAmount);
        dest.writeString(shipperId);
        dest.writeString(chargeAmount);
        dest.writeString(datePrinted);
        dest.writeString(createdFrom);
        dest.writeString(salesRepresentativeId);
        dest.writeString(numberOfPackages);
        dest.writeString(pickDate);
        dest.writeString(shipDate);
        dest.writeString(trackingNumber);
        dest.writeString(dateReceived);
        dest.writeString(isInTransit);
        dest.writeString(isApproved);
        dest.writeString(isInDispute);
        dest.writeString(rmaDocumentNumber);
        dest.writeString(reversalDocumentNumber);
        dest.writeString(active);
        dest.writeLong(version);
        dest.writeString(createdBy);
        dest.writeSerializable(createdAt);
        dest.writeString(updatedBy);
        dest.writeSerializable(updatedAt);
        dest.writeString(poreference);
        dest.writeString(approvalAmount);
        dest.writeString(warehouseIdFrom);
        dest.writeString(warehouseIdTo);
        dest.writeTypedList(inOutLines);
        dest.writeString(productionId);
        dest.writeInt(lineNumber);
    }

    public static class LinesBean implements Parcelable, Cloneable {

        @Override
        public Object clone() {
            LinesBean stu = null;
            try {
                stu = (LinesBean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return stu;
        }

        private String lineNumber;
        private String locatorId;
        private String productId;
        private String attributeSetInstanceId;
        private String description;
        private String quantityUomId;
        private BigDecimal movementQuantity;
        private String pickedQuantity;
        private String isInvoiced;
        private String processed;
        private String rmaLineNumber;
        private String reversalLineNumber;
        private String active;
        private long version;
        private String inOutDocumentNumber;
        private String createdBy;
        private Date createdAt;
        private String updatedBy;
        private Date updatedAt;
        private String locatorIdFrom;
        private String locatorIdTo;
        private String movementDocumentNumber;

        public static final int PARENT_ITEM = 0;//父布局
        public static final int CHILD_ITEM = 1;//子布局
        public int type;// 显示类型
        public boolean isExpand;// 是否展开
        public LinesBean childBean;

        public LinesBean() {
        }


        protected LinesBean(Parcel in) {
            lineNumber = in.readString();
            locatorId = in.readString();
            productId = in.readString();
            attributeSetInstanceId = in.readString();
            description = in.readString();
            quantityUomId = in.readString();
            movementQuantity = (BigDecimal) in.readSerializable();
            pickedQuantity = in.readString();
            isInvoiced = in.readString();
            processed = in.readString();
            rmaLineNumber = in.readString();
            reversalLineNumber = in.readString();
            active = in.readString();
            version = in.readLong();
            inOutDocumentNumber = in.readString();
            createdBy = in.readString();
            createdAt = (Date) in.readSerializable();
            updatedBy = in.readString();
            updatedAt = (Date) in.readSerializable();
            type = in.readInt();
            isExpand = in.readByte() != 0;
            locatorIdFrom = in.readString();
            locatorIdTo = in.readString();
            movementDocumentNumber = in.readString();
            childBean = in.readParcelable(LinesBean.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(lineNumber);
            dest.writeString(locatorId);
            dest.writeString(productId);
            dest.writeString(attributeSetInstanceId);
            dest.writeString(description);
            dest.writeString(quantityUomId);
            dest.writeSerializable(movementQuantity);
            dest.writeString(pickedQuantity);
            dest.writeString(isInvoiced);
            dest.writeString(processed);
            dest.writeString(rmaLineNumber);
            dest.writeString(reversalLineNumber);
            dest.writeString(active);
            dest.writeLong(version);
            dest.writeString(inOutDocumentNumber);
            dest.writeString(createdBy);
            dest.writeSerializable(createdAt);
            dest.writeString(updatedBy);
            dest.writeSerializable(updatedAt);
            dest.writeInt(type);
            dest.writeByte((byte) (isExpand ? 1 : 0));
            dest.writeParcelable(childBean, flags);
            dest.writeString(locatorIdFrom);
            dest.writeString(locatorIdTo);
            dest.writeString(movementDocumentNumber);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<LinesBean> CREATOR = new Creator<LinesBean>() {
            @Override
            public LinesBean createFromParcel(Parcel in) {
                return new LinesBean(in);
            }

            @Override
            public LinesBean[] newArray(int size) {
                return new LinesBean[size];
            }
        };

        public String getLocatorIdFrom() {
            return locatorIdFrom;
        }

        public void setLocatorIdFrom(String locatorIdFrom) {
            this.locatorIdFrom = locatorIdFrom;
        }

        public String getLocatorIdTo() {
            return locatorIdTo;
        }

        public void setLocatorIdTo(String locatorIdTo) {
            this.locatorIdTo = locatorIdTo;
        }

        public String getMovementDocumentNumber() {
            return movementDocumentNumber;
        }

        public void setMovementDocumentNumber(String movementDocumentNumber) {
            this.movementDocumentNumber = movementDocumentNumber;
        }

        public String getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(String lineNumber) {
            this.lineNumber = lineNumber;
        }

        public String getLocatorId() {
            return locatorId;
        }

        public void setLocatorId(String locatorId) {
            this.locatorId = locatorId;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getQuantityUomId() {
            return quantityUomId;
        }

        public void setQuantityUomId(String quantityUomId) {
            this.quantityUomId = quantityUomId;
        }

        public BigDecimal getMovementQuantity() {
            return movementQuantity;
        }

        public void setMovementQuantity(BigDecimal movementQuantity) {
            this.movementQuantity = movementQuantity;
        }

        public String getPickedQuantity() {
            return pickedQuantity;
        }

        public void setPickedQuantity(String pickedQuantity) {
            this.pickedQuantity = pickedQuantity;
        }

        public String getIsInvoiced() {
            return isInvoiced;
        }

        public void setIsInvoiced(String isInvoiced) {
            this.isInvoiced = isInvoiced;
        }

        public String getProcessed() {
            return processed;
        }

        public void setProcessed(String processed) {
            this.processed = processed;
        }

        public String getRmaLineNumber() {
            return rmaLineNumber;
        }

        public void setRmaLineNumber(String rmaLineNumber) {
            this.rmaLineNumber = rmaLineNumber;
        }

        public String getReversalLineNumber() {
            return reversalLineNumber;
        }

        public void setReversalLineNumber(String reversalLineNumber) {
            this.reversalLineNumber = reversalLineNumber;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
        }

        public String getInOutDocumentNumber() {
            return inOutDocumentNumber;
        }

        public void setInOutDocumentNumber(String inOutDocumentNumber) {
            this.inOutDocumentNumber = inOutDocumentNumber;
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

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
        }

        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public boolean isExpand() {
            return isExpand;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }

        public LinesBean getChildBean() {
            return childBean;
        }

        public void setChildBean(LinesBean childBean) {
            this.childBean = childBean;
        }
    }
}
