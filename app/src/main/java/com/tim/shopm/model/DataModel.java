package com.tim.shopm.model;

import android.database.Cursor;

import com.tim.shopm.entity.Commodity;
import com.tim.shopm.entity.InCommodityOrder;
import com.tim.shopm.entity.InOrder;
import com.tim.shopm.entity.Inventory;
import com.tim.shopm.entity.InventoryCommodity;
import com.tim.shopm.entity.OtherAccount;
import com.tim.shopm.entity.OutCommodityOrder;
import com.tim.shopm.entity.OutOrder;
import com.tim.shopm.greendao.CommodityDao;
import com.tim.shopm.greendao.InCommodityOrderDao;
import com.tim.shopm.greendao.InOrderDao;
import com.tim.shopm.greendao.OtherAccountDao;
import com.tim.shopm.greendao.OutCommodityOrderDao;
import com.tim.shopm.greendao.OutOrderDao;
import com.tim.shopm.manager.DatabaseManager;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataModel {
    public static InCommodityOrder lastInCommodityOrder() {
        DatabaseManager.getDaoSession().clear();
        List<InCommodityOrder> list = DatabaseManager.getDaoSession().getInCommodityOrderDao().loadAll();
        if (list.size() > 0) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    public static void removeInOrder(Long id) {
        DatabaseManager.getDaoSession().getInOrderDao().deleteByKey(id);
    }
    public static void synchronousData(long id){
        DatabaseManager.getDaoSession().clear();
        Inventory inventorie = DatabaseManager.getDaoSession().getInventoryDao().load(id);
        for (InventoryCommodity in :
                inventorie.getInventoryCommodities()) {
                updateCommodityNum(in.getCommodity_id(),in.getNum());
        }
        inventorie.setState(Inventory.stop);
        inventorie.update();
    }
    private static void updateCommodityNum(long id,int num){
        DatabaseManager.getDaoSession().clear();
        Commodity commodity = DatabaseManager.getDaoSession().getCommodityDao().load(id);
        commodity.setNum(num);
        DatabaseManager.getDaoSession().getCommodityDao().update(commodity);
    }
    public static float getInCount(Date start, Date end) {
        String sql = "select SUM("
                + InCommodityOrderDao.Properties.Price.columnName
                + "*"
                + InCommodityOrderDao.Properties.Num.columnName
                + ") as COUNT from "
                + InCommodityOrderDao.TABLENAME
                + " Where "
                + InCommodityOrderDao.Properties.Time.columnName + ">=" + start.getTime()
                + " AND " + InCommodityOrderDao.Properties.Time.columnName + "<=" + end.getTime();
        Cursor c = DatabaseManager.getDaoSession().getDatabase().rawQuery(sql, null);
        if (c.getCount() == 1) {
            c.moveToNext();
            return c.getFloat(0);
        }
        return 0;
    }

    public static float getOutCount(Date start, Date end) {
        String sql = "select SUM("
                + OutCommodityOrderDao.Properties.Price.columnName
                + "*"
                + OutCommodityOrderDao.Properties.Num.columnName
                + ") as COUNT from "
                + OutCommodityOrderDao.TABLENAME
                + " Where "
                + OutCommodityOrderDao.Properties.Time.columnName + ">=" + start.getTime()
                + " AND " + OutCommodityOrderDao.Properties.Time.columnName + "<=" + end.getTime();
        Cursor c = DatabaseManager.getDaoSession().getDatabase().rawQuery(sql, null);
        if (c.getCount() == 1) {
            c.moveToNext();
            return c.getFloat(0);
        }
        return 0;
    }

    public static float getStock(Date start, Date end) {
        String sql = "select SUM("
                + CommodityDao.Properties.Price.columnName
                + "*"
                + CommodityDao.Properties.Num.columnName
                + ") as COUNT from "
                + CommodityDao.TABLENAME;
        Cursor c = DatabaseManager.getDaoSession().getDatabase().rawQuery(sql, null);
        if (c.getCount() == 1) {
            c.moveToNext();
            return c.getFloat(0);
        }
        return 0;
    }

    public static float getExpenditure(Date start, Date end) {
        String sql = "select SUM("
                + OtherAccountDao.Properties.Money.columnName
                + ") as COUNT from "
                + OtherAccountDao.TABLENAME
                + " Where "
                + OtherAccountDao.Properties.Time.columnName + ">=" + start.getTime()
                + " AND " + OtherAccountDao.Properties.Time.columnName + "<=" + end.getTime();
        Cursor c = DatabaseManager.getDaoSession().getDatabase().rawQuery(sql, null);
        if (c.getCount() == 1) {
            c.moveToNext();
            return c.getFloat(0);
        }
        return 0;
    }

    public static enum ERROR {
        NOT_ROUND("未找到", -1);

        private final String value;
        private final int code;

        ERROR(String value, int code) {
            this.value = value;
            this.code = code;
        }
    }

    public static void loadCommodities(LoadDataCallBack<List<Commodity>> callBack) {
        DatabaseManager.getDaoSession().clear();
        List<Commodity> commodities = DatabaseManager.getDaoSession().getCommodityDao().loadAll();
        callBack.onSuccess(commodities);
    }
    public static void loadInventorys(LoadDataCallBack<List<Inventory>> callBack) {
        DatabaseManager.getDaoSession().clear();
        List<Inventory> inventories = DatabaseManager.getDaoSession().getInventoryDao().loadAll();
        callBack.onSuccess(inventories);
    }
    public static void loadInventory(long id,LoadDataCallBack<Inventory> callBack) {
        DatabaseManager.getDaoSession().clear();
        Inventory inventories = DatabaseManager.getDaoSession().getInventoryDao().load(id);
        callBack.onSuccess(inventories);
    }
    public static void loadInOrders(LoadDataCallBack<List<InOrder>> callBack, Date start, Date end) {
        DatabaseManager.getDaoSession().clear();
        List<InOrder> inOrders = DatabaseManager.getDaoSession().getInOrderDao().queryBuilder().where(InOrderDao.Properties.Time.ge(start.getTime()), InOrderDao.Properties.Time.le(end.getTime())).list();
            callBack.onSuccess(inOrders);
    }
    public static void loadInOrder(LoadDataCallBack<InOrder> callBack, long id) {
        DatabaseManager.getDaoSession().clear();
        InOrder inOrder = DatabaseManager.getDaoSession().getInOrderDao().load(id);
        callBack.onSuccess(inOrder);
    }
    public static void loadOutOrders(LoadDataCallBack<List<OutOrder>> callBack, Date start, Date end) {
        DatabaseManager.getDaoSession().clear();
        List<OutOrder> outOrders = DatabaseManager.getDaoSession().getOutOrderDao().queryBuilder().where(OutOrderDao.Properties.Time.ge(start.getTime()), OutOrderDao.Properties.Time.le(end.getTime())).list();
            callBack.onSuccess(outOrders);
    }
    public static void loadOutOrder(LoadDataCallBack<OutOrder> callBack, long id) {
        DatabaseManager.getDaoSession().clear();
        OutOrder outOrder = DatabaseManager.getDaoSession().getOutOrderDao().load(id);
        callBack.onSuccess(outOrder);
    }
    public static void loadOthers(LoadDataCallBack<List<OtherAccount>> callBack, Date start, Date end) {
        DatabaseManager.getDaoSession().clear();
        List<OtherAccount> otherAccounts = DatabaseManager.getDaoSession().getOtherAccountDao().queryBuilder().where(OtherAccountDao.Properties.Time.ge(start.getTime()), OtherAccountDao.Properties.Time.le(end.getTime())).list();
            callBack.onSuccess(otherAccounts);
    }

    public static void loadCommodity(long id) {
        DatabaseManager.getDaoSession().clear();
        DatabaseManager.getDaoSession().getCommodityDao().load(id);
    }

    public static void insertCommodity(Commodity entity, LoadDataCallBack<Long> callBack) {
        callBack.onSuccess(DatabaseManager.getDaoSession().getCommodityDao().insert(entity));
    }

    public static void insertInCommodityOrder(InCommodityOrder entity) {
        DatabaseManager.getDaoSession().getInCommodityOrderDao().insert(entity);
    }

    public static void insertOutCommodityOrder(OutCommodityOrder entity) {
        DatabaseManager.getDaoSession().getOutCommodityOrderDao().insert(entity);
    }
    public static void insertInventoryCommodityOrder(InventoryCommodity entity) {
        DatabaseManager.getDaoSession().getInventoryCommodityDao().insert(entity);
    }
    public static void insertOtherAcount(OtherAccount entity) {
        DatabaseManager.getDaoSession().getOtherAccountDao().insert(entity);
    }
    public static void insertInOrder(InOrder entity) {
        DatabaseManager.getDaoSession().getInOrderDao().insert(entity);
    }

    public static void insertOutOrder(OutOrder entity) {
        DatabaseManager.getDaoSession().getOutOrderDao().insert(entity);
    }
    public static void insertInventory(Inventory entity) {
        DatabaseManager.getDaoSession().getInventoryDao().insert(entity);
    }
    public static void removeCommodity(long id) {
        DatabaseManager.getDaoSession().getCommodityDao().deleteByKey(id);
    }

    public static void addCommodity(Commodity entity) {
        DatabaseManager.getDaoSession().clear();
        Commodity temp = DatabaseManager.getDaoSession().getCommodityDao().load(entity.getId());
        temp.setNum(entity.getNum() + temp.getNum());
        updateCommodity(temp);
    }

    public static void updateCommodity(Commodity entity) {
        DatabaseManager.getDaoSession().getCommodityDao().update(entity);
    }

    public static void sellCommodity(Commodity entity) {
        DatabaseManager.getDaoSession().clear();
        Commodity temp = DatabaseManager.getDaoSession().getCommodityDao().load(entity.getId());
        temp.setNum(temp.getNum() - entity.getNum());
        updateCommodity(temp);
    }

    public static void findCommodity(String barcode, LoadDataCallBack<Commodity> callBack) {
        DatabaseManager.getDaoSession().clear();
        List<Commodity> temp = DatabaseManager.getDaoSession().getCommodityDao().queryRaw("WHERE bar_code = ?", barcode);
        if (temp.size() > 0) {
            callBack.onSuccess(temp.get(0));
        } else {
            callBack.onError(ERROR.NOT_ROUND.value, ERROR.NOT_ROUND.code);
        }

    }
}
