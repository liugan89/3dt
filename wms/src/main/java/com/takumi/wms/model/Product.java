package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class Product implements Parcelable {

    private String productId;
    private String productName;
    private String description;
    private String grade;
    private String gsm;
    private String diameterUomId;
    private Double productDiameter;
    private Double outsideDiameter;
    private Double coreDiameter;
    private String widthUomId;
    private Double productWidth;
    private String brandName;
    private Double moisturePct;
    private String quantityUomId;
    private boolean isSerialNumbered;
    private boolean isManagedByLot;
    private String attributeSetId;
    private String inShippingBox;
    private int productDepth;
    private HashMap<String, Object> attributes = new HashMap<>();
    private boolean isChecked;

    protected Product(Parcel in) {
        productId = in.readString();
        productName = in.readString();
        description = in.readString();
        grade = in.readString();
        gsm = in.readString();
        diameterUomId = in.readString();
        if (in.readByte() == 0) {
            productDiameter = null;
        } else {
            productDiameter = in.readDouble();
        }
        if (in.readByte() == 0) {
            outsideDiameter = null;
        } else {
            outsideDiameter = in.readDouble();
        }
        if (in.readByte() == 0) {
            coreDiameter = null;
        } else {
            coreDiameter = in.readDouble();
        }
        widthUomId = in.readString();
        if (in.readByte() == 0) {
            productWidth = null;
        } else {
            productWidth = in.readDouble();
        }
        brandName = in.readString();
        if (in.readByte() == 0) {
            moisturePct = null;
        } else {
            moisturePct = in.readDouble();
        }
        quantityUomId = in.readString();
        isSerialNumbered = in.readByte() != 0;
        isManagedByLot = in.readByte() != 0;
        attributeSetId = in.readString();
        inShippingBox = in.readString();
        productDepth = in.readInt();
        attributes = in.readHashMap(HashMap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(productId);
        dest.writeString(productName);
        dest.writeString(description);
        dest.writeString(grade);
        dest.writeString(gsm);
        dest.writeString(diameterUomId);
        if (productDiameter == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(productDiameter);
        }
        if (outsideDiameter == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(outsideDiameter);
        }
        if (coreDiameter == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(coreDiameter);
        }
        dest.writeString(widthUomId);
        if (productWidth == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(productWidth);
        }
        dest.writeString(brandName);
        if (moisturePct == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(moisturePct);
        }
        dest.writeString(quantityUomId);
        dest.writeByte((byte) (isSerialNumbered ? 1 : 0));
        dest.writeByte((byte) (isManagedByLot ? 1 : 0));
        dest.writeString(attributeSetId);
        dest.writeString(inShippingBox);
        dest.writeInt(productDepth);
        dest.writeMap(attributes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getGsm() {
        return gsm;
    }

    public void setGsm(String gsm) {
        this.gsm = gsm;
    }

    public String getDiameterUomId() {
        return diameterUomId;
    }

    public void setDiameterUomId(String diameterUomId) {
        this.diameterUomId = diameterUomId;
    }

    public Double getProductDiameter() {
        return productDiameter;
    }

    public void setProductDiameter(Double productDiameter) {
        this.productDiameter = productDiameter;
    }

    public Double getOutsideDiameter() {
        return outsideDiameter;
    }

    public void setOutsideDiameter(Double outsideDiameter) {
        this.outsideDiameter = outsideDiameter;
    }

    public Double getCoreDiameter() {
        return coreDiameter;
    }

    public void setCoreDiameter(Double coreDiameter) {
        this.coreDiameter = coreDiameter;
    }

    public String getWidthUomId() {
        return widthUomId;
    }

    public void setWidthUomId(String widthUomId) {
        this.widthUomId = widthUomId;
    }

    public Double getProductWidth() {
        return productWidth;
    }

    public void setProductWidth(Double productWidth) {
        this.productWidth = productWidth;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Double getMoisturePct() {
        return moisturePct;
    }

    public void setMoisturePct(Double moisturePct) {
        this.moisturePct = moisturePct;
    }

    public String getQuantityUomId() {
        return quantityUomId;
    }

    public void setQuantityUomId(String quantityUomId) {
        this.quantityUomId = quantityUomId;
    }

    public boolean isSerialNumbered() {
        return isSerialNumbered;
    }

    public void setSerialNumbered(boolean serialNumbered) {
        isSerialNumbered = serialNumbered;
    }

    public boolean isManagedByLot() {
        return isManagedByLot;
    }

    public void setManagedByLot(boolean managedByLot) {
        isManagedByLot = managedByLot;
    }

    public String getAttributeSetId() {
        return attributeSetId;
    }

    public void setAttributeSetId(String attributeSetId) {
        this.attributeSetId = attributeSetId;
    }

    public String getInShippingBox() {
        return inShippingBox;
    }

    public void setInShippingBox(String inShippingBox) {
        this.inShippingBox = inShippingBox;
    }

    public int getProductDepth() {
        return productDepth;
    }

    public void setProductDepth(int productDepth) {
        this.productDepth = productDepth;
    }

    public HashMap<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, Object> attributes) {
        this.attributes = attributes;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
