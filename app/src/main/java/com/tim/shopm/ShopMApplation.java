package com.tim.shopm;

import android.app.Application;

import com.tim.shopm.manager.DatabaseManager;
import com.tim.shopm.utils.SharedPreferencesUtil;

public class ShopMApplation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.init(this);
        SharedPreferencesUtil.initSetting(this);
    }
}
