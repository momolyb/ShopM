package com.tim.shopm.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.tim.shopm.greendao.DaoSession;
import com.tim.shopm.greendao.OutOrderDao;
import com.tim.shopm.greendao.OutCommodityOrderDao;
//销售订单
@Entity(active = true)
public class OutOrder   {
    public static final int TYPE_ALI = 1;
    public static final int TYPE_WX = 2;
    public static final int TYPE_CASH = 0;
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    private Date time;
    @NotNull
    private int pay_type;

    @ToMany(referencedJoinProperty = "out_order_id")
    private List<OutCommodityOrder> outCommodityOrders;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 2005258081)
    private transient OutOrderDao myDao;

    public OutOrder() {
    }

    @Generated(hash = 1409113237)
    public OutOrder(Long id, @NotNull Date time, int pay_type) {
        this.id = id;
        this.time = time;
        this.pay_type = pay_type;
    }
    public String getPayType(){
        switch (getPay_type()){
            case TYPE_ALI:
                return "支付宝";
            case TYPE_WX:
                return "微信";
            case TYPE_CASH:
                return "现金";
            default:
                return "未知";
        }
    }
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getPay_type() {
        return pay_type;
    }

    public void setPay_type(int pay_type) {
        this.pay_type = pay_type;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public float getMoneyCount() {
        float count = 0;
        List<OutCommodityOrder> t = getOutCommodityOrders();
        for (OutCommodityOrder in :
                t) {
            count += in.getPrice() * in.getNum();
        }
        return count;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 48813799)
    public List<OutCommodityOrder> getOutCommodityOrders() {
        if (outCommodityOrders == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OutCommodityOrderDao targetDao = daoSession.getOutCommodityOrderDao();
            List<OutCommodityOrder> outCommodityOrdersNew = targetDao
                    ._queryOutOrder_OutCommodityOrders(id);
            synchronized (this) {
                if (outCommodityOrders == null) {
                    outCommodityOrders = outCommodityOrdersNew;
                }
            }
        }
        return outCommodityOrders;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1093885429)
    public synchronized void resetOutCommodityOrders() {
        outCommodityOrders = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2082858316)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOutOrderDao() : null;
    }
}
