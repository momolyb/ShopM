package com.tim.shopm.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.tim.shopm.R;
import com.tim.shopm.base.BaseActivity;
import com.tim.shopm.entity.InCommodityOrder;
import com.tim.shopm.entity.InOrder;
import com.tim.shopm.model.DataModel;
import com.tim.shopm.model.LoadDataCallBack;
import com.tim.shopm.utils.StringFormatUtil;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.List;

public class InOrderInfoActivity extends BaseActivity {
    private RecyclerView rv_content;
    private TextView tv_time;
    private TextView tv_count;
    private SlimAdapter adapter;
    private long id;
    public static void start(Context context,long id){
        Intent intent = new Intent(context,InOrderInfoActivity.class);
        intent.putExtra("id",id);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inorder_info);
        initView();
        enableLeftButton("返回",view -> onBackPressed());
        id = getIntent().getLongExtra("id",0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        DataModel.loadInOrder(new LoadDataCallBack<InOrder>() {
            @Override
            public void onSuccess(InOrder inOrder) {
                refreshView(inOrder);
            }

            @Override
            public void onError(String msg, int code) {

            }
        },id);
    }

    private void refreshView(InOrder inOrder) {
        adapter.updateData(inOrder.getInCommodityOrders());
        tv_count.setText(StringFormatUtil.formatMoney(inOrder.getMoneyCount()));
        tv_time.setText(StringFormatUtil.dateToTime(inOrder.getTime(),"yyyy-MM-dd HH:mm:ss"));
    }


    @Override
    protected String getPageTitle() {
        return "进货详情";
    }

    private void initView() {
        rv_content = findViewById(R.id.rv_content);
        tv_time = findViewById(R.id.tv_time);
        tv_count = findViewById(R.id.tv_count);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        adapter = SlimAdapter.create().register(R.layout.item_out_commodity_info, new SlimInjector<InCommodityOrder>() {
            @Override
            public void onInject(InCommodityOrder inCommodityOrder, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_name,inCommodityOrder.getBindCommodity().getName())
                        .text(R.id.et_price, StringFormatUtil.formatMoney(inCommodityOrder.getPrice()))
                        .text(R.id.et_num,String.valueOf(inCommodityOrder.getNum()));
            }
        }).attachTo(rv_content);
    }
}
