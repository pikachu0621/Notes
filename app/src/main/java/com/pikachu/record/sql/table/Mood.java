package com.pikachu.record.sql.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 心情类
 */
@Entity
public class Mood {
    @Id(autoincrement = true)
    private Long id;
/*    //心情标题
    private String title;*/
    //心情编号
    private int mood;
    //内容
    private String text;    /*@NotNull*/
    //创建时间
    private String item;
    @Generated(hash = 1846157926)
    public Mood(Long id, int mood, String text, String item) {
        this.id = id;
        this.mood = mood;
        this.text = text;
        this.item = item;
    }
    @Generated(hash = 112563172)
    public Mood() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getMood() {
        return this.mood;
    }
    public void setMood(int mood) {
        this.mood = mood;
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
