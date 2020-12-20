/**
 * 添加菜单 按键布局
 * Created by Mr.pikachu on 2020/05/31.
 * 属性看attrs.xml
 *
 *
 * 快速点击展开或关闭会有 动画bug   优化了但是还有，写烦了不想改了
 *
 */
package com.pikachu.record.view.add;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;


import com.pikachu.record.R;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;

import java.util.ArrayList;


public class AddButtonLayout extends ViewGroup {


    private Context context;
    private TypedArray typedArray;


    /* 配置需要 */

    //0<=   起始角度  < 结束角度
    private float startAngle = 60;

    //startAngle <   结束角度  <= 220
    private float endAngle = 220;

    //右边外边距
    private int maxRightMargin = 180;

    //底部外边距
    private int maxBottomMargin = 180;

    //外圈按键到 大按键的距离
    private int minToMaxDistance = 300;

    //是否展开状态
    private boolean isShow = false;

    //动画速度
    private int animatorSpeed=100;

    //点击事件
    private OnItemClick onItemClick=new OnItemClick(){

		@Override
		public void click(View view, int position) {
		}
	};


    /* class 需要 */
    private float xx = 0;
    private float yy = 0;
    private boolean isRotation = false;
    private ArrayList<ViewXY> viewXIES;
    //控制动画是否可执行
    private boolean animatorIsEnd = true;



    //坐标保存
    public static class ViewXY {
        public float x;
        public float y;

        public ViewXY(float x, float y) {
            this.x = x;
            this.y = y;
        }


    }


    //点击事件 API
    public interface OnItemClick {
        void click(View view, int position);
    }

    //动画 API
    public interface OnAnimatorSet {
		AnimatorSet startAnimation(View view, long item, float startX, float startY, float endX, float endY);
		AnimatorSet endAnimation(View view, long item, float startX, float startY, float endX, float endY);
    }

	// 可通过  setOnAnimatorSet(OnAnimatorSet onAnimatorSet)  自定义   展开，消失   动画
    public OnAnimatorSet onAnimatorSet=new OnAnimatorSet() {
        @Override
        public AnimatorSet startAnimation(View view, long item, float startX, float startY, float endX, float endY) {
            return startA(view, item, startX, startY, endX, endY);
        }
        @Override
        public AnimatorSet endAnimation(View view, long item, float startX, float startY, float endX, float endY) {
            return endA(view, item, startX, startY, endX, endY);
        }
    };



    public AddButtonLayout(Context context) {
        this(context, null, 0);
    }

    public AddButtonLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.AddButtonLayout);
        startAngle = typedArray.getFloat(R.styleable.AddButtonLayout_start_angle, startAngle);
        endAngle = typedArray.getFloat(R.styleable.AddButtonLayout_end_angle, endAngle);
        maxRightMargin = typedArray.getDimensionPixelSize(R.styleable.AddButtonLayout_max_right_margin, maxRightMargin);
        maxBottomMargin = typedArray.getDimensionPixelSize(R.styleable.AddButtonLayout_max_bottom_margin, maxBottomMargin);
        minToMaxDistance = typedArray.getDimensionPixelSize(R.styleable.AddButtonLayout_min_to_max_distance, minToMaxDistance);
        isShow = typedArray.getBoolean(R.styleable.AddButtonLayout_is_show, isShow);
        animatorSpeed = typedArray.getInteger(R.styleable.AddButtonLayout_animator_speed, animatorSpeed);
        typedArray.recycle();
    }


    @SuppressLint("NewApi")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //测量并保存layout的宽高(使用getDefaultSize时，wrap_content和match_perent都是填充屏幕)
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
							 getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {


        Log.d(ToolPublic.TOG, "Left->" + l + "Top->" + t + "Right->" + r + "Bottom->" + b);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int childCount = getChildCount();

        //原点坐标
        int dotsX = 0, dotsY = 0;

        //90<=   起始角度  < 结束角度
        float startAngleTrue = startAngle >= 0 && startAngle < endAngle ? startAngle + 90 : 90;

        //startAngle <   结束角度  <= 300
        float endAngleTrue = endAngle > startAngle && endAngle <= 220 ? endAngle + 90 : 310;

        //平均角度
        float averageAngle = (endAngleTrue - startAngleTrue) / (childCount - 1 > 0 ? childCount - 1 : 1);

        viewXIES = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {

            View childView = getChildAt(i);

            int w = childView.getMeasuredWidth();
            int h = childView.getMeasuredHeight();
            int wr = w / 2;
            int hr = h / 2;


            if (i == 0) {

                //计算 大的添加按键位置
                int left = measuredWidth - (w + maxRightMargin);
                int top = measuredHeight - (h + maxBottomMargin);
                int right = measuredWidth - maxRightMargin;
                int bottom = measuredHeight - maxBottomMargin;

                childView.layout(left, top, right, bottom);


                dotsX = left + wr;
                dotsY = top + hr;

                xx = childView.getX();
                yy = childView.getY();
                viewXIES.add(i, new ViewXY(xx, yy));
                setOneOnClick(childView);
            } else {

                final int ii=i;
                //计算 小的添加按键位置
                /// x1   =   x0   +   r   *   cos(ao   *   3.14   /180   )
                // y1   =   y0   +   r   *   sin(ao   *   3.14   /180   )

                double varI=(startAngleTrue + averageAngle * (i - 1)) * Math.PI / 180;
                double x1 = dotsX + minToMaxDistance * Math.cos(varI);
                double y1 = dotsY + minToMaxDistance * Math.sin(varI);

                childView.layout((int) x1 - wr, (int) y1 - hr, (int) x1 + wr, (int) y1 + hr);

                float x = childView.getX();
                float y = childView.getY();
                viewXIES.add(i, new ViewXY(x, y));
                childView.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							onItemClick.click(v, ii - 1);
						}
					});
				
				if (!isShow) {
                    childView.setVisibility(View.INVISIBLE);
                    continue;
                }
                onAnimatorSet.startAnimation(childView, animatorSpeed * i, xx - x, yy - y, 0, 0)
					.start();

            }

        }


        if (isShow) {
            //旋转
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(getChildAt(0), "rotation", 0, 45);
            objectAnimator.setDuration(animatorSpeed * 3);
            objectAnimator.setInterpolator(new Interpolator(){
					@Override
					public float getInterpolation(float t_1) {
						int var = 5;
						t_1 -= 1.0f;
						return t_1 * t_1 * ((var + 1) * t_1 + var) + 1.0f;
					}
				});	
		    objectAnimator.start();
            isRotation = true;
        }

    }


    //   展开/关闭   按键点击事件
    public void setOneOnClick(View view) {

        view.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					float startRotation;
					float endRotation;


					if (isRotation) {
						//点击收起
						startRotation = 45;
						endRotation = 0;
						isRotation = false;

						if (animatorIsEnd) {
							animatorIsEnd = false;
							int childCount = getChildCount() - 1;
							for (int i = childCount; i >= 1; i--) {
								final int ii = i;
								final View childView = getChildAt(i);

								float x = viewXIES.get(i).x;
								float y = viewXIES.get(i).y;

								AnimatorSet animatorSet_false = onAnimatorSet.endAnimation(childView, animatorSpeed * (childCount - i), 0, 0, xx - x, yy - y);
								animatorSet_false.addListener(new AnimatorListenerAdapter() {
										@Override
										public void onAnimationEnd(Animator animation) {
											super.onAnimationEnd(animation);
											childView.setVisibility(View.INVISIBLE);
											if (ii == 1)
												animatorIsEnd = true;
										}
									});
								animatorSet_false.start();
							}
						}

					} else {

						startRotation = 0;
						endRotation = 45;
						isRotation = true;

						if (animatorIsEnd) {
							animatorIsEnd = false;
							final int childCount = getChildCount();
							for (int i = 1; i < childCount; i++) {
								final int ii = i;
								View childView = getChildAt(i);

								childView.setVisibility(View.VISIBLE);

								float x = viewXIES.get(i).x;
								float y = viewXIES.get(i).y;

								AnimatorSet animatorSet = onAnimatorSet.startAnimation(childView, animatorSpeed * i, xx - x, yy - y, 0, 0);
								animatorSet.addListener(new AnimatorListenerAdapter() {
										@Override
										public void onAnimationEnd(Animator animation) {
											super.onAnimationEnd(animation);
											if (childCount - 1 == ii)
												animatorIsEnd = true;
										}
									});
								animatorSet.start();
							}
						}
					}

					//旋转
					ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(v, "rotation", startRotation, endRotation);
					objectAnimator.setDuration(500);
					objectAnimator.setInterpolator(new Interpolator(){
							@Override
							public float getInterpolation(float t_1) {
								int var = 5;
								t_1 -= 1.0f;
								return t_1 * t_1 * ((var + 1) * t_1 + var) + 1.0f;
							}
						});
					objectAnimator.start();
				}
			});


    }


    //展开动画
    public AnimatorSet startA(View view, long item, float startX, float startY, float endX, float endY) {

        //渐显
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 0.6f, 1f);
        alphaAnimation.setDuration(item);
        //缩放 x
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        scaleXAnimator.setDuration(item);
        //缩放 y
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        scaleYAnimator.setDuration(item);
        //旋转
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 180, 0);
        objectAnimator.setDuration(item);
        objectAnimator.setInterpolator(new Interpolator(){
				@Override
				public float getInterpolation(float t) {
					int var = -1;
					t -= 1.0f;
					return t * t * ((var + 1) * t + var) + 1.0f;
				}
			});

		//平移 x
        ObjectAnimator ofXAnimator = ObjectAnimator.ofFloat(view, "translationX", startX, endX);
        ofXAnimator.setDuration(item);
        //平移 y
        ObjectAnimator ofYAnimator = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        ofYAnimator.setDuration(item);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofYAnimator, ofXAnimator, objectAnimator, scaleYAnimator, scaleXAnimator, alphaAnimation);

        return animatorSet;
    }


    //收起 动画
    public AnimatorSet endA(View view, long item, float startX, float startY, float endX, float endY) {

        //渐显
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.6f);
        alphaAnimation.setDuration(item);
        //缩放 x
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        scaleXAnimator.setDuration(item);
        //缩放 y
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        scaleYAnimator.setDuration(item);
        //旋转
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0, 180);
        objectAnimator.setDuration(item);
        objectAnimator.setInterpolator(new Interpolator(){
				@Override
				public float getInterpolation(float t) {
					int var = -1;
					t -= 1.0f;
					return t * t * ((var + 1) * t + var) + 1.0f;
				}
			});
		
		
	    //平移 x
        ObjectAnimator ofXAnimator = ObjectAnimator.ofFloat(view, "translationX", startX, endX);
        ofXAnimator.setDuration(item);
        //平移 y
        ObjectAnimator ofYAnimator = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        ofYAnimator.setDuration(item);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(ofYAnimator, ofXAnimator, objectAnimator, scaleYAnimator, scaleXAnimator, alphaAnimation);

        return animatorSet;
    }






    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    public void setEndAngle(float endAngle) {
        this.endAngle = endAngle;
        invalidate();
    }

    public void setMaxRightMargin(int maxRightMargin) {
        this.maxRightMargin = ToolOther.dp2px(maxRightMargin, context);
        invalidate();
    }

    public void setMaxBottomMargin(int maxBottomMargin) {
        this.maxBottomMargin = ToolOther.dp2px(maxBottomMargin, context);
        invalidate();
    }

    public void setMinToMaxDistance(int minToMaxDistance) {
        this.minToMaxDistance = ToolOther.dp2px(minToMaxDistance, context);
        invalidate();
    }

    public void setShow(boolean show) {
        isShow = show;
        invalidate();
    }

    public void setAnimatorSpeed(int animatorSpeed) {
        this.animatorSpeed = animatorSpeed;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnAnimatorSet(OnAnimatorSet onAnimatorSet) {
        this.onAnimatorSet = onAnimatorSet;
    }






    public float getStartAngle() {
        return startAngle;
    }

    public float getEndAngle() {
        return endAngle;
    }

    public int getMaxRightMargin() {
        return maxRightMargin;
    }

    public int getMaxBottomMargin() {
        return maxBottomMargin;
    }

    public int getMinToMaxDistance() {
        return minToMaxDistance;
    }

    public boolean isShow() {
        return isShow;
    }

    public int getAnimatorSpeed() {
        return animatorSpeed;
    }

    public OnItemClick getOnItemClick() {
        return onItemClick;
    }

    public OnAnimatorSet getOnAnimatorSet() {
        return onAnimatorSet;
    }






}
