package com.tim.shopm.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.TextView;

import com.tim.common.ToastUtil;
import com.tim.shopm.R;
import com.tim.shopm.base.BaseActivity;
import com.tim.shopm.utils.DialogUtil;
import com.tim.shopm.utils.PhotoUtils;
import com.tim.shopm.utils.SharedPreferencesUtil;


import static com.tim.shopm.entity.OutOrder.TYPE_ALI;

public class SettingActivity extends BaseActivity {
    private PhotoUtils photoutil;
    private TextView tv_password;
    private TextView tv_alipay_file;
    private TextView tv_wxpay_file;

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected String getPageTitle() {
        return "设置";
    }

    public static final int CODE_ALIPAY = 1;
    public static final int CODE_WXPAY = 2;
    private int mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
        SharedPreferencesUtil.initSetting(this);
        enableLeftButton(R.drawable.ic_launcher_foreground, view -> onBackPressed());
        initDate();
    }

    void selectFile(int code) {
        mode = code;
        if (photoutil == null)
            photoutil = new PhotoUtils(this);
        photoutil.autoObtainStoragePermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = photoutil.onActivityResult(requestCode, resultCode, data);
        if (uri != null) {
            SharedPreferencesUtil.put(TYPE_ALI == mode ? "alipay" : "wxpay", photoutil.getPath(uri));
            initDate();
        }
    }

    private void initDate() {
        tv_password.setText(TextUtils.isEmpty(SharedPreferencesUtil.get("pass"))?"未设置密码":"已设置密码");
        tv_alipay_file.setText(TextUtils.isEmpty(SharedPreferencesUtil.get("alipay"))?"未设置二维码":"已设置二维码");
        tv_wxpay_file.setText(TextUtils.isEmpty(SharedPreferencesUtil.get("wxpay"))?"未设置二维码":"已设置二维码");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        photoutil.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initView() {
        tv_password = findViewById(R.id.tv_password);
        tv_alipay_file = findViewById(R.id.tv_alipay_file);
        tv_wxpay_file = findViewById(R.id.tv_wxpay_file);
        tv_password.setOnClickListener(view -> setPass());
        tv_alipay_file.setOnClickListener(view -> selectFile(CODE_ALIPAY));
        tv_wxpay_file.setOnClickListener(view -> selectFile(CODE_WXPAY));
    }
    private boolean ischeck = false;
    private void setPass() {
        if (!ischeck&&!TextUtils.isEmpty(SharedPreferencesUtil.get("pass"))){
            checkpass();
        }else
        DialogUtil.showEditWindows(this, new DialogUtil.OnSubmit() {
            @Override
            public void submit(String str) {
                SharedPreferencesUtil.put("pass",str);
                initDate();
            }
        },null,"请输入启动密码","密码设置","确认","取消",false,getCurrentFocus());
    }
    private void checkpass(){
        DialogUtil.showEditWindows(this, new DialogUtil.OnSubmit() {
            @Override
            public void submit(String str) {
                if (SharedPreferencesUtil.get("pass").equals(str))
                setPass();
                else {
                    ToastUtil.showDefaultShortToast(SettingActivity.this,"密码错误");
                    checkpass();
                }
            }
        },null,"请输入启动密码","密码验证","确认","取消",false,getCurrentFocus());
    }
}
