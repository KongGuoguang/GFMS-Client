package com.zzu.gfms.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.CalendarAdapter;
import com.zzu.gfms.bean.Day;
import com.zzu.gfms.data.LocalRepository;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.utils.DayUtil;
import com.zzu.gfms.view.CalendarView;

import java.util.Calendar;
import java.util.List;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        date = (TextView) view.findViewById(R.id.text_date);
        count = (TextView) view.findViewById(R.id.text_count);
        calendarView = (CalendarView) view.findViewById(R.id.calendar_view);
        button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List<DayRecord> dayRecords = LocalRepository.getDayRecordOfMonth(1);
//                for (DayRecord da :
//                        dayRecords) {
//                    Log.d("结果", da.toString());
//                }
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year = DayUtil.getYear(calendar);
        int month = DayUtil.getMonth(calendar);
        String dateStr = year + "年" + month + "月";
        date.setText(dateStr);

        days = DayUtil.getAllDays(calendar);
        CalendarAdapter adapter = new CalendarAdapter(days);
        calendarView.setAdapter(adapter);

        //addData();
    }

    private void addData(){
        DayRecord dayRecord = new DayRecord();
        dayRecord.setWorkerID(1);
        dayRecord.setDayRecordID(3);
        dayRecord.setDay(Calendar.getInstance().getTimeInMillis());
        dayRecord.setTotal(200);
        dayRecord.save();
    }
}
