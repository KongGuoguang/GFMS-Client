package com.zzu.gfms;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.adapter.DetailRecordAdapter;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;
import com.zzu.gfms.domain.CommitDayRecordUseCase;
import com.zzu.gfms.domain.DeleteAllDetailRecordDraftUseCase;
import com.zzu.gfms.domain.DeleteSingleDetailRecordDraftUseCase;
import com.zzu.gfms.domain.GetAllDetailRecordDraftsUseCase;
import com.zzu.gfms.domain.SaveSingleDetailRecordDraftUseCase;
import com.zzu.gfms.event.AddDetailRecordFailed;
import com.zzu.gfms.event.AddDetailRecordSuccess;
import com.zzu.gfms.utils.ConstantUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

public class AddDayRecordActivity extends BaseActivity {

    private QMUITopBar topBar;
    private int year, month, day;
    private TextView date;
    private TextView totalWorkCount;
    private RecyclerView recyclerView;

    private List<DetailRecord> detailRecordList = new ArrayList<>();
    private List<DetailRecordDraft> detailRecordDraftList = new ArrayList<>();

    private DetailRecordAdapter adapter;

    private int totalCount;

    private SaveSingleDetailRecordDraftUseCase saveSingleDetailRecordDraftUseCase;
    private DeleteSingleDetailRecordDraftUseCase deleteSingleDetailRecordDraftUseCase;
    private GetAllDetailRecordDraftsUseCase getAllDetailRecordDraftsUseCase;
    private DeleteAllDetailRecordDraftUseCase deleteAllDetailRecordDraftUseCase;
    private CommitDayRecordUseCase commitDayRecordUseCase;

    private DayRecord dayRecord = new DayRecord();
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day_record);
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        initView();
        initUseCase();
        loadDetailRecordDrafts();
        EventBus.getDefault().register(this);
    }

    private void initView(){
        date = (TextView) findViewById(R.id.text_date);
        String dateStr = "工作日期：" + year + "年" + month + "月" + day + "日";
        date.setText(dateStr);

        totalWorkCount = (TextView) findViewById(R.id.text_count);

        topBar = (QMUITopBar) findViewById(R.id.top_bar);
        topBar.setTitle("日报填写");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topBar.addRightImageButton(R.mipmap.icon_add, R.id.topbar_right_add_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AddDayRecordActivity.this, AddDetailRecordActivity.class));
                    }
                });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new DetailRecordAdapter(detailRecordList);
        adapter.setOnDeleteListener(new DetailRecordAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                deleteDetailRecord(position);
                deleteDetailRecordDraft(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initUseCase(){
        saveSingleDetailRecordDraftUseCase = new SaveSingleDetailRecordDraftUseCase();
        deleteAllDetailRecordDraftUseCase = new DeleteAllDetailRecordDraftUseCase();
        deleteSingleDetailRecordDraftUseCase = new DeleteSingleDetailRecordDraftUseCase();
        getAllDetailRecordDraftsUseCase = new GetAllDetailRecordDraftsUseCase();
        commitDayRecordUseCase = new CommitDayRecordUseCase();
    }

    private void loadDetailRecordDrafts(){
        getAllDetailRecordDraftsUseCase
                .get(ConstantUtil.worker.getWorkerID(), year+month+day)
                .execute(new Consumer<List<DetailRecordDraft>>() {
                    @Override
                    public void accept(List<DetailRecordDraft> detailRecordDrafts) throws Exception {
                        if (detailRecordDrafts != null && detailRecordDrafts.size() > 0){
                            for (DetailRecordDraft detailRecordDraft : detailRecordDrafts){
                                detailRecordDraftList.add(detailRecordDraft);
                                DetailRecord detailRecord = new DetailRecord();
                                detailRecord.setClothesID(detailRecordDraft.getClothesID());
                                detailRecord.setWorkTypeID(detailRecordDraft.getWorkTypeID());
                                detailRecord.setCount(detailRecordDraft.getCount());
                                detailRecordList.add(detailRecord);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
        ;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetailRecord(DetailRecord record){
        addDetailRecord(record);
    }

    private void addDetailRecord(DetailRecord detailRecord){
        for (DetailRecord record : detailRecordList){
            if (record.getWorkTypeID() == detailRecord.getWorkTypeID() &&
                    record.getClothesID() == detailRecord.getClothesID()){
                EventBus.getDefault().post(new AddDetailRecordFailed());
                return;
            }
        }
        EventBus.getDefault().post(new AddDetailRecordSuccess());

        detailRecordList.add(detailRecord);
        totalCount = totalCount + detailRecord.getCount();
        totalWorkCount.setText(getString(R.string.work_count, totalCount));

        adapter.notifyItemInserted(detailRecordList.size()-1);
        recyclerView.smoothScrollToPosition(detailRecordList.size()-1);
        saveDetailRecordDraft(detailRecord);
    }

    private void saveDetailRecordDraft(DetailRecord detailRecord){
        DetailRecordDraft detailRecordDraft = new DetailRecordDraft();
        detailRecordDraft.setWorkerId(ConstantUtil.worker.getWorkerID());
        detailRecordDraft.setWorkTypeID(detailRecord.getWorkTypeID());
        detailRecordDraft.setClothesID(detailRecord.getClothesID());
        detailRecordDraft.setCount(detailRecord.getCount());
        detailRecordDraftList.add(detailRecordDraft);
        saveSingleDetailRecordDraftUseCase.save(detailRecordDraft).execute();
    }

    private void deleteDetailRecord(int position){
        DetailRecord detailRecord = detailRecordList.get(position);
        detailRecordList.remove(position);
        adapter.notifyItemRemoved(position);
        totalCount = totalCount - detailRecord.getCount();
        if (totalCount == 0){
            totalWorkCount.setText("");
        }else {
            totalWorkCount.setText(getString(R.string.work_count, totalCount));
        }
        adapter.notifyItemRangeChanged(position, detailRecordList.size() - position);
    }

    private void deleteDetailRecordDraft(int position){
        DetailRecordDraft detailRecordDraft = detailRecordDraftList.get(position);
        deleteSingleDetailRecordDraftUseCase.delete(detailRecordDraft).execute();
        detailRecordDraftList.remove(position);
    }

    private void commitDayRecord(){

        commitDayRecordUseCase.commit(gson.toJson(dayRecord), gson.toJson(detailRecordList), 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onClick(View view) {
    }
}
