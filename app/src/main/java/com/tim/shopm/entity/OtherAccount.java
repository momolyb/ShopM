package com.tim.shopm.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.tim.shopm.greendao.DaoSession;
import com.tim.shopm.greendao.OtherAccountDao;
//其他
@Entity(active = true)
public class OtherAccount  {

    public static final int TYPE_IN = 0;
    public static final int TYPE_OUT = 1;
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    private String context;
    @NotNull
    private float money;
    @NotNull
    private Date time;
    /**
     * #TYPE_IN 收入
     * #TYPE_OUT 支出
     */
    @NotNull
    private int Type;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 478116217)
    private transient OtherAccountDao myDao;
    public OtherAccount() {
    }

    @Generated(hash = 1015922725)
    public OtherAccount(Long id, @NotNull String context, float money,
            @NotNull Date time, int Type) {
        this.id = id;
        this.context = context;
        this.money = money;
        this.time = time;
        this.Type = Type;
    }

    public static int getTypeIn() {
        return TYPE_IN;
    }

    public static int getTypeOut() {
        return TYPE_OUT;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
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
    @Generated(hash = 122597262)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getOtherAccountDao() : null;
    }
}
