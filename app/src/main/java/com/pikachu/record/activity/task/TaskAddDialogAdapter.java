package com.pikachu.record.activity.task;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pikachu.record.R;
import com.pikachu.record.monitor.DataSynEvent;
import com.pikachu.record.monitor.TimeChoose;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Task;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/*import android.app.TimePickerDialog;*/
/*import android.widget.TimePicker;*/
/*import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.pikachu.record.monitor.timing.AlarmManagerTask;
import com.pikachu.record.monitor.timing.AlarmReceiver;*/


//弹窗
public class TaskAddDialogAdapter {


    private final Context context;
    private final Activity activity;
    private final InitialSql initialSql;
    private BottomSheetDialog dialog;
    private TimeChoose timeChoose;
    private View box_view;
    private TextView titleTextView, finishTextView, stopTimeTV;
    private EditText msgEditView, titleEditView;
    private LinearLayout linear_1;
    private final String upStr;
    private final String addStr;
    private final String finishStr;
    private final String initStr;


    public interface EndAdd {
        void endAdd();
    }

    private EndAdd endAdd = () -> {
    };


    public TaskAddDialogAdapter(Context context) {
        this.context = context;
        activity = (Activity) context;

        int textBColor = context.getResources().getColor(R.color.color_8e8e8e);
        upStr = context.getResources().getString(R.string.mood_mood_list_3);
        addStr = context.getResources().getString(R.string.home_task);
        finishStr = context.getResources().getString(R.string.mood_complete);
        initStr = context.getResources().getString(R.string.mood_mood_list_2);
        initialSql = new InitialSql(context);
    }


    public void showDialog(boolean cancelable, boolean cancel) {


        findDialogView();
        dialogH(cancelable, cancel, addStr,
                ToolTime.getNextDay(new Date(), +1, ToolPublic.TIME_DATA), "", "", p1 -> addAndUpData(false, null));

    }


    //更新，和详情
    public void showDialog(boolean cancelable, boolean cancel, final Task task) {
        findDialogView();
        dialogH(cancelable, cancel, upStr, task.getStopTime(), task.getTitle(), task.getText(), p1 -> addAndUpData(true, task));
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
            box_view = LayoutInflater.from(context).inflate(R.layout.task_add_data_ui, null);

            titleTextView = box_view.findViewById(R.id.id_task_add_data_text_2);
            finishTextView = box_view.findViewById(R.id.id_task_add_data_text_1);

            linear_1 = box_view.findViewById(R.id.id_task_add_data_linear_1);
            stopTimeTV = box_view.findViewById(R.id.id_task_add_data_text_3);

            titleEditView = box_view.findViewById(R.id.id_task_add_data_edit_1);
            msgEditView = box_view.findViewById(R.id.id_task_add_data_edit_2);


            dialog.setContentView(box_view);
            dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    private void dialogH(boolean cancelable, boolean cancel,
                         String titleStr, final String stopStr, String titleEdit, String msg,
                         OnClickListener finishOnClick) {
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.show();
        titleTextView.setText(titleStr);
        stopTimeTV.setText(stopStr);
        titleEditView.setText(titleEdit);
        msgEditView.setText(msg);
        finishTextView.setOnClickListener(finishOnClick);
        linear_1.setOnClickListener(p1 -> {

            if (timeChoose == null) {
                timeChoose = new TimeChoose(context, (date, v) -> {//选中事件回调
                    stopTimeTV.setText(ToolTime.date2String(date, ToolPublic.TIME_DATA));
                }

                );
            }
            timeChoose.show(stopStr);


        });
    }


    public void cancelDialog() {
        if (dialog != null) dialog.cancel();
    }


    //isUpData=false 添加数据，isUpData=true  更新数据;
    private void addAndUpData(boolean isUpData, Task task) {
        String title = titleEditView.getText().toString();
        String msg = msgEditView.getText().toString();
        String stopTime = stopTimeTV.getText().toString();

        if (title.equals(""))
            title = initStr;
        if (msg.equals(""))
            msg = initStr;
        if (stopTime.equals(""))
            stopTime = ToolTime.getItem(ToolPublic.TIME_DATA);


        if (isUpData) {
            task.setTitle(title);
            task.setText(msg);
            task.setStopTime(stopTime);
            initialSql.updateTask(task);
        } else {
            initialSql.setOneTaskData(new Task(null, "", title, msg, stopTime, false, ToolTime.getItem(ToolPublic.TIME_DATA)));
            addAlarm(stopTime, task);
        }


        ToolOther.tw(activity, finishStr, R.drawable.toast_true_icon);
        cancelDialog();
        //发布事件
        EventBus.getDefault().post(new DataSynEvent());
        endAdd.endAdd();
    }


    //添加 定时
    public void addAlarm(String stopTime, Task task) {


//        
//        AlarmManagerTask.cancelAlarmBroadcast(context, task.getId(),
//                                              AlarmReceiver.class);    
    }


    //删除 定时
    public void deleteAlarm(int id) {


    }

    //更新 定时
    public void upAlarm(String stopTime, int id) {


    }


    //添加或者刷新
    public void setEndAdd(EndAdd endAdd) {
        this.endAdd = endAdd;
    }


}
