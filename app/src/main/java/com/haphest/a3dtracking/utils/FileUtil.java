package com.haphest.a3dtracking.utils;

import android.os.Environment;

import java.io.File;

public class FileUtil {

    public static final File getImageRootDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    public static final String getImageDir() {
        return getImageRootDir().getAbsolutePath() + File.separator + "wms";
    }

    public static final String getTempDir() {
        return getImageRootDir().getAbsolutePath() + File.separator + "wmsTemp";
    }

    public static final void createParentDirs(File f) {
        if (f != null) {
            File p = f.getParentFile();
            if (!p.exists()) {
                p.mkdirs();
            }
        }
    }

    public static final void createDirs(File f) {
        if (f != null) {
            if (!f.exists()) {
                f.mkdirs();
            }
        }
    }


    public static void deleteFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            deleteFile(f);
        }
    }


    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            file.delete();//如要保留文件夹，只删除文件，请注释这行
        } else if (file.exists()) {
            file.delete();
        }
    }

}
