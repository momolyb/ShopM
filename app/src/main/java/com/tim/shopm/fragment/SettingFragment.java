package com.tim.shopm.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tim.common.ToastUtil;
import com.tim.shopm.R;
import com.tim.shopm.base.BaseFragment;
import com.tim.shopm.utils.DialogUtil;
import com.tim.shopm.utils.PhotoUtils;
import com.tim.shopm.utils.SharedPreferencesUtil;


import static com.tim.shopm.entity.OutOrder.TYPE_ALI;

public class SettingFragment extends BaseFragment {
    private PhotoUtils photoutil;
    private TextView tv_password;
    private TextView tv_alipay_file;
    private TextView tv_wxpay_file;

    @Override
    protected String getPageTitle() {
        return "设置";
    }

    public static final int CODE_ALIPAY = 1;
    public static final int CODE_WXPAY = 2;
    private int mode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_setting,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        SharedPreferencesUtil.initSetting(getContext());
        initDate();
    }

    void selectFile(int code) {
        mode = code;
        if (photoutil == null)
            photoutil = new PhotoUtils(this);
        photoutil.autoObtainStoragePermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        tv_password = getView().findViewById(R.id.tv_password);
        tv_alipay_file = getView().findViewById(R.id.tv_alipay_file);
        tv_wxpay_file = getView().findViewById(R.id.tv_wxpay_file);
        tv_password.setOnClickListener(view -> setPass());
        tv_alipay_file.setOnClickListener(view -> selectFile(CODE_ALIPAY));
        tv_wxpay_file.setOnClickListener(view -> selectFile(CODE_WXPAY));
    }
    private boolean ischeck = false;
    private void setPass() {
        if (!ischeck&&!TextUtils.isEmpty(SharedPreferencesUtil.get("pass"))){
            checkpass();
        }else
            DialogUtil.showEditWindows(getContext(), new DialogUtil.OnSubmit() {
                @Override
                public void submit(String str) {
                    SharedPreferencesUtil.put("pass",str);
                    ischeck = false;
                    initDate();
                }
            },null,"请输入启动密码","密码设置","确认","取消",false,getView());
    }
    private void checkpass(){
        DialogUtil.showEditWindows(getContext(), new DialogUtil.OnSubmit() {
            @Override
            public void submit(String str) {
                if (SharedPreferencesUtil.get("pass").equals(str)) {
                    ischeck = true;
                    setPass();
                }else {
                    ToastUtil.showDefaultShortToast(getContext(),"密码错误");
                    checkpass();
                }
            }
        },null,"请输入启动密码","密码验证","确认","取消",false,getView());
    }
}
