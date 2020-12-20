package com.pikachu.record.sql.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 日记表
 */
@Entity
public class Diary {
    @Id(autoincrement = true)
    private Long id;
    //标题
    private String title;
    //内容
    private String text;
    //天气
    private String weather;
    //图片路径
    private String imagePath;
    //时间
    private String time;
    @Generated(hash = 2050267801)
    public Diary(Long id, String title, String text, String weather,
            String imagePath, String time) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.weather = weather;
        this.imagePath = imagePath;
        this.time = time;
    }
    @Generated(hash = 112123061)
    public Diary() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getText() {
        return this.text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getWeather() {
        return this.weather;
    }
    public void setWeather(String weather) {
        this.weather = weather;
    }
    public String getImagePath() {
        return this.imagePath;
    }
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    
}
