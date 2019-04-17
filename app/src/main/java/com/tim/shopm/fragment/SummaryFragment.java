package com.tim.shopm.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tim.shopm.R;
import com.tim.shopm.activity.InOrderInfoActivity;
import com.tim.shopm.activity.OtherOutActivity;
import com.tim.shopm.activity.OutOrderInfoActivity;
import com.tim.shopm.base.BaseFragment;
import com.tim.shopm.entity.Commodity;
import com.tim.shopm.entity.InOrder;
import com.tim.shopm.entity.OtherAccount;
import com.tim.shopm.entity.OutOrder;
import com.tim.shopm.model.DataModel;
import com.tim.shopm.model.LoadDataCallBack;
import com.tim.shopm.utils.StringFormatUtil;

import net.idik.lib.slimadapter.SlimAdapter;
import net.idik.lib.slimadapter.SlimAdapterEx;
import net.idik.lib.slimadapter.SlimInjector;
import net.idik.lib.slimadapter.viewinjector.IViewInjector;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SummaryFragment extends BaseFragment {
    private RecyclerView rv_content;
    private SlimAdapterEx adapter;
    private TextView tv_time_select;
    private TextView tv_net_receipt;
    private TextView tv_payment_for_goods;
    private TextView tv_stock;
    private TextView tv_income;
    private TextView tv_expenditure;
    private RadioButton rb_jhjl;
    private RadioButton rb_xsjl;
    private RadioButton rb_kxx;
    private RadioButton rb_qtzc;
    private RadioGroup rg;

    private Date start;
    private Date end;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_summary, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableRightButton("支出", view1 -> onRightClick());
        rv_content = view.findViewById(R.id.rv_content);
        rv_content.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_content.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        tv_time_select = view.findViewById(R.id.tv_time_select);
        tv_net_receipt = view.findViewById(R.id.tv_net_receipt);
        tv_payment_for_goods = view.findViewById(R.id.tv_payment_for_goods);
        tv_stock = view.findViewById(R.id.tv_stock);
        tv_income = view.findViewById(R.id.tv_income);
        tv_expenditure = view.findViewById(R.id.tv_expenditure);
        tv_time_select.setOnClickListener(view1 -> showTimePicker());
        adapter = SlimAdapter.create(SlimAdapterEx.class).register(R.layout.item_income, new SlimInjector<OutOrder>() {
            @Override
            public void onInject(OutOrder data, IViewInjector injector) {
                injector.text(R.id.tv_time, StringFormatUtil.dateToTime(data.getTime(), "yyyy-MM-dd HH:mm:ss"))
                        .text(R.id.tv_pay_model, data.getPayType())
                        .text(R.id.tv_count, StringFormatUtil.formatMoney(data.getMoneyCount()))
                        .clicked(R.id.item, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                goCommodityInfo(data);
                                OutOrderInfoActivity.start(getContext(),data.getId());
                            }
                        });
            }
        }).register(R.layout.item_payment_for_goods, new SlimInjector<InOrder>() {
            @Override
            public void onInject(InOrder inCommodityOrder, IViewInjector iViewInjector) {
                iViewInjector
                        .text(R.id.tv_time, StringFormatUtil.dateToTime(inCommodityOrder.getTime(), "yyyy-MM-dd HH:mm:ss"))
                        .text(R.id.tv_count, StringFormatUtil.formatMoney(inCommodityOrder.getMoneyCount()))
                        .clicked(R.id.item, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                InOrderInfoActivity.start(getContext(),inCommodityOrder.getId());
                            }
                        });
            }
        }).register(R.layout.item_stock, new SlimInjector<Commodity>() {
            @Override
            public void onInject(Commodity commodity, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_name, commodity.getName())
                        .text(R.id.tv_num, String.valueOf(commodity.getNum()))
                        .text(R.id.tv_price, StringFormatUtil.formatMoney(commodity.getPrice()))
                        .text(R.id.tv_count, StringFormatUtil.formatMoney(commodity.getNum() * commodity.getPrice()));
            }
        }).register(R.layout.item_stock, new SlimInjector<OtherAccount>() {
            @Override
            public void onInject(OtherAccount otherAccount, IViewInjector iViewInjector) {
                iViewInjector.text(R.id.tv_name, otherAccount.getContext())
                        .text(R.id.tv_num, StringFormatUtil.formatMoney(otherAccount.getMoney()))
                        .textColor(R.id.tv_num, otherAccount.getType() == OtherAccount.getTypeIn() ? Color.parseColor("#00AF4D") : Color.parseColor("#C40000"))
                        .text(R.id.tv_count, StringFormatUtil.dateToTime(otherAccount.getTime(), "yyyy-MM-dd HH:mm:ss"));
            }
        }).attachTo(rv_content);
        rg = view.findViewById(R.id.rg);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                refreshData();
            }
        });
    }

    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerDialog2;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showTimePicker() {
        if (datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setTitle("请选择开始时间");
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar time = Calendar.getInstance();
                    time.set(i, i1, i2, 0, 0, 0);
                    start = time.getTime();
                    if (datePickerDialog2 == null) {
                        datePickerDialog2 = new DatePickerDialog(getContext());
                        datePickerDialog2.setTitle("请选择结束时间");
                        datePickerDialog2.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                Calendar time = Calendar.getInstance();
                                time.set(i, i1, i2, 23, 59, 59);
                                end = time.getTime();
                                datePickerDialog2.dismiss();
                                refreshData();
                            }
                        });
                    }
                    datePickerDialog2.updateDate(i, i1, i2);
                    datePickerDialog2.show();
                    datePickerDialog.dismiss();
                }
            });
        }
        datePickerDialog.show();
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    private void refreshData() {
        if (start == null || end == null) {
            start = new Date(0);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            end = calendar.getTime();
            tv_time_select.setText("开始时间——结束时间");
        } else {
            tv_time_select.setText(dateFormat.format(start) + "——" + dateFormat.format(end));
        }
        tv_income.setText(StringFormatUtil.formatMoney(DataModel.getOutCount(start, end)));
        tv_payment_for_goods.setText(StringFormatUtil.formatMoney(DataModel.getInCount(start, end)));
        tv_stock.setText(StringFormatUtil.formatMoney(DataModel.getStock(start, end)));
        tv_expenditure.setText(StringFormatUtil.formatMoney(DataModel.getExpenditure(start, end)));
        switch (rg.getCheckedRadioButtonId()) {
            case R.id.rb_jhjl:
                DataModel.loadInOrders(new LoadDataCallBack<List<InOrder>>() {
                    @Override
                    public void onSuccess(List<InOrder> inOrders) {
                        adapter.updateData(inOrders);
                    }

                    @Override
                    public void onError(String msg, int code) {

                    }
                }, start, end);
                break;
            case R.id.rb_kxx:
                DataModel.loadCommodities(new LoadDataCallBack<List<Commodity>>() {
                    @Override
                    public void onSuccess(List<Commodity> inOrders) {
                        adapter.updateData(inOrders);
                    }

                    @Override
                    public void onError(String msg, int code) {

                    }
                });
                break;
            case R.id.rb_qtzc:
                DataModel.loadOthers(new LoadDataCallBack<List<OtherAccount>>() {
                    @Override
                    public void onSuccess(List<OtherAccount> inOrders) {
                        adapter.updateData(inOrders);
                    }

                    @Override
                    public void onError(String msg, int code) {

                    }
                }, start, end);
                break;
            case R.id.rb_xsjl:
                DataModel.loadOutOrders(new LoadDataCallBack<List<OutOrder>>() {
                    @Override
                    public void onSuccess(List<OutOrder> inOrders) {
                        adapter.updateData(inOrders);
                    }

                    @Override
                    public void onError(String msg, int code) {

                    }
                }, start, end);
                break;
        }
    }

    public void onRightClick() {
        startActivity(new Intent(getContext(), OtherOutActivity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void goCommodityInfo(Commodity data) {

    }

    @Override
    protected String getPageTitle() {
        return "看账";
    }
}
