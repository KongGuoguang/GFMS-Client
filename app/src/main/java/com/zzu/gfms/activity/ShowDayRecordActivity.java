package com.zzu.gfms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.DetailRecordAdapter;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.utils.ConvertState;
import com.zzu.gfms.domain.GetDetailRecordsUseCase;
import com.zzu.gfms.domain.SaveDetailRecordsUseCase;
import com.zzu.gfms.domain.SubmitModifyApplicationUseCase;
import com.zzu.gfms.event.SubmitModifyApplicationSuccess;
import com.zzu.gfms.utils.CalendarUtil;
import com.zzu.gfms.utils.Constants;
import com.zzu.gfms.utils.ExceptionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ShowDayRecordActivity extends BaseActivity {

    private int year, month, day;
    private String dayRecordId;
    private String convertState;

    private TextView totalWorkCount;

    private List<DetailRecord> detailRecordList = new ArrayList<>();
    private DetailRecordAdapter adapter;

    private int totalCount;

    private GetDetailRecordsUseCase getDetailRecordsUseCase;
    private SaveDetailRecordsUseCase saveDetailRecordsUseCase;
    private SubmitModifyApplicationUseCase submitModifyApplicationUseCase;

    private QMUITipDialog submitLoading;

    private QMUITipDialog loading;

    private Disposable getDetailRecordsDisposable;

    private Disposable submitModifyApplicationDisposable;

    private QMUIDialog applyModifyDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);
        initIntentExtra();
        initView();
        initUseCase();
        loadDetailRecords();
    }

    private void initIntentExtra(){
        Intent intent = getIntent();
        year = intent.getIntExtra(Constants.YEAR, 0);
        month = intent.getIntExtra(Constants.MONTH, 0);
        day = intent.getIntExtra(Constants.DAY, 0);
        dayRecordId = intent.getStringExtra(Constants.DAY_RECORD_ID);
        convertState = intent.getStringExtra(Constants.CONVERT_STATE);
    }

    private void initView(){
        TextView dateText = findViewById(R.id.text_month);
        String dateStr = "工作日期：" + year + "年" + month + "月" + day + "日";
        dateText.setText(dateStr);

        totalWorkCount = findViewById(R.id.text_count);

        QMUITopBar topBar = findViewById(R.id.top_bar);
        topBar.setTitle("日报详情");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
        topBar.addRightImageButton(R.mipmap.icon_modify, R.id.topbar_right_modify_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ConvertState.DAY_RECORD_MODIFY_NOT_CHECK.equals(convertState)){
                            showErrorDialog("修改申请正在审核，请勿重复申请！");
                        }else {
                            showApplyModifyDialog();
                        }
                    }
                });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new DetailRecordAdapter(detailRecordList, false);
        recyclerView.setAdapter(adapter);

        submitLoading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在提交申请...")
                .create();

        loading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("加载中...")
                .create();
    }

    private void initUseCase(){
        getDetailRecordsUseCase = new GetDetailRecordsUseCase();
        saveDetailRecordsUseCase = new SaveDetailRecordsUseCase();
        submitModifyApplicationUseCase = new SubmitModifyApplicationUseCase();
    }

    private void loadDetailRecords(){
        loading.show();
        getDetailRecordsUseCase.get(dayRecordId)
                .execute(new Observer<List<DetailRecord>>() {
                    int i = 0;
                    @Override
                    public void onSubscribe(Disposable d) {
                        getDetailRecordsDisposable = d;
                    }

                    @Override
                    public void onNext(List<DetailRecord> detailRecords) {
                        i++;
                        if (detailRecords != null && detailRecords.size() > 0){
                            loading.dismiss();
                            detailRecordList.clear();
                            totalCount = 0;
                            for (DetailRecord detailRecord : detailRecords){
                                totalCount = totalCount + detailRecord.getCount();
                                detailRecordList.add(detailRecord);
                            }
                            adapter.notifyDataSetChanged();
                            totalWorkCount.setText(getString(R.string.work_count, totalCount));
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
        if (getDetailRecordsDisposable != null && !getDetailRecordsDisposable.isDisposed()){
            getDetailRecordsDisposable.dispose();
        }

        if (submitModifyApplicationDisposable != null && !submitModifyApplicationDisposable.isDisposed()){
            submitModifyApplicationDisposable.dispose();
        }
    }

    private void showApplyModifyDialog(){
        if (applyModifyDialog == null){

            final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
            builder.setTitle("申请修改")
                    .setPlaceholder("请输入修改原因")
                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                        }
                    })
                    .addAction("确认申请", new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            dialog.dismiss();
                            submitModifyApplication(builder.getEditText().getText().toString());
                        }
                    });

            applyModifyDialog = builder.create();
        }

        applyModifyDialog.show();
    }

    private void submitModifyApplication(String modifyReason){

        if (TextUtils.isEmpty(modifyReason)){
            showErrorDialog("修改原因不能为空");
            return;
        }
        submitLoading.show();
        submitModifyApplicationUseCase.submit(dayRecordId, modifyReason)
                .execute(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        submitModifyApplicationDisposable = d;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        submitLoading.dismiss();
                        showToast("已发送申请，等待审核");
                        EventBus.getDefault().post(new SubmitModifyApplicationSuccess());
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        submitLoading.dismiss();
                        showToast(ExceptionUtil.parseErrorMessage(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
