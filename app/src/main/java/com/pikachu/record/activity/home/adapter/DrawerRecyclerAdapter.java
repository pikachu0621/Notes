package com.pikachu.record.activity.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pikachu.record.R;

import java.util.ArrayList;


/**
 * 侧滑
 */

public class DrawerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<ListData> listData;
    private ItemOnClick itemOnClick;

    public static class ListData {
        public int imageId;
        public String title;

        public ListData(@DrawableRes int imageId, String title) {
            this.imageId = imageId;
            this.title = title;
        }

    }

    public interface ItemOnClick {
        void OnClick(int position, ListData listData);
    }


    public void setItemOnClick(ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }


    public DrawerRecyclerAdapter(Context context, ArrayList<ListData> listData) {
        this.context = context;
        this.listData = listData;
    }

    public DrawerRecyclerAdapter(Context context, ArrayList<ListData> listData, ItemOnClick itemOnClick) {
        this.context = context;
        this.listData = listData;
        this.itemOnClick = itemOnClick;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_drawer_item_ui, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        //((ItemHolder) holder).imageView.setImageResource();
        Glide.with(context).load(listData.get(position).imageId).into(((ItemHolder) holder).imageView);
        ((ItemHolder) holder).textView.setText(listData.get(position).title);
        ((ItemHolder) holder).linearLayout.setOnClickListener(p1 -> {
            if (itemOnClick != null)
                itemOnClick.OnClick(position, listData.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        LinearLayout linearLayout;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.id_home_drawer_item_textView_1);
            imageView = itemView.findViewById(R.id.id_home_drawer_item_imageView_1);
            linearLayout = itemView.findViewById(R.id.id_home_drawer_item_linearLayout_1);
        }
    }


}
