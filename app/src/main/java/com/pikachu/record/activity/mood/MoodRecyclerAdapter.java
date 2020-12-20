package com.pikachu.record.activity.mood;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.record.R;
import com.pikachu.record.sql.table.Mood;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import java.util.List;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;


public class MoodRecyclerAdapter extends RecyclerView.Adapter {


    private final Context context;
    private List<Mood> moodData;
    private final Activity activity;

    public interface ItemOnClick{
        boolean onLongClick(View v,int position,Mood mood);
        void onClick(View v,int position,Mood mood);        
    }
    
    
    
    public ItemOnClick itemOnClick= new ItemOnClick(){

        @Override
        public boolean onLongClick(View v, int position,Mood mood) {
            return false;
        }
        
        @Override
        public void onClick(View v, int position,Mood mood) {
            
        }
    };
	
	
	

    public MoodRecyclerAdapter(Context context,List<Mood> moodData) {
        this.context = context;
        this.activity = (Activity)context;
        this.moodData=moodData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.mood_list_item_ui, parent, false);
        return new ItemViewHolder(inflate);
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.linearLayout.setOnLongClickListener(new OnLongClickListener(){
				@Override
				public boolean onLongClick(View p1) {
					return itemOnClick.onLongClick(itemHolder.linearLayout,position,moodData.get(position));
				}
			});
        itemHolder.linearLayout.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    itemOnClick.onClick(itemHolder.linearLayout,position,moodData.get(position));
                }
            });
		itemHolder.linearLayout.setBackgroundColor(ToolPublic.MOOD_STR_TO_COLOR.get(moodData.get(position).getMood()).color);
        itemHolder.textView_1.setText(ToolPublic.MOOD_STR_TO_COLOR.get(moodData.get(position).getMood()).str);
        itemHolder.textView_2.setText(ToolTime.getTimeH(moodData.get(position).getItem(), ToolPublic.TIME_DATA));
        itemHolder.textView_3.setText(moodData.get(position).getText());
    }
	
    @Override
    public int getItemCount() {
        return moodData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout linearLayout;
        public TextView textView_1;
        public TextView textView_2;
        public TextView textView_3;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.id_mood_item_linear_1);
            textView_1 = itemView.findViewById(R.id.id_mood_item_text_1);
            textView_2 = itemView.findViewById(R.id.id_mood_item_text_2);
            textView_3 = itemView.findViewById(R.id.id_mood_item_text_3);
        }
    }


    public void setItemOnClick(ItemOnClick itemOnClick){
        this.itemOnClick=itemOnClick;
    }
    
    
    
    
    
    public void setMoodData(List<Mood> moodData) {
        this.moodData = moodData;
    }
    

}
