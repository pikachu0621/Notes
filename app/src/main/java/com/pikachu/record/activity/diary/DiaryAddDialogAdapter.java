package com.pikachu.record.activity.diary;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pikachu.record.R;
import com.pikachu.record.monitor.DataSynEvent;
import com.pikachu.record.monitor.ReturnImagePath;
import com.pikachu.record.sql.data.InitialSql;
import com.pikachu.record.sql.table.Diary;
import com.pikachu.record.tool.ToolFile;
import com.pikachu.record.tool.ToolOther;
import com.pikachu.record.tool.ToolPublic;
import com.pikachu.record.tool.ToolTime;
import com.pikachu.record.view.radius.QMUIRadiusImageView;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

//import com.bumptech.glide.Glide;


//弹窗
public class DiaryAddDialogAdapter {


    private final Context context;
    private final Activity activity;
    private InitialSql initialSql;
    private BottomSheetDialog dialog;
    private View box_view;
    private TextView finishTextView,titleTextView,addImageTextView;
	private QMUIRadiusImageView addImageImageView;
    private ImageView addImageImageView_2;
    private EditText addTitleEditView,addTextEditView;
    private String upStr,addStr,titleInit,textInit,finishStr;
    private String imagePath;



    public interface EndAdd {
        void endAdd();
    }
    private EndAdd endAdd=new EndAdd(){
        @Override
        public void endAdd() {}
    };



    public DiaryAddDialogAdapter(Context context) {
        this.context = context;

        activity = (Activity) context;
        upStr = context.getResources().getString(R.string.mood_mood_list_3);
        addStr = context.getResources().getString(R.string.home_diary);
		titleInit = context.getResources().getString(R.string.diary_title_init);
		textInit = context.getResources().getString(R.string.diary_text_init);
	    finishStr = context.getResources().getString(R.string.mood_complete);

	    initialSql = new InitialSql(context);

    }


    //添加
    public void showDialog(boolean cancelable, boolean cancel) {
        imagePath="";
        if (dialog == null) {
            findDialogView();
	    }
        
        dialogF(cancelable, cancel, addStr, "", "", new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    addAndUpData(false, null, imagePath);
                }
            }, new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    ReturnImagePath.toPhoto(activity);
                }
            });
        addImageImageView_2.setVisibility(View.VISIBLE);
        addImageImageView.setImageDrawable(null);
    }




    //更新，和详情
    public void showDialog(boolean cancelable, boolean cancel, final Diary diary) {
        if (dialog == null) {
			findDialogView();
        }
		dialogF(cancelable, cancel, upStr, diary.getTitle(), diary.getText(), new OnClickListener(){
                @Override
                public void onClick(View p1) {
					addAndUpData(true, diary, imagePath == null ||
                                 imagePath == "" ?diary.getImagePath(): imagePath);
                }
            }, new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    ReturnImagePath.toPhoto(activity);
                }
            });
        if(diary.getImagePath()==null||diary.getImagePath().equals("")){
            addImageImageView_2.setVisibility(View.VISIBLE);
            addImageImageView.setImageDrawable(null);
        }else{
            addImageImageView_2.setVisibility(View.GONE);

            Glide.with(context).load(Uri.fromFile(new File(diary.getImagePath()))).into(addImageImageView);
            //addImageImageView.setImageURI(Uri.fromFile(new File(diary.getImagePath())));
        }
    }








	private void findDialogView() {


        dialog = new BottomSheetDialog(context,R.style.BottomSheetEdit){
            @Override
            public void onStart() {
                super. onStart() ;
                if (box_view == null) return;
                View parent = (View) box_view. getParent();
                BottomSheetBehavior behavior = BottomSheetBehavior.from(parent);
                box_view. measure(0, 0);
                behavior.setPeekHeight(box_view. getMeasuredHeight());
                CoordinatorLayout.LayoutParams params =(CoordinatorLayout. LayoutParams) parent.getLayoutParams();
                params. gravity = Gravity.TOP | Gravity. CENTER_HORIZONTAL;
                parent. setLayoutParams(params) ;

            }
        };
        box_view = LayoutInflater.from(context).inflate(R.layout.diary_add_data_ui, null);
        dialog.setContentView(box_view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


		titleTextView = box_view.findViewById(R.id.id_diary_add_data_text_2);
		finishTextView = box_view.findViewById(R.id.id_diary_add_data_text_1);
		addTitleEditView = box_view.findViewById(R.id.id_diary_add_data_edit_1);
		addImageTextView = box_view.findViewById(R.id.id_diary_add_data_text_3);
		addImageImageView = box_view.findViewById(R.id.id_diary_add_data_image_1);
        addImageImageView_2 = box_view.findViewById(R.id.id_diary_add_data_image_2);
		addTextEditView = box_view.findViewById(R.id.id_diary_add_data_edit_2);
	}


	private void dialogF(boolean cancelable, boolean cancel,
						 String addTitle, String titleStr, String textStr,
						 OnClickListener onClick, OnClickListener onClick2) {

		dialog.setCancelable(cancelable);
		dialog.setCanceledOnTouchOutside(cancel);
		dialog.show();
		titleTextView.setText(addTitle);
		addTitleEditView.setText(titleStr);
		addTextEditView.setText(textStr);
		finishTextView.setOnClickListener(onClick);
        addImageImageView.setOnClickListener(onClick2);
        
	}




    //isUpData=false 添加数据，isUpData=true  更新数据;
    private void addAndUpData(boolean isUpData, Diary diary, String path) {
        String title = addTitleEditView.getText().toString();
		String text = addTextEditView.getText().toString();


		if (title.equals(""))
            title = titleInit;

        if (text.equals(""))
            text = textInit;



        if (isUpData) {
            diary.setTitle(title);
            diary.setText(text);
            diary.setImagePath(path);
            initialSql.updateDiary(diary);
        } else {
			initialSql.setOneDiaryData(new Diary(null, title, text, "", path == null || path.equals("") ?"": path, ToolTime.getItem(ToolPublic.TIME_DATA)));
        }
        ToolOther.tw(activity, finishStr, R.drawable.toast_true_icon);
        cancelDialog();
        //发布事件后进行主页更新
        EventBus.getDefault().post(new DataSynEvent());
        endAdd.endAdd();
    }








    //添加或者刷新完成后
    public void setEndAdd(EndAdd endAdd) {
        this.endAdd = endAdd;   
    }



	//关闭
    public void cancelDialog() {
        if (dialog != null) dialog.cancel();
    }




    //设置路径和图片
    public void setImagePath(String imagePath) {


        if (imagePath == null || imagePath.equals("")) {
            this.imagePath = "";   
            addImageImageView_2.setVisibility(View.VISIBLE);
            addImageImageView.setImageDrawable(null);
        } else {

            String pathImage=context.getExternalFilesDir("").toString() +ToolPublic.IMAGE_PATH + ToolFile.getFileMD5(imagePath);
            if (ToolFile.copyFile(imagePath, pathImage)) {
                addImageImageView.setImageURI(Uri.fromFile(new File(pathImage)));   
                this.imagePath = pathImage;
                addImageImageView_2.setVisibility(View.GONE);
            } else {
                addImageImageView.setImageURI(Uri.fromFile(new File(imagePath)));
                this.imagePath = imagePath;
                addImageImageView_2.setVisibility(View.GONE);
            }

        }



    }















}
