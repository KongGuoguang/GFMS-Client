package com.zzu.gfms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.fragment.ModifyAuditFragment;
import com.zzu.gfms.fragment.WorkRecordFragment;
import com.zzu.gfms.fragment.WorkStatisticsFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private QMUITabSegment tabSegment;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabs();
        initPagers();

    }

    private void initTabs(){
        tabSegment = (QMUITabSegment) findViewById(R.id.tabs);
        QMUITabSegment.Tab workRecord = new QMUITabSegment.Tab("工作记录");
        QMUITabSegment.Tab modifyAudit = new QMUITabSegment.Tab("修改审核");
        QMUITabSegment.Tab workStatistics = new QMUITabSegment.Tab("工作统计");
        tabSegment.addTab(workRecord).addTab(modifyAudit).addTab(workStatistics);
    }

    private void initPagers(){
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new WorkRecordFragment());
        fragments.add(new ModifyAuditFragment());
        fragments.add(new WorkStatisticsFragment());

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        tabSegment.setupWithViewPager(viewPager, false);
    }

}
