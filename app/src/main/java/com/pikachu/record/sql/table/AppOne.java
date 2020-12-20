package com.pikachu.record.sql.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 判定是否第一次
 */
@Entity
public class AppOne {

    @Id(autoincrement = true)
    private Long id;
    private boolean isOne;
    @Generated(hash = 737990133)
    public AppOne(Long id, boolean isOne) {
        this.id = id;
        this.isOne = isOne;
    }
    @Generated(hash = 891444366)
    public AppOne() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getIsOne() {
        return this.isOne;
    }
    public void setIsOne(boolean isOne) {
        this.isOne = isOne;
    }
}
