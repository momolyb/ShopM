package com.tim.shopm;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tim.common.PermissionsUtils;
import com.tim.shopm.activity.SellCommodityActivity;
import com.tim.shopm.adapter.MyPagerAdapter;
import com.tim.shopm.fragment.InventoryFragment;
import com.tim.shopm.fragment.ProductManagerFragment;
import com.tim.shopm.fragment.SettingFragment;
import com.tim.shopm.fragment.SummaryFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp_content;
    private TabLayout tl_content_indicator;
    private ArrayList<Fragment> fragmentList;
    private ArrayList<ItemTable> list_Title;
    private int selected = 0;

    public class ItemTable {
        public String title;
        public int icon;

        public ItemTable(String title, int icon) {
            this.title = title;
            this.icon = icon;
        }
    }

    {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ProductManagerFragment());
        fragmentList.add(new SummaryFragment());
        fragmentList.add(new InventoryFragment());
        fragmentList.add(new SettingFragment());
        list_Title = new ArrayList<>();
        list_Title.add(new ItemTable("商品", R.drawable.icon_sp));
        list_Title.add(new ItemTable("看账", R.drawable.icon_kz));
        list_Title.add(new ItemTable("盘点", R.drawable.icon_pd));
        list_Title.add(new ItemTable("设置", R.drawable.icon_sz));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        try {
            PermissionsUtils.requestPermissions(this, 0, null, getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS).requestedPermissions);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        vp_content = findViewById(R.id.vp_content);
        tl_content_indicator = findViewById(R.id.tl_content_indicator);
        vp_content.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getApplicationContext(), fragmentList, list_Title));
        tl_content_indicator.removeAllTabs();
        for (ItemTable str :
                list_Title) {
            tl_content_indicator.addTab(tl_content_indicator.newTab().setCustomView(R.layout.layout_tab).setText(str.title).setIcon(str.icon));
        }
        findViewById(R.id.btn_sell).setOnClickListener(view -> startSellActivity());
        tl_content_indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
//                switch (tab.getPosition()) {
//                    case 1:
//                        startSellActivity();
//                        tl_content_indicator.getTabAt(selected).select();
//                        break;
//                    default:
                selected = tab.getPosition();
                vp_content.setCurrentItem(tab.getPosition());
//                        break;
//                }
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    private void startSellActivity() {
        Intent intent = new Intent(this, SellCommodityActivity.class);
        startActivity(intent);
    }
}
