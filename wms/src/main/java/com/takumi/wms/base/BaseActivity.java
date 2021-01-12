package com.takumi.wms.base;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.PermissionChecker;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.gyf.barlibrary.ImmersionBar;
import com.obm.mylibrary.ScanConnect;
import com.obm.mylibrary.ScanEvent;
import com.takumi.wms.R;
import com.takumi.wms.utils.FileUtil;
import com.takumi.wms.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;


public abstract class BaseActivity extends Activity {
    protected ImmersionBar mImmersionBar;
    protected static final int WHITEBAR = -1;

    protected Uri photoUri;
    protected static final int CODE_TAKE_PHOTO = 900;//相机RequestCode
    protected static final int REQUEST_IMAGE_SHOW = 901;
    protected static final int TYPE_TAKE_PHOTO = 902;//Uri获取类型判断
    protected static final int REQUEST_ALBUM = 903;

    protected final static int SerialScanRequest = 101;

    private static final int CODE_FOR_WRITE_PERMISSION = 9000;
    private ScanEvent scanEvent;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                try {
                    Intent i = new Intent();
                    String s = new String(msg.obj.toString().getBytes(), "utf-8");
                    i.putExtra("number", s.trim());
                    i.putExtra("type", "laser_scan");
                    onActivityResult(SerialScanRequest, RESULT_OK, i);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
    };

    protected void laserTest() {
        Message msg = Message.obtain();
        msg.what = 0;
        msg.obj = "M1D13K50101";
        handler.sendMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewID());//设置xml布局
        ButterKnife.bind(this);//绑定Butterknife
        //MyTransform.bind(this);
        scanEvent = new ScanEvent(this, handler, 0);
        /*scanEvent = new ScanEvent(this, new ScanEvent.OnScanListener() {
            @Override
            public void onScan(String s) {
                ToastUtil.showToast(BaseActivity.this, "onScan");
                Intent i = new Intent();
                i.putExtra("number", s);
                onActivityResult(SerialScanRequest, RESULT_OK, i);
            }
        });*/

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ScanConnect.receive(keyCode);
        //ToastUtil.showToast(BaseActivity.this,"onKeyDown");
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    protected abstract int getContentViewID();
    //沉浸式状态栏初始化

    protected void initImmersionBar(@ColorRes int color, boolean isDarkFont) {
        mImmersionBar = ImmersionBar.with(this);

        //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，
        // 如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarDarkFont(isDarkFont, 0.2f);
        if (color != WHITEBAR) {
            mImmersionBar.barColor(color);
        }
        mImmersionBar.fitsSystemWindows(true);

        mImmersionBar.init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //在BaseActivity里销毁
        }
        scanEvent.scan_stop();
    }

    protected void onTakePhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 进入这儿表示没有权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // 提示已经禁止
                ToastUtil.showToast(this, "请打开相机权限");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CODE_FOR_WRITE_PERMISSION);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 24) {
                photoUri = get24MediaFileUri(TYPE_TAKE_PHOTO);
                if (photoUri != null) {
                    Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
                }
            } else {
                Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoUri = getMediaFileUri(TYPE_TAKE_PHOTO);
                takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takeIntent, CODE_TAKE_PHOTO);
            }
        }
    }

    protected void onAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_ALBUM);
    }

    protected void showBottomDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.bottom_photo_album, null);
        LinearLayout ll_bottom_album = (LinearLayout) dialogView.findViewById(R.id.ll_bottom_album);
        LinearLayout ll_bottom_photo = (LinearLayout) dialogView.findViewById(R.id.ll_bottom_photo);
        ll_bottom_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAlbum();
                dialog.dismiss();
            }
        });

        ll_bottom_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTakePhoto();
                dialog.dismiss();
            }
        });

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(dialogView);
        dialog.show();
    }


    public Uri getMediaFileUri(int type) {
        File mediaStorageDir = new File(FileUtil.getTempDir());
        FileUtil.createDirs(mediaStorageDir);
        //创建Media File
        if (type == TYPE_TAKE_PHOTO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg");
        } else {
            return null;
        }
        return Uri.fromFile(mediaFile);
    }

    private File mediaFile;
    protected File imageFile;

    /**
     * 版本24以上
     */
    public Uri get24MediaFileUri(int type) {
        if (!canStorage()) {
            return null;
        } else {
            File mediaStorageDir = new File(FileUtil.getTempDir());
            FileUtil.createDirs(mediaStorageDir);
            //创建Media File
            if (type == TYPE_TAKE_PHOTO) {
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg");
            } else {
                return null;
            }
            return FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", mediaFile);
        }
    }

    protected boolean canStorage() {
        if (ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_FOR_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户同意，执行操作

            } else {
                //用户不同意，向用户展示该权限作用
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    new AlertDialog.Builder(this)
                            .setMessage("权限申请")
                            .setPositiveButton("OK", (dialog1, which) ->
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_FOR_WRITE_PERMISSION))
                            .setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                //原图
                String filePath = mediaFile.getAbsolutePath();
                final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                imageFile = new File(FileUtil.getImageDir() + File.separator + mediaFile.getName());
                FileUtil.createParentDirs(imageFile);
                //利用Bitmap对象创建缩略图
                compressImage(bitmap, imageFile.getPath());
            }
        } else if (requestCode == REQUEST_ALBUM) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);  //获取照片路径
                    cursor.close();
                    final Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    imageFile = new File(FileUtil.getImageDir() + File.separator + System.currentTimeMillis() + ".jpg");
                    FileUtil.createParentDirs(imageFile);
                    //利用Bitmap对象创建缩略图
                    compressImage(bitmap, imageFile.getPath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 质量压缩法：
    private Bitmap compressImage(Bitmap image, String filepath) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 20, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            //压缩好后写入文件中
            FileOutputStream fos = new FileOutputStream(filepath);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private boolean selfPermissionGranted(Context context, String permission) {

        boolean ret = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.M) {
                ret = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
            } else {
                ret = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return ret;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mediaFile != null) {
            outState.putSerializable("mediaFile", mediaFile);
        }
        if (photoUri != null) {
            outState.putParcelable("photoUri", photoUri);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mediaFile = (File) savedInstanceState.getSerializable("mediaFile");
        photoUri = savedInstanceState.getParcelable("photoUri");
    }
}
