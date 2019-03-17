package com.tim.shopm.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tim.shopm.R;
import com.tim.shopm.activity.InCommodityActivity;
import com.tim.shopm.base.BaseFragment;
import com.tim.shopm.entity.Commodity;
import com.tim.shopm.model.DataModel;
import com.tim.shopm.model.LoadDataCallBack;
import com.tim.shopm.utils.StringFormatUtil;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimAdapterEx;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.List;

public class SummaryFragment extends BaseFragment {
    private RecyclerView rv_content;
    private SlimAdapterEx adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableRightButton(R.mipmap.add,view1 -> onRightClick());
        rv_content = view.findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = SlimAdapter.create(SlimAdapterEx.class).register(R.layout.item_product, new SlimInjector<Commodity>() {
            @Override
            public void onInject(Commodity data, IViewInjector injector) {
                injector.text(R.id.tv_name,data.getName())
                        .text(R.id.tv_num,"×"+data.getNum())
                        .text(R.id.tv_price,StringFormatUtil.formatMoney(data.getPrice()))
                        .clicked(R.id.item, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                goCommodityInfo(data);
                            }
                        })
                        .text(R.id.tv_bar_code,data.getBar_code());
            }
        }).attachTo(rv_content);
    }

    public void onRightClick() {
        startActivity(new Intent(getContext(), InCommodityActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        DataModel.loadCommodities(new LoadDataCallBack<List<Commodity>>() {
            @Override
            public void onSuccess(List<Commodity> commodities) {
                adapter.updateData(commodities);
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    private void goCommodityInfo(Commodity data) {

    }

    @Override
    protected String getPageTitle() {
        return "商品列表";
    }
}
