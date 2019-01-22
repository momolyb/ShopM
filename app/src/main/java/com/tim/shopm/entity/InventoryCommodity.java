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
import com.tim.shopm.greendao.InventoryCommodityDao;

@Entity(active = true)
public class InventoryCommodity  {
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    private String bar_code;
    @NotNull
    private int num;
    @NotNull
    private Long inventory_id;
    private Long commodity_id;
    @ToOne(joinProperty = "commodity_id")
    private Commodity commodity;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 250175374)
    private transient InventoryCommodityDao myDao;
    @Generated(hash = 733609773)
    private transient Long commodity__resolvedKey;

    public Long getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(Long commodity_id) {
        this.commodity_id = commodity_id;
    }

    public InventoryCommodity() {
    }

    @Generated(hash = 156012476)
    public InventoryCommodity(Long id, @NotNull String bar_code, int num,
            @NotNull Long inventory_id, Long commodity_id) {
        this.id = id;
        this.bar_code = bar_code;
        this.num = num;
        this.inventory_id = inventory_id;
        this.commodity_id = commodity_id;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Long getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(Long inventory_id) {
        this.inventory_id = inventory_id;
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
    @Generated(hash = 72079416)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInventoryCommodityDao() : null;
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
