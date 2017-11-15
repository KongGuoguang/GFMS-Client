package com.zzu.gfms.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zzu.gfms.R;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.event.AddDetailRecordFailed;
import com.zzu.gfms.event.AddDetailRecordSuccess;
import com.zzu.gfms.view.ClothesTypePicker;
import com.zzu.gfms.view.WorkTypePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AddDetailRecordActivity extends BaseActivity {

    private TextView clothesTypeText;

    private TextView workTypeText;

    private EditText workCount;

    private QMUIPopup selectClothesTypePopup;

    private QMUIPopup selectWorkTypePopup;

    private DetailRecord detailRecord = new DetailRecord();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail_record);
        initView();
        EventBus.getDefault().register(this);
    }

    private void initView(){
        QMUITopBar topBar = (QMUITopBar) findViewById(R.id.top_bar);
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        topBar.setTitle("日报编辑");
        clothesTypeText = (TextView) findViewById(R.id.text_clothes_type);
        workTypeText = (TextView) findViewById(R.id.text_work_type);
        workCount = (EditText) findViewById(R.id.edit_work_count);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_select_clothes:
                selectClothesType();
                break;
            case R.id.text_select_work:
                selectWorkType();
                break;
            case R.id.button_confirm:
                addDetailRecord();
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void selectClothesType(){
        if (selectClothesTypePopup == null){
            selectClothesTypePopup = new QMUIPopup(this, QMUIPopup.DIRECTION_BOTTOM);
            ClothesTypePicker clothesTypePicker = new ClothesTypePicker(this);
            clothesTypePicker.setOnClothesSelectedListener(new ClothesTypePicker.OnClothesSelectedListener() {
                @Override
                public void onClothesSelected(ClothesType clothesType) {
                    clothesTypeText.setText(clothesType.getName());
                    detailRecord.setClothesID(clothesType.getClothesID());
                }
            });
            selectClothesTypePopup.setContentView(clothesTypePicker);
            selectClothesTypePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectClothesTypePopup.show(clothesTypeText);
    };

    private void selectWorkType(){
        if (selectWorkTypePopup == null){
            selectWorkTypePopup = new QMUIPopup(this, QMUIPopup.DIRECTION_BOTTOM);
            WorkTypePicker workTypePicker = new WorkTypePicker(this);
            workTypePicker.setOnWorkSelectedListener(new WorkTypePicker.OnWorkSelectedListener() {
                @Override
                public void onWorkSelected(WorkType workType) {
                    workTypeText.setText(workType.getName());
                    detailRecord.setWorkTypeID(workType.getWorkTypeID());
                }
            });
            selectWorkTypePopup.setContentView(workTypePicker);
            selectWorkTypePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectWorkTypePopup.show(workTypeText);
    }

    private void addDetailRecord(){
        if (TextUtils.isEmpty(clothesTypeText.getText())){
            showErrorDialog("衣服类型不能为空，请重新选择");
            return;
        }

        if (TextUtils.isEmpty(workTypeText.getText())){
            showErrorDialog("工作类型不能为空，请重新选择");
            return;
        }

        if (TextUtils.isEmpty(workCount.getText()) ||
                Integer.parseInt(workCount.getText().toString()) == 0){
            showErrorDialog("完成量有误，请重新输入");
            return;
        }

        detailRecord.setCount(Integer.parseInt(workCount.getText().toString()));

        EventBus.getDefault().post(detailRecord);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetaiRecordFailed(AddDetailRecordFailed event){
        showErrorDialog("衣服类型、工作类型都相同的记录只能填写一条!");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetaiRecordSuccess(AddDetailRecordSuccess event){
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
