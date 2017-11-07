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
import com.zzu.gfms.DetailRecordActivity;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.CalendarAdapter;
import com.zzu.gfms.bean.Day;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.domain.GetDayRecordsOfMonthUseCase;
import com.zzu.gfms.domain.SaveClothesTypeUseCase;
import com.zzu.gfms.domain.SaveAllDayRecordsUseCase;
import com.zzu.gfms.domain.SaveWorkTypeUseCase;
import com.zzu.gfms.event.AddDayRecordSuccess;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.DayUtil;
import com.zzu.gfms.view.CalendarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    private TextView dayCount;

    private TextView workCount;

    private List<Day> days;

    private Button button;

    private List<DayRecord> dayRecords;

    private SaveAllDayRecordsUseCase saveAllDayRecordsUseCase;

    private GetDayRecordsOfMonthUseCase getDayRecordsOfMonthUseCase;

    private CalendarAdapter adapter;

    private int year, month;

    private Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveAllDayRecordsUseCase = new SaveAllDayRecordsUseCase();
        getDayRecordsOfMonthUseCase = new GetDayRecordsOfMonthUseCase();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
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
//                RemoteRepository.getDayRecordOfMonth(1, year, month)
//                        .subscribeOn(Schedulers.io())
//                        .subscribe();
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
                    Intent intent = new Intent(getActivity(), DetailRecordActivity.class);
                    intent.putExtra("year", day.getYear());
                    intent.putExtra("month", day.getMonth());
                    intent.putExtra("day", day.getDay());
                    intent.putExtra("dayRecordId", day.getDayRecord().getDayRecordID());
                    startActivity(intent);
                }
            }
        });

        loadDayRecordsOfMonth();
    }

    /**
     * 加载本月工作记录
     */
    private void loadDayRecordsOfMonth(){
        getDayRecordsOfMonthUseCase.get(ConstantUtil.worker.getWorkerID(), year, month)
                .execute(new Observer<List<DayRecord>>() {

                    int i = 0;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<DayRecord> dayRecords) {
                        i++;

                        if (dayRecords != null && dayRecords.size() > 0){
                            dayCount.setText(getString(R.string.day_count, dayRecords.size()));
                            WorkRecordFragment.this.dayRecords = dayRecords;
                            addDayRecordsToDays();
                            adapter.notifyDataSetChanged();
                            if (i == 2){
                                saveAllDayRecordsUseCase.save(dayRecords).execute();
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

    private void addDayRecordsToDays(){
        if (days == null || days.size() == 0 ||
                dayRecords == null || dayRecords.size() == 0) return;

        int totalCount = 0;
        for (DayRecord dayRecord: dayRecords){
            totalCount = totalCount + dayRecord.getTotal();
            String date = dayRecord.getDay();
            int length = date.length();
            if (length < 10) break;
            int dayOfMonth = Integer.parseInt(dayRecord.getDay().substring(length-2));
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

    @Subscribe
    public void onAddDayRecordSuccess(AddDayRecordSuccess event){
        loadDayRecordsOfMonth();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
        EventBus.getDefault().unregister(this);
    }
}
