package com.tim.shopm.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.tim.common.ToastUtil;
import com.tim.shopm.R;
import com.tim.shopm.base.BaseActivity;
import com.tim.shopm.utils.SharedPreferencesUtil;

import java.io.File;
import java.net.URI;

import static com.tim.shopm.entity.OutOrder.TYPE_ALI;

public class PayActivity extends BaseActivity {
    private ImageView iv_pay;

    public static Intent getIntent(Context context, int mode) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("mode", mode);
        return intent;
    }

    private int model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        enableLeftButton("返回", view -> onBackPressed());
        enableRightButton("确认", view -> pay());
        model = getIntent().getIntExtra("mode", 0);
        init();
    }

    private void init() {
        iv_pay = findViewById(R.id.iv_pay);
        SharedPreferencesUtil.initSetting(this);
        if (TextUtils.isEmpty(SharedPreferencesUtil.get(TYPE_ALI == model ? "alipay" : "wxpay"))) {
            ToastUtil.showDefaultShortToast(this,"未设置支付二维码");
            startActivityForResult(SettingActivity.getIntent(this),1);
            return;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(new File(URI.create(SharedPreferencesUtil.get(TYPE_ALI == model ? "alipay" : "wxpay"))).getAbsolutePath());
        iv_pay.setImageBitmap(bitmap);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            init();
        }
    }

    private void pay() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected String getPageTitle() {
        return "支付";
    }
}
