package com.pikachu.record.activity.account;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.pikachu.record.R;
import com.pikachu.record.activity.dialog.PDialog;
import com.pikachu.record.monitor.DataSynEvent;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Account;
import com.pikachu.record.tool.ToolState;
import com.pikachu.record.view.top.TopView;

import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.List;





public class AccountActivity extends AppCompatActivity implements  AccountRecyclerAdapter.ItemOnClick {

    private RecyclerView recyclerView;
    private TopView topView;
    private SwipeRefreshLayout swipe;

    private String account_1;


    private InitialSql initialSql;
    private List<Account> accountData;
    private AccountRecyclerAdapter accountRecyclerAdapter;
    private AccountAddDialogAdapter accountAddDialogAdapter;

    
    //腹泻  突破dex 64k方法限制
    /*@Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolState.setFullScreen(this, true, false);//全屏
        setContentView(R.layout.account_activity);
		findView();
        init();
    }

	private void findView() {
		
		recyclerView=findViewById(R.id.id_account_recycler_1);
		topView=findViewById(R.id.id_account_topView_1);
		swipe=findViewById(R.id.id_account_swipeRefresh_1);
		
		account_1=getResources().getString(R.string.home_account);
		
	}



    private void init() {
        topView.setText(account_1);
        getData();

        accountRecyclerAdapter = new AccountRecyclerAdapter(this,accountData);
        recyclerView.setAdapter(accountRecyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        accountRecyclerAdapter.setItemOnClick(this);


        //刷新
        swipe.setOnRefreshListener(() -> {
            getData();
            swipe.setRefreshing(false);
        });
		
		
		//初始addDialog
        accountAddDialogAdapter = new AccountAddDialogAdapter(this);
        accountAddDialogAdapter.setEndAdd(() -> getData());


        //TopView添加 按键点击事件
        topView.setRightImageOnClick(p1 -> accountAddDialogAdapter.showDialog(true,true));

    }


    private void getData(){
        if(initialSql==null)
            initialSql = new InitialSql(this);
        accountData = initialSql.getAccountData();
        Collections.reverse(accountData);
        if(accountRecyclerAdapter!=null){
            accountRecyclerAdapter.setAccountData(accountData);    
            accountRecyclerAdapter.notifyDataSetChanged();
        }
        
    }



    
    
    @Override
    public boolean onLongClick(View v, int position,final Account account) {
       
        PDialog.PDialog(this)
            .setMsg("是否删除此条账单？")
            .setLeftStr("确定删除", (v1, pDialog) -> {
                initialSql.deleteAccount(account);
                pDialog.dismiss();
                getData();
                //发布更新事件
                EventBus.getDefault().post(new DataSynEvent());
            })
            .setRightStr("取消删除", null)
            .show();
        return true;
    }
    
    
    

    @Override
    public void onClick(View v, int position,Account account) {
        accountAddDialogAdapter.showDialog(true,true,account);   
    }

    

    




}
