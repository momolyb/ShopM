package com.tim.shopm.model;

import com.tim.shopm.entity.Commodity;
import com.tim.shopm.entity.InCommodityOrder;
import com.tim.shopm.entity.InOrder;
import com.tim.shopm.entity.OutCommodityOrder;
import com.tim.shopm.entity.OutOrder;
import com.tim.shopm.manager.DatabaseManager;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    public static InCommodityOrder lastInCommodityOrder() {
        DatabaseManager.getDaoSession().clear();
        List<InCommodityOrder> list = DatabaseManager.getDaoSession().getInCommodityOrderDao().loadAll();
        if (list.size()>0){
            return list.get(list.size()-1);
        }
        return null;
    }

    public static void removeInOrder(Long id) {
        DatabaseManager.getDaoSession().getInOrderDao().deleteByKey(id);
    }

    public static enum ERROR{
        NOT_ROUND("未找到",-1);

        private final String value;
        private final int code;

        ERROR(String value, int code) {
            this.value = value;
            this.code = code;
        }
    }
    public static void loadCommodities(LoadDataCallBack<List<Commodity>> callBack){
        DatabaseManager.getDaoSession().clear();
        List<Commodity> commodities = DatabaseManager.getDaoSession().getCommodityDao().loadAll();
        if (commodities.size()>0){
            callBack.onSuccess(commodities);
        }else {
            callBack.onError(ERROR.NOT_ROUND.value,ERROR.NOT_ROUND.code);
        }
    }
    public static void loadCommodity(long id){
        DatabaseManager.getDaoSession().clear();
       DatabaseManager.getDaoSession().getCommodityDao().load(id);
    }
    public static void insertCommodity(Commodity entity,LoadDataCallBack<Long> callBack){
        callBack.onSuccess(DatabaseManager.getDaoSession().getCommodityDao().insert(entity));
    }
    public static void insertInCommodityOrder(InCommodityOrder entity){
        DatabaseManager.getDaoSession().getInCommodityOrderDao().insert(entity);
    }
    public static void insertOutCommodityOrder(OutCommodityOrder entity){
        DatabaseManager.getDaoSession().getOutCommodityOrderDao().insert(entity);
    }
    public static void insertInOrder(InOrder entity){
       DatabaseManager.getDaoSession().getInOrderDao().insert(entity);
    }
    public static void insertOutOrder(OutOrder entity){
        DatabaseManager.getDaoSession().getOutOrderDao().insert(entity);
    }
    public static void removeCommodity(long id){
        DatabaseManager.getDaoSession().getCommodityDao().deleteByKey(id);
    }
    public static void addCommodity(Commodity entity){
        DatabaseManager.getDaoSession().clear();
        Commodity temp = DatabaseManager.getDaoSession().getCommodityDao().load(entity.getId());
        temp.setNum(entity.getNum()+temp.getNum());
        updateCommodity(temp);
    }
    public static void updateCommodity(Commodity entity){
        DatabaseManager.getDaoSession().getCommodityDao().update(entity);
    }
    public static void sellCommodity(Commodity entity){
        DatabaseManager.getDaoSession().clear();
        Commodity temp = DatabaseManager.getDaoSession().getCommodityDao().load(entity.getId());
        temp.setNum(temp.getNum()-entity.getNum());
        updateCommodity(temp);
    }
    public static void findCommodity(String barcode,LoadDataCallBack<Commodity> callBack) {
        DatabaseManager.getDaoSession().clear();
        List<Commodity> temp = DatabaseManager.getDaoSession().getCommodityDao().queryRaw("WHERE bar_code = ?",barcode);
        if (temp.size()>0){
            callBack.onSuccess(temp.get(0));
        }else {
            callBack.onError(ERROR.NOT_ROUND.value,ERROR.NOT_ROUND.code);
        }

    }
}
