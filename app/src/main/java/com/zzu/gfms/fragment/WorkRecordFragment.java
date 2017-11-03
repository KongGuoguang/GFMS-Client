package com.zzu.gfms.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.AddDayRecordActivity;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.CalendarAdapter;
import com.zzu.gfms.bean.Day;
import com.zzu.gfms.data.RemoteRepository;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.domain.GetDayRecordsOfMonthUseCase;
import com.zzu.gfms.domain.SaveClothesTypeUseCase;
import com.zzu.gfms.domain.SaveDayRecordsUseCase;
import com.zzu.gfms.domain.SaveWorkTypeUseCase;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.DayUtil;
import com.zzu.gfms.view.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkRecordFragment extends Fragment {


    public WorkRecordFragment() {
        // Required empty public constructor
    }

    private QMUITopBar topBar;

    private CalendarView calendarView;

    private TextView date;

    private TextView dayCount;

    private TextView workCount;

    private List<Day> days;

    private Button button;

    private List<DayRecord> dayRecords;

    private SaveDayRecordsUseCase saveDayRecordsUseCase;

    private GetDayRecordsOfMonthUseCase getDayRecordsOfMonthUseCase;

    private CalendarAdapter adapter;

    private int year, month;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        LogUtils.d("onCreate");
        super.onCreate(savedInstanceState);
        saveDayRecordsUseCase = new SaveDayRecordsUseCase();
        getDayRecordsOfMonthUseCase = new GetDayRecordsOfMonthUseCase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.d("onCreateView");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LogUtils.d("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        topBar = (QMUITopBar) view.findViewById(R.id.top_bar);
        topBar.setTitle("工作日历");
        date = (TextView) view.findViewById(R.id.text_date);
        dayCount = (TextView) view.findViewById(R.id.text_day_count);
        workCount = (TextView) view.findViewById(R.id.text_work_count);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addData();
//                addClothesType();
//                addWorkType();
                RemoteRepository.getDayRecordOfMonth(1, year, month)
                        .subscribeOn(Schedulers.io())
                        .subscribe();
            }
        });

        Calendar calendar = Calendar.getInstance();
        year = DayUtil.getYear(calendar);
        month = DayUtil.getMonth(calendar);
        String dateStr = year + "年" + month + "月";
        date.setText(dateStr);

        days = DayUtil.getAllDays(calendar);
        adapter = new CalendarAdapter(days);
        calendarView.setAdapter(adapter);
        calendarView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Day day = days.get(position);
                if (!day.isCurrentMonth()){
                    return;
                }

                if (!day.isHasWorkRecord()){
                    Intent intent = new Intent(getActivity(), AddDayRecordActivity.class);
                    intent.putExtra("year", day.getYear());
                    intent.putExtra("month", day.getMonth());
                    intent.putExtra("day", day.getDay());
                    startActivity(intent);
                }else {
                    //TODO 打开日志详情
                }
            }
        });

        refreshView();
    }

    private void refreshView(){
        getDayRecordsOfMonthUseCase.get(1)
                .execute(new Observer<List<DayRecord>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<DayRecord> dayRecords) {
                        if (dayRecords != null && dayRecords.size() > 0){
                            dayCount.setText(getString(R.string.day_count, dayRecords.size()));
                            WorkRecordFragment.this.dayRecords = dayRecords;
                            addDayRecordsToDays();
                            adapter.notifyDataSetChanged();
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

    private void addData(){
        List<DayRecord> dayRecords = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int year = DayUtil.getYear(calendar);
        int month = DayUtil.getMonth(calendar);
        for (int i = 10; i < 20; i++){
            calendar.set(year, month -1, i);
            DayRecord dayRecord = new DayRecord();
            dayRecord.setWorkerID(1);
            dayRecord.setDayRecordID(i);
            dayRecord.setDay(calendar.getTimeInMillis());
            dayRecord.setTotal(100 + i);
            dayRecord.setConvertState("1");
            dayRecords.add(dayRecord);
        }

        this.dayRecords = dayRecords;

        addDayRecordsToDays();

        adapter.notifyDataSetChanged();

        saveDayRecordsUseCase.get(dayRecords).execute();



    }

    private void addClothesType(){
        List<ClothesType> clothesTypes = new ArrayList<>();
        ClothesType clothesType = new ClothesType();
        clothesType.setClothesID(1);
        clothesType.setParentID(0);
        clothesType.setName("上衣");
        clothesType.setEnterpriseID(1);
        clothesTypes.add(clothesType);

        ClothesType clothesType1 = new ClothesType();
        clothesType1.setClothesID(2);
        clothesType1.setParentID(0);
        clothesType1.setName("下衣");
        clothesType1.setEnterpriseID(1);
        clothesTypes.add(clothesType1);

        ClothesType clothesType2 = new ClothesType();
        clothesType2.setClothesID(3);
        clothesType2.setParentID(1);
        clothesType2.setName("外套");
        clothesType2.setEnterpriseID(1);
        clothesTypes.add(clothesType2);

        ClothesType clothesType3 = new ClothesType();
        clothesType3.setClothesID(4);
        clothesType3.setParentID(1);
        clothesType3.setName("毛衣");
        clothesType3.setEnterpriseID(1);
        clothesTypes.add(clothesType3);

        ClothesType clothesType4 = new ClothesType();
        clothesType4.setClothesID(5);
        clothesType4.setParentID(1);
        clothesType4.setName("短袖");
        clothesType4.setEnterpriseID(1);
        clothesTypes.add(clothesType4);

        ClothesType clothesType5 = new ClothesType();
        clothesType5.setClothesID(6);
        clothesType5.setParentID(2);
        clothesType5.setName("裤子");
        clothesType5.setEnterpriseID(1);
        clothesTypes.add(clothesType5);

        ClothesType clothesType6 = new ClothesType();
        clothesType6.setClothesID(7);
        clothesType6.setParentID(2);
        clothesType6.setName("短裤");
        clothesType6.setEnterpriseID(1);
        clothesTypes.add(clothesType6);

        ClothesType clothesType7 = new ClothesType();
        clothesType7.setClothesID(8);
        clothesType7.setParentID(2);
        clothesType7.setName("内裤");
        clothesType7.setEnterpriseID(1);
        clothesTypes.add(clothesType7);

        new SaveClothesTypeUseCase(clothesTypes).execute();
    }

    private void addWorkType(){
        List<WorkType> workTypes = new ArrayList<>();

        WorkType workType = new WorkType();
        workType.setWorkTypeID(1);
        workType.setName("缝纫");
        workType.setEnterpriseID(1);
        workTypes.add(workType);

        WorkType workType1 = new WorkType();
        workType1.setWorkTypeID(2);
        workType1.setName("锁边");
        workType1.setEnterpriseID(1);
        workTypes.add(workType1);

        WorkType workType2 = new WorkType();
        workType2.setWorkTypeID(3);
        workType2.setName("裁剪");
        workType2.setEnterpriseID(1);
        workTypes.add(workType2);

        new SaveWorkTypeUseCase(workTypes).execute();
    }

    private void addDayRecordsToDays(){
        if (days == null || days.size() == 0 ||
                dayRecords == null || dayRecords.size() == 0) return;

        int totalCount = 0;
        Calendar calendar = Calendar.getInstance();
        for (DayRecord dayRecord: dayRecords){
            totalCount = totalCount + dayRecord.getTotal();
            calendar.setTimeInMillis(dayRecord.getDay());
            int dayOfMonth = DayUtil.getDayOfMonth(calendar);
            for (Day day: days){
                if (day.isCurrentMonth() && day.getDay() == dayOfMonth){
                    day.setHasWorkRecord(true);
                    day.setDayRecord(dayRecord);
                    break;
                }
            }
        }
        workCount.setText(getString(R.string.work_count, totalCount));
    }
}
