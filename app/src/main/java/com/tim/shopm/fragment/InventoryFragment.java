package com.tim.shopm.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tim.shopm.R;
import com.tim.shopm.activity.InventoryCommodityActivity;
import com.tim.shopm.activity.InventoryInfoActivity;
import com.tim.shopm.base.BaseFragment;
import com.tim.shopm.entity.Inventory;
import com.tim.shopm.entity.InventoryCommodity;
import com.tim.shopm.model.DataModel;
import com.tim.shopm.model.LoadDataCallBack;
import com.tim.shopm.utils.StringFormatUtil;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.List;

public class InventoryFragment extends BaseFragment {
    private RecyclerView rv_content;
    private SlimAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv_content = view.findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new LinearLayoutManager(getContext()));
        enableRightButton("新建", view1 -> createInventory());
        rv_content.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        adapter = SlimAdapter.create().register(R.layout.item_inventory_log, new SlimInjector<Inventory>() {
            @Override
            public void onInject(Inventory inventory, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_title, inventory.getContext())
                        .text(R.id.tv_time, StringFormatUtil.dateToTime(inventory.getTime(), "yyyy-MM-dd HH:mm:ss"))
                        .text(R.id.tv_state, inventory.getState() == Inventory.ing ? "进行中" : "已结束")
                        .textColor(R.id.tv_state, inventory.getState() == Inventory.ing ? Color.parseColor("#00AF4D") : Color.parseColor("#C40000"))
                        .text(R.id.tv_rst, inventoryResq(inventory) ? "正常" : "异常")
                        .textColor(R.id.tv_rst, inventoryResq(inventory) ? Color.parseColor("#00AF4D") : Color.parseColor("#C40000"))
                        .clicked(R.id.item, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InventoryInfoActivity.start(getContext(),inventory.getId());
                            }
                        });
            }
        }).attachTo(rv_content);
    }

    @Override
    public void onResume() {
        super.onResume();
        DataModel.loadInventorys(new LoadDataCallBack<List<Inventory>>() {
            @Override
            public void onSuccess(List<Inventory> inventories) {
                adapter.updateData(inventories);
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    private boolean inventoryResq(Inventory inventory) {
        for (InventoryCommodity inventoryCommodity : inventory.getInventoryCommodities()
        ) {
            if (inventoryCommodity.getNum() != inventoryCommodity.getStnum()) {
                return false;
            }
        }
        return true;
    }

    private void createInventory() {
        InventoryCommodityActivity.start(getContext());
    }

    @Override
    protected String getPageTitle() {
        return "盘点";
    }
}
