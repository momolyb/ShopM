package com.tim.shopm;

import android.app.Application;

import com.tim.shopm.manager.DatabaseManager;

public class ShopMApplation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DatabaseManager.init(this);
    }
}
