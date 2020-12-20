package com.pikachu.record.monitor;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.pikachu.record.R;
import com.pikachu.record.tool.ToolPublic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
时间选择
*/
public class TimeChoose {
    
    
    private Context context;
    private TimePickerView pvCustomTime;
    private OnTimeSelectListener onTimeSelectListener;
    private Calendar selectedDate,endDate;
    private int center;
    
    public TimeChoose(Context context,OnTimeSelectListener onTimeSelectListener){
        this.context=context;
        this.onTimeSelectListener =onTimeSelectListener;
       
        center=context.getResources().getColor(R.color.colorPrimary);
        init();
    }
    
    
    
    
    

    public void init(){
        
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         pvCustomTime.show();
         */
        selectedDate = Calendar.getInstance();//系统当前时间
        //startDate = Calendar.getInstance();
        //startDate.set(2014, 1, 23);
        endDate = Calendar.getInstance();
        endDate.set(2100, 0, 0);
        //时间选择器 ，自定义布局
        
        
           
            
        
        
        pvCustomTime = new TimePickerBuilder(context, onTimeSelectListener)
        
        
        
        
            /*.setType(TimePickerView.Type.ALL)//default is all
             .setCancelText("Cancel")
             .setSubmitText("Sure")
             .setContentTextSize(18)
             .setTitleSize(20)
             .setTitleText("Title")
             .setTitleColor(Color.BLACK)*/
             /*.setDividerColor(Color.WHITE)//设置分割线的颜色
             */
             .setTextColorCenter(center)//设置选中项的颜色
             /*
             .setLineSpacingMultiplier(1.6f)//设置两横线之间的间隔倍数
             .setTitleBgColor(Color.DKGRAY)//标题背景颜色 Night mode
             .setBgColor(Color.BLACK)//滚轮背景颜色 Night mode
             .setSubmitColor(Color.WHITE)
             .setCancelColor(Color.WHITE)*/
            /*.animGravity(Gravity.RIGHT)// default is center*/
            .setDate(selectedDate)
            
            .setRangDate(selectedDate, endDate)
            
            
            
            
            
            
            .setLayoutRes(R.layout.time_choose_ui, new CustomListener() {

                @Override
                public void customLayout(View v) {
                    
                    
                    TextView tvSubmit = v.findViewById(R.id.id_time_choose_text_1);
                    
                    tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvCustomTime.returnData();
                                pvCustomTime.dismiss();
                            }
                        });
                        
                }
                
                
                
                
            })
            
            .setContentTextSize(18)
            .setType(new boolean[]{true, true, true, true, true, false})
            .setLabel("年", "月", "日", "时", "分", "秒")
            .setLineSpacingMultiplier(1.2f)
            .setTextXOffset(0, 0, 0, 0, 0, 0)
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .setDividerColor(0x00000000)
            .isDialog(true)
            .build();
        
            
            
            /*
        Dialog mDialog = pvCustomTime.getDialog();
        
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                );

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvCustomTime.getDialogContainerLayout().setLayoutParams(params);

            
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setType(WindowManager.LayoutParams.TYPE_STATUS_BAR);
                
                //dialogWindow.findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                
            }
            
        }*/
        
        
        
        
    }
    
    
    public void show(){
        pvCustomTime.show();
    }
    
    
    public void show(String dataStr){
        SimpleDateFormat sdf = new SimpleDateFormat(ToolPublic.TIME_DATA, Locale.CHINA);
        Date date=null;
        try {
            date = sdf.parse(dataStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectedDate = Calendar.getInstance();
        selectedDate.setTime(date);
        pvCustomTime.setDate(selectedDate);
        pvCustomTime.show();
    }
    public void show(Date date){
        pvCustomTime.setDate(selectedDate);
        pvCustomTime.show();
    }
    
    
    public void dismiss(){
        pvCustomTime.dismiss();
    }
    
    
    
    
    
    
    
    
    
    
    
    
}
