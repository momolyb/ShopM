package com.tim.shopm.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.tim.shopm.greendao.DaoSession;
import com.tim.shopm.greendao.CommodityDao;
import com.tim.shopm.greendao.OutCommodityOrderDao;

@Entity(active = true)
public class OutCommodityOrder   {
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    private Date time;
    @NotNull
    private int num;
    @NotNull
    private Long out_order_id;
    private Long commodity_id;
    @ToOne(joinProperty = "commodity_id")
    private Commodity commodity;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 960621917)
    private transient OutCommodityOrderDao myDao;
    @Generated(hash = 733609773)
    private transient Long commodity__resolvedKey;

    public OutCommodityOrder() {
    }

    @Generated(hash = 1507052281)
    public OutCommodityOrder(Long id, @NotNull Date time, int num, @NotNull Long out_order_id,
            Long commodity_id) {
        this.id = id;
        this.time = time;
        this.num = num;
        this.out_order_id = out_order_id;
        this.commodity_id = commodity_id;
    }

    public Long getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(Long commodity_id) {
        this.commodity_id = commodity_id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Long getOut_order_id() {
        return out_order_id;
    }

    public void setOut_order_id(Long out_order_id) {
        this.out_order_id = out_order_id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
    @Generated(hash = 1894859273)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOutCommodityOrderDao() : null;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1475116964)
    public Commodity getCommodity() {
        Long __key = this.commodity_id;
        if (commodity__resolvedKey == null || !commodity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CommodityDao targetDao = daoSession.getCommodityDao();
            Commodity commodityNew = targetDao.load(__key);
            synchronized (this) {
                commodity = commodityNew;
                commodity__resolvedKey = __key;
            }
        }
        return commodity;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2030056668)
    public void setCommodity(Commodity commodity) {
        synchronized (this) {
            this.commodity = commodity;
            commodity_id = commodity == null ? null : commodity.getId();
            commodity__resolvedKey = commodity_id;
        }
    }
}
