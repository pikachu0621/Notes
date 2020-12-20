package com.pikachu.record.activity.task;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.record.R;
import com.pikachu.record.sql.table.Task;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import java.util.List;


public class TaskRecyclerAdapter extends RecyclerView.Adapter {


    private final Context context;
    private List<Task> taskData;
    private final Activity activity;

    public interface ItemOnClick{
        void finishOnClick(View v,int position,Task task);
        void editorOnClick(View v,int position,Task task);
        void deleteOnClick(View v,int position,Task task);
        
    }
    
    
    
    public ItemOnClick itemOnClick= new ItemOnClick(){
        @Override
        public void finishOnClick(View v, int position, Task task) {}
        @Override
        public void editorOnClick(View v, int position, Task task) {}
        @Override
        public void deleteOnClick(View v, int position, Task task) {}
    };
	
	
	

    public TaskRecyclerAdapter(Context context,List<Task> taskData) {
        this.context = context;
        this.activity = (Activity)context;
        this.taskData=taskData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.task_list_item_ui, parent, false);
        return new ItemViewHolder(inflate);
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        
        
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        Task task = taskData.get(position);
        
        
        itemHolder.textView_1.setText("创建时间 "+
                                      ToolTime.getTimeH(task.getTime(),ToolPublic.TIME_DATA));
        itemHolder.textView_2.setText("提醒时间 "+
                                      ToolTime.getTimeH(task.getStopTime(),ToolPublic.TIME_DATA));
        itemHolder.textView_3.setText(task.getTitle()+"");
        itemHolder.textView_4.setText(task.getText()+"");
        
        itemHolder.linearLayout_2.setVisibility(task.getIsAs()?View.GONE:View.VISIBLE);
        
        itemHolder.linearLayout_2.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    itemOnClick.finishOnClick(p1,position,taskData.get(position));
                }
            });
        itemHolder.linearLayout_3.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    itemOnClick.editorOnClick(p1,position,taskData.get(position));
                }
            });
        itemHolder.linearLayout_4.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    itemOnClick.deleteOnClick(p1,position,taskData.get(position));
                }
            });
        
        
    }
	
    @Override
    public int getItemCount() {
        return taskData.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout linearLayout_1,linearLayout_2,linearLayout_3,linearLayout_4;
        public TextView textView_1;
        public TextView textView_2;
        public TextView textView_3;
        
        public TextView textView_4;
        public TextView textView_5;
        public TextView textView_6;
        public TextView textView_7;
        

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout_1 = itemView.findViewById(R.id.id_task_item_linear_1);
            linearLayout_2 = itemView.findViewById(R.id.id_task_item_linear_2);
            linearLayout_3 = itemView.findViewById(R.id.id_task_item_linear_3);
            linearLayout_4 = itemView.findViewById(R.id.id_task_item_linear_4);
            
            textView_1 = itemView.findViewById(R.id.id_task_item_text_1);
            textView_2 = itemView.findViewById(R.id.id_task_item_text_2);
            textView_3 = itemView.findViewById(R.id.id_task_item_text_3);
            textView_4 = itemView.findViewById(R.id.id_task_item_text_4);
            
            
            textView_5 = itemView.findViewById(R.id.id_task_item_text_5);
            textView_6 = itemView.findViewById(R.id.id_task_item_text_6);
            textView_7 = itemView.findViewById(R.id.id_task_item_text_7);
            
        }
    }


    public void setItemOnClick(ItemOnClick itemOnClick){
        this.itemOnClick=itemOnClick;
    }
    
    
    
    
    
    public void setTaskData(List<Task> taskData) {
        this.taskData = taskData;
    }
    

}
