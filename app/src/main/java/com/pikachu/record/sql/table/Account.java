package com.pikachu.record.sql.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 账单表
 */
@Entity
public class Account {
    @Id(autoincrement = true)
    private Long id;
    //是否支出  true=支出   false=收入
    private boolean budget;
    //标题
    private String title;
    //支出/收入 多少
    private float howMuch;
    //内容
    private String text;
    //创建时间
    private String item;
    @Generated(hash = 1023645783)
    public Account(Long id, boolean budget, String title, float howMuch,
            String text, String item) {
        this.id = id;
        this.budget = budget;
        this.title = title;
        this.howMuch = howMuch;
        this.text = text;
        this.item = item;
    }
    @Generated(hash = 882125521)
    public Account() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public boolean getBudget() {
        return this.budget;
    }
    public void setBudget(boolean budget) {
        this.budget = budget;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public float getHowMuch() {
        return this.howMuch;
    }
    public void setHowMuch(float howMuch) {
        this.howMuch = howMuch;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getItem() {
        return this.item;
    }
    public void setItem(String item) {
        this.item = item;
    }


}
