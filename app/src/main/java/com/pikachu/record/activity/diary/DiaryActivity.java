package com.pikachu.record.activity.diary;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pikachu.record.R;
import com.pikachu.record.activity.dialog.PDialog;
import com.pikachu.record.monitor.DataSynEvent;
import com.pikachu.record.monitor.ReturnImagePath;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Diary;
import com.pikachu.record.tool.ToolFile;
import com.pikachu.record.tool.ToolState;
import com.pikachu.record.view.top.TopView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;

/*import android.content.Context;
import android.support.multidex.MultiDex;*/





public class DiaryActivity extends ReturnImagePath implements  DiaryRecyclerAdapter.ItemOnClick {

    

    private RecyclerView recyclerView;
    private TopView topView;
    private SwipeRefreshLayout swipe;

    private String diary_1;


    private InitialSql initialSql;
    private List<Diary> diaryData;
    private DiaryRecyclerAdapter diaryRecyclerAdapter;
    private DiaryAddDialogAdapter diaryAddDialogAdapter;

    
    //腹泻  突破dex 64k方法限制
    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }*/
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolState.setFullScreen(this, true, false);//全屏
        setContentView(R.layout.diary_activity);
		findView();
        init();
    }

	private void findView() {
        
        topView=findViewById(R.id.id_diary_topView_1);
		recyclerView=findViewById(R.id.id_diary_recycler_1);
		swipe=findViewById(R.id.id_diary_swipeRefresh_1);
		diary_1=getResources().getString(R.string.diary_activity_1);

	}



    private void init() {
        topView.setText(diary_1);
        getData();

        diaryRecyclerAdapter = new DiaryRecyclerAdapter(this,diaryData);
        recyclerView.setAdapter(diaryRecyclerAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
		//添加item的点击事件和长按事件
        diaryRecyclerAdapter.setItemOnClick(this);


        //刷新
        swipe.setOnRefreshListener(() -> {
            getData();
            swipe.setRefreshing(false);
        });


		//初始addDialog
        diaryAddDialogAdapter = new DiaryAddDialogAdapter(this);
        //弹窗添加完毕后
        diaryAddDialogAdapter.setEndAdd(this::getData);


        //TopView添加 按键点击事件
        topView.setRightImageOnClick(p1 -> diaryAddDialogAdapter.showDialog(true,true));

            
            
            
    }


	//获取数据
    private void getData(){
        if(initialSql==null)
            initialSql = new InitialSql(this);
        diaryData = initialSql.getDiaryData();
        Collections.reverse(diaryData);
        if(diaryRecyclerAdapter!=null){
            diaryRecyclerAdapter.setDiaryData(diaryData);    
            diaryRecyclerAdapter.notifyDataSetChanged();
        }

    }


    //父类 返回接口 
    @Override
    public void setImagetPath(String path) {
        diaryAddDialogAdapter.setImagePath(path);
    }
    
    


    @Override
    public boolean onLongClick(View v, int position,final Diary diary) {

        PDialog.PDialog(this)
            .setMsg("是否删除此条笔记？")
            .setLeftStr("确定删除", (v1, pDialog) -> {
                initialSql.deleteDiary(diary);
                pDialog.dismiss();
                getData();
                ToolFile.deleteSingleFile(diary.getImagePath());
                //发布更新事件
                EventBus.getDefault().post(new DataSynEvent());
            })
            .setRightStr("取消删除", null)
            .show();
            
            
        return true;
    }

    @Override
    public void onClick(View v, int position,Diary diary) {
        diaryAddDialogAdapter.showDialog(true,true,diary);   
    }


    
    
	
	






}

