package com.zzu.gfms.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.AddDayRecordActivity;
import com.zzu.gfms.ShowDayRecordActivity;
import com.zzu.gfms.ModifyDayRecordActivity;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.CalendarAdapter;
import com.zzu.gfms.bean.Day;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.utils.ConvertState;
import com.zzu.gfms.domain.GetDayRecordsOfMonthUseCase;
import com.zzu.gfms.domain.SaveAllDayRecordsUseCase;
import com.zzu.gfms.event.AddDayRecordSuccess;
import com.zzu.gfms.event.SubmitModifyApplicationSuccess;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.DayUtil;
import com.zzu.gfms.view.CalendarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    private TextView dayCount;

    private TextView workCount;

    private List<Day> days;

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
        QMUITopBar topBar = (QMUITopBar) view.findViewById(R.id.top_bar);
        topBar.setTitle("工作日历");
        TextView date = (TextView) view.findViewById(R.id.text_date);
        dayCount = (TextView) view.findViewById(R.id.text_day_count);
        workCount = (TextView) view.findViewById(R.id.text_work_count);
        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendar_view);

        final Calendar calendar = Calendar.getInstance();
        year = DayUtil.getYear(calendar);
        month = DayUtil.getMonth(calendar);
        final String dateStr = year + "年" + month + "月";
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

                Intent intent = new Intent();
                intent.putExtra("year", day.getYear());
                intent.putExtra("month", day.getMonth());
                intent.putExtra("day", day.getDay());

                if (!day.isHasWorkRecord()){
                    intent.setClass(getActivity(), AddDayRecordActivity.class);
                }else {
                    DayRecord dayRecord = day.getDayRecord();
                    if (dayRecord != null){
                        intent.putExtra("dayRecordId", dayRecord.getDayRecordID());
                        switch (dayRecord.getConvertState()){
                            case ConvertState.DAY_RECORD_TEMPORARY:
                                intent.setClass(getActivity(), ModifyDayRecordActivity.class);
                                break;
                            case ConvertState.DAY_RECORD_MODIFY_NOT_CHECK:
                                intent.putExtra("convertState", dayRecord.getConvertState());
                            case ConvertState.DAY_RECORD_SUBMIT:
                                intent.setClass(getActivity(), ShowDayRecordActivity.class);
                                break;
                            default:
                        }
                    }
                }
                startActivity(intent);
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
            if (ConvertState.DAY_RECORD_MODIFY_PASSED.equals(dayRecord.getConvertState())){
                continue;
            }
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

    @Subscribe
    public void onSubmitModifyApplicationSuccess(SubmitModifyApplicationSuccess event){
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
