package com.pikachu.record.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * 状态栏工具
 * 2020-05-27
 * pikachu
 */


public class ToolState {



    /**
     * 获取状态栏高
     * @param context
     */
    public static int getStateHeight(Context context) {
        int statusBarHeight1 = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }




    /**
     * 设置全屏，设置状态栏透明
     * @param activity
     * @param isFull  是否全屏
     * @param isWhite 态栏字体颜色是否为 白色 （默认 黑）
     */
    public static void setFullScreen(Activity activity,boolean isFull,boolean isWhite) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option;
                if(isFull) {

                    if (!isWhite){
                        option= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏底部虚拟按键
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY//设置虚拟按键是滑动出还是点击显示
                                    |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//设置状态栏字体颜色为黑色
                    }else{
                        option= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION//隐藏底部虚拟按键
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;//设置虚拟按键是滑动出还是点击显示
                    }

                }else{
                    if (!isWhite){
                        option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                |View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//设置状态栏字体颜色为黑色
                    }else{
                        option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                    }

                }
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                //设置状态栏为透明，否则在部分手机上会呈现系统默认的浅灰色
                window.setStatusBarColor(Color.TRANSPARENT);
                //window.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    window.setNavigationBarColor(Color.parseColor("#60000000"));
//                }else{
//                    //导航栏颜色也可以考虑设置为透明色   6.0以下
//                    window.setNavigationBarColor(Color.TRANSPARENT);
//                }
            } else {
                Window window = activity.getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
                window.setAttributes(attributes);
            }
        }


    }




    /**
     * 设置自定义高状态栏高，一般用于沉静式
     * @param activity
     * @param idH   idH 第一个占用控件id
     * @param idX   idC 第二个悬浮控件id
     */
    public static void setStateHeight(Activity activity, int idH, int idX){
        View view;
        int h=getStateHeight(activity);
        if (idH!=0){
            LinearLayout.LayoutParams linearParams;
            view= activity.findViewById(idH);
            linearParams = (LinearLayout.LayoutParams) view.getLayoutParams(); //取控件view 的布局管理器
            linearParams.height =h ;
            view.setLayoutParams(linearParams);
        }
        if (idX!=0) {
            view = activity.findViewById(idX);
            RelativeLayout.LayoutParams linearParams1 = (RelativeLayout.LayoutParams) view.getLayoutParams();
            linearParams1.height = h;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                //判断android版本小于6.0状态栏颜色变为#50000000  Build.VERSION.SDK_INT手机当前版本   Build.VERSION_CODES.O最低的版本
                view.setBackgroundColor(0x50000000);
            }
            view.setLayoutParams(linearParams1);
        }
    }
















}
