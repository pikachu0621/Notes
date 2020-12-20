package com.pikachu.record.activity.diary;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pikachu.record.R;
import com.pikachu.record.sql.table.Diary;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import java.io.File;
import java.util.List;

//import com.bumptech.glide.Glide;


public class DiaryRecyclerAdapter extends RecyclerView.Adapter {


    private final Context context;
    private final Activity activity;
    private List<Diary> diaryData;
    private LinearLayout.LayoutParams pa,pa2;
   
    
    public interface ItemOnClick{
        boolean onLongClick(View v,int position,Diary diary);
        void onClick(View v,int position,Diary diary);        
    }
    
    
    
    public ItemOnClick itemOnClick= new ItemOnClick(){

        @Override
        public boolean onLongClick(View v, int position,Diary diary) {
            return false;
        }
        
        @Override
        public void onClick(View v, int position,Diary diary) {}
    };
	
	
	

    public DiaryRecyclerAdapter(Context context,List<Diary> diaryData) {
        this.context = context;
        this.activity = (Activity)context;
        this.diaryData=diaryData;
        pa = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pa2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ToolOther.dp2px(100,context));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.diary_list_item_ui, parent, false);
        return new ItemViewHolder(inflate);
    }




    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
       
        
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        
        String path=diaryData.get(position).getImagePath();
        if(path==null||path.equals("")){
            itemHolder.relativeLayout.setVisibility(View.GONE);
            itemHolder.textView_4.setVisibility(View.VISIBLE);
            itemHolder.textView_4.setText(ToolTime.getTimeH(diaryData.get(position).getTime(),ToolPublic.TIME_DATA));
            itemHolder.relativeLayout_2.setLayoutParams(pa);
            itemHolder.textView_3.setMaxLines(20);
            
        }else{
            itemHolder.relativeLayout.setVisibility(View.VISIBLE);
            itemHolder.textView_4.setVisibility(View.GONE);
            itemHolder.textView_1.setText(ToolTime.getTimeH(diaryData.get(position).getTime(),ToolPublic.TIME_DATA));
            itemHolder.relativeLayout_2.setLayoutParams(pa2);
            itemHolder.textView_3.setMaxLines(5);
            
            //Glide.with(context).load(path).into(itemHolder.imageView_1);
            //itemHolder.imageView_1.setImageURI(Uri.fromFile(new File(path)));
            Glide.with(context).load(Uri.fromFile(new File(path))).into(itemHolder.imageView_1);
            //Picasso.with(context).load(Uri.fromFile(new File(path))).into(itemHolder.imageView_1);
            
        }
        
        itemHolder.linearLayout.setOnLongClickListener(p1 -> itemOnClick.onLongClick(p1,position,diaryData.get(position)));
        itemHolder.linearLayout.setOnClickListener(p1 -> itemOnClick.onClick(p1,position,diaryData.get(position)));
		itemHolder.textView_2.setText(diaryData.get(position).getTitle());
        itemHolder.textView_3.setText(diaryData.get(position).getText());

    }
	
    @Override
    public int getItemCount() {
        return diaryData.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout relativeLayout,relativeLayout_2;
        public LinearLayout linearLayout;
        public ImageView imageView_1;
        public TextView textView_1, textView_2, textView_3, textView_4;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.id_diary_item_relative_1);       
            relativeLayout_2 = itemView.findViewById(R.id.id_diary_item_relative_2);            
            linearLayout = itemView.findViewById(R.id.id_diary_item_linear_1);
            imageView_1 = itemView.findViewById(R.id.id_diary_item_image_1);
            textView_1 = itemView.findViewById(R.id.id_diary_item_text_1);
            textView_2 = itemView.findViewById(R.id.id_diary_item_text_2);
            textView_3 = itemView.findViewById(R.id.id_diary_item_text_3);
            textView_4 = itemView.findViewById(R.id.id_diary_item_text_4);
            
        }
    }


    public void setItemOnClick(ItemOnClick itemOnClick){
        this.itemOnClick=itemOnClick;
    }
    
    
    
    
    
    public void setDiaryData(List<Diary> diaryData) {
        this.diaryData = diaryData;
    }
    

}
