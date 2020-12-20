package com.pikachu.record.activity.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pikachu.record.R;
import com.pikachu.record.activity.diary.DiaryActivity;
import com.pikachu.record.sql.table.Diary;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import java.io.File;
import java.util.List;
import java.util.Random;
//import com.bumptech.glide.Glide;


public class DiaryAdapter {

    private final Context context;
    private final Activity activity;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter;
    private Random random;
    private List<Diary> diaries;
    public DiaryAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;

        findView();
    }

    private void findView() {
        recyclerView = activity.findViewById(R.id.id_home_main_scroll_recyclerView_3);
        random = new Random(2);
    }


    public void loadDataUpUi(List<Diary> diaries) {

        //Collections.reverse(diaries);
        if (diaries.size()>ToolPublic.DIARY_FRONT){
            this.diaries=diaries.subList(0,ToolPublic.DIARY_FRONT);
        }else{
            this.diaries=diaries;
        }
        setRecyclerViewAdapter();
    }


    private void setRecyclerViewAdapter(){

        if (adapter ==null){
            adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View inflate = LayoutInflater.from(context).inflate(R.layout.home_main_list_diary_ui, parent, false);
                    return new ItemHolder(inflate);
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

                    ItemHolder itemHolder = (ItemHolder) holder;
                    String imagePath = diaries.get(position).getImagePath();
                    if (imagePath==null||imagePath.equals("")){
                        /*
                        int i = random.nextInt(ToolPublic.MOOD_STR_TO_COLOR.size());
                        itemHolder.image.setBackgroundColor(ToolPublic.MOOD_STR_TO_COLOR.get(i).color);
                        */
                        //Glide.with(context).load(R.drawable.home_drawer_my_bg).into(itemHolder.image);
                        //itemHolder.image.setImageResource(R.drawable.home_drawer_my_bg);
                        Glide.with(context).load(R.drawable.home_drawer_my_bg).into(itemHolder.image);
                    }else {
                        
                        //itemHolder.image.setImageURI(Uri.fromFile(new File(imagePath)));
                        Glide.with(context).load(Uri.fromFile(new File(imagePath))).into(itemHolder.image);
                        
                    }
                    itemHolder.time.setText(ToolTime.getTimeH(diaries.get(position).getTime(), ToolPublic.TIME_DATA));
                    itemHolder.title.setText(diaries.get(position).getTitle());
                    itemHolder.text.setText(diaries.get(position).getText());
                    itemHolder.on.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v) {
								onLinClick(v,position);
							}
						});
               }

                @Override
                public int getItemCount() {
                    return diaries.size();
                }

                class ItemHolder extends RecyclerView.ViewHolder{
                    public final LinearLayout on;
                    public final ImageView image;
                    public final TextView time;
                    public final TextView title;
                    public final TextView text;
                    public ItemHolder(@NonNull View itemView) {
                        super(itemView);
                        on = itemView.findViewById(R.id.id_home_main_list_diary_linear_1);
                        image = itemView.findViewById(R.id.id_home_main_list_diary_image_1);
                        time = itemView.findViewById(R.id.id_home_main_list_diary_text_1);
                        title = itemView.findViewById(R.id.id_home_main_list_diary_text_2);
                        text = itemView.findViewById(R.id.id_home_main_list_diary_text_3);
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


    //点击
    public void onLinClick(View view,int position){
        activity.startActivity(new Intent(context,DiaryActivity.class));
    }







}
