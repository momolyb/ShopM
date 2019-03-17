package com.tim.shopm.base;

import android.graphics.Bitmap;
import android.os.Handler;

import com.google.zxing.Result;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;

public abstract class CaptureActivity extends BaseActivity{
    public abstract ViewfinderView getViewfinderView();

    public abstract void handleDecode(Result obj, Bitmap barcode, float scaleFactor);

    public abstract void drawViewfinder();

    public abstract CameraManager getCameraManager();

    public abstract Handler getHandler();
}
