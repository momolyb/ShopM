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
import com.tim.shopm.greendao.InCommodityOrderDao;

@Entity(active = true)
public class InCommodityOrder {
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    private Date time;
    @NotNull
    private float price;
    @NotNull
    private int num;
    private Long in_order_id;
    @NotNull
    private Long commodity_id;
    @ToOne(joinProperty = "commodity_id")
    private Commodity bindCommodity;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 519002038)
    private transient InCommodityOrderDao myDao;
    @Generated(hash = 1383153668)
    private transient Long bindCommodity__resolvedKey;

    public InCommodityOrder() {
    }


    @Generated(hash = 1679035397)
    public InCommodityOrder(Long id, @NotNull Date time, float price, int num, Long in_order_id,
            @NotNull Long commodity_id) {
        this.id = id;
        this.time = time;
        this.price = price;
        this.num = num;
        this.in_order_id = in_order_id;
        this.commodity_id = commodity_id;
    }


    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Long getIn_order_id() {
        return in_order_id;
    }

    public void setIn_order_id(Long in_order_id) {
        this.in_order_id = in_order_id;
    }

    public Long getCommodity_id() {
        return commodity_id;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    /** To-one relationship, resolved on first access. */
    @Generated(hash = 736578216)
    public Commodity getBindCommodity() {
        Long __key = this.commodity_id;
        if (bindCommodity__resolvedKey == null
                || !bindCommodity__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            CommodityDao targetDao = daoSession.getCommodityDao();
            Commodity bindCommodityNew = targetDao.load(__key);
            synchronized (this) {
                bindCommodity = bindCommodityNew;
                bindCommodity__resolvedKey = __key;
            }
        }
        return bindCommodity;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1465743540)
    public void setBindCommodity(@NotNull Commodity bindCommodity) {
        if (bindCommodity == null) {
            throw new DaoException(
                    "To-one property 'commodity_id' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.bindCommodity = bindCommodity;
            commodity_id = bindCommodity.getId();
            bindCommodity__resolvedKey = commodity_id;
        }
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
    @Generated(hash = 1681026528)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInCommodityOrderDao() : null;
    }


    public void setCommodity_id(Long commodity_id) {
        this.commodity_id = commodity_id;
    }

}
