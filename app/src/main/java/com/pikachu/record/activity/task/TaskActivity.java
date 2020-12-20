package com.pikachu.record.activity.task;

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
import com.pikachu.record.sql.table.Task;
import com.pikachu.record.tool.ToolState;
import com.pikachu.record.view.top.TopView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;
/*import android.content.Context;
import android.support.multidex.MultiDex;*/





public class TaskActivity extends AppCompatActivity implements  TaskRecyclerAdapter.ItemOnClick {


    private RecyclerView recyclerView;
    private TopView topView;
    private SwipeRefreshLayout swipe;

    private String task_1;


    private InitialSql initialSql;
    private List<Task> taskData;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    private TaskAddDialogAdapter taskAddDialogAdapter;


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
        setContentView(R.layout.task_activity);
		findView();
        init();
    }

	private void findView() {

		recyclerView = findViewById(R.id.id_task_recycler_1);
		topView = findViewById(R.id.id_task_topView_1);
		swipe = findViewById(R.id.id_task_swipeRefresh_1);

		task_1 = getResources().getString(R.string.task_activity_1);

	}



    private void init() {
        topView.setText(task_1);
        getData();

        taskRecyclerAdapter = new TaskRecyclerAdapter(this, taskData);
        recyclerView.setAdapter(taskRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskRecyclerAdapter.setItemOnClick(this);


        //刷新
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){

				@Override
				public void onRefresh() {
					getData();
					swipe.setRefreshing(false);
				}
			});

		
         //初始addDialog
         taskAddDialogAdapter = new TaskAddDialogAdapter(this);
         taskAddDialogAdapter.setEndAdd(new TaskAddDialogAdapter.EndAdd(){
         @Override
         public void endAdd() {
         getData();
         }
         });
         

        //TopView添加 按键点击事件
        topView.setRightImageOnClick(new OnClickListener(){
				@Override
				public void onClick(View p1) {
					taskAddDialogAdapter.showDialog(true,true);
				}
			});

    }


    private void getData() {
        if (initialSql == null)
            initialSql = new InitialSql(this);
        taskData = initialSql.getTaskData();
        Collections.reverse(taskData);
        if (taskRecyclerAdapter != null) {
            taskRecyclerAdapter.setTaskData(taskData);    
            taskRecyclerAdapter.notifyDataSetChanged();
        }

    }


    
    
    
    
    
    
    
    @Override
    public void finishOnClick(View v, int position, Task task) {
        task.setIsAs(true);
        initialSql.updateTask(task);
        getData();
        //发布更新事件
        EventBus.getDefault().post(new DataSynEvent());        
    }

    
    
    @Override
    public void editorOnClick(View v, int position, Task task) {
        taskAddDialogAdapter.showDialog(true,true,task);
    }



    @Override
    public void deleteOnClick(View v, int position, final Task task) {

        PDialog.PDialog(this)
            .setMsg("是否删除此条任务数据？")
            .setLeftStr("确定删除", new PDialog.DialogTopOnClick(){
                @Override
                public void onClick(View v, PDialog pDialog) {
                    initialSql.deleteTask(task);
                    pDialog.dismiss();
                    getData();
                    //发布更新事件
                    EventBus.getDefault().post(new DataSynEvent());
                }
            })
            .setRightStr("取消删除", null)
            .show();
    }

    
    
    
    
    
}
