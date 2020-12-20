package com.pikachu.record.activity.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.record.R;
import com.pikachu.record.sql.table.Task;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import java.util.List;
import java.util.Random;

public class TaskAdapter {

    private final Context context;
    private final Activity activity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String string1;
    private String string2;
    private int color1;
    private int color2;
    private String string3;
    private String string4;
    private Random random;
    private List<Task> tasks;

    private TaskActivityItemOnclick taskActivityItemOnclick=
    new TaskActivityItemOnclick(){
        @Override
        public void deleteOnClick(View view, int position, Task task) {
        }

        @Override
        public void completeOnClick(View view, int position, Task task) {
        }
    };

    
    public TaskAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;
        findByView();
    }

    
    
    public interface TaskActivityItemOnclick{
        void deleteOnClick(View view,int position,Task task);
        void completeOnClick(View view,int position,Task task);
    }
    
    

    private void findByView() {
        recyclerView = activity.findViewById(R.id.id_home_main_scroll_recyclerView_1);
        string1 = context.getResources().getString(R.string.home_main_list_1);
        string2 = context.getResources().getString(R.string.home_main_list_6);
        color1 = context.getResources().getColor(R.color.color_fa);
        color2 = context.getResources().getColor(R.color.color_complete_1);
        string3 = context.getResources().getString(R.string.home_main_list_7);
        string4 = context.getResources().getString(R.string.home_main_list_8);
        random = new Random(1);//随机颜色
    }




    public void loadDataUpUi(List<Task> tasks) {
        if (tasks.size()>ToolPublic.TASK_FRONT){
            this.tasks=tasks.subList(0,ToolPublic.TASK_FRONT);
        }else{
            this.tasks=tasks;
        }
        setRecyclerViewAdapter();
    }



    private void setRecyclerViewAdapter(){

        if (adapter==null){
            adapter = new RecyclerView.Adapter() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View inflate = LayoutInflater.from(context).inflate(R.layout.home_main_list_task_ui, parent, false);
                    return new ItemHolder(inflate);
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

                    ItemHolder itemHolder = (ItemHolder) holder;
                    itemHolder.item.setText(tasks.get(position).getTitle());
                    itemHolder.text.setText(tasks.get(position).getText());
                    itemHolder.startTime.setText(tasks.get(position).getTime());

                    itemHolder.delete.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								taskActivityItemOnclick.deleteOnClick(v,position,tasks.get(position));
							}
						});
					
					
					
					
					
					
                    itemHolder.complete.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								taskActivityItemOnclick.completeOnClick(v,position,tasks.get(position));
								
							}
						});
					
				
                    if (tasks.get(position).getIsAs()){
                        itemHolder.isAs.setText(string2);
                        itemHolder.isAs.setTextColor(color2);

                        itemHolder.complete.setVisibility(View.GONE);
                        itemHolder.view.setVisibility(View.GONE);
                    }else {
                        itemHolder.isAs.setText(string1);
                        itemHolder.isAs.setTextColor(color1);
                        itemHolder.view.setVisibility(View.VISIBLE);
                        itemHolder.complete.setVisibility(View.VISIBLE);
                    }
                    itemHolder.stopTime.setText(ToolTime.getTimeH(tasks.get(position).getStopTime(), ToolPublic.TIME_DATA));

                    int i = random.nextInt(ToolPublic.MOOD_STR_TO_COLOR.size());
                    itemHolder.cardView.setCardBackgroundColor(ToolPublic.MOOD_STR_TO_COLOR.get(i).color);

                }

                @Override
                public int getItemCount() {
                    return tasks.size();
                }

                class ItemHolder extends RecyclerView.ViewHolder{

                    public TextView item;
                    public TextView text;
                    public TextView startTime;
                    public TextView isAs;
                    public TextView stopTime;
                    public TextView delete;
                    public TextView complete;
                    private CardView cardView;
                    private View view;

                    public ItemHolder(@NonNull View itemView) {
                        super(itemView);
                        item = itemView.findViewById(R.id.id_home_main_list_textView_1);
                        text = itemView.findViewById(R.id.id_home_main_list_textView_2);
                        startTime = itemView.findViewById(R.id.id_home_main_list_textView_3);
                        isAs = itemView.findViewById(R.id.id_home_main_list_textView_4);
                        stopTime = itemView.findViewById(R.id.id_home_main_list_textView_5);
                        delete = itemView.findViewById(R.id.id_home_main_list_textView_6);
                        complete = itemView.findViewById(R.id.id_home_main_list_textView_7);
                        cardView = itemView.findViewById(R.id.id_home_main_list_cardView_1);
                        view = itemView.findViewById(R.id.id_home_main_list_view_1);
                    }
                }


            };
            recyclerView.setAdapter(adapter);
            LinearLayoutManager ms= new LinearLayoutManager(context);
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(ms);

        }else {
            
            adapter.notifyDataSetChanged();
        }
    }
    
    
    
    
    public void setTaskActivityItemOnclick(TaskActivityItemOnclick taskActivityItemOnclick) {
        this.taskActivityItemOnclick = taskActivityItemOnclick;
    }

    





}
