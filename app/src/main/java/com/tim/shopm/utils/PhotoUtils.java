package com.tim.shopm.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;


import com.tim.common.ToastUtil;
import com.tim.shopm.R;

import java.io.File;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * @author Liuyibo
 */
public class PhotoUtils {
    private  final String TAG = "PhotoUtils";
    private  final int CODE_GALLERY_REQUEST = 0xa0;
    private  final int CODE_CAMERA_REQUEST = 0xa1;
    private  final int CODE_RESULT_REQUEST = 0xa2;
    private  final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private  final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
//    private static HashMap<String,PhotoUtils> utilsHashMap = new HashMap<>();
    /**
     * 照相后存储的路径，不设置将不能使用可调用{@link #create(Object, boolean),#create(Object, int, int)}方法使用默认设置
     */
    private File fileUri;
    /**
     *赋值为空时{@link #outputX,#outputY}取-1
     */
    private File fileCropUri;
    /**
     * -1为不裁剪，0为不限制裁剪
     */
    private  int outputX ;
    private  int outputY ;

    private Object context;
    private Uri imageUri;
    private Uri cropImageUri;

    /**
     * @see #create(Object, File, File, int, int)
     * @param context
     * @param canCut 是否可以支持剪裁
     * @return
     */
    public static PhotoUtils create(Object context,boolean canCut){
        return PhotoUtils.create(context,
                new File(getContext(context).getExternalFilesDir(Environment.DIRECTORY_DCIM),
                        "Img"+System.currentTimeMillis()+".jpg"),
                !canCut?null:new File(getContext(context).getExternalCacheDir(),
                        "temp"+System.currentTimeMillis()+".jpg"),
                canCut?0:-1,canCut?0:-1);
    }

    /**
     * @see #create(Object, File, File, int, int)
     * @param context
     * @param width
     * @param height
     * @return
     */
    public static PhotoUtils create(Object context,int width,int height){
        return PhotoUtils.create(context,
                new File(getContext(context).getExternalFilesDir(Environment.DIRECTORY_DCIM),
                        "Img"+System.currentTimeMillis()+".jpg"),
                (width==-1||height==-1)?null:new File(getContext(context).getExternalCacheDir(),
                        "temp"+System.currentTimeMillis()+".jpg"),
                width,height);
    }
    /**
     * @see #create(Object, File, File, int, int)
     * @param context 上下文
     * @param camera 照相后保存的文件
     * @param crop 裁剪后保存的文件
     * @return
     */
    public static PhotoUtils create(Object context,File camera,File crop){
        return PhotoUtils.create(context,camera,crop,crop==null?-1:0,crop==null?-1:0);
    }
    /**
     * 创建util
     * @param context 上下文
     * @param camera 照相后保存的文件
     * @param crop  裁剪后保存的文件 {@link #fileCropUri}
     * @param width 宽
     * @param height  高
     * @return
     */
    public static PhotoUtils create(Object context,File camera,File crop,int width,int height){
//        String key = camera.getAbsolutePath()+crop.getAbsolutePath()+width+height;
//        if (utilsHashMap.get(key)==null){
//            utilsHashMap.put(key,new PhotoUtils(camera,crop,width,height,context));
//        }
//        return utilsHashMap.get(key);
        return new PhotoUtils(camera,crop,width,height,context);
    }

    private PhotoUtils(File fileUri, File fileCropUri, int outputX, int outputY, Object context) {
        this.fileUri = fileUri;
        this.fileCropUri = fileCropUri;
        this.outputX = outputX;
        this.outputY = outputY;
        this.context = context;
        this.cropImageUri = Uri.fromFile(fileCropUri);
        this.imageUri = Uri.fromFile(fileUri);
    }

    /**
     * 只使用相册时调用
     * @param context
     */
    public PhotoUtils(Object context) {
        this.context = context;
        this.outputX = -1;
        this.outputY = -1;
    }

    private PopupWindow window;
    public void show(View view){
        if (window==null){
            initSelectWindows();
        }
        if (window.isShowing()){
            return;
        }
        Objects.requireNonNull(window).showAtLocation(view, Gravity.BOTTOM,0,0);
    }
    private void initSelectWindows(){
        window = new PopupWindow();
        View contentView = LayoutInflater.from(getContext(context)).inflate(R.layout.image_select_windows_layout,null);
        window.setContentView(contentView);
        window.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        window.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        contentView.setOnClickListener(v->window.dismiss());
        contentView.findViewById(R.id.itv_camera).setOnClickListener(v->autoObtainCameraPermission());
        contentView.findViewById(R.id.itv_gallery).setOnClickListener(v->autoObtainStoragePermission());
//        setCloseOnTouchOutside


    }
    /**
     * @param requestCode 调用系统相机请求码
     */
    public  void takePicture(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri

            if (context instanceof Activity) {
                imageUri = FileProvider.getUriForFile(((Activity) context).getApplicationContext(),
                        "com.linkever.fileprovider", fileUri);
            } else if (context instanceof Fragment) {
                imageUri = FileProvider.getUriForFile(Objects.requireNonNull(((Fragment) context).getContext()),
                        "com.linkever.fileprovider", fileUri);
            }
        }
        //调用系统相机
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intentCamera, requestCode);
        } else if (context instanceof Fragment) {
            ((Fragment) context).startActivityForResult(intentCamera, requestCode);
        }
    }

    /**
     * @param requestCode 打开相册的请求码
     */
    public  void openPic(int requestCode) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(photoPickerIntent, requestCode);
        } else if (context instanceof Fragment) {
            ((Fragment) context).startActivityForResult(photoPickerIntent, requestCode);
        }
    }

    /**
     * @param orgUri      剪裁原图的Uri
     * @param desUri      剪裁后的图片的Uri
     * @param aspectX     X方向的比例
     * @param aspectY     Y方向的比例
     * @param width       剪裁图片的宽度
     * @param height      剪裁图片高度
     * @param requestCode 剪裁图片的请求码
     */
    public  void cropImageUri( Uri orgUri, Uri desUri, int aspectX, int aspectY, int width, int height, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(orgUri, "image/*");
        //发送裁剪信号
        intent.putExtra("crop", "true");

        if (width!=-0&&height!=0){
            intent.putExtra("aspectX", width);
            intent.putExtra("aspectY", height);
            intent.putExtra("outputX", width);
            intent.putExtra("outputY", height);
        }else {
            intent.putExtra("aspectX", 0);
            intent.putExtra("aspectY", 0);
        }
        intent.putExtra("scale", true);
        //将剪切的图片保存到目标Uri中
        intent.putExtra(MediaStore.EXTRA_OUTPUT, desUri);
        //1-false用uri返回图片
        //2-true直接用bitmap返回图片（此种只适用于小图片，返回图片过大会报错）
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else if (context instanceof Fragment) {
            ((Fragment) context).startActivityForResult(intent, requestCode);
        }

    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(context), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(context), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(context), Manifest.permission.CAMERA)) {
                ToastUtil.showDefaultShortToast(getContext(context),"您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(getActivity(context), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {

                takePicture(  CODE_CAMERA_REQUEST);
            } else {
                ToastUtil.showDefaultShortToast(getContext(context),"设备没有SD卡！");
            }
        }
    }

    /**
     * 自动获取sdk权限
     */

    public void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(context), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(context), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            openPic( CODE_GALLERY_REQUEST);
        }

    }

    public void onRequestPermissionsResult( int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {

                        takePicture( CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtil.showDefaultShortToast(getContext(context),"设备没有SD卡！");
                    }
                } else {

                    ToastUtil.showDefaultShortToast(getContext(context),"请允许打开相机！！");
                }
                break;


            }
            //调用系统相册申请Sdcard权限回调
            case STORAGE_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openPic(CODE_GALLERY_REQUEST);
                } else {
                    ToastUtil.showDefaultShortToast(getContext(context),"请允许打操作SDCard！！");
                }
                break;
            default:
        }
    }

    public Uri onActivityResult( int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照完成回调
                case CODE_CAMERA_REQUEST:
                    cropImageUri(imageUri, cropImageUri, 1, 1, outputX, outputY, CODE_RESULT_REQUEST);
                    return imageUri;
                //访问相册完成回调
                case CODE_GALLERY_REQUEST:
                    if (hasSdcard()) {
                        Uri newUri = Uri.parse(getPath(data.getData()));
                        if (outputX==-1||outputY==-1){
                            return newUri;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(getContext(context), "com.tim.fileprovider", new File(newUri.getPath()));
                        }
                        cropImageUri( newUri, cropImageUri, 1, 1, outputX, outputY, CODE_RESULT_REQUEST);
                        return newUri;
                    } else {
                        ToastUtil.showDefaultShortToast(getContext(context),"设备没有SD卡！");
                    }
                case CODE_RESULT_REQUEST:
                    return cropImageUri;
                default:
            }
        }
        return null;
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public  boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }
    /**
     * 读取uri所在的图片
     *
     * @param uri      图片对应的Uri
     * @return 获取图像的Bitmap
     */
    public  Bitmap getBitmapFromUri(Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext(context).getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param uri     当前相册照片的Uri
     * @return 解析后的Uri对应的String
     */
    @SuppressLint("NewApi")
    public  String getPath(final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String pathHead = "file:///";
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(getContext(context), uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return pathHead + getDataColumn(contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return pathHead + getDataColumn(contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + getDataColumn( uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return pathHead + uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private  String getDataColumn( Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = getContext(context).
                    getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private  boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private  boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private  boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 获取Activity
     * @param context 上下文
     * @return
     */
    private static Activity getActivity(Object context){
        return Objects.requireNonNull(context instanceof Fragment ? ((Fragment) context).getActivity() : (Activity) context);
    }

    /**
     * 获取Context
     * @param context 上下文
     * @return
     */
    private static Context getContext(Object context){
        return Objects.requireNonNull(context instanceof Fragment ? ((Fragment) context).getContext() : ((Activity) context).getBaseContext());
    }
}