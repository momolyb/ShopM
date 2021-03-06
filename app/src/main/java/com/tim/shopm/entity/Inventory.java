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
import com.tim.shopm.greendao.InventoryCommodityDao;
import com.tim.shopm.greendao.InventoryDao;

//盘点订单
@Entity(active = true)
public class Inventory {
    public static final int ing = 0;
    public static final int stop = 1;
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    private String context;
    @NotNull
    private int state;
    @NotNull
    private Date time;
    @ToMany(referencedJoinProperty = "inventory_id")
    private List<InventoryCommodity> inventoryCommodities;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 433391049)
    private transient InventoryDao myDao;

    public Inventory() {
    }

    @Generated(hash = 1899948786)
    public Inventory(Long id, @NotNull String context, int state, @NotNull Date time) {
        this.id = id;
        this.context = context;
        this.state = state;
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1369570626)
    public synchronized void resetInventoryCommodities() {
        inventoryCommodities = null;
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1763418221)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInventoryDao() : null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 597265287)
    public List<InventoryCommodity> getInventoryCommodities() {
        if (inventoryCommodities == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InventoryCommodityDao targetDao = daoSession.getInventoryCommodityDao();
            List<InventoryCommodity> inventoryCommoditiesNew = targetDao
                    ._queryInventory_InventoryCommodities(id);
            synchronized (this) {
                if (inventoryCommodities == null) {
                    inventoryCommodities = inventoryCommoditiesNew;
                }
            }
        }
        return inventoryCommodities;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
