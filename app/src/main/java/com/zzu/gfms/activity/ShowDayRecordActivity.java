package com.zzu.gfms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
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
import com.zzu.gfms.utils.ExceptionUtil;
import com.zzu.gfms.utils.ViewUtil;

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

    private EditText modifyReasonEdit;

    private List<DetailRecord> detailRecordList = new ArrayList<>();
    private DetailRecordAdapter adapter;

    private int totalCount;

    private GetDetailRecordsUseCase getDetailRecordsUseCase;
    private SaveDetailRecordsUseCase saveDetailRecordsUseCase;
    private SubmitModifyApplicationUseCase submitModifyApplicationUseCase;

    private QMUITipDialog loading;

    private Disposable getDetailRecordsDisposable;

    private Disposable submitModifyApplicationDisposable;


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
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        dayRecordId = intent.getStringExtra("dayRecordId");
        convertState = intent.getStringExtra("convertState");
    }

    private void initView(){
        TextView dateText = (TextView) findViewById(R.id.text_date);
        String dateStr = "工作日期：" + year + "年" + month + "月" + day + "日";
        dateText.setText(dateStr);

        totalWorkCount = (TextView) findViewById(R.id.text_count);

        modifyReasonEdit = (EditText) findViewById(R.id.edit_modify_reason);

        QMUITopBar topBar = (QMUITopBar) findViewById(R.id.top_bar);
        topBar.setTitle("日报详情");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new DetailRecordAdapter(detailRecordList, false);
        recyclerView.setAdapter(adapter);

        loading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在提交申请")
                .create();

        TextView submit = (TextView) findViewById(R.id.button_submit);
        if (ConvertState.DAY_RECORD_MODIFY_NOT_CHECK.equals(convertState)){
            ViewUtil.setViewEnable(submit, false);
        }
    }

    private void initUseCase(){
        getDetailRecordsUseCase = new GetDetailRecordsUseCase();
        saveDetailRecordsUseCase = new SaveDetailRecordsUseCase();
        submitModifyApplicationUseCase = new SubmitModifyApplicationUseCase();
    }

    private void loadDetailRecords(){
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

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_submit:
                submitModifyApplication();
                break;
        }
    }

    private void submitModifyApplication(){
        if (TextUtils.isEmpty(modifyReasonEdit.getText())){
            showErrorDialog("修改原因不能为空");
        }
        loading.show();
        submitModifyApplicationUseCase.submit(dayRecordId, modifyReasonEdit.getText().toString())
                .execute(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        submitModifyApplicationDisposable = d;
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        loading.dismiss();
                        showToast("修改申请提交成功！");
                        EventBus.getDefault().post(new SubmitModifyApplicationSuccess());
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.dismiss();
                        showToast(ExceptionUtil.parseErrorMessage(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
