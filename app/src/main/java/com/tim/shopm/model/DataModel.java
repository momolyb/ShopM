package com.tim.shopm.model;

import com.tim.shopm.entity.Commodity;
import com.tim.shopm.manager.DatabaseManager;

import java.util.List;

public class DataModel {
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
        List<Commodity> commodities = DatabaseManager.getDaoSession().getCommodityDao().loadAll();
        if (commodities.size()>0){
            callBack.onSuccess(commodities);
        }else {
            callBack.onError(ERROR.NOT_ROUND.value,ERROR.NOT_ROUND.code);
        }
    }
    public static void loadCommodity(long id,LoadDataCallBack<Commodity> callBack){
        callBack.onSuccess(DatabaseManager.getDaoSession().getCommodityDao().load(id));
    }
    public static void insertCommodity(Commodity entity,LoadDataCallBack<Long> callBack){
        callBack.onSuccess(DatabaseManager.getDaoSession().getCommodityDao().insert(entity));
    }
    public static void removeCommodity(long id){
        DatabaseManager.getDaoSession().getCommodityDao().deleteByKey(id);
    }
    public static void addCommodity(Commodity entity){
        Commodity temp = DatabaseManager.getDaoSession().getCommodityDao().load(entity.getId());
        temp.setNum(entity.getNum()+temp.getNum());
        updateCommodity(temp);
    }
    public static void updateCommodity(Commodity entity){
        DatabaseManager.getDaoSession().getCommodityDao().update(entity);
    }
    public static void findCommodity(String barcode,LoadDataCallBack<Commodity> callBack) {
        List<Commodity> temp = DatabaseManager.getDaoSession().getCommodityDao().queryRaw("WHERE bar_code = ?",barcode);
        if (temp.size()>0){
            callBack.onSuccess(temp.get(0));
        }else {
            callBack.onError(ERROR.NOT_ROUND.value,ERROR.NOT_ROUND.code);
        }

    }
}
