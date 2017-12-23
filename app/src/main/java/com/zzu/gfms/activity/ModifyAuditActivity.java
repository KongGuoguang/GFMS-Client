package com.zzu.gfms.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.OperationRecord;

public class ModifyAuditActivity extends AppCompatActivity {

    private Gson gson;

    private OperationRecord operationRecord;

    private TextView modifyCheckState;

    private TextView modifyNotPassedReason;

    private TextView modifyCheckDate;

    private TextView applyDate;

    private TextView applyReason;

    private TextView workDate;

    private TextView workCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_audit);

        gson = new Gson();
        String json = getIntent().getStringExtra("operationRecord");
        try {
            operationRecord = gson.fromJson(json, OperationRecord.class);
        }catch (Exception e){
            LogUtils.e(e.toString());
            return;
        }
        initView();
    }

    private void initView(){
        QMUITopBar topBar = (QMUITopBar) findViewById(R.id.top_bar);
        topBar.setTitle("申请详情");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        if (operationRecord == null) return;

        modifyCheckState = (TextView) findViewById(R.id.text_modify_check_state);
        modifyNotPassedReason = (TextView) findViewById(R.id.text_modify_not_passed_reason);
        modifyCheckDate = (TextView) findViewById(R.id.text_modify_check_date);
        applyDate = (TextView) findViewById(R.id.text_apply_date);
        applyReason = (TextView) findViewById(R.id.text_apply_reason);
        workDate = (TextView) findViewById(R.id.text_work_date);
        workCount = (TextView) findViewById(R.id.text_work_count);
    }
}
