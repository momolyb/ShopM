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
    private String bar_code;
    @NotNull
    private Date time;
    @NotNull
    private float price;
    @NotNull
    private int num;
    @NotNull
    private Long in_order_id;
    @NotNull
    private Long commodity_id;

    @ToOne(joinProperty = "commodity_id")
    private Commodity commodity;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 519002038)
    private transient InCommodityOrderDao myDao;
    @Generated(hash = 733609773)
    private transient Long commodity__resolvedKey;
    public InCommodityOrder() {
    }

    @Generated(hash = 1572865360)
    public InCommodityOrder(Long id, @NotNull String bar_code, @NotNull Date time,
            float price, int num, @NotNull Long in_order_id,
            @NotNull Long commodity_id) {
        this.id = id;
        this.bar_code = bar_code;
        this.time = time;
        this.price = price;
        this.num = num;
        this.in_order_id = in_order_id;
        this.commodity_id = commodity_id;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
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

    public void setCommodity_id(Long commodity_id) {
        this.commodity_id = commodity_id;
    }
    @Keep
    public Commodity getCommodity() {
        return commodity;
    }
    @Keep
    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
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
    @Generated(hash = 1681026528)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getInCommodityOrderDao() : null;
    }
}
