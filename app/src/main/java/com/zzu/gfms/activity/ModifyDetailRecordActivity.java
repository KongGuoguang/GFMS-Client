package com.zzu.gfms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.R;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.event.ModifyDetailRecord;
import com.zzu.gfms.utils.ConstantUtil;

import org.greenrobot.eventbus.EventBus;

public class ModifyDetailRecordActivity extends BaseActivity {

    private EditText workCount;

    private int workTypeId;

    private int clothesId;

    private int count;

    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_detail_record);
        initIntentExtra();
        initView();
    }

    private void initIntentExtra(){
        Intent intent = getIntent();
        workTypeId = intent.getIntExtra("workTypeID", 0);
        clothesId = intent.getIntExtra("clothesID", 0);
        count = intent.getIntExtra("count", 0);
        position = intent.getIntExtra("position", -1);
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
        TextView clothesTypeText = (TextView) findViewById(R.id.text_clothes_type);
        clothesTypeText.setText(ConstantUtil.getClothesName(clothesId));

        TextView workTypeText = (TextView) findViewById(R.id.text_work_type);
        workTypeText.setText(ConstantUtil.getWorkName(workTypeId));

        workCount = (EditText) findViewById(R.id.edit_work_count);
        workCount.setText(String.valueOf(count));
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_confirm:
                modifyDetailRecord();
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }

    private void modifyDetailRecord(){

        if (TextUtils.isEmpty(workCount.getText()) ||
                Integer.parseInt(workCount.getText().toString()) == 0){
            showErrorDialog("完成量有误，请重新输入");
            return;
        }

        EventBus.getDefault().post(new ModifyDetailRecord(position, Integer.parseInt(workCount.getText().toString())));
        finish();
    }
}
