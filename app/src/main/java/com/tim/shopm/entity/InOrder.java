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
import com.tim.shopm.greendao.InCommodityOrderDao;
import com.tim.shopm.greendao.InOrderDao;

@Entity(active = true)
public class InOrder {
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    private Date time;

    @ToMany(referencedJoinProperty = "in_order_id")
    private List<InCommodityOrder> inCommodityOrders;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1362542278)
    private transient InOrderDao myDao;
    public InOrder() {
    }


    @Generated(hash = 927504490)
    public InOrder(Long id, @NotNull Date time) {
        this.id = id;
        this.time = time;
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 564269643)
    public List<InCommodityOrder> getInCommodityOrders() {
        if (inCommodityOrders == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            InCommodityOrderDao targetDao = daoSession.getInCommodityOrderDao();
            List<InCommodityOrder> inCommodityOrdersNew = targetDao
                    ._queryInOrder_InCommodityOrders(id);
            synchronized (this) {
                if (inCommodityOrders == null) {
                    inCommodityOrders = inCommodityOrdersNew;
                }
            }
        }
        return inCommodityOrders;
    }


    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 760465908)
    public synchronized void resetInCommodityOrders() {
        inCommodityOrders = null;
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
    @Generated(hash = 924774995)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInOrderDao() : null;
    }

}
