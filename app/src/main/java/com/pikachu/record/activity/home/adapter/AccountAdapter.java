package com.pikachu.record.activity.home.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.pikachu.record.R;
import com.pikachu.record.sql.table.Account;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;
import java.text.DecimalFormat;
import java.util.List;
import android.content.Intent;
import com.pikachu.record.activity.diary.DiaryActivity;
import android.view.View.OnClickListener;
import com.pikachu.record.activity.account.AccountActivity;

public class AccountAdapter {

    private final Context context;
    private final Activity activity;



    private int[] viewId={
            R.id.id_home_main_list_account_linear_1,//今 点击
            R.id.id_home_main_list_account_linear_2,//月 点击
            R.id.id_home_main_list_account_linear_3,//年 点击

            R.id.id_home_main_list_account_textView_1,//今 收 0
            R.id.id_home_main_list_account_textView_2,//今 提示 1
            R.id.id_home_main_list_account_textView_3,//今 支 2
            R.id.id_home_main_list_account_textView_4,//月 收 3
            R.id.id_home_main_list_account_textView_5,//月 提示 4
            R.id.id_home_main_list_account_textView_6,//月 收 5
            R.id.id_home_main_list_account_textView_7,//年 收 6
            R.id.id_home_main_list_account_textView_8,//年 提示 7
            R.id.id_home_main_list_account_textView_9,//年 收 8
    };
    private LinearLayout[] linearLayouts=new LinearLayout[3];
    private TextView[] textViews=new TextView[9];

    private int stringId[]={
           R.string.home_main_account_7,
            R.string.home_main_account_8,
            R.string.home_main_account_5,
            R.string.home_main_account_9,
            R.string.home_main_account_10,

    };
    private String[] stringsRes=new String[5];



    public AccountAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;
        findView();
    }

    private void findView() {

        for (int i=0;i<viewId.length;i++){
            View view=activity.findViewById(viewId[i]);
            if (i<3)
                linearLayouts[i]=(LinearLayout)view;
            else
                textViews[i-3]=(TextView) view;
        }

        for (int i=0;i<stringId.length;i++) {
            stringsRes[i] = context.getResources().getString(stringId[i]);
        }
        
        
        //三个点击事件
        for(View r:linearLayouts){
            r.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View p1) {
                        activity.startActivity(new Intent(context, AccountActivity.class));
                    }
                });
         }
        

    }

    public void loadDataUpUi(List<Account> accounts) {


        //返回  今  月  年 的收支
        float[] zAndS= tdBranch(accounts);

        DecimalFormat df = new DecimalFormat("#00.00");
        textViews[0].setText(df.format(zAndS[0]));
        textViews[2].setText(df.format(Math.abs(zAndS[1])));
        
        textViews[3].setText(df.format(zAndS[2]));
        textViews[5].setText(df.format(Math.abs(zAndS[3])));
        
        textViews[6].setText(df.format(zAndS[4]));
        textViews[8].setText(df.format(Math.abs(zAndS[5])));


        //返回 提示字符串
        // 0（今天还没有记录||今天共有n条记录）
        // 1（根据当前时间判断，今天是那个月并返回 （6月01日 - 6月30日）
        // 2（返回本年号 2020年）
        String[] strTimes= tsTime(accounts);
        textViews[1].setText(strTimes[0]);
        textViews[4].setText(strTimes[1]);
        textViews[7].setText(strTimes[2]);
        
        
        
    }

    private String[] tsTime(List<Account> accounts) {
        String[] strings = new String[3];
        int oneL=0;
        String endItem = ToolTime.getItem(ToolPublic.TIME_DATA);
        String tDayItem = ToolTime.getItem("yyyy/MM/dd")+" 00:00:00";
        for (Account account: accounts) {
            String startItem = account.getItem();
            int i0 = ToolTime.compareDate(tDayItem, startItem, ToolPublic.TIME_DATA);
            int i1 = ToolTime.compareDate(startItem,endItem, ToolPublic.TIME_DATA);
            if ((i0==-1||i0==0)&&(i1==-1||i1==0))
                oneL ++;
        }
        if (oneL>0){
            strings[0]=stringsRes[0]+oneL+stringsRes[1];
        }else {
            strings[0]=stringsRes[2];
        }


        String thisMonthFirstDay = ToolTime.getThisMonthFirstDay(stringsRes[3]);
        String thisMonthLastDay = ToolTime.getThisMonthLastday(stringsRes[3]);
        strings[1]=thisMonthFirstDay+"  -  "+ thisMonthLastDay;

        strings[2]=ToolTime.getYearStart()+stringsRes[4];
        return strings;
    }

    private float[] tdBranch(List<Account> accounts) {
        //返回  今  月  年 的收支
        float[] floats = new float[6];
        String endItem = ToolTime.getItem(ToolPublic.TIME_DATA);

        String tDayItem = ToolTime.getItem("yyyy/MM/dd")+" 00:00:00";
        String thisMonthFirstDay = ToolTime.getThisMonthFirstDay("yyyy/MM/dd")+" 00:00:00";
        String year = ToolTime.getItem("yyyy")+"/01/01 00:00:00";

        for (Account account : accounts) {
            String startItem = account.getItem();




            int i0 = ToolTime.compareDate(tDayItem, startItem, ToolPublic.TIME_DATA); //tDayItem < startItem || tDayItem = startItem
            int i1 = ToolTime.compareDate(startItem,endItem, ToolPublic.TIME_DATA);// startItem < endItem || startItem = endItem
            if ((i0==-1||i0==0)&&(i1==-1||i1==0)){
                if (account.getBudget()){
                    floats[1]+=account.getHowMuch();
                }else {
                    floats[0]+=account.getHowMuch();
                }
            }


            int l0 = ToolTime.compareDate(thisMonthFirstDay, startItem, ToolPublic.TIME_DATA);
            int l2 = ToolTime.compareDate(startItem,endItem, ToolPublic.TIME_DATA);
            if ((l0==-1||l0==0)&&(l2==-1||l2==0)) {
                if (account.getBudget()){
                    floats[3]+=account.getHowMuch();
                }else {
                    floats[2]+=account.getHowMuch();
                }
            }


            int l3 = ToolTime.compareDate(year, startItem, ToolPublic.TIME_DATA);
            int l4 = ToolTime.compareDate(startItem,endItem, ToolPublic.TIME_DATA);
            if ((l3==-1||l3==0)&&(l4==-1||l4==0)) {
                if (account.getBudget()){
                    floats[5]+=account.getHowMuch();
                }else {
                    floats[4]+=account.getHowMuch();
                }
            }
        }


        return floats;
    }

}
