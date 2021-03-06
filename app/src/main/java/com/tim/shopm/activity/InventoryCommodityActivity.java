
/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tim.shopm.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.AmbientLightManager;
import com.google.zxing.client.android.BeepManager;
import com.google.zxing.client.android.FinishListener;
import com.google.zxing.client.android.InactivityTimer;
import com.google.zxing.client.android.ViewfinderView;
import com.google.zxing.client.android.camera.CameraManager;
import com.tim.shopm.CaptureActivityHandler;
import com.tim.shopm.R;
import com.tim.shopm.base.CaptureActivity;
import com.tim.shopm.entity.Commodity;
import com.tim.shopm.entity.Inventory;
import com.tim.shopm.entity.InventoryCommodity;
import com.tim.shopm.model.DataModel;
import com.tim.shopm.model.LoadDataCallBack;
import com.tim.shopm.utils.DialogUtil;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * This activity opens the camera and does the actual scanning on a background thread. It draws a
 * viewfinder to help the user place the barcode correctly, shows feedback as the image processing
 * is happening, and then overlays the results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class InventoryCommodityActivity extends CaptureActivity implements SurfaceHolder.Callback {

    private static final String TAG = InventoryCommodityActivity.class.getSimpleName();

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private Result savedResultToShow;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Collection<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private AmbientLightManager ambientLightManager;
    private List<InventoryCommodity> commodities = new ArrayList<>();
    private RecyclerView rv_content;
    private SlimAdapter adapter;
    private EditText et_title;
    private long id;
    public static void start(Context context) {
        context.startActivity(new Intent(context,InventoryCommodityActivity.class));
    }
    public static void start(Context context,long id) {
        Intent intent = new Intent(context,InventoryCommodityActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        id = getIntent().getLongExtra("id",-1);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_inventory_commodity);
        enableLeftButton("返回", view -> onBackPressed());
        enableRightButton("保存", view -> close());
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);
        rv_content = findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        et_title = findViewById(R.id.et_title);
        adapter = SlimAdapter.create().register(R.layout.item_out_commodity, new SlimInjector<InventoryCommodity>() {
            @Override
            public void onInject(InventoryCommodity data, IViewInjector injector) {
                injector.text(R.id.tv_name, data.getCommodity().getName())
                        .text(R.id.et_price, String.valueOf(data.getCommodity().getPrice()))
                        .text(R.id.et_num, String.valueOf(data.getNum()));
                ((EditText) injector.findViewById(R.id.et_num)).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (TextUtils.isEmpty(charSequence)) {
                            charSequence = "0";
                        }
                        while (charSequence.length() > 1 && charSequence.toString().startsWith("0")) {
                            charSequence = charSequence.toString().substring(1);
                            ((EditText) injector.findViewById(R.id.et_num)).setText(charSequence);
                            ((EditText) injector.findViewById(R.id.et_num)).setSelection(charSequence.length());
                        }
                        if (charSequence.length() > 5) {
                            charSequence = charSequence.subSequence(0, charSequence.length() - 1);
                            ((EditText) injector.findViewById(R.id.et_num)).setText(charSequence);
                            ((EditText) injector.findViewById(R.id.et_num)).setSelection(charSequence.length());
                        }
                        data.setNum(Integer.parseInt(charSequence.toString()));
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
            }
        }).attachTo(rv_content).updateData(commodities);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        initData();
    }
    private Inventory inventory;
    void close() {
       if (id==-1){
           Inventory inventory = new Inventory();
           inventory.setTime(new Date());
           inventory.setState(Inventory.ing);
           inventory.setContext(et_title.getText().toString());
           DataModel.insertInventory(inventory);
           for (Iterator<InventoryCommodity> it = commodities.iterator(); it.hasNext(); ) {
               InventoryCommodity commodity = it.next();
               commodity.setInventory_id(inventory.getId());
               DataModel.insertInventoryCommodityOrder(commodity);
           }
       }else {
           inventory.setTime(new Date());
           for (InventoryCommodity co :
                   inventory.getInventoryCommodities()) {
               co.update();
           }
           inventory.update();
       }
        finish();
    }

//    private void edit() {
//        DialogUtil.showEditWindows(this, new DialogUtil.OnSubmit() {
//            @Override
//            public void submit(String str) {
//                upData(str);
//            }
//        }, null, "请输入条形码", "手动输入", "确定", "取消", false, getCurrentFocus());
//    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
    void initData(){
        commodities = new ArrayList<>();
       if (id==-1){
           DataModel.loadCommodities(new LoadDataCallBack<List<Commodity>>() {
               @Override
               public void onSuccess(List<Commodity> commodities) {
                   for (Commodity com :
                           commodities) {
                       update1(com.getBar_code());
                   }
               }

               @Override
               public void onError(String msg, int code) {

               }
           });
       }else {
           DataModel.loadInventory(id, new LoadDataCallBack<Inventory>() {

               @Override
               public void onSuccess(Inventory inventory) {
                   InventoryCommodityActivity.this.inventory = inventory;
                    adapter.updateData(inventory.getInventoryCommodities());
               }

               @Override
               public void onError(String msg, int code) {

               }
           });
       }
    }
    void init() {

        // historyManager must be initialized here to updateCommodity the history preference

        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());
//    cameraManager.setManualFramingRect(1920, 1080);
        viewfinderView = findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;

        resetStatusView();


        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);

        inactivityTimer.onResume();


        decodeFormats = null;
        characterSet = null;
        SurfaceView surfaceView = findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        if (cameraManager != null)
            cameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler, R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    /**
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult   The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode     A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        upData(rawResult.getText());
    }

    void upData(String barCode) {
        restartPreviewAfterDelay(500);
        if (!add(barCode))
            DataModel.findCommodity(barCode, new LoadDataCallBack<Commodity>() {
                @Override
                public void onSuccess(Commodity commodity) {
                    InventoryCommodity inventoryCommodity = new InventoryCommodity();
                    inventoryCommodity.setBar_code(barCode);
                    inventoryCommodity.setNum(1);
                    inventoryCommodity.setStnum(commodity.getNum());
                    inventoryCommodity.setCommodity(commodity);
                    inventoryCommodity.setCommodity_id(commodity.getId());
                    commodities.add(inventoryCommodity);
                    adapter.updateData(commodities);
                }

                @Override
                public void onError(String msg, int code) {
                    startNewCommodity(barCode);
                }
            });
    }
    void update1(String barCode){
        if (!isIn(barCode))
            DataModel.findCommodity(barCode, new LoadDataCallBack<Commodity>() {
                @Override
                public void onSuccess(Commodity commodity) {
                    InventoryCommodity inventoryCommodity = new InventoryCommodity();
                    inventoryCommodity.setBar_code(barCode);
                    inventoryCommodity.setNum(0);
                    inventoryCommodity.setStnum(commodity.getNum());
                    inventoryCommodity.setCommodity(commodity);
                    inventoryCommodity.setCommodity_id(commodity.getId());
                    commodities.add(inventoryCommodity);
                    adapter.updateData(commodities);
                }

                @Override
                public void onError(String msg, int code) {
                    startNewCommodity(barCode);
                }
            });
    }
    boolean isIn(String barCode) {
        for (InventoryCommodity item :
                commodities) {
            if (item.getBar_code().equals(barCode)) {
                adapter.updateData(commodities);
                return true;
            }
        }
        return false;
    }
    boolean add(String barCode) {
        for (InventoryCommodity item :
                commodities) {
            if (item.getBar_code().equals(barCode)) {
                item.setNum(item.getNum() + 1);
                adapter.updateData(commodities);
                return true;
            }
        }
        return false;
    }

    private static final int CODE_ADD = 1;

    private void startNewCommodity(String bar_code) {
        startActivityForResult(NewCommodityActivity.add(this, bar_code), CODE_ADD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_ADD) {
            upData(((Commodity) data.getParcelableExtra("data")).getBar_code());
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats, null, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    protected String getPageTitle() {
        return "盘点";
    }
}
