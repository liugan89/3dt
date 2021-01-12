package com.takumi.wms.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bumptech.glide.Glide;
import com.takumi.wms.R;
import com.takumi.wms.base.BaseActivity;
import com.takumi.wms.config.Constant;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.obj.OssFile;
import com.takumi.wms.utils.OSSUtil;
import com.takumi.wms.utils.ToastUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;

public class ImageActivity extends BaseActivity {

    @BindView(R.id.iv_showImage)
    ImageView iv_showImage;

    private OssFile image;
    private int position;
    private boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImmersionBar(R.color.black, false);
        image = (OssFile) getIntent().getSerializableExtra(Constant.Flags.Param1);
        position = getIntent().getIntExtra(Constant.Flags.Param2, -1);
        editable = getIntent().getBooleanExtra(Constant.Flags.Param3, false);
        Glide.with(this).load(image).into(iv_showImage);
        if (editable) {
            iv_showImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showDeleteDialog();
                    return true;
                }
            });
        }
        iv_showImage.setClickable(true);
        iv_showImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void showDeleteDialog() {
        Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        View view = LayoutInflater.from(this).inflate(R.layout.buttom_delete, null);
        TextView delete = view.findViewById(R.id.tv_buttom_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OSSUtil ou = new OSSUtil();
                ou.deletePic(ImageActivity.this, image.getObjectKey(), new NetUtil.MyCallBack<DeleteObjectResult>() {
                    @Override
                    public void onSuccess(DeleteObjectResult result) {
                        ToastUtil.showToast(ImageActivity.this, "删除成功");
                        Intent i = new Intent();
                        i.putExtra(Constant.Flags.Param1, position);
                        setResult(RESULT_OK, i);
                        finish();
                    }

                    @Override
                    public boolean onError(String msg) {
                        ToastUtil.showToast(ImageActivity.this, msg);
                        return false;
                    }
                });
            }
        });
        TextView save = view.findViewById(R.id.tv_buttom_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canStorage()) {
                    try {
                        File dic = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "wmsPic");
                        File f = new File(dic, image.getObjectKey());
                        File p = f.getParentFile();
                        if (!p.exists()) {
                            p.mkdirs();
                        }
                        if (!f.exists()) {
                            f.createNewFile();
                        }
                        copyFile(image, f);
                        ToastUtil.showToast(ImageActivity.this, "已保存图片至\"" + f.getPath() + "\"");
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        window.setContentView(view);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
    }

    /**
     * 复制单个文件
     *
     * @param oldFile
     * @param newFile
     * @return boolean
     */
    public void copyFile(File oldFile, File newFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldFile); //读入原文件
                FileOutputStream fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            System.out.println("保存文件操作出错");
            e.printStackTrace();

        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_image;
    }
}
