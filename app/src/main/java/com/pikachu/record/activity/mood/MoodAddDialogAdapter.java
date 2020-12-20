package com.pikachu.record.activity.mood;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pikachu.record.R;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import java.util.Map;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import com.pikachu.record.tool.ToolPublic;
import androidx.annotation.NonNull;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.EditText;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Mood;
import com.pikachu.record.tool.ToolTime;
import com.pikachu.record.tool.ToolOther;
import org.greenrobot.eventbus.EventBus;
import com.pikachu.record.monitor.DataSynEvent;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import android.view.Gravity;


//弹窗
public class MoodAddDialogAdapter {


    private final Context context;
    private final Activity activity;
    private InitialSql initialSql;
    private BottomSheetDialog dialog;
    private View box_view;
    private RecyclerView recyclerView;
    private TextView titleTextView,finishTextView;
    private EditText msgEditView;
    private RecyclerView.Adapter adapter;
    private List<ToolPublic.MoodStrColor> moodStrColor;
    private int textBColor;
    private String upStr,addStr,finishStr,initStr;
    private int tPos=0;
    
    
    
    public interface EndAdd{
        void endAdd();
    }
    private EndAdd endAdd=new EndAdd(){
        @Override
        public void endAdd() {
        }
    };
    
    
    
    public MoodAddDialogAdapter(Context context,List<ToolPublic.MoodStrColor> moodStrColor) {
        this.context = context;
        activity = (Activity) context;
        this.moodStrColor=moodStrColor;
        textBColor=context.getResources().getColor(R.color.color_8e8e8e);
        upStr=context.getResources().getString(R.string.mood_mood_list_3);
        addStr=context.getResources().getString(R.string.home_mood);
        finishStr=context.getResources().getString(R.string.mood_complete);
        initStr=context.getResources().getString(R.string.mood_mood_list_2);
        initialSql=new InitialSql(context);
    }


    public void showDialog(boolean cancelable,boolean cancel){
        tPos=0;
        findDialogView();
        dialogH(cancelable,cancel,addStr,"",new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    addAndUpData(false,null);
                }
            });
        
    }
    
    
    

    //更新，和详情
    public void showDialog(boolean cancelable,boolean cancel,final Mood mood){
        tPos=mood.getMood();
        findDialogView();
        dialogH(cancelable,cancel,upStr,mood.getText(),new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    addAndUpData(true,mood);
                }
            });
    }
    
    
    
    
    
    
    
    private void findDialogView(){
        
        if (dialog==null) {
            dialog = new BottomSheetDialog(context,R.style.BottomSheetEdit){
                @Override
                public void onStart(){
                    super. onStart() ;
                    if (box_view == null) return;
                    View parent = (View) box_view. getParent();
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
                    box_view. measure(0,0);
                    behavior.setPeekHeight(box_view. getMeasuredHeight());
                    CoordinatorLayout.LayoutParams params =(CoordinatorLayout. LayoutParams) parent.getLayoutParams();
                    params. gravity = Gravity.TOP | Gravity. CENTER_HORIZONTAL;
                    parent. setLayoutParams (params) ;

                }
            };
            box_view = LayoutInflater.from(context).inflate(R.layout.mood_add_data_ui, null);
            titleTextView = box_view.findViewById(R.id.id_mood_add_data_text_2);
            finishTextView = box_view.findViewById(R.id.id_mood_add_data_text_1);
            msgEditView = box_view.findViewById(R.id.id_mood_add_data_edit_1);
            recyclerView=box_view.findViewById(R.id.id_mood_add_data_recyclerView_1);
            setDialogRecyclerAdapter();
            dialog.setContentView(box_view);
            dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }else{
            LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
            llm.scrollToPositionWithOffset(tPos, 0);
            llm.setStackFromEnd(false);
            adapter.notifyDataSetChanged();
        }    
    }
    
    
    private void dialogH(boolean cancelable,boolean cancel,
                           String titleStr,String msg,
                           OnClickListener onClick){
          dialog.setCancelable(cancelable);
          dialog.setCanceledOnTouchOutside(cancel);
          dialog.show();
          titleTextView.setText(titleStr);
          msgEditView.setText(msg);
          finishTextView.setOnClickListener(onClick);   
    }
    
    
    
    


    public void cancelDialog(){
        if (dialog!=null) dialog.cancel();
    }


    
    
    private void setDialogRecyclerAdapter(){
        
       adapter=new RecyclerView.Adapter(){

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup p1, int p2) {
                View inflate = LayoutInflater.from(context).inflate(R.layout.mood_add_list_item_ui, p1, false);
                return new ItemHolder(inflate);
                
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder p1, final int p2) {
                ItemHolder gg=(ItemHolder)p1;
                gg.text.setText(moodStrColor.get(p2).str);
                
                if(tPos==p2){
                    gg.text.setBackgroundColor(moodStrColor.get(p2).color);
                    gg.text.setTextColor(Color.WHITE);
                }else{
                    gg.text.setBackgroundColor(Color.WHITE);
                    gg.text.setTextColor(textBColor);
                }
                gg.text.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View p1) {
                            tPos=p2;
                            adapter.notifyDataSetChanged();
                        }
                    });
            }

            @Override
            public int getItemCount() {
                return moodStrColor.size();
            }
            
            class ItemHolder extends RecyclerView.ViewHolder{
                public TextView text;
                
                public ItemHolder(@NonNull View itemView) {
                    super(itemView);
                    text = itemView.findViewById(R.id.id_mood_add_list_item_textView_1);
                }
                
            };
            
            
            
        };
        recyclerView.setAdapter(adapter);
        LinearLayoutManager ms= new LinearLayoutManager(context);
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        ms.scrollToPositionWithOffset(tPos, 0);
        ms.setStackFromEnd(false);
        
        recyclerView.setLayoutManager(ms);
        
        
        
        
    }



    //isUpData=false 添加数据，isUpData=true  更新数据;
    private void addAndUpData(boolean isUpData,Mood mood){
        String msg = msgEditView.getText().toString();
        if(msg.equals(""))
            msg=initStr;
        if(isUpData){
            mood.setMood(tPos);
            mood.setText(msg);
            initialSql.updateMood(mood);
        }else{
            initialSql.setOneMoodData(new Mood(null,tPos,msg,ToolTime.getItem(ToolPublic.TIME_DATA)));
        }
        ToolOther.tw(activity,finishStr,R.drawable.toast_true_icon);
        cancelDialog();
        //发布事件
        EventBus.getDefault().post(new DataSynEvent());
        endAdd.endAdd();
    }
    
    
    
    
    //添加或者刷新
    public void setEndAdd(EndAdd endAdd){
        this.endAdd=endAdd;   
    }
    









}
