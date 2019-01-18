package com.tim.shopm.manager;

import android.content.Context;

import com.tim.shopm.greendao.DaoMaster;
import com.tim.shopm.greendao.DaoSession;

import org.greenrobot.greendao.database.Database;

public class DatabaseManager {
    private static final String NAME = "shop";



    private Context context;
    private static DaoSession mDaoSession;

    private DatabaseManager(Context context){
        this.context = context;
        setupDataBase();
    }
    private static DatabaseManager instence;

    public static DatabaseManager init(Context context) {
        if (instence==null){
            instence = new DatabaseManager(context);
        }
        return instence;
    }
    public void setupDataBase(){
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(context,NAME);
        Database db = openHelper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        mDaoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        if (mDaoSession==null){
            throw new RuntimeException("not init DatabaseManager");
        }
        return mDaoSession;
    }
}
