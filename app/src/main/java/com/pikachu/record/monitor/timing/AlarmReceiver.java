package com.pikachu.record.monitor.timing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.method.DateTimeKeyListener;
import android.util.Log;
import android.widget.Toast;
import com.pikachu.record.tool.ToolTime;
import com.pikachu.record.tool.ToolPublic;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Alarm", "从服务启动广播：at " + ToolTime.getItem(ToolPublic.TIME_DATA));
    }

}
