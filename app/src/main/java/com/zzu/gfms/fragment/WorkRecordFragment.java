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

import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.zzu.gfms.activity.AddDayRecordActivity;
import com.zzu.gfms.activity.ShowDayRecordActivity;
import com.zzu.gfms.activity.ModifyDayRecordActivity;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.CalendarAdapter;
import com.zzu.gfms.bean.Day;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.utils.ConvertState;
import com.zzu.gfms.domain.GetDayRecordsUseCase;
import com.zzu.gfms.domain.SaveDayRecordUseCase;
import com.zzu.gfms.event.AddDayRecordSuccess;
import com.zzu.gfms.event.SubmitModifyApplicationSuccess;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.CalendarUtil;
import com.zzu.gfms.view.CalendarView;
import com.zzu.gfms.view.MonthPicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkRecordFragment extends Fragment {


    public WorkRecordFragment() {
        // Required empty public constructor
    }

    private TextView monthText;

    private TextView dayCount;

    private TextView workCount;

    private QMUIPullRefreshLayout pullRefreshLayout;

    private List<Day> days = new ArrayList<>();

    private List<DayRecord> dayRecords;

    private SaveDayRecordUseCase saveDayRecordUseCase;

    private GetDayRecordsUseCase getDayRecordsUseCase;

    private CalendarAdapter calendarAdapter;

    private int currentYear, currenMonth, currentDay;

    private Disposable disposable;

    private boolean isRefreshing;

    private Consumer<List<DayRecord>> dayRecordsConsumer;

    private QMUIPopup selectMonthPopup;

    private Calendar calendar;

    private int resultCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveDayRecordUseCase = new SaveDayRecordUseCase();
        getDayRecordsUseCase = new GetDayRecordsUseCase();
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

        calendar = Calendar.getInstance();
        currentYear = CalendarUtil.getYear(calendar);
        currenMonth = CalendarUtil.getMonth(calendar);
        currentDay = CalendarUtil.getDayOfMonth(calendar);

        QMUITopBar topBar = (QMUITopBar) view.findViewById(R.id.top_bar);
        topBar.setTitle("工作日历");

        monthText = (TextView) view.findViewById(R.id.text_month);
        final String dateStr = currentYear + "年" + this.currenMonth + "月";
        monthText.setText(dateStr);
        monthText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMonth();
            }
        });

        dayCount = (TextView) view.findViewById(R.id.text_day_count);
        workCount = (TextView) view.findViewById(R.id.text_work_count);

        pullRefreshLayout = (QMUIPullRefreshLayout) view.findViewById(R.id.layout_pull_refresh);
        pullRefreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
            @Override
            public void onMoveTarget(int offset) {
            }

            @Override
            public void onMoveRefreshView(int offset) {
            }

            @Override
            public void onRefresh() {
                LogUtils.d("onRefresh");
                isRefreshing = true;
                loadDayRecordsOfMonth();
            }
        });

        CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        days.addAll(CalendarUtil.getAllDays(calendar));
        calendarAdapter = new CalendarAdapter(days);
        calendarView.setAdapter(calendarAdapter);
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
                            case ConvertState.DAY_RECORD_MODIFY_PASSED:
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
        disposable = getDayRecordsUseCase
                .get(ConstantUtil.worker.getWorkerID(), CalendarUtil.formatDate(currentYear, currenMonth))
                .execute(getDayRecordsConsumer());
    }

    private Consumer<List<DayRecord>> getDayRecordsConsumer(){
        resultCount = 0;

        if (dayRecordsConsumer == null){
            dayRecordsConsumer = new Consumer<List<DayRecord>>() {

                @Override
                public void accept(List<DayRecord> dayRecords) throws Exception {

                    LogUtils.d("dayRecordsConsumer, accept");

                    resultCount++;

                    if (dayRecords != null && dayRecords.size() > 0){
                        Iterator<DayRecord> iterator = dayRecords.iterator();
                        while(iterator.hasNext()){
                            DayRecord dayRecord = iterator.next();
                            if(ConvertState.DAY_RECORD_MODIFY_HISTORY.equals(dayRecord.getConvertState())){
                                iterator.remove();
                            }
                        }
                        dayCount.setText(getString(R.string.day_count, dayRecords.size()));
                        WorkRecordFragment.this.dayRecords = dayRecords;
                        addDayRecordsToDays();
                        calendarAdapter.notifyDataSetChanged();

                        if (resultCount == 2){
                            LogUtils.d("dayRecordsConsumer, save dayRecords");
                            saveDayRecordUseCase.save(dayRecords).execute();
                        }
                    }

                    if (isRefreshing && resultCount == 2){
                        isRefreshing = false;
                        pullRefreshLayout.finishRefresh();
                    }
                }
            };
        }
        return dayRecordsConsumer;
    }

    private void addDayRecordsToDays(){
        if (days == null || days.size() == 0 ||
                dayRecords == null || dayRecords.size() == 0) return;

        int totalCount = 0;
        for (DayRecord dayRecord: dayRecords){
            if (ConvertState.DAY_RECORD_MODIFY_HISTORY.equals(dayRecord.getConvertState())){
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

    private void selectMonth(){

        if (selectMonthPopup == null){
            selectMonthPopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            MonthPicker monthPicker = new MonthPicker(getActivity());
            monthPicker.setOnMonthChangedListener(new MonthPicker.OnMonthChangedListener() {
                @Override
                public void onMonthChanged(MonthPicker view, int year, int month) {
                    currentYear = year;
                    currenMonth = month;
                    calendar.set(currentYear, currenMonth -1, currentDay);
                    days.clear();
                    days.addAll(CalendarUtil.getAllDays(calendar));
                    calendarAdapter.notifyDataSetChanged();

                    final String dateStr = currentYear + "年" + currenMonth + "月";
                    monthText.setText(dateStr);
                    dayCount.setText("");
                    workCount.setText("");

                    if (disposable != null && !disposable.isDisposed()){
                        disposable.dispose();
                    }
                    loadDayRecordsOfMonth();
                }
            });
            selectMonthPopup.setContentView(monthPicker);
            selectMonthPopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectMonthPopup.show(monthText);
    }
}
