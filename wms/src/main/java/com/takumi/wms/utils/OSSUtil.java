package com.takumi.wms.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CannedAccessControlList;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetObjectACLRequest;
import com.alibaba.sdk.android.oss.model.GetObjectACLResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.takumi.wms.BuildConfig;
import com.takumi.wms.model.OssImage;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.obj.OssFile;
import com.takumi.wms.widget.LoadingDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.acl.Acl;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

public class OSSUtil implements Parcelable {
    private static OSS oss;
    private List<OssFile> downloadImages = new ArrayList<>(5);
    private List<OssFile> uploadImages = new ArrayList<>(5);

    private static String bucketName = BuildConfig.OSS_bucketName;

    public OSSUtil() {
    }

    protected OSSUtil(Parcel in) {
        imageCount = in.readInt();
        isdoImageSuccessed = in.readByte() != 0;
        downloadImages = in.readArrayList(OssFile.class.getClassLoader());
        uploadImages = in.readArrayList(OssFile.class.getClassLoader());
    }

    public static final Creator<OSSUtil> CREATOR = new Creator<OSSUtil>() {
        @Override
        public OSSUtil createFromParcel(Parcel in) {
            return new OSSUtil(in);
        }

        @Override
        public OSSUtil[] newArray(int size) {
            return new OSSUtil[size];
        }
    };

    public List<OssFile> getDownloadImages() {
        return downloadImages;
    }

    public List<OssFile> getUploadImages() {
        return uploadImages;
    }

    public static final synchronized void initOSS(Context context) {
        if (oss == null) {
            try {
                String endpoint = BuildConfig.OSS_endpoint;
                ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                //推荐使用OSSAuthCredentialsProvider。token过期可以及时更新
                OSSPlainTextAKSKCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(appInfo.metaData.getString("OSS_id"), appInfo.metaData.getString("OSS_Secret"));
                //该配置类如果不设置，会有默认配置，具体可看该类
                ClientConfiguration conf = new ClientConfiguration();
                conf.setConnectionTimeout(60 * 1000); // 连接超时，默认15秒
                conf.setSocketTimeout(60 * 1000); // socket超时，默认15秒
                conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
                conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
                oss = new OSSClient(context.getApplicationContext(), endpoint, credentialProvider);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private int imageCount;
    private LoadingDialog ld;

    private void showDialog(Context context) {
        imageCount++;
        if (ld == null || !ld.isShowing()) {
            ld = new LoadingDialog(context);
            ld.show();
        }
    }

    private void dismissDialog() {
        if (imageCount <= 0) {
            ld.dismiss();
        }
    }

    public static String makeKey(File f) {
        return DateUtil.toDate(System.currentTimeMillis()) + File.separator + f.getName();
    }

    public static String makeKey(String fileName) {
        return DateUtil.toDate(System.currentTimeMillis()) + File.separator + fileName;
    }

    public static String makeUrl(String fileKey) {
        return "https://" + bucketName + ".oss-cn-qingdao.aliyuncs.com/" + fileKey;
    }

    public void uploadPic(Context context, OssFile f, final NetUtil.MyCallBack callBack) throws IOException {
        isdoImageSuccessed = true;
        showDialog(context);
        initOSS(context);
        PutObjectRequest put = new PutObjectRequest(bucketName, f.getObjectKey(), f.getPath());
        ObjectMetadata metadata = new ObjectMetadata();
        String s = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            s = Files.probeContentType(f.toPath());
        } else {
            s = "image/jpeg";
        }
        metadata.setContentType(s);
        metadata.addUserMetadata("x-oss-object-acl", "public-read");
        put.setMetadata(metadata);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                imageCount--;
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
                dismissDialog();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                imageCount--;
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(clientExcepion.getMessage());
                    }
                }
                if (serviceException != null) {
                    // 服务异常
                    if (callBack != null) {
                        callBack.onError(serviceException.getRawMessage());
                    }
                }
                dismissDialog();

            }
        });
    }

    private boolean isdoImageSuccessed = true;

    public void uploadPics(Context context, List<OssFile> files, final NetUtil.MyCallBack callBack) throws IOException {
        if (files != null) {
            for (OssFile f : files) {
                uploadPic(context, f, new NetUtil.MyCallBack() {
                    @Override
                    public void onSuccess(Object result) {
                        if (imageCount <= 0) {
                            doCallback(callBack);
                        }
                    }

                    @Override
                    public boolean onError(String msg) {
                        isdoImageSuccessed = false;
                        if (imageCount <= 0) {
                            doCallback(callBack);
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void doCallback(NetUtil.MyCallBack callBack) {
        if (isdoImageSuccessed) {
            callBack.onSuccess(null);
        } else {
            callBack.onError("上传或下载图片失败");
        }
    }


    private String getKeyFromUrl(String objectUrl) {
        String[] parts = objectUrl.split("/");
        int l = parts.length;
        return parts[l - 2] + "/" + parts[l - 1];
    }

    public void downloadPic(Context context, final String objectUrl, final NetUtil.MyCallBack callBack) {
        isdoImageSuccessed = true;
        showDialog(context);
        initOSS(context);
        String objectKey = getKeyFromUrl(objectUrl);
        GetObjectRequest get = new GetObjectRequest(bucketName, objectKey);
        //设置下载进度回调
        get.setProgressListener(new OSSProgressCallback<GetObjectRequest>() {
            @Override
            public void onProgress(GetObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        OSSAsyncTask task = oss.asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = null;
                FileOutputStream fos = null;
                try {
                    inputStream = result.getObjectContent();
                    OssFile obj = OssFile.createOssFile(FileUtil.getImageDir() + File.separator + objectKey, objectKey, false);
                    FileUtil.createParentDirs(obj);
                    if (!obj.exists()) {
                        obj.createNewFile();
                    }
                    fos = new FileOutputStream(obj);
                    byte[] buffer = new byte[2048];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        // 处理下载的数据
                        fos.write(buffer, 0, len);
                    }
                    Observable.just(obj).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<OssFile>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(OssFile file) {
                            downloadImages.add(file);
                            imageCount--;
                            if (callBack != null) {
                                callBack.onSuccess(file);
                            }
                            dismissDialog();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                imageCount--;
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(clientExcepion.getMessage());
                    }
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                    if (callBack != null) {
                        callBack.onError(serviceException.getRawMessage());
                    }
                }
                dismissDialog();
            }
        });
    }


    public void downloadPics(Context context, List<OssImage> objectKeys, final NetUtil.MyCallBack callBack) {
        if (objectKeys != null) {
            for (OssImage ossImage : objectKeys) {
                downloadPic(context, ossImage.getDocumentImageUrl(), new NetUtil.MyCallBack() {
                    @Override
                    public void onSuccess(Object result) {
                        if (imageCount <= 0) {
                            doCallback(callBack);
                        }
                    }

                    @Override
                    public boolean onError(String msg) {
                        isdoImageSuccessed = false;
                        if (imageCount <= 0) {
                            doCallback(callBack);
                        }
                        return false;
                    }
                });
            }
        }
    }


    public void downloadPics(Context context, String[] ss, final NetUtil.MyCallBack callBack) {
        if (ss != null) {
            for (String k : ss) {
                if (!TextUtils.isEmpty(k)) {

                    downloadPic(context, k, new NetUtil.MyCallBack() {
                        @Override
                        public void onSuccess(Object result) {
                            if (imageCount <= 0) {
                                doCallback(callBack);
                            }
                        }

                        @Override
                        public boolean onError(String msg) {
                            isdoImageSuccessed = false;
                            if (imageCount <= 0) {
                                doCallback(callBack);
                            }
                            return false;
                        }
                    });
                }
            }
        }
    }


    public void deletePic(Context context, String objectKey, final NetUtil.MyCallBack callBack) {
        showDialog(context);
        initOSS(context);
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(bucketName, objectKey);
        // 异步删除
        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
                imageCount--;
                if (callBack != null) {
                    callBack.onSuccess(result);
                }
                dismissDialog();
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {

                imageCount--;
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                    if (callBack != null) {
                        callBack.onError(clientExcepion.getMessage());
                    }
                }
                if (serviceException != null) {
                    // 服务异常
                    if (callBack != null) {
                        callBack.onError(serviceException.getRawMessage());
                    }
                }
                dismissDialog();
            }

        });
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageCount);
        dest.writeByte((byte) (isdoImageSuccessed ? 1 : 0));
        dest.writeList(uploadImages);
        dest.writeList(downloadImages);
    }
}
