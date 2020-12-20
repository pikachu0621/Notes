package com.pikachu.record.activity.mood;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pikachu.record.R;
import com.pikachu.record.activity.dialog.PDialog;
import com.pikachu.record.monitor.DataSynEvent;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Mood;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolState;
import com.pikachu.record.view.top.TopView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
/*import android.content.Context;
import android.support.multidex.MultiDex;*/





public class MoodActivity extends AppCompatActivity implements  MoodRecyclerAdapter.ItemOnClick {

    private RecyclerView recyclerView;
    private TopView topView;
    private SwipeRefreshLayout swipe;

    private String mood_1;


    private InitialSql initialSql;
    private List<Mood> moodData;
    private MoodRecyclerAdapter moodRecyclerAdapter;
    private MoodAddDialogAdapter moodAddDialogAdapter;

    
    //腹泻  突破dex 64k方法限制
    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolState.setFullScreen(this, true, false);//全屏
        setContentView(R.layout.mood_activity);
		findView();
        getColorMoodStr();
        init();
    }

	private void findView() {
		
		recyclerView=findViewById(R.id.id_mood_recycler_1);
		topView=findViewById(R.id.id_mood_topView_1);
		swipe=findViewById(R.id.id_mood_swipeRefresh_1);
		
		mood_1=getResources().getString(R.string.mood_activity_1);
		
	}

    
    
    private void getColorMoodStr(){
        
        if(ToolPublic.MOOD_STR_TO_COLOR==null||ToolPublic.MOOD_STR_TO_COLOR.size()==0){
            
            //mood 文字+颜色
            String[] moodStr = getResources().getStringArray(R.array.mood_str);
            int[] moodColor = getResources().getIntArray(R.array.mood_color);

            for (int i=0;i < moodStr.length; i++) {
                ToolPublic.MOOD_STR_TO_COLOR.add(new ToolPublic.MoodStrColor(moodStr[i], moodColor[i]));
            }
            
            
        }
        
        
        
    }


    private void init() {
        topView.setText(mood_1);
        getData();

        moodRecyclerAdapter = new MoodRecyclerAdapter(this,moodData);
        recyclerView.setAdapter(moodRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moodRecyclerAdapter.setItemOnClick(this);


        //刷新
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh() {
					getData();
					swipe.setRefreshing(false);
				}
			});
		
		
		//初始addDialog
        moodAddDialogAdapter = new MoodAddDialogAdapter(this,ToolPublic.MOOD_STR_TO_COLOR);
        moodAddDialogAdapter.setEndAdd(new MoodAddDialogAdapter.EndAdd(){
                @Override
                public void endAdd() {
                    getData();
               }
            });


        //TopView添加 按键点击事件
        topView.setRightImageOnClick(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					moodAddDialogAdapter.showDialog(true,true);
				}
			});

    }


    private void getData(){
        if(initialSql==null)
            initialSql = new InitialSql(this);
        moodData = initialSql.getMoodData();
        Collections.reverse(moodData);
        if(moodRecyclerAdapter!=null){
            moodRecyclerAdapter.setMoodData(moodData);    
            moodRecyclerAdapter.notifyDataSetChanged();
        }
        
    }



    
    
    @Override
    public boolean onLongClick(View v, int position,final Mood mood) {
       
        PDialog.PDialog(this)
            .setMsg("是否删除此条心情数据？")
            .setLeftStr("确定删除", new PDialog.DialogTopOnClick(){
                @Override
                public void onClick(View v, PDialog pDialog) {
                    initialSql.deleteMood(mood);
                    pDialog.dismiss();
                    getData();
                    //发布更新事件
                    EventBus.getDefault().post(new DataSynEvent());
                }
            })
            .setRightStr("取消删除", null)
            .show();
        return true;
    }

    @Override
    public void onClick(View v, int position,Mood mood) {
        moodAddDialogAdapter.showDialog(true,true,mood);   
    }

    

    




}
