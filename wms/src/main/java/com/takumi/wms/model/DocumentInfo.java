package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DocumentInfo implements Parcelable {


    private String documentId;
    private List<OssImage> documentImages;
    private List<DocumentLine> documentLines;

    protected DocumentInfo(Parcel in) {
        documentId = in.readString();
        documentImages = in.createTypedArrayList(OssImage.CREATOR);
        documentLines = in.createTypedArrayList(DocumentLine.CREATOR);
    }

    public static final Creator<DocumentInfo> CREATOR = new Creator<DocumentInfo>() {
        @Override
        public DocumentInfo createFromParcel(Parcel in) {
            return new DocumentInfo(in);
        }

        @Override
        public DocumentInfo[] newArray(int size) {
            return new DocumentInfo[size];
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

    public List<DocumentLine> getDocumentLines() {
        return documentLines;
    }

    public void setDocumentLines(List<DocumentLine> documentLines) {
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

}
