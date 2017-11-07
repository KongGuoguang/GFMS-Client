package com.zzu.gfms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zzu.gfms.adapter.DetailRecordAdapter;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.domain.GetDetailRecordsUseCase;
import com.zzu.gfms.domain.SaveDetailRecordsUseCase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DetailRecordActivity extends AppCompatActivity {

    private int year, month, day;
    private String dayRecordId;

    private TextView totalWorkCount;
    private RecyclerView recyclerView;

    private List<DetailRecord> detailRecordList = new ArrayList<>();
    private DetailRecordAdapter adapter;

    private int totalCount;

    private GetDetailRecordsUseCase getDetailRecordsUseCase;
    private SaveDetailRecordsUseCase saveDetailRecordsUseCase;

    private QMUITipDialog loading;

    private Disposable disposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_record);
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        dayRecordId = intent.getStringExtra("dayRecordId");
        initView();
        initUseCase();
        loadDetailRecords();
    }

    private void initView(){
        TextView dateText = (TextView) findViewById(R.id.text_date);
        String dateStr = "工作日期：" + year + "年" + month + "月" + day + "日";
        dateText.setText(dateStr);

        totalWorkCount = (TextView) findViewById(R.id.text_count);

        QMUITopBar topBar = (QMUITopBar) findViewById(R.id.top_bar);
        topBar.setTitle("日报详情");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new DetailRecordAdapter(detailRecordList);
        recyclerView.setAdapter(adapter);

        loading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在提交申请")
                .create();
    }

    private void initUseCase(){
        getDetailRecordsUseCase = new GetDetailRecordsUseCase();
        saveDetailRecordsUseCase = new SaveDetailRecordsUseCase();
    }

    private void loadDetailRecords(){
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
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_cancel:
                finish();
                break;
            case R.id.button_submit:
                break;
        }
    }
}
