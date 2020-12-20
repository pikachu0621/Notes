/**
 * 圆形统计图
 * Created by Mr.pikachu on 2020/06/03.
 * 可以添加中心圆，
 */
package com.pikachu.record.view.statistics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import com.pikachu.record.R;
import com.pikachu.record.tool.ToolOther;
import java.util.ArrayList;
import java.util.List;

public class CircleView extends View {

    private Context context;


    /* 配置 */
    //小圆半径
    private float minCircleRadius = 20;
    //边框线颜色
    @ColorInt
    private int lineColor = Color.WHITE;
    //边框宽度
    private float lineWidth = 0.8f;
    //开始绘制角度
    private float startAngle = -90f;
    //是否要边线
    private boolean isEdge = false;


    /* class配置 */
    private float total = 0;
    private float totalPercent = 0;
    private float lineTotal = 0;
    private RectF rectF;
    private int radius;
    private float X;
    private float Y;
    private Paint paint;
    private Paint paintTow;
    private List<CircleGroupView.Data> data;


    public CircleView(Context context) {
        this(context, null, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        minCircleRadius = ta.getDimension(R.styleable.CircleView_c_min_circle_radius, ToolOther.dp2px(minCircleRadius, context));
        lineWidth = ta.getDimension(R.styleable.CircleView_c_line_width, ToolOther.dp2px(lineWidth, context));
        lineColor = ta.getColor(R.styleable.CircleView_c_line_color, lineColor);
        startAngle = ta.getFloat(R.styleable.CircleView_c_start_angle, startAngle);
        ta.recycle();
        init();

    }

    private void init() {

        data = new ArrayList<>();
        rectF = new RectF();

        paint = new Paint();//画笔
        paint.setAntiAlias(true);//抗锯齿
        paint.setStyle(Paint.Style.FILL);//填充

        paintTow = new Paint();//边框画笔
        paintTow.setAntiAlias(true);
        paintTow.setStrokeWidth(lineWidth);
        paintTow.setColor(lineColor);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
        X = getMeasuredWidth() / 2;
        Y = getMeasuredHeight() / 2;
        rectF.left = X - radius;
        rectF.top = Y - radius;
        rectF.right = X + radius;
        rectF.bottom = Y + radius;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        lineTotal = 0;
        total = 0;

        for (CircleGroupView.Data data : data) {
            total += data.value;
            if (isEdge) {
                lineTotal += lineWidth;
            }
        }

        totalPercent = (360 - lineTotal) / total;
        float d = startAngle;
        for (CircleGroupView.Data data : data) {

            paint.setColor(data.Color);//画笔颜色
            float value = totalPercent * data.value;
            canvas.drawArc(rectF, d, value, true, paint);//扇形
            d += value;
            if (isEdge) {
                canvas.drawArc(rectF, d, lineWidth, true, paintTow);//边框线
                d += lineWidth;
            }
        }

        paint.setColor(Color.WHITE);//画笔颜色
        canvas.drawCircle(X, Y, minCircleRadius, paint);//画布画圆
    }


    /**
     * 添加值_1
     *
     * @param data
     * @return
     */
    public CircleView addData(CircleGroupView.Data data) {
        this.data.add(data);
        return this;
    }

    /**
     * 添加值_2
     *
     * @param data
     * @return
     */
    public CircleView addData(List<CircleGroupView.Data> data) {
        this.data = data;
        return this;
    }

    /**
     * 结束添加
     */
    public void endAdd() {
        invalidate();
        //this.data=new ArrayList<>();
    }


    public float getMinCircleRadius() {
        return minCircleRadius;
    }

    public void setMinCircleRadius(float minCircleRadius) {
        this.minCircleRadius = minCircleRadius;
        invalidate();
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
        invalidate();
    }

    public float getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
        invalidate();
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }


}
