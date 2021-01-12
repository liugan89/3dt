package com.takumi.wms.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NoticeDocument implements Parcelable {

    private String id;
    private String contractNo;

    public NoticeDocument(String id, String contractNo) {
        this.id = id;
        this.contractNo = contractNo;
    }

    protected NoticeDocument(Parcel in) {
        id = in.readString();
        contractNo = in.readString();
    }

    public static final Creator<NoticeDocument> CREATOR = new Creator<NoticeDocument>() {
        @Override
        public NoticeDocument createFromParcel(Parcel in) {
            return new NoticeDocument(in);
        }

        @Override
        public NoticeDocument[] newArray(int size) {
            return new NoticeDocument[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(contractNo);
    }
}
