/**
 * 用于组合圆形统计图
 * Created by Mr.pikachu on 2020/06/03.
 */

package com.pikachu.record.view.statistics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.record.R;
import com.pikachu.record.tool.ToolOther;

import java.util.ArrayList;
import java.util.List;



public class CircleGroupView extends LinearLayout {

    private Context context;

    /* 配置 */
    private float statisticsSize=80;
    private float topMargin=10;
    private float leftMargin=10;
    private float bottomMargin=30;


    /* item 配置 没有XML属性需要可自己加 */
    private float cardViewSize=15;
    private int textColor=0xff515151;
    private float textSize=10;
    private int textType= Typeface.BOLD;
    private float textLeftMargin=3;
    private float cardLeftMargin=10;


    private List<Data> data;

    //布局
    private LinearLayout linearLayout;
    private CircleView circleView;//可get后设置属性
    private LinearLayout.LayoutParams layoutParams;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    public static class Data{
        public Data(String text, @ColorInt int color, float value) {
            this.text = text;
            Color = color;
            this.value = value;
        }
        public String text;
        public int Color;
        public float value;
    }

    public CircleGroupView(Context context) {
        this(context,null,0);
    }

    public CircleGroupView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleGroupView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleGroupView);
        statisticsSize = ta.getDimension(R.styleable.CircleGroupView_statistics_size,ToolOther.dp2px(statisticsSize, context));
        topMargin = ta.getDimension(R.styleable.CircleGroupView_top_margin,ToolOther.dp2px(topMargin,context));
        leftMargin = ta.getDimension(R.styleable.CircleGroupView_left_margin,ToolOther.dp2px(leftMargin,context));
        bottomMargin = ta.getDimension(R.styleable.CircleGroupView_bottom_margin,ToolOther.dp2px(bottomMargin,context));
        
        ta.recycle();
        newView();
    }




    @SuppressLint("WrongConstant")
    private void newView() {

        linearLayout = new LinearLayout(context);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setLayoutParams(layoutParams);

        circleView = new CircleView(context);
        layoutParams = new LinearLayout.LayoutParams((int)statisticsSize, (int)statisticsSize);
        layoutParams.leftMargin=(int)leftMargin;
        layoutParams.topMargin=(int)topMargin;
        layoutParams.bottomMargin=(int)bottomMargin;
        circleView.setLayoutParams(layoutParams);


        recyclerView = new RecyclerView(context);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        recyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
        recyclerView.setScrollBarStyle(RecyclerView.SCROLL_AXIS_NONE);
        recyclerView.setLayoutParams(layoutParams);

        linearLayout.addView(circleView);
        linearLayout.addView(recyclerView);

        addView(linearLayout);
    }





    //item 布局
    private ViewGroup getItemView(){
        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(HORIZONTAL);

        CardView cardView = new CardView(context);
        int i = ToolOther.dp2px(cardViewSize, context);
        layoutParams = new LinearLayout.LayoutParams(i, i);
        layoutParams.leftMargin=ToolOther.dp2px(cardLeftMargin,context);
        cardView.setCardElevation(0);
        cardView.setMaxCardElevation(0);
        cardView.setLayoutParams(layoutParams);

        TextView textView = new TextView(context);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin=ToolOther.dp2px(textLeftMargin,context);
        textView.setTextColor(textColor);
        textView.setTypeface(Typeface.defaultFromStyle(textType));
        textView.setTextSize(textSize);
        textView.setLayoutParams(layoutParams);

        linearLayout.addView(cardView);
        linearLayout.addView(textView);

        return linearLayout;
    }



    private void setRecyclerViewAdapter(){


        if (adapter==null) {

            adapter = new RecyclerView.Adapter() {
                @NonNull
                @Override
                public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new ItemViewHolder(getItemView());
                }

                @Override
                public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                    ((ItemViewHolder) holder).cardView.setCardBackgroundColor(data.get(position).Color);
                    ((ItemViewHolder) holder).textView.setText(data.get(position).text);
                }

                @Override
                public int getItemCount() {
                    return data.size();
                }


                class ItemViewHolder extends RecyclerView.ViewHolder {
                    CardView cardView;
                    TextView textView;

                    public ItemViewHolder(@NonNull ViewGroup itemView) {
                        super(itemView);
                        cardView = (CardView) itemView.getChildAt(0);
                        textView = (TextView) itemView.getChildAt(1);
                    }
                }
            };
            recyclerView.setAdapter(adapter);
            LinearLayoutManager ms = new LinearLayoutManager(context);
            ms.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(ms);
        }else {
            adapter.notifyDataSetChanged();
        }
    }



    /**
     * 添加值
     * @param data
     * @return
     */
    public CircleGroupView addData(Data data){
        if (this.data==null)
            this.data=new ArrayList<>();
        if (data!=null)
            this.data.add(data);
        return this;
    }
    /**
     * 结束添加
     */
    public void endAdd(){
        circleView.addData(data).endAdd();
        setRecyclerViewAdapter();
    }



    /**
     * 添加值
     * @param data
     * @return
     */
    public void addData( List<Data> data){
        /*if (this.data==null)
            this.data=new ArrayList<>();*/
        this.data=data;
        setRecyclerViewAdapter();
        circleView.addData(data);
    }





    public float getStatisticsSize() {
        return statisticsSize;
    }

    public void setStatisticsSize(float statisticsSize) {
        this.statisticsSize = ToolOther.dp2px(statisticsSize,context);
        layoutParams = new LinearLayout.LayoutParams((int)this.statisticsSize, (int)this.statisticsSize);
        circleView.setLayoutParams(layoutParams);
    }

    public float getTopMargin() {
        return topMargin;
    }

    public void setTopMargin(float topMargin) {
        this.topMargin = ToolOther.dp2px( topMargin,context);
        layoutParams = new LinearLayout.LayoutParams((int)this.statisticsSize, (int)this.statisticsSize);
        layoutParams.topMargin=(int) this.topMargin;
        circleView.setLayoutParams(layoutParams);
    }

    public float getLeftMargin() {
        return leftMargin;
    }

    public void setLeftMargin(float leftMargin) {
        this.leftMargin = ToolOther.dp2px(leftMargin,context);
        layoutParams = new LinearLayout.LayoutParams((int)this.statisticsSize, (int)this.statisticsSize);
        layoutParams.leftMargin=(int) this.leftMargin;
        circleView.setLayoutParams(layoutParams);
    }

    public float getBottomMargin() {
        return bottomMargin;
    }

    public void setBottomMargin(float bottomMargin) {
        this.bottomMargin = ToolOther.dp2px(bottomMargin,context);
        layoutParams = new LinearLayout.LayoutParams((int)this.statisticsSize, (int)this.statisticsSize);
        layoutParams.bottomMargin=(int) this.bottomMargin;
        circleView.setLayoutParams(layoutParams);
    }


    public CircleView getCircleView() {
        return circleView;
    }



}
