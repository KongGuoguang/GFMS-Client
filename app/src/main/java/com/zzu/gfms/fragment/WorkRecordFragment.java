package com.zzu.gfms.fragment;


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
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.CalendarAdapter;
import com.zzu.gfms.bean.Day;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.domain.GetDayRecordsOfMonthUseCase;
import com.zzu.gfms.domain.SaveDayRecordsUseCase;
import com.zzu.gfms.utils.DayUtil;
import com.zzu.gfms.view.CalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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

    private TextView count;

    private List<Day> days;

    private Button button;

    private List<DayRecord> dayRecords;

    private SaveDayRecordsUseCase saveDayRecordsUseCase;

    private GetDayRecordsOfMonthUseCase getDayRecordsOfMonthUseCase;

    private CalendarAdapter adapter;

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
        date = (TextView) view.findViewById(R.id.text_date);
        count = (TextView) view.findViewById(R.id.text_count);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = DayUtil.getYear(calendar);
        int month = DayUtil.getMonth(calendar);
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
                    //TODO 打开日志填报
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
                            count.setText(getString(R.string.work_count, dayRecords.size()));
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
            dayRecords.add(dayRecord);
        }

        this.dayRecords = dayRecords;

        addDayRecordsToDays();

        adapter.notifyDataSetChanged();

        saveDayRecordsUseCase.get(dayRecords).execute();



    }

    private void addDayRecordsToDays(){
        if (days == null || days.size() == 0 ||
                dayRecords == null || dayRecords.size() == 0) return;

        Calendar calendar = Calendar.getInstance();
        for (DayRecord dayRecord: dayRecords){
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
    }
}
