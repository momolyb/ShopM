package com.tim.shopm;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tim.shopm.adapter.MyPagerAdapter;
import com.tim.shopm.fragment.ProductManagerFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ViewPager vp_content;
    private TabLayout tl_content_indicator;
    private ArrayList<Fragment> fragmentList;
    private ArrayList<String> list_Title;

    {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ProductManagerFragment());
        fragmentList.add(new Fragment());
        fragmentList.add(new Fragment());
        fragmentList.add(new Fragment());
        list_Title = new ArrayList<>();
        list_Title.add("商品");
        list_Title.add("卖货");
        list_Title.add("盘点");
        list_Title.add("看账");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        vp_content = findViewById(R.id.vp_content);
        tl_content_indicator = findViewById(R.id.tl_content_indicator);
        vp_content.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), getApplicationContext(), fragmentList, list_Title));
        tl_content_indicator.removeAllTabs();
        for (String str :
                list_Title) {
            tl_content_indicator.addTab(tl_content_indicator.newTab().setCustomView(R.layout.layout_tab).setText(str));
        }
        tl_content_indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                vp_content.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }
}
