package com.pikachu.record.activity.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.pikachu.record.R;
import com.pikachu.record.activity.mood.MoodActivity;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.data.MoodDao;
import com.pikachu.record.sql.table.Mood;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;
import com.pikachu.record.view.statistics.CircleGroupView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import android.view.View.OnClickListener;

/**
 * 用于初始首页心情
 */


public class MoodAdapter {

    private Activity activity;
    private Context context;
    private CircleGroupView circleGroupView;
    private TextView textView1;
    private TextView textView2;
    private TextView textView4;
    private TextView textView6;
    private TextView textView7;
    private TextView textView11;


    public MoodAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;
        findByID();
    }

    private void findByID() {
        //统计图
        circleGroupView = activity.findViewById(R.id.id_home_main_circleGroupView_1);
        //最大的概括字
        textView1 = activity.findViewById(R.id.id_home_main_scroll_textView_1);
        //总结文字
        textView2 = activity.findViewById(R.id.id_home_main_scroll_textView_2);
        textView11 = activity.findViewById(R.id.id_home_main_scroll_textView_11);
        //好心情次数
        textView4 = activity.findViewById(R.id.id_home_main_scroll_textView_4);
        //坏心情次数
        textView6 = activity.findViewById(R.id.id_home_main_scroll_textView_6);
        //添加更多
        textView7 = activity.findViewById(R.id.id_home_main_scroll_textView_7);
    }


    public void loadDataUpUi(List<Mood> moods) {
        //返回 符合时间内    出现排名前MOOD_FRONT个的心情id和出现次数 （不得超过 MOOD_FRONT）
        List<Map.Entry<Integer,Integer>> moodMap=  getMoodList(moods);

        List<CircleGroupView.Data> data=new ArrayList<>();
        int i=0;
        int moodOneId=0;
        int moodOneSecond=0;
        
        for (Map.Entry<Integer,Integer> entry : moodMap) {
            Integer keyMood = entry.getKey();
            Integer valueNext = entry.getValue();
            data.add(new CircleGroupView.Data(ToolPublic.MOOD_STR_TO_COLOR.get(keyMood).str,
                    ToolPublic.MOOD_STR_TO_COLOR.get(keyMood).color,
                    valueNext));
                    
            if (i==0){
                moodOneId= keyMood;
                moodOneSecond=valueNext;
                i++;
            }
        }

        if (moodMap.size() <= 0) {
           /* circleGroupView.addData(
                    new CircleGroupView.Data(ToolPublic.MOOD_STR_TO_COLOR.get(0).str,
                            ToolPublic.MOOD_STR_TO_COLOR.get(0).color,
                            1)
            ).endAdd();*/
            data.add(new CircleGroupView.Data(ToolPublic.MOOD_STR_TO_COLOR.get(0).str,
                    ToolPublic.MOOD_STR_TO_COLOR.get(0).color,
                    1));
        }
        circleGroupView.addData(data);
        //text配置
        setTextVar(moodOneId,moodOneSecond,moods);

        //添加更多  点击事件
        textView7.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					activity.startActivity(new Intent(activity, MoodActivity.class));
				}
			});
    }


    /**
     * 文字整理
     * @param moodOneId
     * @param moods
     */
    private void setTextVar(int moodOneId,int moodOneSecond,List<Mood> moods) {

        String str = ToolPublic.MOOD_STR_TO_COLOR.get(moodOneId).str;
        int moodGood=0;
        int moodBad=0;
        textView1.setText(str);
        textView1.setTextColor(ToolPublic.MOOD_STR_TO_COLOR.get(moodOneId).color);
        textView2.setText(str);
        textView2.setTextColor(ToolPublic.MOOD_STR_TO_COLOR.get(moodOneId).color);
        textView11.setText(moodOneSecond+"");
        textView11.setTextColor(ToolPublic.MOOD_STR_TO_COLOR.get(moodOneId).color);


        for (Mood mood :
                moods) {
            if (mood.getMood() == 0 || mood.getMood() == 1
                    || mood.getMood() == 3 || mood.getMood() == 2
                    || mood.getMood() == 4 || mood.getMood() == 5
                    || mood.getMood() == 13 || mood.getMood() == 17 ) {
                moodGood++;
            }else moodBad++;
        }
        textView4.setText(moodGood<=0? context.getResources().getString(R.string.home_main_mood_5) :
                moodGood+context.getResources().getString(R.string.home_main_mood_1_2));
        textView6.setText(
                moodBad<=0? context.getResources().getString(R.string.home_main_mood_5) :
                        moodBad+context.getResources().getString(R.string.home_main_mood_1_2));

    }


    /**
     * 整理出符合的数据
     *
     * @param moods
     * @return
     */
    private List<Map.Entry<Integer, Integer>> getMoodList(List<Mood> moods) {
        ArrayList<Mood> moods1 = new ArrayList<>();
        //HashMap<Integer, Integer> map = new HashMap<>();
        HashMap<Integer, Integer> map2 = new HashMap<>();
        for (Mood mood : moods) {
            if (ToolTime.dateDiff(
                    ToolTime.getItem(ToolPublic.TIME_DATA),
                    mood.getItem(),
                    ToolPublic.LATELY_DAY,
                    3,
                    ToolPublic.TIME_DATA)){
                moods1.add(mood);
            }
        }

        for (Mood mood : moods1) {
            boolean f=false;
            Integer keyMood=0;
            Integer valueNext=0;
            for (Map.Entry<Integer,Integer> entry : map2.entrySet()) {
                 f=false;
                 keyMood= entry.getKey();
                 valueNext= entry.getValue();
                if (keyMood==mood.getMood()){
                    f=true;
                    break;
                }
            }
            if (f){
                map2.put(keyMood,valueNext+1);
            }else {
                map2.put(mood.getMood(),1);
            }
        }



        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(map2.entrySet());
        //降序排序
        Collections.sort(list, new Comparator<Map.Entry<Integer,Integer>>(){

				@Override
				public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
					//第二个元素（o1）比第一个元素（o2）大，返回-1
                    
                    int cco= o1.getValue();
                    int cco2= o2.getValue();
                    
					if(cco>cco2) return -1;
                    else if(cco<cco2) return 1;
					else return 0;
                    
				}
			});
		
            
            
            /*
            
        for(int i=list.size()-1;i>=0;i--){
            Map.Entry<Integer, Integer> entry = list.get(i);
            map.put(entry.getKey(),entry.getValue());
            if (i>=ToolPublic.MOOD_FRONT-1)
                break;
        }
        */
        
        
        
        if (list.size() > ToolPublic.MOOD_FRONT) 
            return list = list.subList(0, ToolPublic.MOOD_FRONT);

        return list;
    }


}
