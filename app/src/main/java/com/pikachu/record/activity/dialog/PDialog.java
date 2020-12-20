package com.pikachu.record.activity.dialog;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pikachu.record.R;




public class PDialog {


    private Context context;
    private String msg,leftStr,rightStr;
    private boolean cancelable=true,cancel=true;
    private BottomSheetDialog dialog;
    private View box_view;
    private TextView msgView,leftView,rightView;
    private DialogTopOnClick topOnClick=new DialogTopOnClick(){
        @Override
        public void onClick(View v, PDialog pDialog) {
            dismiss();
        }
    };
    private DialogBottomOnClick bottomOnClick=new DialogBottomOnClick(){
        @Override
        public void onClick(View v, PDialog pDialog) {
            dismiss();
        }
    };

    public interface DialogTopOnClick {
        void onClick(View v, PDialog pDialog);   
    }
    public interface DialogBottomOnClick {
        void onClick(View v, PDialog pDialog);   
    }


    @SuppressLint("StaticFieldLeak")
    private static PDialog pDialog;


    public PDialog(Context context) {
        this.context = context;
    }

    public static PDialog PDialog(Context context) {
        //if (pDialog == null) {
            pDialog = new PDialog(context);
        //}
        return pDialog;
    }



    public PDialog setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    public PDialog setLeftStr(String leftStr, DialogTopOnClick topOnClick) {
        if (topOnClick != null)
            this.topOnClick = topOnClick;
        this.leftStr = leftStr;
        return this;
    }
    public PDialog setRightStr(String rightStr, DialogBottomOnClick bottomOnClick) {
        if (bottomOnClick != null)
            this.bottomOnClick = bottomOnClick;
        this.rightStr = rightStr;
        return this;
    }
    public PDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;    
    }
    public PDialog setCanceledOnTouchOutside(boolean cancel) {
        this.cancel = cancel;
        return this;    
    }



    public void show() {
        showDialog();
    }

    public void dismiss() {
        if (dialog != null) dialog.dismiss();
    }


    private void showDialog() {

        if (dialog == null) {
            dialog = new BottomSheetDialog(context){
                @Override
                public void onStart(){
                    super. onStart() ;
                    if (box_view == null) return;
                    View parent = (View) box_view. getParent();
                    BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
                    box_view. measure(0,0);
                    behavior.setPeekHeight(box_view. getMeasuredHeight());
                    CoordinatorLayout.LayoutParams params =(CoordinatorLayout. LayoutParams) parent.getLayoutParams();
                    params. gravity = Gravity.TOP | Gravity. CENTER_HORIZONTAL;
                    parent. setLayoutParams (params) ;
                }
            };
            box_view = LayoutInflater.from(context).inflate(R.layout.delete_ui,null);
            msgView = box_view.findViewById(R.id.id_delete_text_2);
            leftView = box_view.findViewById(R.id.id_delete_text_3);
            rightView = box_view.findViewById(R.id.id_delete_text_4);
            dialog.setContentView(box_view);
            dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        msgView.setVisibility(msg == null ?View.GONE: View.VISIBLE);
        leftView.setVisibility(leftStr == null ?View.GONE: View.VISIBLE);
        rightView.setVisibility(rightStr == null ?View.GONE: View.VISIBLE);

        if (msg != null)
            msgView.setText(msg);
        if (leftStr != null)    {
            leftView.setOnClickListener(p1 -> topOnClick.onClick(p1, PDialog.this));
            leftView.setText(leftStr);
        }
        if (rightStr != null)    {
            rightView.setOnClickListener(p1 -> bottomOnClick.onClick(p1, PDialog.this));
            rightView.setText(rightStr);
        }


        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.show();



    }











}
