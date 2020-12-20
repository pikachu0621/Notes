package com.pikachu.record.activity.account;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pikachu.record.R;
import com.pikachu.record.sql.table.Account;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;

import java.text.DecimalFormat;
import java.util.List;


public class AccountRecyclerAdapter extends RecyclerView.Adapter {


    private final Context context;
    private List<Account> accountData;
    private final Activity activity;
    private int[] col;
    private DecimalFormat df;
    
    
    public interface ItemOnClick{
        boolean onLongClick(View v,int position,Account account);
        void onClick(View v,int position,Account account);        
    }
    
    
    
    public ItemOnClick itemOnClick= new ItemOnClick(){

        @Override
        public boolean onLongClick(View v, int position,Account account) {
            return false;
        }
        
        @Override
        public void onClick(View v, int position,Account account) {
            
        }
    };
	
	
	

    @SuppressLint("NewApi")
    public AccountRecyclerAdapter(Context context, List<Account> accountData) {
        this.context = context;
        this.activity = (Activity)context;
        this.accountData=accountData;
        col =new int[]{context.getResources().getColor(R.color.color_pay),
                     context.getResources().getColor(R.color.color_enter)};
        df = new DecimalFormat("#00.00");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.account_list_item_ui, parent, false);
        return new ItemViewHolder(inflate);
    }




    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final ItemViewHolder itemHolder = (ItemViewHolder) holder;
        itemHolder.linearLayout.setOnLongClickListener(new OnLongClickListener(){
				@Override
				public boolean onLongClick(View p1) {
					return itemOnClick.onLongClick(itemHolder.linearLayout,position,accountData.get(position));
				}
			});
        itemHolder.linearLayout.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    itemOnClick.onClick(itemHolder.linearLayout,position,accountData.get(position));
                }
            });
            
        Account acc= accountData.get(position);
            
		itemHolder.textView_1.setText(acc.getTitle());
        itemHolder.textView_2.setText(acc.getText());
        
        
        
        
        //float much= acc.getHowMuch();
        float much= acc.getHowMuch();
        String listMuch;
        if(acc.getBudget()){
            itemHolder.textView_3.setTextColor(col[0]);
            listMuch=df.format(-Math.abs(much));
        }else {
            itemHolder.textView_3.setTextColor(col[1]); 
            listMuch="+"+df.format(Math.abs(much));
        }   
        itemHolder.textView_3.setText(listMuch);
        
        itemHolder.textView_4.setText(ToolTime.getTimeH(acc.getItem(), ToolPublic.TIME_DATA));
        
    }
	
    @Override
    public int getItemCount() {
        return accountData.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout linearLayout;
        public TextView textView_1,textView_2,textView_3,textView_4;
        

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.id_account_item_linear_1);
            textView_1 = itemView.findViewById(R.id.id_account_item_text_1);
            textView_2 = itemView.findViewById(R.id.id_account_item_text_2);
            textView_3 = itemView.findViewById(R.id.id_account_item_text_3);
            textView_4 = itemView.findViewById(R.id.id_account_item_text_4);
            
        }
    }

    

    
    public void setItemOnClick(ItemOnClick itemOnClick){
        this.itemOnClick=itemOnClick;
    }
    public void setAccountData(List<Account> accountData) {
        this.accountData = accountData;
    }
    

}
