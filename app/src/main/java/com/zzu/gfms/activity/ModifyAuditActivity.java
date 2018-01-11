package com.zzu.gfms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.DetailRecordAdapter;
import com.zzu.gfms.adapter.DetailRecordDiffCallBack;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.OperationRecord;
import com.zzu.gfms.data.utils.ConvertState;
import com.zzu.gfms.domain.GetDetailRecordsUseCase;
import com.zzu.gfms.domain.SaveDetailRecordsUseCase;
import com.zzu.gfms.event.ModifyDayRecordSuccess;
import com.zzu.gfms.utils.Constants;
import com.zzu.gfms.utils.ExceptionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ModifyAuditActivity extends BaseActivity {

    private OperationRecord operationRecord;

    private List<DetailRecord> detailRecordList = new ArrayList<>();
    private DetailRecordAdapter adapter;

    private QMUITipDialog loading;

    private Disposable disposable;

    private GetDetailRecordsUseCase getDetailRecordsUseCase;
    private SaveDetailRecordsUseCase saveDetailRecordsUseCase;

    private String dayRecordId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_audit);

        Gson gson = new Gson();
        String json = getIntent().getStringExtra(Constants.JSON_OPERATION_RECORD);
        try {
            operationRecord = gson.fromJson(json, OperationRecord.class);
        }catch (Exception e){
            LogUtils.file(e.toString());
            return;
        }
        initView();
        initUseCase();
        loadDetailRecords();
        EventBus.getDefault().register(this);
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

        String convertState = operationRecord.getConvertState();
        dayRecordId = operationRecord.getDayRecordID();

        TextView modifyCheckState = (TextView) findViewById(R.id.text_modify_check_state);
        String convertStateName = "审核状态：" + ConvertState.getConvertStateName(convertState);
        modifyCheckState.setText(convertStateName);

        TextView modifyNotPassedReason = (TextView) findViewById(R.id.text_modify_not_passed_reason);
        TextView modifyCheckDate = (TextView) findViewById(R.id.text_modify_check_date);

        switch (convertState){
            case ConvertState.OPERATION_RECORD_MODIFY_NOT_PASSED:
                modifyNotPassedReason.setVisibility(View.VISIBLE);
                String notPassedReason = "未通过原因：" + operationRecord.getCheckReason();
                modifyNotPassedReason.setText(notPassedReason);
            case ConvertState.OPERATION_RECORD_MODIFY_PASSED:
                modifyCheckDate.setVisibility(View.VISIBLE);
                String checkTime = operationRecord.getCheckTime();
                if (!TextUtils.isEmpty(checkTime) && checkTime.length() > 10){
                    checkTime = checkTime.substring(0, 10);
                }
                String checkDate = "审核日期：" + checkTime;
                modifyCheckDate.setText(checkDate);
                break;
        }

        TextView applyDateText = (TextView) findViewById(R.id.text_apply_date);
        String applyTime = operationRecord.getApplyTime();
        if (!TextUtils.isEmpty(applyTime) && applyTime.length() > 10){
            applyTime = applyTime.substring(0, 10);
        }
        String applyDate = "申请日期：" + applyTime;
        applyDateText.setText(applyDate);

        TextView applyReason = (TextView) findViewById(R.id.text_apply_reason);
        String reason = "修改原因：" + operationRecord.getModifyReason();
        applyReason.setText(reason);

        TextView workDateText = (TextView) findViewById(R.id.text_work_date);
        String workDate = "工作日期：" + operationRecord.getDay();
        workDateText.setText(workDate);

        TextView workCountText = (TextView) findViewById(R.id.text_work_count);
        String workCount = "完成总量：" + operationRecord.getTotal() + "件";
        workCountText.setText(workCount);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new DetailRecordAdapter(detailRecordList, false);
        recyclerView.setAdapter(adapter);

        TextView modifyDayRecord = (TextView) findViewById(R.id.text_modify_day_record);
        if (ConvertState.DAY_RECORD_MODIFY_PASSED.equals(operationRecord.getDayRecordConvertState())){
            dayRecordId = operationRecord.getCopyNewDayRecordID();
            modifyDayRecord.setVisibility(View.VISIBLE);
            modifyDayRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String date = operationRecord.getDay();
                    Intent intent = new Intent(ModifyAuditActivity.this, ModifyDayRecordActivity.class);
                    if (date != null && date.length() == 10){
                        int year = Integer.parseInt(date.substring(0, 4));
                        int month = Integer.parseInt(date.substring(5, 7));
                        int day = Integer.parseInt(date.substring(8, 10));

                        intent.putExtra(Constants.YEAR, year);
                        intent.putExtra(Constants.MONTH, month);
                        intent.putExtra(Constants.DAY, day);
                    }

                    intent.putExtra(Constants.DAY_RECORD_ID, dayRecordId);

                    startActivity(intent);
                }
            });
        }

        loading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中...")
                .create();
    }

    private void initUseCase(){
        getDetailRecordsUseCase = new GetDetailRecordsUseCase();
        saveDetailRecordsUseCase = new SaveDetailRecordsUseCase();
    }

    private void loadDetailRecords(){
        loading.show();
        getDetailRecordsUseCase.get(dayRecordId)
                .execute(new Observer<List<DetailRecord>>() {
                    int i = 0;
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<DetailRecord> detailRecords) {
                        i++;
                        if (detailRecords != null && detailRecords.size() > 0){
                            loading.dismiss();
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                                    new DetailRecordDiffCallBack(detailRecordList, detailRecords), true);
                            diffResult.dispatchUpdatesTo(adapter);
                            detailRecordList.clear();
                            detailRecordList.addAll(detailRecords);
                            //adapter.notifyDataSetChanged();
                            if (i == 2){
                                saveDetailRecordsUseCase.save(detailRecords).execute();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.dismiss();
                        if (detailRecordList.size() == 0){
                            showErrorDialog(ExceptionUtil.parseErrorMessage(e));
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        EventBus.getDefault().unregister(this);
    }

    /**
     * 监听修改日报成功事件
     * @param event
     */
    @Subscribe
    public void onModifyDayRecordSuccess(ModifyDayRecordSuccess event){

        if (dayRecordId == null || !dayRecordId.equals(event.getDayRecordId())){
            return;
        }

        operationRecord.setDayRecordConvertState(ConvertState.DAY_RECORD_SUBMIT);
        operationRecord.async().save();
        finish();
    }
}
