package com.tim.shopm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tim.shopm.R;
import com.tim.shopm.base.BaseActivity;
import com.tim.shopm.entity.Commodity;
import com.tim.shopm.model.DataModel;
import com.tim.shopm.model.LoadDataCallBack;
import com.tim.shopm.utils.DialogUtil;
import com.tim.shopm.utils.StringFormatUtil;

public class NewCommodityActivity extends BaseActivity {
    public static final String MODE_NEW = "new";
    public static final String MODE_ADD = "add";
    public static final String MODE_EDIT = "edit";
    private EditText mEtName;
    private EditText mEtBarCode;
    private EditText mEtPrice;
    private TextView mTvNum;
    private TextView mBtnDel;
    private TextView mBtnSave;
    private TextView mBtnCreate;

    private Commodity commodity;
    private String barCode;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_commodity);
        initView();
        initDate();
    }

    private void setEditable(boolean canEdit){
        mEtPrice.setEnabled(canEdit);
        mEtName.setEnabled(canEdit);
        mEtBarCode.setEnabled(canEdit && TextUtils.isEmpty(barCode));
    }
    public static Intent add(Context context, String barcode) {
        Intent intent = new Intent(context,NewCommodityActivity.class);
        intent.putExtra("bar_code",barcode);
        intent.putExtra("mode",MODE_ADD);
        return intent;
    }
    public static Intent create(Context context) {
        Intent intent = new Intent(context,NewCommodityActivity.class);
        intent.putExtra("mode",MODE_NEW);
        return intent;
    }
    public static Intent edit(Context context, String mode, Commodity commodity) {
        Intent intent = new Intent(context,NewCommodityActivity.class);
        intent.putExtra("data",commodity);
        intent.putExtra("mode",MODE_EDIT);
        return intent;
    }
    private void initDate() {
         mode = getIntent().getStringExtra("mode");

        switch (mode){
            case MODE_ADD:
                barCode = getIntent().getStringExtra("bar_code");
                commodity = new Commodity();
                if (!TextUtils.isEmpty(barCode)) {
                    commodity.setBar_code(barCode);
                }
                setEditable(true);
                mBtnCreate.setVisibility(View.VISIBLE);
                mBtnSave.setVisibility(View.GONE);
                mBtnDel.setVisibility(View.GONE);
                break;
            case MODE_NEW:
                commodity = new Commodity();
                setEditable(true);
                mBtnCreate.setVisibility(View.VISIBLE);
                mBtnSave.setVisibility(View.GONE);
                mBtnDel.setVisibility(View.GONE);
                break;
            case MODE_EDIT:
                commodity = getIntent().getParcelableExtra("data");
                setEditable(true);
                mBtnCreate.setVisibility(View.GONE);
                mBtnSave.setVisibility(View.VISIBLE);
                mBtnDel.setVisibility(View.VISIBLE);
                break;
        }
        refreshView();
    }
    void refreshView(){
        mEtName.setText(commodity.getName());
        if (commodity.getPrice()>0){
            mEtPrice.setText(StringFormatUtil.formatMoneyEdit(commodity.getPrice()));
        }else {
            mEtPrice.setText("");
        }
        mEtBarCode.setText(commodity.getBar_code());
        mTvNum.setText(String.valueOf(commodity.getNum()));
    }
    private void initView() {
        mEtName = findViewById(R.id.et_name);
        mEtBarCode = findViewById(R.id.et_bar_code);
        mEtPrice = findViewById(R.id.et_price);
        mTvNum = findViewById(R.id.tv_num);
        mBtnDel = findViewById(R.id.btn_del);
        mBtnSave = findViewById(R.id.btn_save);
        mBtnCreate = findViewById(R.id.btn_create);
        mBtnCreate.setOnClickListener(view -> submit());
        mBtnSave.setOnClickListener(view -> submit());
        mBtnDel.setOnClickListener(view -> delete());
        mEtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commodity.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try{
                    if (!TextUtils.isEmpty(charSequence))
                        commodity.setPrice(Float.parseFloat(charSequence.toString()));
                }catch (Exception e){
                    mEtPrice.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mEtBarCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                commodity.setBar_code(charSequence.toString());
                DataModel.findCommodity(charSequence.toString().trim(), new LoadDataCallBack<Commodity>() {
                    @Override
                    public void onSuccess(Commodity commodity) {
                        if (MODE_ADD.equals(mode)){
                            setEditable(false);
                        }
                        NewCommodityActivity.this.commodity = commodity;
                        refreshView();
                    }

                    @Override
                    public void onError(String msg, int code) {

                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void delete() {
        DataModel.removeCommodity(commodity.getId());
        Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
        finish();
    }

    private void submit() {
        // validate
        String name = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "请输入商品名称", Toast.LENGTH_SHORT).show();
            return;
        }

        String code = mEtBarCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "请输入商品条码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (MODE_ADD.equals(mode)&&!mEtName.isEnabled()){
            Intent data = new Intent();
            data.putExtra("data",commodity);
            setResult(RESULT_OK,data);
            finish();
            return;
        }
        if (commodity.getId()==null) {
            DataModel.insertCommodity(commodity, new LoadDataCallBack<Long>() {
                @Override
                public void onSuccess(Long aLong) {
                    if (mode.equals(MODE_ADD)) {
                        commodity.setId(aLong);
                        Intent data = new Intent();
                        data.putExtra("data", commodity);
                        setResult(RESULT_OK, data);
                    } else {
                        setResult(RESULT_OK);
                    }
                    finish();
                }

                @Override
                public void onError(String msg, int code) {

                }
            });
        }else {
            DialogUtil.showAskWindows(this, getWindow().getDecorView(), "提示", "已存在该商品是否更新数据", "更新", "取消", false, view -> update(),null);
        }
    }

    private void update() {
        DataModel.updateCommodity(commodity);
    }

    protected String getPageTitle() {
        return "商品编辑";
    }
}
