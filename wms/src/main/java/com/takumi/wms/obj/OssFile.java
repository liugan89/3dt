package com.takumi.wms.obj;

import android.support.annotation.NonNull;

import com.takumi.wms.utils.OSSUtil;

import java.io.File;

public class OssFile extends File {
    private OssFile(@NonNull String pathname) {
        super(pathname);
    }


    public static OssFile createOssFile(@NonNull String pathname, @NonNull String objectKey, boolean isUpload) {
        OssFile f = new OssFile(pathname);
        f.objectKey = objectKey;
        f.objectUrl = OSSUtil.makeUrl(objectKey);
        f.isUpload = isUpload;
        return f;
    }

    public static OssFile createOssFile(@NonNull File file, @NonNull String objectKey, boolean isUpload) {
        OssFile f = new OssFile(file.getPath());
        f.objectKey = objectKey;
        f.objectUrl = OSSUtil.makeUrl(objectKey);
        f.isUpload = isUpload;
        return f;
    }

    private String objectKey;
    private String objectUrl;
    private boolean isUpload = false;

    public String getObjectKey() {
        return objectKey;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public String getObjectUrl() {
        return objectUrl;
    }
}
