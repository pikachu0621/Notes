package com.pikachu.record.tool;

import androidx.annotation.ColorInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ToolPublic {

    public static final String TOG="test_t";







    //时间格式
    public static final String TIME_DATA="yyyy/MM/dd HH:mm:ss";

    //图片储存路径  -> sd卡 -> 包名 -> ...
    public static final String IMAGE_PATH="/image/";

    //主页统计图加载心情排名的前多少个  0<MOOD_FRONT<=16
    public static final int MOOD_FRONT=5;
        //主页 显示最近多少天数据(心情)  这里有个bug 如果记录都超过30天 就不获取了
    public static final int LATELY_DAY=30;
    //主页 任务加载几个
    public static final int TASK_FRONT=4;
    //主页 笔记加载几个
    public static final int DIARY_FRONT=4;






    //心情数据库
    public static final String MOOD_DB="mood.db";
    //任务数据库
    public static final String TASK_DB="task.db";
    //账单数据库
    public static final String ACCOUNT_DB="account.db";
    //日记数据库
    public static final String DIARY_DB="diary.db";
    //第一次数据库
    public static final String APP_ONE_DB="app_one.db";


    public static class MoodStrColor{
        public String str;
        @ColorInt
        public int color;
        public MoodStrColor(String str, int color) {
            this.str = str;
            this.color = color;
        }
    }
    //颜色值保存
    public static List<MoodStrColor> MOOD_STR_TO_COLOR = new ArrayList<>();





}
