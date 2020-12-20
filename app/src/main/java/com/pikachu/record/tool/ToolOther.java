package com.pikachu.record.tool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;

import com.pikachu.record.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 其他工具
 * 2020-05-27
 * pikachu
 */



public class ToolOther {






    /**
     *  自定义提示
     * @param activity 上下文
     * @param msg      提示内容
     * @param img      图片资源id
     */
    public static Toast tw;
    public static void tw(Activity activity, String msg, @DrawableRes int img){
            if (tw!=null) {
                tw.cancel();
                tw=null;
            }
            tw = new Toast(activity);
            tw.setDuration(Toast.LENGTH_LONG);
            tw.setGravity(Gravity.CENTER, 0, 0);//居中

            LinearLayout linearLayout = new LinearLayout(activity);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setBackground(activity.getResources().getDrawable(R.drawable.toast_bg));
            linearLayout.setPadding(dp2px(60,activity),dp2px(20,activity),dp2px(60,activity),dp2px(20,activity));
            linearLayout.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(layoutParams);


            ImageView imageView = new ImageView(activity);
            imageView.setImageResource(img);
            layoutParams = new LinearLayout.LayoutParams(dp2px(40,activity), dp2px(40,activity));
            layoutParams.bottomMargin=dp2px(10,activity);
            imageView.setLayoutParams(layoutParams);

            TextView textView = new TextView(activity);
            textView.setText(msg);
            textView.setTextColor(0xFFFFFFFF);
            textView.setTextSize(15);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(layoutParams);

            linearLayout.addView(imageView);
            linearLayout.addView(textView);

            tw.setView(linearLayout);
            tw.show();

    }








    /**
     * 文件读取   % 读取sd卡文件    $ 读取app内部文件  @ 读取资源文件(只支持assets)
     * @param a             上下文
     * @param filePath      路径
     * @return              返回String内容
     */
    public static String fr(Context a, String  filePath){
        try{
            FileInputStream xr;
            String path=filePath.substring(1);
            String state= Environment.getExternalStorageState();
            if(filePath.startsWith("%")&&state.equals(Environment.MEDIA_MOUNTED)){
                File sdPath = Environment.getExternalStorageDirectory();//获取SD卡路径
                xr = new FileInputStream(new File(sdPath,path));
                BufferedReader fr=new BufferedReader(new InputStreamReader(xr));
                String data= fr.readLine();
                xr.close();
                return data;
            }else  if(filePath.startsWith("$")){
                xr= a.openFileInput(path);
                BufferedReader fr=new BufferedReader(new InputStreamReader(xr));
                String data= fr.readLine();
                xr.close();
                return data;
            }else if(filePath.startsWith("@")){
                InputStream is=a.getAssets().open(path);
                byte[] bf=new byte[is.available()];
                is.read(bf);
                String data=new String(bf);
                is.close();
                return data;
            }else {
                return null;
            }
        }catch (IOException e){
            Log.e("cg",e.toString());
            return null;
        }
    }







    /**
     * dp  转  px
     * @param dpValue px值
     * @param context 上下文
     */
    public static int dp2px(float dpValue, Context context) {
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics())+0.5F);
    }

    /**
     * dp  转  px
     * @param dpValue px值
     * @param context 上下文
     */
    public static int dp2px(int dpValue, Context context) {
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpValue, context.getResources().getDisplayMetrics())+0.5F);
    }


    /**
     * int 颜色转 string
     *
     * @param color
     * @return
     */
    public static String getHexString(int color) {
        String s = "#";
        int colorStr = (color & 0xff000000) | (color & 0x00ff0000) | (color & 0x0000ff00) | (color & 0x000000ff);
        s = s + Integer.toHexString(colorStr);
        return s;
    }





    //////////////图片操作///////////////

    /**
     * 图片转换成base64字符串
     *
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imgBytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }


    /**
     * Base64字符串转换成图片
     *
     * @param string
     * @return
     */
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }


    /**
     * Drawable转换成一个Bitmap
     *
     * @param drawable drawable对象
     * @return
     */
    public static final Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }





}
