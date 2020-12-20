package com.pikachu.record.activity.account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pikachu.record.R;
import com.pikachu.record.monitor.DataSynEvent;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Account;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;


//弹窗
public class AccountAddDialogAdapter {


    private final Context context;
    private final Activity activity;
    private InitialSql initialSql;
    private BottomSheetDialog dialog;
    private View box_view;
    private TextView titleTextView, finishTextView, sTextView, zTextView;
    private EditText titleEditView, zsEditView, msgEditView;
    private String upStr, addStr, finishStr, initStr_1, initStr_2;
    private boolean budget;
    private int zColor, sColor, fff, bar;
    private DecimalFormat df;
    private GradientDrawable[] dra;

    //添加结束后
    public interface EndAdd {
        void endAdd();
    }

    private EndAdd endAdd = new EndAdd() {
        @Override
        public void endAdd() {
        }
    };


    @SuppressLint("NewApi")
    public AccountAddDialogAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;
        zColor = context.getResources().getColor(R.color.color_pay);
        sColor = context.getResources().getColor(R.color.color_enter);
        fff = context.getResources().getColor(R.color.color_ffffff);
        bar = context.getResources().getColor(R.color.color_515151);


        upStr = context.getResources().getString(R.string.mood_mood_list_3);
        addStr = context.getResources().getString(R.string.home_account);
        finishStr = context.getResources().getString(R.string.mood_complete);
        initStr_1 = context.getResources().getString(R.string.account_title_hint);
        initStr_2 = context.getResources().getString(R.string.account_remark_hint);

        initialSql = new InitialSql(context);
        df = new DecimalFormat("#00.00");


        initDar();

    }


    private void initDar() {


        dra = new GradientDrawable[]{
                new GradientDrawable(),
                new GradientDrawable(),
                new GradientDrawable(),
                new GradientDrawable(),
                new GradientDrawable()};


        dra[0].setCornerRadius(1000);
        dra[0].setColor(zColor);

        dra[1].setCornerRadius(1000);
        dra[1].setColor(sColor);

        dra[2].setCornerRadius(1000);
        dra[2].setColor(0x000000);

        dra[3].setCornerRadius(ToolOther.dp2px(10, context));
        dra[3].setStroke(ToolOther.dp2px(1, context), zColor);

        dra[4].setCornerRadius(ToolOther.dp2px(10, context));
        dra[4].setStroke(ToolOther.dp2px(1, context), sColor);


    }


    @SuppressLint("NewApi")
    public void showDialog(boolean cancelable, boolean cancel) {
        findDialogView();

        dialogH(cancelable, cancel, addStr, "", true, 0, "", new OnClickListener() {
            @Override
            public void onClick(View p1) {
                addAndUpData(false, null);
            }
        });

    }


    //更新，和详情
    @SuppressLint("NewApi")
    public void showDialog(boolean cancelable, boolean cancel, final Account account) {
        budget = account.getBudget();
        findDialogView();
        dialogH(cancelable, cancel, upStr,
                account.getTitle(),
                account.getBudget(),
                account.getHowMuch(),
                account.getText(),
                new OnClickListener() {
                    @Override
                    public void onClick(View p1) {
                        addAndUpData(true, account);
                    }
                });
    }


    private void findDialogView() {

        if (dialog == null) {
            dialog = new BottomSheetDialog(context, R.style.BottomSheetEdit) {
                @Override
                public void onStart() {
                    super.onStart();
                    if (box_view == null) return;
                    View parent = (View) box_view.getParent();
                    BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
                    box_view.measure(0, 0);
                    behavior.setPeekHeight(box_view.getMeasuredHeight());
                    CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) parent.getLayoutParams();
                    params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                    parent.setLayoutParams(params);

                }
            };
            box_view = LayoutInflater.from(context).inflate(R.layout.account_add_data_ui, null);
            titleTextView = box_view.findViewById(R.id.id_account_add_data_text_2);
            finishTextView = box_view.findViewById(R.id.id_account_add_data_text_1);
            titleEditView = box_view.findViewById(R.id.id_account_add_data_edit_1);
            zTextView = box_view.findViewById(R.id.id_account_add_data_text_3);
            sTextView = box_view.findViewById(R.id.id_account_add_data_text_4);
            zsEditView = box_view.findViewById(R.id.id_account_add_data_edit_2);
            msgEditView = box_view.findViewById(R.id.id_account_add_data_edit_3);
            dialog.setContentView(box_view);
            //findViewById(R.id.design_bottom_sheet)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            zTextView.setOnClickListener(p1 -> isSz(budget = true));
            sTextView.setOnClickListener(p1 -> isSz(budget = false));
            zsEditView.addTextChangedListener(new MoneyTextWatcher(zsEditView));

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void dialogH(boolean cancelable, boolean cancel,
                         String titleStr, String titleEditStr,
                         boolean budgets, float much,
                         String msg, OnClickListener onClick) {
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.show();

        titleTextView.setText(titleStr);
        titleEditView.setText(titleEditStr);

        isSz(budgets);
        zsEditView.setText(df.format(Math.abs(much)));

        msgEditView.setText(msg);
        finishTextView.setOnClickListener(onClick);
    }


    public void cancelDialog() {
        if (dialog != null) dialog.cancel();
    }


    //isUpData=false 添加数据，isUpData=true  更新数据;
    private void addAndUpData(boolean isUpData, Account account) {


        String title = titleEditView.getText().toString();
        String sz = zsEditView.getText().toString();
        float much = 0;
        String msg = msgEditView.getText().toString();


        if (title.equals(""))
            title = initStr_1;

        if (sz.equals(""))
            much = 0;
        else
            much = Float.parseFloat(sz);

        if (budget)
            much = -Math.abs(much);
        else
            much = Math.abs(much);


        if (msg.equals(""))
            msg = initStr_2;

        if (isUpData) {
            account.setBudget(budget);
            account.setTitle(title);
            account.setHowMuch(much);
            account.setText(msg);
            initialSql.updateAccount(account);
        } else {
            initialSql.setOneAccountData(new Account(null, budget, title, much, msg, ToolTime.getItem(ToolPublic.TIME_DATA)));
        }
        ToolOther.tw(activity, finishStr, R.drawable.toast_true_icon);
        cancelDialog();
        //发布事件
        EventBus.getDefault().post(new DataSynEvent());
        endAdd.endAdd();
    }


    private void isSz(boolean budget) {
        if (budget) {
            zTextView.setTextColor(fff);
            zTextView.setBackground(dra[0]);
            sTextView.setTextColor(bar);
            sTextView.setBackground(dra[2]);
            zsEditView.setTextColor(zColor);
            zsEditView.setBackground(dra[3]);

        } else {

            //GradientDrawable dra=(GradientDrawable)zTextView.getBackground();
            zTextView.setTextColor(bar);
            zTextView.setBackground(dra[2]);
            sTextView.setTextColor(fff);
            sTextView.setBackground(dra[1]);
            zsEditView.setTextColor(sColor);
            zsEditView.setBackground(dra[4]);

        }


    }


    //添加或者刷新
    public void setEndAdd(EndAdd endAdd) {
        this.endAdd = endAdd;
    }


}
