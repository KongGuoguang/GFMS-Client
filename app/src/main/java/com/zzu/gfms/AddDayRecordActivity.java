package com.zzu.gfms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.adapter.DetailRecordAdapter;
import com.zzu.gfms.data.dbflow.DetailRecord;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class AddDayRecordActivity extends AppCompatActivity {

    private QMUITopBar topBar;
    private int year, month, day;
    private TextView date;
    private TextView count;
    private RecyclerView recyclerView;

    private List<DetailRecord> detailRecords = new ArrayList<>();
    private DetailRecordAdapter adapter;

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

        count = (TextView) findViewById(R.id.text_count);

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
        recyclerView.setAdapter(new DetailRecordAdapter(detailRecords));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetailRecord(DetailRecord record){
        detailRecords.add(record);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
