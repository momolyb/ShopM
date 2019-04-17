package com.tim.shopm.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.tim.shopm.R;
import com.tim.shopm.base.BaseActivity;
import com.tim.shopm.entity.OtherAccount;
import com.tim.shopm.model.DataModel;

import java.util.Date;

public class OtherOutActivity extends BaseActivity {
    private EditText et_content;
    private EditText et_money;
    private RadioGroup rg;

    @Override
    protected String getPageTitle() {
        return "添加支出项";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_out);
        enableLeftButton("返回", v -> onBackPressed());
        enableRightButton("保存", view -> submit());
        initView();
    }

    private void initView() {
        et_content = findViewById(R.id.et_content);
        et_money = findViewById(R.id.et_money);
        rg = findViewById(R.id.rg);
    }

    private void submit() {
        // validate
        String content = et_content.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请填写内容", Toast.LENGTH_SHORT).show();
            return;
        }

        String money = et_money.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            Toast.makeText(this, "请填写金额", Toast.LENGTH_SHORT).show();
            return;
        }
        OtherAccount otherAccount = new OtherAccount();
        otherAccount.setContext(et_content.getText().toString());
        otherAccount.setTime(new Date());
        otherAccount.setMoney(rg.getCheckedRadioButtonId()==R.id.in?Float.parseFloat(et_money.getText().toString()):0-Float.parseFloat(et_money.getText().toString()));
        otherAccount.setType(rg.getCheckedRadioButtonId()==R.id.in ? OtherAccount.getTypeIn() : OtherAccount.getTypeOut());
        DataModel.insertOtherAcount(otherAccount);
        finish();

    }
}
