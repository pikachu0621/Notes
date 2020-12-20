/**
 * 顶部head 控件
 * Created by Mr.pikachu on 2020/05/28.
 *
 *      设置左边的图像
 *      xml: app:LeftImage/ java: video setLeftImageId(@DrawableRes int leftImageId)
 *      右边同上 Left改成Right
 *
 *      左边的点击事件
 *      void setLeftImageOnClick(OnClickListener onClickListener)
 *      右边同上  Left改成Right
 *
 *      其他看方法注解
 */
package com.pikachu.record.view.top;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pikachu.record.R;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolState;

import java.lang.annotation.RetentionPolicy;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.Nullable;
import java.lang.annotation.Retention;

public class TopView extends LinearLayout {

    private Context context;

    private TypedArray typedArray;

    private float height;
    private int backgroundColor;
    private int leftImageId;
    private int rightImageId;
    private float image_Padding;
    private float image_Margin;
    private float image_WH;
    private String text;
    private int textColor;
    private int textType;
    private float textSize;
    private float textPadding;
    private int textGravity;

    private int stateTowColor;
    private boolean isReturnActivity;
    private boolean isShowLeftImage;
    private boolean isShowRightImage;
    private boolean isShowText;
    private boolean isShowState;
    private int isShowStateTow;


    private RelativeLayout relativeLayout;
    private LinearLayout.LayoutParams liParams;
    private RelativeLayout.LayoutParams reParams;
    private View view;
    private View viewTow;
    private LinearLayout linearLayout;
    private ImageView leftImageView;
    private ImageView rightImageView;
    private TextView textView;


    private static final int NORMAL = 0, BOLD = 1, ITALIC = 2, BOLD_ITALIC = 3;
    @IntDef(value = {NORMAL, BOLD, ITALIC, BOLD_ITALIC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextType {
    }


    private static final int LIFE = 0, CENTRE = 1, RIGHT = 2;
    @IntDef(value = {LIFE, CENTRE, RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextGravity {
    }


    private static final int VISIBLE = 0, FIT = 1, GONE = 2;
    @IntDef(value = {VISIBLE, FIT, GONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShowStateTow {
    }


    public TopView(Context context) {
        super(context);
        this.context = context;
        height =  ToolOther.dp2px(55,context);
        backgroundColor = 0xffffffff;
        leftImageId = 0;
        rightImageId = 0;
        image_Padding =height*0.11f;
        image_Margin =  ToolOther.dp2px(5,context);
        image_WH = height;
        text = "TopView";
        textColor = context.getResources().getColor(R.color.color_515151);
        textType = BOLD;
        textSize = 18;
        textPadding = ToolOther.dp2px(15,context);
        textGravity = CENTRE;
        stateTowColor = 0x40000000;
        isReturnActivity = true;
        isShowLeftImage = true;
        isShowRightImage = true;
        isShowText = true;
        isShowState = false;
        isShowStateTow = FIT;
        //创建控件
        upView();
    }

    public TopView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopView);
        height = typedArray.getDimension(R.styleable.TopView_Height, ToolOther.dp2px(55,context));
        backgroundColor = typedArray.getColor(R.styleable.TopView_BackgroundColor, 0xffffffff);

        leftImageId = typedArray.getResourceId(R.styleable.TopView_LeftImage, 0);
        rightImageId = typedArray.getResourceId(R.styleable.TopView_RightImage, 0);

        image_Padding = typedArray.getDimension(R.styleable.TopView_Image_Padding,height*0.11f/*ToolOther.dp2px(6,context)*/);
        image_Margin = typedArray.getDimension(R.styleable.TopView_Image_Margin,  ToolOther.dp2px(5,context));
        image_WH = typedArray.getDimension(R.styleable.TopView_Image_WH, height);


        text = typedArray.getString(R.styleable.TopView_Text);
        textColor = typedArray.getColor(R.styleable.TopView_TextColor, context.getResources().getColor(R.color.color_515151));
        textType = typedArray.getInteger(R.styleable.TopView_TextType, BOLD);
        textSize = typedArray.getDimension(R.styleable.TopView_TextSize, 18);
        textPadding = typedArray.getDimension(R.styleable.TopView_TextPadding, ToolOther.dp2px(15,context));
        textGravity = typedArray.getInteger(R.styleable.TopView_TextGravity, CENTRE);

        stateTowColor = typedArray.getColor(R.styleable.TopView_StateTowColor, 0x40000000);
        isReturnActivity = typedArray.getBoolean(R.styleable.TopView_IsReturnActivity, true);
        isShowLeftImage = typedArray.getBoolean(R.styleable.TopView_IsShowLeftImage, true);
        isShowRightImage = typedArray.getBoolean(R.styleable.TopView_IsShowRightImage, true);
        isShowText = typedArray.getBoolean(R.styleable.TopView_IsShowText, true);
        isShowState = typedArray.getBoolean(R.styleable.TopView_IsShowState, false);
        isShowStateTow = typedArray.getInteger(R.styleable.TopView_IsShowStateTow, FIT);
        typedArray.recycle();
        //创建控件
        upView();
    }


    private void upView() {


        if (relativeLayout == null) {
            relativeLayout = new RelativeLayout(context);
            setTopBackgroundColor(backgroundColor);
            reParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            relativeLayout.setLayoutParams(reParams);

            int stateHeight = ToolState.getStateHeight(context);

            viewTow = new View(context);
            setStateTowColor(stateTowColor);
            viewTow.setLayoutParams(reParams);
            reParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,stateHeight );
            viewTow.setLayoutParams(reParams);
            setShowStateTow(isShowStateTow);

            LinearLayout linearLayout_1 = new LinearLayout(context);
            linearLayout_1.setOrientation(VERTICAL);
            liParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout_1.setLayoutParams(liParams);

            view = new View(context);
            view.setBackgroundColor(0x00000000);
            liParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,stateHeight );
            view.setLayoutParams(liParams);
            setShowState(isShowState);

            linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            liParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height);
            linearLayout.setLayoutParams(liParams);

            leftImageView = new ImageView(context);
            setLeftImageId(leftImageId);
            liParams = new LinearLayout.LayoutParams((int)image_WH, (int)image_WH);
            liParams.leftMargin = (int) image_Margin;
            leftImageView.setLayoutParams(liParams);

            textView = new TextView(context);
            liParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            liParams.weight = 1;
            setText(text);
            setTextType(textType);
            setTextGravity(textGravity);
            setTextColor(textColor);
            setTextSize(textSize);
            setTextPadding(textPadding);
            textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            textView.setMaxLines(1);
            textView.setLayoutParams(liParams);

            rightImageView = new ImageView(context);
            setRightImageId(rightImageId);
            liParams = new LinearLayout.LayoutParams((int)image_WH, (int)image_WH);
            liParams.rightMargin = (int) image_Margin;
            rightImageView.setLayoutParams(liParams);

            setImage_Padding(image_Padding);



            linearLayout.addView(leftImageView);
            linearLayout.addView(textView);
            linearLayout.addView(rightImageView);

            linearLayout_1.addView(view);
            linearLayout_1.addView(linearLayout);

            relativeLayout.addView(linearLayout_1);
            relativeLayout.addView(viewTow);

            addView(relativeLayout);
        }

        setLeftImageOnClick(new OnClickListener(){

				@Override
				public void onClick(View p1) {
					((Activity) context).finish();
				}
			});
        setReturnActivity(isReturnActivity);
    }


    /**
     * 左边点击事件
     * @param onClickListener
     */
    public void setLeftImageOnClick(OnClickListener onClickListener){
        leftImageView.setOnClickListener(onClickListener);
    }

    /**
     * 右边点击事件
     * @param onClickListener
     */
    public void setRightImageOnClick(OnClickListener onClickListener){
        rightImageView.setOnClickListener(onClickListener);
    }


    /**
     * 左边长按事件
     * @param onLongClickListener
     */
    public void setLeftImageOnLongClick(OnLongClickListener onLongClickListener){
        leftImageView.setOnLongClickListener(onLongClickListener);
    }

    /**
     * 右边长按事件
     * @param onLongClickListener
     */
    public void setRightImageOnLongClick(OnLongClickListener onLongClickListener){
        rightImageView.setOnLongClickListener(onLongClickListener);
    }



    /**
     * 设置标题字体样式
     * NORMAL = 无
     * BOLD = 加粗
     * ITALIC = 斜体
     * BOLD_ITALIC =  加粗&斜体
     *
     * xml使用  app:TextType="bold"
     * @param textType
     */
    public void setTextType(@TextType int textType) {
        this.textType = textType;
        textView.setTypeface(Typeface.defaultFromStyle(textType));
    }


    /**
     * 设置标题显示位置
     * LIFE 左边
     * CENTRE 居中
     * RIGHT 右边
     *
     * xml使用  app:TextGravity="centre"
     * @param textGravity
     */
    public void setTextGravity(@TextGravity int textGravity) {
        this.textGravity = textGravity;
        if (textGravity == LIFE)
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        else if (textGravity == RIGHT)
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        else if(textGravity==CENTRE)
            textView.setGravity(Gravity.CENTER);
    }



    /**
     * 设置TopView 的高度
     *
     * xml使用  app:Height="55dp"
     * @param height
     */
    public void setTopHeight(float height) {
        this.height = height;
        liParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ToolOther.dp2px(height, context));
        linearLayout.setLayoutParams(liParams);
//        setImage_Padding(height*0.11f);
    }


    /**
     * 设置TopView 背景
     *
     * xml使用  app:BackgroundColor="#fff"
     * @param backgroundColor
     */
    public void setTopBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        relativeLayout.setBackgroundColor(backgroundColor);
    }


    /**
     * 设置左边ImageView 的src
     *
     * xml使用  app:LeftImage="@...."
     * @param leftImageId
     */
    public void setLeftImageId(@DrawableRes int leftImageId) {
        this.leftImageId = leftImageId;
        leftImageView.setImageResource(leftImageId);
    }


    /**
     * 设置右边ImageView 的src
     *
     * xml使用  app:RightImage="@...."
     * @param rightImageId
     */
    public void setRightImageId(@DrawableRes int rightImageId) {
        this.rightImageId = rightImageId;
        rightImageView.setImageResource(rightImageId);
    }


    /**
     * 设置两个图片的内边距
     *
     * xml使用  app:Image_Padding="5dp"
     * @param image_Padding
     */
    public void setImage_Padding(float image_Padding) {
        this.image_Padding = image_Padding;
        int padding = ToolOther.dp2px(image_Padding,context);
        leftImageView.setPadding(padding,padding,padding,padding);
        rightImageView.setPadding(padding,padding,padding,padding);
    }


    /**
     * 设置两个ImageView  的外外边距  （右边的只设置 右外边距  左边的只设置 左外边距）
     *
     * xml使用  app:Image_Margin="10dp"
     * @param image_Margin
     */
    public void setImage_Margin(float image_Margin) {
        this.image_Margin = image_Margin;
        int wh = ToolOther.dp2px(image_WH,context);
        int ma = ToolOther.dp2px(image_Margin,context);
        liParams=new LinearLayout.LayoutParams(wh,wh);
        liParams.leftMargin=ma;
        leftImageView.setLayoutParams(liParams);
        liParams=new LinearLayout.LayoutParams(wh,wh);
        liParams.rightMargin=ma;
        rightImageView.setLayoutParams(liParams);
    }


    /**
     * 设置两个ImageView 的宽高 （宽高相同）
     *
     * xml使用  app:Image_WH="10dp"
     * @param image_WH
     */
    public void setImage_WH(float image_WH) {
        this.image_WH = image_WH;
        int wh = ToolOther.dp2px(image_WH,context);
        liParams=new LinearLayout.LayoutParams(wh,wh);
        leftImageView.setLayoutParams(liParams);
        rightImageView.setLayoutParams(liParams);
    }


    /**
     * 设置标题文本
     *
     * xml使用  app:Text="6666"
     * @param text
     */
    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }


    /**
     * 设置标题文本颜色
     *
     * xml使用  app:TextColor="#fff"
     * @param textColor
     */
    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        textView.setTextColor(textColor);
    }


    /**
     * 设置标题文本大小
     *
     * xml使用  app:TextSize="12sp"
     * @param textSize
     */
    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textView.setTextSize(textSize);
    }


    /**
     * 设置标题文本内边距
     * 当标题Type为LIFE 设置标题文本左边的内边距
     * 当标题Type为RIGHT 设置标题文本右边的内边距
     * 否则不设置
     *
     * xml使用  app:TextPadding="5dp"
     * @param textPadding
     */
    public void setTextPadding(float textPadding) {
        this.textPadding = textPadding;
        if (textGravity==LIFE) {
            textView.setPadding((int) textPadding, 0, 0, 0);
            textView.setEllipsize(TextUtils.TruncateAt.END);
        }else if (textGravity==RIGHT) {
            textView.setPadding(0, 0, (int) textPadding, 0);
            textView.setEllipsize(TextUtils.TruncateAt.START);
        }else{
            textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        }
    }


    /**
     * 设置 状态栏 背景颜色 一般不用改（30-50）
     *
     * xml使用  app:StateTowColor="#40000000"
     * @param stateTowColor
     */
    public void setStateTowColor(@ColorInt int stateTowColor) {
        this.stateTowColor = stateTowColor;
        viewTow.setBackgroundColor(stateTowColor);
    }


    /**
     * 是否显示 左边的ImageView
     *
     * xml使用  app:ShowLeftImage="true"
     * @param showLeftImage
     */
    public void setShowLeftImage(boolean showLeftImage) {
        isShowLeftImage = showLeftImage;
        leftImageView.setVisibility(showLeftImage ? View.VISIBLE : View.GONE);
    }


    /**
     * 是否显示 右边的ImageView
     *
     * xml使用  app:ShowRightImage="false"
     * @param showRightImage
     */
    public void setShowRightImage(boolean showRightImage) {
        isShowRightImage = showRightImage;
        rightImageView.setVisibility(showRightImage ? View.VISIBLE : View.GONE);
    }


    /**
     * 是否显示标题
     *
     * xml使用  app:ShowText="true"
     * @param showText
     */
    public void setShowText(boolean showText) {
        isShowText = showText;
        textView.setVisibility(showText ? View.VISIBLE : View.GONE);
    }


    /**
     * 是否占用状态栏空间
     *
     * xml使用  app:ShowState="false"
     * @param showState
     */
    public void setShowState(boolean showState) {
        isShowState = showState;
        view.setVisibility(!showState ? View.VISIBLE : View.GONE);
    }


    /**
     * 是否显示  加重状态栏颜色（加个黑条）
     * VISIBLE  显示
     * FIT 适配模式（低于6.0显示   高于6.0不显示）
     * GONE  不显示
     *
     * xml使用  app:ShowStateTow="fit"
     * @param showStateTow
     */
    public void setShowStateTow(@ShowStateTow int showStateTow) {
        isShowStateTow = showStateTow;
        if (showStateTow == VISIBLE) {
            viewTow.setVisibility(View.VISIBLE);
        } else if (showStateTow == FIT) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                viewTow.setVisibility(View.VISIBLE);
            } else {
                viewTow.setVisibility(View.GONE);
            }
        } else {
            viewTow.setVisibility(View.GONE);
        }
    }



    /**
     * 启用或禁用 leftImage 单机事件
     *
     * xml使用  app:ReturnActivity="true"
     * @param returnActivity
     */
    public void setReturnActivity(boolean returnActivity) {
        isReturnActivity = returnActivity;
        leftImageView.setClickable(returnActivity);
    }





    //============= GET ===============//

    public float getTopHeight() {
        return height;
    }

    public int getTopBackgroundColor() {
        return backgroundColor;
    }

    public int getLeftImageId() {
        return leftImageId;
    }

    public int getRightImageId() {
        return rightImageId;
    }

    public float getImage_Padding() {
        return image_Padding;
    }

    public float getImage_Margin() {
        return image_Margin;
    }

    public float getImage_WH() {
        return image_WH;
    }

    public String getText() {
        return text;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getTextType() {
        return textType;
    }

    public float getTextSize() {
        return textSize;
    }

    public float getTextMargin() {
        return textPadding;
    }

    public int getTextGravity() {
        return textGravity;
    }

    public int getStateTowColor() {
        return stateTowColor;
    }

    public boolean isReturnActivity() {
        return isReturnActivity;
    }

    public boolean isShowLeftImage() {
        return isShowLeftImage;
    }

    public boolean isShowRightImage() {
        return isShowRightImage;
    }

    public boolean isShowText() {
        return isShowText;
    }

    public boolean isShowState() {
        return isShowState;
    }

    public int isShowStateTow() {
        return isShowStateTow;
    }
}
