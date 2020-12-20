package com.pikachu.record.activity.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pikachu.record.R;
import com.pikachu.record.activity.account.AccountActivity;
import com.pikachu.record.activity.account.AccountAddDialogAdapter;
import com.pikachu.record.activity.dialog.PDialog;
import com.pikachu.record.activity.diary.DiaryActivity;
import com.pikachu.record.activity.diary.DiaryAddDialogAdapter;
import com.pikachu.record.activity.home.adapter.AccountAdapter;
import com.pikachu.record.activity.home.adapter.DiaryAdapter;
import com.pikachu.record.activity.home.adapter.DrawerRecyclerAdapter;
import com.pikachu.record.activity.home.adapter.MoodAdapter;
import com.pikachu.record.activity.home.adapter.TaskAdapter;
import com.pikachu.record.activity.mood.MoodActivity;
import com.pikachu.record.activity.mood.MoodAddDialogAdapter;
import com.pikachu.record.activity.task.TaskActivity;
import com.pikachu.record.activity.task.TaskAddDialogAdapter;
import com.pikachu.record.monitor.DataSynEvent;
import com.pikachu.record.monitor.ReturnImagePath;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Task;
import com.pikachu.record.tool.ToolGaussBlur;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolState;
import com.pikachu.record.view.add.AddButtonLayout;
import com.pikachu.record.view.top.TopView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/*import android.content.Context;
import android.support.multidex.MultiDex;*/

public class HomeActivity extends ReturnImagePath implements TaskAdapter.TaskActivityItemOnclick {


    //id
    private DrawerLayout drawerLayout;
    private TopView topView;
    private ImageView imageViewTopBg;
    private ImageView imageViewHead;
    private TextView textViewName;
    private TextView textViewAuto;
    private RecyclerView drawerRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AddButtonLayout addButtonLayout;

    //drawable
    private Drawable bg;

    //string
    private String str;
    private String strMy;
    private String strMood;
    private String strTask;
    private String strDiary;
    private String strAccount;
    private String strSet;

    private ArrayList<DrawerRecyclerAdapter.ListData> listData;
    private DrawerRecyclerAdapter drawerRecyclerAdapter;
    private MoodAdapter moodAdapter;
    private InitialSql initialSql;
    private TaskAdapter taskAdapter;
    private AccountAdapter accountAdapter;
    private DiaryAdapter diaryAdapter;

    private MoodAddDialogAdapter moodAddDialogAdapter;
    private DiaryAddDialogAdapter diaryAddDialogAdapter;
    private AccountAddDialogAdapter accountAddDialogAdapter;
    private TaskAddDialogAdapter taskAddDialogAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToolState.setFullScreen(this, true, false);//全屏
        setContentView(R.layout.home_activity);
        ToolState.setStateHeight(this, 0, R.id.id_home_view_1);
        findView();
        iniView();
        iniDrawer();


    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void findView() {

        drawerLayout = findViewById(R.id.id_home_drawerLayout_1);
        topView = findViewById(R.id.id_home_main_topView_1);
        imageViewTopBg = findViewById(R.id.id_home_drawer_imageView_1);
        imageViewHead = findViewById(R.id.id_home_drawer_imageView_2);
        textViewName = findViewById(R.id.id_home_drawer_textView_1);
        textViewAuto = findViewById(R.id.id_home_drawer_textView_2);
        drawerRecyclerView = findViewById(R.id.id_home_drawer_recyclerView_1);
        swipeRefreshLayout = findViewById(R.id.id_home_main_swipe_1);
        addButtonLayout = findViewById(R.id.id_home_main_AddButtonLayout);


        bg = getResources().getDrawable(R.drawable.home_drawer_my_bg);

        str = getResources().getString(R.string.long_text_test);
        strMy = getResources().getString(R.string.home_drawer_my);
        strMood = getResources().getString(R.string.home_drawer_mood);
        strTask = getResources().getString(R.string.home_drawer_task);
        strDiary = getResources().getString(R.string.home_drawer_diary);
        strAccount = getResources().getString(R.string.home_drawer_account);
        strSet = getResources().getString(R.string.home_drawer_set);

    }


    //主 ui
    private void iniView() {

        //订阅eventbus
        EventBus.getDefault().register(this);

        //初始addDialog    (4个)
        moodAddDialogAdapter = new MoodAddDialogAdapter(this, ToolPublic.MOOD_STR_TO_COLOR);
        diaryAddDialogAdapter = new DiaryAddDialogAdapter(this);
        accountAddDialogAdapter = new AccountAddDialogAdapter(this);
        taskAddDialogAdapter = new TaskAddDialogAdapter(this);


        //左边点击事件
        topView.setLeftImageOnClick(p1 -> drawerLayout.openDrawer(Gravity.LEFT));


        //菜单点击事件
        addButtonLayout.setOnItemClick((view, position) -> {
            if (position == 0) {
                moodAddDialogAdapter.showDialog(true, true);
            } else if (position == 1) {
                taskAddDialogAdapter.showDialog(true, true);
            } else if (position == 2) {
                diaryAddDialogAdapter.showDialog(true, true);
            } else if (position == 3) {
                accountAddDialogAdapter.showDialog(true, true);
            }

            //String text = ((AddButtonView) view).getText();
            //ToolOther.tw(HomeActivity.this, text, R.drawable.toast_true_icon);
        });


        //数据库初始
        initialSql = new InitialSql(this);
        //四个组件初始
        moodAdapter = new MoodAdapter(this);
        taskAdapter = new TaskAdapter(this);
        accountAdapter = new AccountAdapter(this);
        diaryAdapter = new DiaryAdapter(this);

        //下拉刷新
        swipeRefreshLayout.setOnRefreshListener(() -> {
            upDataLoadUi();
            swipeRefreshLayout.setRefreshing(false);
        });
        //设置task 的item 监听
        taskAdapter.setTaskActivityItemOnclick(this);

        //加载数据并更新ui
        upDataLoadUi();
    }


    //侧滑 ui
    private void iniDrawer() {
        //侧滑 bg 高斯  //后期登录与用户头像同步 由于部分手机性能问题（可能造成ui阻塞）这里用子线程
        new Thread(() -> {
            final Bitmap bitmap = ToolGaussBlur.GaussBlur(((BitmapDrawable) bg).getBitmap(), 50);
            imageViewTopBg.post(() -> imageViewTopBg.setImageBitmap(bitmap));
        }).start();

        listData = new ArrayList<>();
        listData.add(new DrawerRecyclerAdapter.ListData(R.drawable.home_drawer_item_my_icon, strMy));
        listData.add(new DrawerRecyclerAdapter.ListData(R.drawable.home_drawer_item_mood_icon, strMood));
        listData.add(new DrawerRecyclerAdapter.ListData(R.drawable.home_drawer_item_task_icon, strTask));
        listData.add(new DrawerRecyclerAdapter.ListData(R.drawable.home_drawer_item_account_icon, strAccount));
        listData.add(new DrawerRecyclerAdapter.ListData(R.drawable.home_drawer_item_diary_icon, strDiary));
        listData.add(new DrawerRecyclerAdapter.ListData(R.drawable.home_drawer_item_set_icon, strSet));

        drawerRecyclerAdapter = new DrawerRecyclerAdapter(this, listData);
        drawerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerRecyclerView.setAdapter(drawerRecyclerAdapter);


        //设置item的点击事件
        drawerRecyclerAdapter.setItemOnClick((position, listData) -> {

            if (position == 0) {
                //我都小窝
                ToolOther.tw(HomeActivity.this, listData.title, R.drawable.toast_true_icon);
            } else if (position == 1) {
                //心情
                startActivity(new Intent(HomeActivity.this, MoodActivity.class));

            } else if (position == 2) {
                //任务
                startActivity(new Intent(HomeActivity.this, TaskActivity.class));

            } else if (position == 3) {
                //账单
                startActivity(new Intent(HomeActivity.this, AccountActivity.class));

            } else if (position == 4) {
                //笔记
                startActivity(new Intent(HomeActivity.this, DiaryActivity.class));

            } else if (position == 5) {
                //设置
                ToolOther.tw(HomeActivity.this, listData.title, R.drawable.toast_true_icon);
            }
        });
    }


    //加载数据并更新ui   调用这个函数进行主页刷新
    private void upDataLoadUi() {
        initialSql.querySqlData((moods, tasks, accounts, diaries) -> {
            moodAdapter.loadDataUpUi(moods);
            taskAdapter.loadDataUpUi(tasks);
            accountAdapter.loadDataUpUi(accounts);
            diaryAdapter.loadDataUpUi(diaries);

        });

    }


    //topview 图片设置
    @Override
    public void setImagetPath(String path) {
        diaryAddDialogAdapter.setImagePath(path);
    }


    //eventbus 监听
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSynEvent(DataSynEvent event) {
        upDataLoadUi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除订阅
        EventBus.getDefault().unregister(this);
    }


    //首页 任务列表 完成删除回调
    @Override
    public void deleteOnClick(View view, int position, final Task task) {


        PDialog.PDialog(this)
                .setMsg("是否删除此条数据")
                .setLeftStr("删除", (v, pDialog) -> {
                    initialSql.deleteTask(task);
                    pDialog.dismiss();
                    //发布更新事件
                    upDataLoadUi();
                })
                .setRightStr("取消", null)
                .show();


    }

    @Override
    public void completeOnClick(View view, int position, Task task) {

        task.setIsAs(true);
        initialSql.updateTask(task);
        upDataLoadUi();


    }


}
