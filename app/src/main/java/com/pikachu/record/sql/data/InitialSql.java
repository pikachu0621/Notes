package com.pikachu.record.sql.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.pikachu.record.R;
import com.pikachu.record.sql.table.Account;
import com.pikachu.record.sql.table.AppOne;
import com.pikachu.record.sql.table.Diary;
import com.pikachu.record.sql.table.Mood;
import com.pikachu.record.sql.table.Task;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;

/**
 * 初始数据库(四个)/和查询数据（首页）
 * <p>
 * 判断是否第一次启动
 * 是的话创建数据库和数据
 *
 * 查询数据
 */

public class InitialSql {


    private Context context;
    private DaoSession daoMasterAppOne;
    private DaoSession daoMasterMood;
    private DaoSession daoMasterTask;
    private DaoSession daoMasterAccount;
    private DaoSession daoMasterDiary;
    private ToolSqlMaster instance;


    //数据库查询回调
    public interface SqlData{
        void sqlData(List<Mood> moods,List<Task> tasks,List<Account> accounts,List<Diary> diaries);
    }


    public InitialSql(Context context) {
        this.context = context;
        iniSql();
    }



    private void iniSql() {
        instance = ToolSqlMaster.getInstance(ToolPublic.APP_ONE_DB).init(context);
        daoMasterAppOne = instance.getDaoSession();

        instance = ToolSqlMaster.getInstance(ToolPublic.MOOD_DB).init(context);
        daoMasterMood = instance.getDaoSession();

        instance = ToolSqlMaster.getInstance(ToolPublic.TASK_DB).init(context);
        daoMasterTask = instance.getDaoSession();

        instance = ToolSqlMaster.getInstance(ToolPublic.ACCOUNT_DB).init(context);
        daoMasterAccount = instance.getDaoSession();

        instance = ToolSqlMaster.getInstance(ToolPublic.DIARY_DB).init(context);
        daoMasterDiary = instance.getDaoSession();
    }

    private void writeSql() {
        if (getOneApp(daoMasterAppOne)) {
            daoMasterAppOne.insertOrReplace(new AppOne(null, true));
            //Mood
            String[] moods = context.getResources().getStringArray(R.array.main_mood_str);
            for (int i = 0; i < moods.length; i++) {
                daoMasterMood.insertOrReplace(
                        new Mood(null,
                                (i < 3) ? 0 : i,
                                moods[i],
                                ToolTime.getItem(ToolPublic.TIME_DATA))
                );
            }






            //Task
            String[] tasks = context.getResources().getStringArray(R.array.main_task_str);
            String title = null;
            boolean f=false;
            for (int i = 1; i <= tasks.length; i++) {
                if (i % 2 == 0) {
                    Log.i(ToolPublic.TOG, "Title=>" + title + "Text=>" + tasks[i-1]);
                    daoMasterTask.insertOrReplace(
                            new Task(null,
                                    context.getResources().getString(R.string.main_task_1),
                                    title,
                                    tasks[i-1],
                                    context.getResources().getString(R.string.main_task_1),
                                    f=!f,
                                    ToolTime.getItem(ToolPublic.TIME_DATA))
                    );
                }
                title = tasks[i-1];
            }



            //Account
            String[] accounts = context.getResources().getStringArray(R.array.main_account_str);
            for (int i = 1; i <= accounts.length; i++) {
                if (i % 2 == 0) {
                    Log.i(ToolPublic.TOG, "Title=>" + title + "Text=>" + accounts[i-1]);
                    daoMasterAccount.insertOrReplace(
                            new Account(null,
                                    true,
                                    title,
                                    i*20,
                                    accounts[i-1],
                                    ToolTime.getItem(ToolPublic.TIME_DATA))
                    );
                }
                title = accounts[i-1];
            }


            //diary
            String[] diaries = context.getResources().getStringArray(R.array.main_diary_str);
            /*Drawable drawable = context.getResources().getDrawable(R.drawable.main_bg_icon);
            String imageString = ToolTime.bitmapToString(ToolTime.drawableToBitmap(drawable));
            Log.i(ToolPublic.TOG, imageString);*/
            for (int i = 1; i <= diaries.length; i++) {
                if (i % 2 == 0) {

                    daoMasterDiary.insertOrReplace(
                            new Diary(null,
                                    title,
                                    diaries[i-1],
                                    "晴",
                                    "",
                                    ToolTime.getItem(ToolPublic.TIME_DATA))
                    );
                }
                title = diaries[i-1];
            }

        }
    }

    private boolean getOneApp(DaoSession daoMasterAppOne) {
        List<AppOne> appOnes = daoMasterAppOne.loadAll(AppOne.class);
        if (appOnes.size() <= 0) return true;
        return false;
    }

    //开始初始数据库
    public void startInitialSql(){
        writeSql();
    }

    //查询回调
    public void querySqlData(SqlData sqlData){

        List<Mood> moods = getMoodBuilder().orderDesc(MoodDao.Properties.Item).list();
        List<Task> tasks =  getTaskBuilder().orderDesc(TaskDao.Properties.Time).list();
        List<Account> accounts = getAccountBuilder().orderDesc(AccountDao.Properties.Item).list();
        List<Diary> diaries = getDiaryBuilder().orderDesc(DiaryDao.Properties.Time).list();
        sqlData.sqlData(moods,tasks,accounts,diaries);
    }




    //查询   查询全部数据
    public List<Mood> getMoodData(){
        return daoMasterMood.loadAll(Mood.class);
    }
    public List<Task> getTaskData(){
        return daoMasterTask.loadAll(Task.class);
    }
    public List<Account> getAccountData(){
        return daoMasterAccount.loadAll(Account.class);
    }
    public List<Diary> getDiaryData(){
        return daoMasterDiary.loadAll(Diary.class);
    }

    //查询   指定条件查询
    public QueryBuilder<Mood> getMoodBuilder() {
        //用法
       /* List<Mood> 王小二 = getCdnMoodData()
                .where(MoodDao.Properties.Item.eq("王小二"))
                .list();*/
        return daoMasterMood.queryBuilder(Mood.class);
    }
    public QueryBuilder<Task> getTaskBuilder() {
        return daoMasterTask.queryBuilder(Task.class);
    }
    public QueryBuilder<Account> getAccountBuilder() {
        return daoMasterAccount.queryBuilder(Account.class);
    }
    public QueryBuilder<Diary> getDiaryBuilder() {
        return daoMasterDiary.queryBuilder(Diary.class);
    }

    //查询   按照主键(或者其他值查找)查询 单条数据
    public <K> Mood getOneMoodData(K key) {
       /* //用法
        getOneMoodData("2020/06/07 17:21:00");*/
        return daoMasterMood.load(Mood.class, key);
    }
    public <K> Task getOneTaskData(K key) {
        return daoMasterTask.load(Task.class, key);
    }
    public <K> Account getOneAccountData(K key) {
        return daoMasterAccount.load(Account.class, key);
    }
    public <K> Diary getOneDiaryData(K key) {
        return daoMasterDiary.load(Diary.class, key);
    }



    //写入   写入一条数据
    public void setOneMoodData(Mood mood){
        daoMasterMood.insertOrReplace(mood);
    }
    public void setOneTaskData(Task task){
        daoMasterTask.insertOrReplace(task);
    }
    public void setOneAccountData(Account account){
        daoMasterAccount.insertOrReplace(account);
    }
    public void setOneDiaryData(Diary diary){
        daoMasterDiary.insertOrReplace(diary);
    }

    //写入   写入多条数据
    public void setManyMoodData(final List<Mood> moods){
        daoMasterMood.runInTx(new Runnable(){

				@Override
				public void run() {
					for (Mood mood : moods)
						daoMasterMood.insertOrReplace(mood);
					
				}
			});
		
		
		
    }
    public void setManyTaskData(final List<Task> tasks){
        daoMasterTask.runInTx(new Runnable(){

				@Override
				public void run() {
					for (Task task : tasks)
						daoMasterTask.insertOrReplace(task);
					
				}
			});
		
		
		
		
    }
    public void setManyAccountData(final List<Account> accounts){
        daoMasterAccount.runInTx(new Runnable(){

				@Override
				public void run() {
					for (Account account : accounts)
						daoMasterAccount.insertOrReplace(account);
					
				}
			});
			
    }
    public void setManyDiaryData(final List<Diary> diaries){
        daoMasterDiary.runInTx(new Runnable(){

				@Override
				public void run() {
					for (Diary diary : diaries)
						daoMasterDiary.insertOrReplace(diary);
					
				}
			});
   }



    //更新  更新一条数据 （根据查询出来的对象）
    public void updateMood(Mood mood) {
        //用法
        /*Mood oneMoodData = getOneMoodData(1L);
        oneMoodData.setMood(2);
        updateMood(oneMoodData);*/
        daoMasterMood.update(mood);
    }
    public void updateTask(Task task) {
        daoMasterTask.update(task);
    }
    public void updateAccount(Account account) {
        daoMasterAccount.update(account);
    }
    public void updateDiary(Diary diary) {
        daoMasterDiary.update(diary);
    }



    //删除  删除一条数 (根据查询出来的对象)
    public void deleteMood(Mood mood) {
        //用法
        /*Mood oneMoodData = getOneMoodData(1L);
        deleteMood(oneMoodData);*/
        daoMasterMood.delete(mood);
    }
    public void deleteTask(Task task) {
        daoMasterTask.delete(task);
    }
    public void deleteAccount(Account account) {
        daoMasterAccount.delete(account);
    }
    public void deleteDiary(Diary diary) {
        daoMasterDiary.delete(diary);
    }

    //删除   删除全部数据
    public void deleteAllMood(Class<Mood> cls) {
        daoMasterMood.deleteAll(cls);
    }
    public void deleteAllTask(Class<Task> cls) {
        daoMasterTask.deleteAll(cls);
    }
    public void deleteAllAccount(Class<Account> cls) {
        daoMasterAccount.deleteAll(cls);
    }
    public void deleteAllDiary(Class<Diary> cls) {
        daoMasterDiary.deleteAll(cls);
    }





    public DaoSession getDaoMasterMood() {
        return daoMasterMood;
    }
    public DaoSession getDaoMasterTask() {
        return daoMasterTask;
    }
    public DaoSession getDaoMasterAccount() {
        return daoMasterAccount;
    }
    public DaoSession getDaoMasterDiary() {
        return daoMasterDiary;
    }



}



