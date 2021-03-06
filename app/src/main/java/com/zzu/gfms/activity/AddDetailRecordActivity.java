package com.zzu.gfms.activity;

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
        QMUITopBar topBar = findViewById(R.id.top_bar);
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        topBar.setTitle("日报编辑");
        clothesTypeText = findViewById(R.id.text_clothes_type);
        workTypeText = findViewById(R.id.text_work_type);
        workCount = findViewById(R.id.edit_work_count);
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

    /**
     * 选择衣服类型
     */
    private void selectClothesType(){
        if (selectClothesTypePopup == null){
            selectClothesTypePopup = new QMUIPopup(this, QMUIPopup.DIRECTION_BOTTOM);
            ClothesTypePicker clothesTypePicker = new ClothesTypePicker(this);
            clothesTypePicker.setOnButtonClickedListener(new ClothesTypePicker.OnButtonClickedListener() {
                @Override
                public void onConfirm(ClothesType clothesType) {
                    selectClothesTypePopup.dismiss();
                    clothesTypeText.setText(clothesType.getName());
                    detailRecord.setClothesID(clothesType.getClothesID());
                }

                @Override
                public void onCancel() {
                    selectClothesTypePopup.dismiss();
                }

                @Override
                public void onReset() {
                    selectClothesTypePopup.dismiss();
                    clothesTypeText.setText("");
                    detailRecord.setClothesID(0);
                }
            });

            selectClothesTypePopup.setContentView(clothesTypePicker);
            selectClothesTypePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectClothesTypePopup.show(clothesTypeText);
    };

    /**
     * 选择工作类型
     */
    private void selectWorkType(){
        if (selectWorkTypePopup == null){
            selectWorkTypePopup = new QMUIPopup(this, QMUIPopup.DIRECTION_BOTTOM);
            WorkTypePicker workTypePicker = new WorkTypePicker(this);
            workTypePicker.setOnButtonClickedListener(new WorkTypePicker.OnButtonClickedListener() {
                @Override
                public void onConfirm(WorkType workType) {
                    selectWorkTypePopup.dismiss();
                    if (workType == null) return;
                    workTypeText.setText(workType.getName());
                    detailRecord.setWorkTypeID(workType.getWorkTypeID());
                }

                @Override
                public void onCancel() {
                    selectWorkTypePopup.dismiss();
                }

                @Override
                public void onReset() {
                    selectWorkTypePopup.dismiss();
                    workTypeText.setText("");
                    detailRecord.setWorkTypeID(0);
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
