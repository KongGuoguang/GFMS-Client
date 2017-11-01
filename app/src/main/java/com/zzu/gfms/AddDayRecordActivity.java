package com.zzu.gfms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.adapter.DetailRecordAdapter;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.event.AddDetailRecordFailed;
import com.zzu.gfms.event.AddDetailRecordSuccess;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AddDayRecordActivity extends BaseActivity {

    private QMUITopBar topBar;
    private int year, month, day;
    private TextView date;
    private TextView totalWorkCount;
    private RecyclerView recyclerView;

    private List<DetailRecord> detailRecords = new ArrayList<>();
    private DetailRecordAdapter adapter;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day_record);
        Intent intent = getIntent();
        year = intent.getIntExtra("year", 0);
        month = intent.getIntExtra("month", 0);
        day = intent.getIntExtra("day", 0);
        initView();
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
        adapter = new DetailRecordAdapter(detailRecords);
        adapter.setOnDeleteListener(new DetailRecordAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                deleteDetailRecord(position);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetailRecord(DetailRecord record){
        addDetailRecord(record);
    }

    private void addDetailRecord(DetailRecord detailRecord){
        for (DetailRecord record : detailRecords){
            if (record.getWorkTypeID() == detailRecord.getWorkTypeID() &&
                    record.getClothesID() == detailRecord.getClothesID()){
                EventBus.getDefault().post(new AddDetailRecordFailed());
                return;
            }
        }
        EventBus.getDefault().post(new AddDetailRecordSuccess());

        detailRecords.add(detailRecord);
        count = count + detailRecord.getCount();
        totalWorkCount.setText(getString(R.string.work_count, count));

        adapter.notifyItemInserted(detailRecords.size()-1);
        recyclerView.smoothScrollToPosition(detailRecords.size()-1);
    }

    private void deleteDetailRecord(int position){
        LogUtils.d("delete position = " + position);
        DetailRecord detailRecord = detailRecords.get(position);
        detailRecords.remove(position);
        adapter.notifyItemRemoved(position);
        count = count - detailRecord.getCount();
        if (count == 0){
            totalWorkCount.setText("");
        }else {
            totalWorkCount.setText(getString(R.string.work_count, count));
        }
        adapter.notifyItemRangeChanged(position, detailRecords.size() - position);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onClick(View view) {
    }
}
