package com.takumi.wms.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class StatusItem implements Parcelable{

    private String statusId;
    private String description;
    private boolean checked;

    public StatusItem(String statusId, String description) {
        this.statusId = statusId;
        this.description = description;
    }

    public StatusItem() {
    }

    protected StatusItem(Parcel in) {
        statusId = in.readString();
        description = in.readString();
        checked = in.readByte() != 0;
    }

    public static final Creator<StatusItem> CREATOR = new Creator<StatusItem>() {
        @Override
        public StatusItem createFromParcel(Parcel in) {
            return new StatusItem(in);
        }

        @Override
        public StatusItem[] newArray(int size) {
            return new StatusItem[size];
        }
    };

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(statusId);
        dest.writeString(description);
        dest.writeByte((byte) (checked ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusItem that = (StatusItem) o;
        return Objects.equals(statusId, that.statusId) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusId, description);
    }
}
