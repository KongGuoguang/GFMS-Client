package com.zzu.gfms;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.domain.GetClothesTypeUseCase;
import com.zzu.gfms.domain.GetWorkTypeUseCase;
import com.zzu.gfms.domain.SaveClothesTypeUseCase;
import com.zzu.gfms.domain.SaveWorkTypeUseCase;
import com.zzu.gfms.fragment.ModifyAuditFragment;
import com.zzu.gfms.fragment.WorkRecordFragment;
import com.zzu.gfms.fragment.WorkStatisticsFragment;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.ExceptionUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private QMUITabSegment tabSegment;

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTabs();
        initPagers();
        initWorkType();
        initClothesType();
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
        viewPager.setOffscreenPageLimit(2);
        tabSegment.setupWithViewPager(viewPager, false);
    }

    private void initClothesType() {
        if (ConstantUtil.clothesTypes == null || ConstantUtil.clothesTypes.size() <= 0){
            new GetClothesTypeUseCase().execute(new Observer<List<ClothesType>>() {

                int i = 0;
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<ClothesType> clothesTypes) {
                    i++;
                    ConstantUtil.clothesTypes = clothesTypes;
                    if (i == 2){
                        new SaveClothesTypeUseCase(clothesTypes).execute();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    LogUtils.d("initClothesType onError " + ExceptionUtil.parseErrorMessage(e));
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    private void initWorkType() {
        if (ConstantUtil.workTypes == null || ConstantUtil.workTypes.size() <= 0){
            new GetWorkTypeUseCase().execute(new Observer<List<WorkType>>() {
                int i = 0;
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(List<WorkType> workTypes) {
                    i++;
                    ConstantUtil.workTypes = workTypes;
                    if (i == 2){
                        new SaveWorkTypeUseCase(workTypes).execute();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    LogUtils.d("initWorkType onError " + ExceptionUtil.parseErrorMessage(e));
                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

}
