package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class OutDocumentInfo implements Parcelable{



    private String documentId;
    private List<OssImage> documentImages;
    private List<DocumentLinesItem> documentLines;

    protected OutDocumentInfo(Parcel in) {
        documentId = in.readString();
        documentImages = in.createTypedArrayList(OssImage.CREATOR);
        documentLines = in.createTypedArrayList(DocumentLinesItem.CREATOR);
    }

    public static final Creator<OutDocumentInfo> CREATOR = new Creator<OutDocumentInfo>() {
        @Override
        public OutDocumentInfo createFromParcel(Parcel in) {
            return new OutDocumentInfo(in);
        }

        @Override
        public OutDocumentInfo[] newArray(int size) {
            return new OutDocumentInfo[size];
        }
    };

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public List<OssImage> getDocumentImages() {
        return documentImages;
    }

    public void setDocumentImages(List<OssImage> documentImages) {
        this.documentImages = documentImages;
    }

    public List<DocumentLinesItem> getDocumentLines() {
        return documentLines;
    }

    public void setDocumentLines(List<DocumentLinesItem> documentLines) {
        this.documentLines = documentLines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentId);
        dest.writeTypedList(documentImages);
        dest.writeTypedList(documentLines);
    }

    public static class DocumentLinesItem implements Parcelable{

        private String documentLineId;
        private String productIdItem;
        private double quantityItem;
        private String poNumberItem;
        private Product productAttributeDto;
        private List<DocumentLine> itemissuance;

        protected DocumentLinesItem(Parcel in) {
            documentLineId = in.readString();
            productIdItem = in.readString();
            quantityItem = in.readDouble();
            poNumberItem = in.readString();
            productAttributeDto = in.readParcelable(Product.class.getClassLoader());
            itemissuance = in.createTypedArrayList(DocumentLine.CREATOR);
        }

        public static final Creator<DocumentLinesItem> CREATOR = new Creator<DocumentLinesItem>() {
            @Override
            public DocumentLinesItem createFromParcel(Parcel in) {
                return new DocumentLinesItem(in);
            }

            @Override
            public DocumentLinesItem[] newArray(int size) {
                return new DocumentLinesItem[size];
            }
        };

        public String getDocumentLineId() {
            return documentLineId;
        }

        public void setDocumentLineId(String documentLineId) {
            this.documentLineId = documentLineId;
        }

        public String getProductIdItem() {
            return productIdItem;
        }

        public void setProductIdItem(String productIdItem) {
            this.productIdItem = productIdItem;
        }

        public double getQuantityItem() {
            return quantityItem;
        }

        public void setQuantityItem(double quantityItem) {
            this.quantityItem = quantityItem;
        }

        public String getPoNumberItem() {
            return poNumberItem;
        }

        public void setPoNumberItem(String poNumberItem) {
            this.poNumberItem = poNumberItem;
        }

        public Product getProductAttributeDto() {
            return productAttributeDto;
        }

        public void setProductAttributeDto(Product productAttributeDto) {
            this.productAttributeDto = productAttributeDto;
        }

        public List<DocumentLine> getItemissuance() {
            return itemissuance;
        }

        public void setItemissuance(List<DocumentLine> itemissuance) {
            this.itemissuance = itemissuance;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(documentLineId);
            dest.writeString(productIdItem);
            dest.writeDouble(quantityItem);
            dest.writeString(poNumberItem);
            dest.writeParcelable(productAttributeDto, flags);
            dest.writeTypedList(itemissuance);
        }
    }
}
