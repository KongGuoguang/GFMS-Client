package com.zzu.gfms.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.domain.GetClothesTypeUseCase;
import com.zzu.gfms.domain.GetWorkTypeUseCase;
import com.zzu.gfms.domain.SaveClothesTypeUseCase;
import com.zzu.gfms.domain.SaveWorkTypeUseCase;
import com.zzu.gfms.event.ModifyPwdSuccess;
import com.zzu.gfms.event.SubmitModifyApplicationSuccess;
import com.zzu.gfms.fragment.MeFragment;
import com.zzu.gfms.fragment.ModifyAuditFragment;
import com.zzu.gfms.fragment.WorkRecordFragment;
import com.zzu.gfms.fragment.WorkStatisticsFragment;
import com.zzu.gfms.utils.ConstantUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

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
        EventBus.getDefault().register(this);
    }

    private void initTabs(){
        tabSegment = (QMUITabSegment) findViewById(R.id.tabs);
        QMUITabSegment.Tab workRecord = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(this, R.mipmap.icon_work_record),null,
                "工作记录",true);
        QMUITabSegment.Tab modifyAudit = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(this, R.mipmap.icon_modify_audit),null,
                "修改审核",true);
        QMUITabSegment.Tab workStatistics = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(this, R.mipmap.icon_work_statistics),null,
                "工作统计", true);
        QMUITabSegment.Tab me = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(this, R.mipmap.icon_me),null,
                "我的",true);
        tabSegment.addTab(workRecord).addTab(modifyAudit).addTab(workStatistics).addTab(me);
    }

    private void initPagers(){
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(new WorkRecordFragment());
        fragments.add(new ModifyAuditFragment());
        fragments.add(new WorkStatisticsFragment());
        fragments.add(new MeFragment());

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
        viewPager.setOffscreenPageLimit(3);
        tabSegment.setupWithViewPager(viewPager, false);
    }

    private void initClothesType() {
        if (ConstantUtil.clothesTypes == null || ConstantUtil.clothesTypes.size() <= 0){
            new GetClothesTypeUseCase()
                    .get(ConstantUtil.worker.getWorkerID(), ConstantUtil.worker.getEnterpriseID())
                    .execute(new Consumer<List<ClothesType>>() {
                        int i = 0;
                        @Override
                        public void accept(List<ClothesType> clothesTypes) throws Exception {
                            i++;
                            ConstantUtil.setClothesTypes(clothesTypes);
                            if (i == 2){
                                new SaveClothesTypeUseCase(clothesTypes).execute();
                            }
                        }
                    });
        }
    }

    private void initWorkType() {
        if (ConstantUtil.workTypes == null || ConstantUtil.workTypes.size() <= 0){
            new GetWorkTypeUseCase()
                    .get(ConstantUtil.worker.getWorkerID(), ConstantUtil.worker.getEnterpriseID())
                    .execute(new Consumer<List<WorkType>>() {
                        int i = 0;
                        @Override
                        public void accept(List<WorkType> workTypes) throws Exception {
                            i++;
                            ConstantUtil.setWorkTypes(workTypes);
                            if (i == 2){
                                new SaveWorkTypeUseCase(workTypes).execute();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    /**
     * 接收到修改日志申请发送成功事件
     * @param event
     */
    @Subscribe(priority = 1)
    public void onSubmitModifyApplicationSuccess(SubmitModifyApplicationSuccess event){
        viewPager.setCurrentItem(1, true);
    }

    @Subscribe
    public void onModifyPwdSuccess(ModifyPwdSuccess event){
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
