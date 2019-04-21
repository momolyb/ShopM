package com.tim.shopm.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.tim.shopm.R;
import com.tim.shopm.base.BaseActivity;
import com.tim.shopm.entity.Inventory;
import com.tim.shopm.entity.InventoryCommodity;
import com.tim.shopm.model.DataModel;
import com.tim.shopm.model.LoadDataCallBack;
import com.tim.shopm.utils.DialogUtil;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

public class InventoryInfoActivity extends BaseActivity {
    private RecyclerView rv_content;
    private long id;
    private SlimAdapter adapter;

    public static void start(Context context, long id) {
        Intent intent = new Intent(context, InventoryInfoActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_info);
        rv_content = findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new LinearLayoutManager(this));
        enableLeftButton("返回", view -> onBackPressed());

        id = getIntent().getLongExtra("id", -1);
        adapter = SlimAdapter.create().register(R.layout.item_inventory_info, new SlimInjector<InventoryCommodity>() {
            @Override
            public void onInject(InventoryCommodity data, IViewInjector injector) {
                injector.text(R.id.tv_title, data.getCommodity().getName()).text(R.id.tv_count, "账面数量：" + data.getStnum()).text(R.id.tv_count2, "盘点数量：" + data.getNum());
                if (data.getNum() != data.getStnum()) {
                    injector.textColor(R.id.tv_state, data.getNum() < data.getStnum() ? Color.parseColor("#D82727") : Color.parseColor("#0AAC25")).background(R.id.tv_state, data.getNum() < data.getStnum() ? R.drawable.tv_state_bg : R.drawable.tv_state_bg_1).text(R.id.tv_state, data.getNum() < data.getStnum() ? "亏" : "盈").visibility(R.id.tv_state, View.VISIBLE);
                } else {
                    injector.visibility(R.id.tv_state, View.GONE);
                }
            }
        }).attachTo(rv_content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    void loadData() {
        DataModel.loadInventory(id, new LoadDataCallBack<Inventory>() {
            @Override
            public void onSuccess(Inventory inventory) {
                if (inventory.getState() != Inventory.stop) {
                    enableRight2Button("关闭", view -> close());
                    enableRightButton("继续", view -> InventoryCommodityActivity.start(InventoryInfoActivity.this, id));
                }
                adapter.updateData(inventory.getInventoryCommodities());
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    private void close() {
        DialogUtil.showAskWindows(this, findViewById(R.id.ll), "警告", "是否确认关闭，关闭后账面数据将同步为盘点的数量！！！", "关闭", "取消", false, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshData();
                finish();
            }
        }, null);
    }

    private void refreshData() {
        DataModel.synchronousData(id);
    }

    @Override
    protected String getPageTitle() {
        return "盘点详情";
    }
}
