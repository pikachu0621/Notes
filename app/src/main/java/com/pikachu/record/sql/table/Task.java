package com.pikachu.record.sql.table;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 任务表
 */


@Entity
public class Task {

    @Id(autoincrement = true)
    private Long id;
    //开始时间
    private String startTime;
    //标题
    private String title;
    //内容
    private String text;
    //结束时间 (提醒时间)
    private String stopTime;
    //是否完成
    private boolean isAs;
    //创建时间
    private String time;
    @Generated(hash = 1422138898)
    public Task(Long id, String startTime, String title, String text,
            String stopTime, boolean isAs, String time) {
        this.id = id;
        this.startTime = startTime;
        this.title = title;
        this.text = text;
        this.stopTime = stopTime;
        this.isAs = isAs;
        this.time = time;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getStartTime() {
        return this.startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
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
    public String getStopTime() {
        return this.stopTime;
    }
    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }
    public boolean getIsAs() {
        return this.isAs;
    }
    public void setIsAs(boolean isAs) {
        this.isAs = isAs;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }



}
