package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DocumentLine implements Parcelable {
    public DocumentLine() {
    }

    @SerializedName(value = "documentLineId", alternate = {"itemissuanceId"})
    private String documentLineId;
    private String productId;
    private String productName;
    private String inventoryAttributeSetId;
    private BigDecimal acceptedCount;//收货的时候的接收数量，出库的时候不需要
    private BigDecimal count;
    private String locatorId;
    private String locatorIdTo;
    private String locatorIdFrom;
    private String defectsDescription;
    private HashMap<String, Object> inventoryattribute = new LinkedHashMap<>();
    private List<OssImage> documentLineImageUrls;
    private List<StatusItem> damageStatuss;

    protected DocumentLine(Parcel in) {
        documentLineId = in.readString();
        productId = in.readString();
        productName = in.readString();
        inventoryAttributeSetId = in.readString();
        acceptedCount = (BigDecimal) in.readSerializable();
        count = (BigDecimal) in.readSerializable();
        locatorId = in.readString();
        locatorIdTo = in.readString();
        locatorIdFrom = in.readString();
        defectsDescription = in.readString();
        inventoryattribute = in.readHashMap(LinkedHashMap.class.getClassLoader());
        documentLineImageUrls = in.createTypedArrayList(OssImage.CREATOR);
        damageStatuss = in.createTypedArrayList(StatusItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentLineId);
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(inventoryAttributeSetId);
        dest.writeSerializable(acceptedCount);
        dest.writeSerializable(count);
        dest.writeString(locatorId);
        dest.writeString(locatorIdTo);
        dest.writeString(locatorIdFrom);
        dest.writeString(defectsDescription);
        dest.writeMap(inventoryattribute);
        dest.writeTypedList(documentLineImageUrls);
        dest.writeTypedList(damageStatuss);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DocumentLine> CREATOR = new Creator<DocumentLine>() {
        @Override
        public DocumentLine createFromParcel(Parcel in) {
            return new DocumentLine(in);
        }

        @Override
        public DocumentLine[] newArray(int size) {
            return new DocumentLine[size];
        }
    };

    public String getDocumentLineId() {
        return documentLineId;
    }

    public void setDocumentLineId(String documentLineId) {
        this.documentLineId = documentLineId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getInventoryAttributeSetId() {
        return inventoryAttributeSetId;
    }

    public void setInventoryAttributeSetId(String inventoryAttributeSetId) {
        this.inventoryAttributeSetId = inventoryAttributeSetId;
    }

    public BigDecimal getAcceptedCount() {
        return acceptedCount;
    }

    public void setAcceptedCount(BigDecimal acceptedCount) {
        this.acceptedCount = acceptedCount;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getLocatorId() {
        return locatorId;
    }

    public void setLocatorId(String locatorId) {
        this.locatorId = locatorId;
    }

    public String getLocatorIdTo() {
        return locatorIdTo;
    }

    public void setLocatorIdTo(String locatorIdTo) {
        this.locatorIdTo = locatorIdTo;
    }

    public String getLocatorIdFrom() {
        return locatorIdFrom;
    }

    public void setLocatorIdFrom(String locatorIdFrom) {
        this.locatorIdFrom = locatorIdFrom;
    }

    public String getDefectsDescription() {
        return defectsDescription;
    }

    public void setDefectsDescription(String defectsDescription) {
        this.defectsDescription = defectsDescription;
    }

    public HashMap<String, Object> getInventoryattribute() {
        return inventoryattribute;
    }

    public void setInventoryattribute(HashMap<String, Object> inventoryattribute) {
        this.inventoryattribute = inventoryattribute;
    }

    public List<OssImage> getDocumentLineImageUrls() {
        return documentLineImageUrls;
    }

    public void setDocumentLineImageUrls(List<OssImage> documentLineImageUrls) {
        this.documentLineImageUrls = documentLineImageUrls;
    }

    public List<StatusItem> getDamageStatuss() {
        return damageStatuss;
    }

    public void setDamageStatuss(List<StatusItem> damageStatuss) {
        this.damageStatuss = damageStatuss;
    }
}
