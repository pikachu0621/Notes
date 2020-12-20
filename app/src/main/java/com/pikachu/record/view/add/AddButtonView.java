/**
 * 添加菜单 按键布局
 * Created by Mr.pikachu on 2020/05/31.
 *
 * 属性看attrs.xml
 *
 */

package com.pikachu.record.view.add;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.pikachu.record.R;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.view.radius.QMUIRadiusImageView;

public class AddButtonView extends LinearLayout {

    private Context context;

    private TypedArray typedArray;



    /* 属性配置 */
    private int src=0;
    private int imageSize = 80;
    private int imageRadius = 20;
    private boolean imageIsCircle = true;

    private int imageBorderWidth=0;
    private int imageBorderColor=0x00000000;

    private String text = "AddButtonView";
    private int textType=Typeface.BOLD;
    private int textColor=0xff000000;
    private float textSize=11;
    private int textImageMargin=5;
    private boolean textIsShow=true;


    private LinearLayout linearLayout;
    private LinearLayout.LayoutParams liParams;
    private QMUIRadiusImageView qmuiRadiusImageView;
    private TextView textView;



    public AddButtonView(Context context) {
        this(context,null);
    }


    public AddButtonView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddButtonView);

        src = typedArray.getResourceId(R.styleable.AddButtonView_src, src);
        imageSize = typedArray.getDimensionPixelSize(R.styleable.AddButtonView_image_size, imageSize);
        imageRadius = typedArray.getDimensionPixelSize(R.styleable.AddButtonView_image_radius, imageRadius);
        imageIsCircle = typedArray.getBoolean(R.styleable.AddButtonView_image_is_circle, imageIsCircle);

        imageBorderWidth = typedArray.getDimensionPixelSize(R.styleable.AddButtonView_image_border_width, imageBorderWidth);
        imageBorderColor = typedArray.getColor(R.styleable.AddButtonView_image_border_color, imageBorderColor);

        text = typedArray.getString(R.styleable.AddButtonView_text);
        textType = typedArray.getInteger(R.styleable.AddButtonView_text_type, textType);
        textColor = typedArray.getColor(R.styleable.AddButtonView_text_color, textColor);
        textSize = typedArray.getDimension(R.styleable.AddButtonView_text_size, textSize);
        textImageMargin = typedArray.getDimensionPixelSize(R.styleable.AddButtonView_text_image_margin,  textImageMargin);
        textIsShow = typedArray.getBoolean(R.styleable.AddButtonView_text_is_show, textIsShow);

        typedArray.recycle();

        upView();
    }

    public void upView() {

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        liParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(liParams);

        qmuiRadiusImageView = new QMUIRadiusImageView(context);
        setSrc(src);
        liParams=new LinearLayout.LayoutParams(imageSize,imageSize);
        qmuiRadiusImageView.setLayoutParams(liParams);
        qmuiRadiusImageView.setCornerRadius(imageRadius);
        setImageIsCircle(imageIsCircle);
        qmuiRadiusImageView.setBorderWidth( imageBorderWidth);
        setImageBorderColor(imageBorderColor);
        qmuiRadiusImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        textView = new TextView(context);
        liParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        liParams.topMargin= textImageMargin;
        textView.setLayoutParams(liParams);
        setText(text);
        setTextType(textType);
        setTextColor(textColor);
        setTextIsShow(textIsShow);
        setTextSize(textSize);

        linearLayout.addView(qmuiRadiusImageView);
        linearLayout.addView(textView);
        addView(linearLayout);
    }

    public void setSrc(@DrawableRes int src) {
        this.src = src;
        qmuiRadiusImageView.setImageResource(src);
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
        int i = ToolOther.dp2px(imageSize, context);
        liParams=new LinearLayout.LayoutParams(i,i);
        qmuiRadiusImageView.setLayoutParams(liParams);
    }

    public void setImageRadius(int imageRadius) {
        this.imageRadius = imageRadius;
        qmuiRadiusImageView.setCornerRadius(ToolOther.dp2px(imageRadius,context));
    }

    public void setImageIsCircle(boolean imageIsCircle) {
        this.imageIsCircle = imageIsCircle;
        qmuiRadiusImageView.setCircle(imageIsCircle);
    }

    public void setImageBorderWidth(int imageBorderWidth) {
        this.imageBorderWidth = imageBorderWidth;
        qmuiRadiusImageView.setBorderWidth( ToolOther.dp2px(imageBorderWidth,context));
    }

    public void setImageBorderColor(@ColorInt int imageBorderColor) {
        this.imageBorderColor = imageBorderColor;
        qmuiRadiusImageView.setBorderColor(imageBorderColor);
    }




    public void setText(String text) {
        this.text = text;
        textView.setText(text);
    }

    public void setTextType( int textType) {
        this.textType = textType;
        textView.setTypeface(Typeface.defaultFromStyle(textType));
    }

    public void setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        textView.setTextColor(textColor);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        textView.setTextSize(textSize);
    }

    public void setTextImageMargin(int textImageMargin) {
        this.textImageMargin = textImageMargin;
        liParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        liParams.topMargin= ToolOther.dp2px(textImageMargin,context);
        textView.setLayoutParams(liParams);
    }

    public void setTextIsShow(boolean textIsShow) {
        this.textIsShow = textIsShow;
        textView.setVisibility(textIsShow ? View.VISIBLE : View.GONE);
    }







    public int getSrc() {
        return src;
    }

    public int getImageSize() {
        return imageSize;
    }

    public int getImageRadius() {
        return imageRadius;
    }

    public boolean isImageIsCircle() {
        return imageIsCircle;
    }

    public int getImageBorderWidth() {
        return imageBorderWidth;
    }

    public int getImageBorderColor() {
        return imageBorderColor;
    }

    public String getText() {
        return text;
    }

    public int getTextType() {
        return textType;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public int getTextImageMargin() {
        return textImageMargin;
    }

    public boolean isTextIsShow() {
        return textIsShow;
    }



}
