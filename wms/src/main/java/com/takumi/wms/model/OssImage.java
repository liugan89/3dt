package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OssImage implements Parcelable {
    private String documentImageId;
    private String documentImageUrl;

    protected OssImage(Parcel in) {
        documentImageId = in.readString();
        documentImageUrl = in.readString();
    }

    public static final Creator<OssImage> CREATOR = new Creator<OssImage>() {
        @Override
        public OssImage createFromParcel(Parcel in) {
            return new OssImage(in);
        }

        @Override
        public OssImage[] newArray(int size) {
            return new OssImage[size];
        }
    };

    public String getDocumentImageId() {
        return documentImageId;
    }

    public void setDocumentImageId(String documentImageId) {
        this.documentImageId = documentImageId;
    }

    public String getDocumentImageUrl() {
        return documentImageUrl;
    }

    public void setDocumentImageUrl(String documentImageUrl) {
        this.documentImageUrl = documentImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(documentImageId);
        dest.writeString(documentImageUrl);
    }
}
