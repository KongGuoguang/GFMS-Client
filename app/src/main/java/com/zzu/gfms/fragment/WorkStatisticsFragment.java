package com.zzu.gfms.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.view.ClothesTypePicker;
import com.zzu.gfms.view.SpinnerDatePicker;
import com.zzu.gfms.view.WorkTypePicker;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkStatisticsFragment extends Fragment implements View.OnClickListener{


    public WorkStatisticsFragment() {
        // Required empty public constructor
    }

    private TextView startDateText;

    private TextView selectStartDate;

    private int startYear;

    private int startMonth;

    private int startDay;

    private TextView endDateText;

    private TextView selectEndDate;

    private int endYear;

    private int endMonth;

    private int endDay;

    private TextView clothesTypeText;

    private TextView selectClothType;

    private int clothesId;

    private TextView workTypeText;

    private TextView selectWorkType;

    private int workTypeId;

    private TextView select;

    private QMUIPopup selectStartDatePopup;

    private QMUIPopup selectEndDatePopup;

    private QMUIPopup selectClothesTypePopup;

    private QMUIPopup selectWorkTypePopup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_statistics, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view){
        QMUITopBar topBar = (QMUITopBar) view.findViewById(R.id.top_bar);
        topBar.setTitle("工作查询统计");

        startDateText = (TextView) view.findViewById(R.id.text_start_date);
        selectStartDate = (TextView) view.findViewById(R.id.text_select_start_date);
        selectStartDate.setOnClickListener(this);

        endDateText = (TextView) view.findViewById(R.id.text_end_date);
        selectEndDate = (TextView) view.findViewById(R.id.text_select_end_date);
        selectEndDate.setOnClickListener(this);

        clothesTypeText = (TextView) view.findViewById(R.id.text_clothes_type);
        selectClothType = (TextView) view.findViewById(R.id.text_select_clothes);
        selectClothType.setOnClickListener(this);

        workTypeText = (TextView) view.findViewById(R.id.text_work_type);
        selectWorkType = (TextView) view.findViewById(R.id.text_select_work);
        selectWorkType.setOnClickListener(this);

        select = (TextView) view.findViewById(R.id.text_select);
        select.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_select_start_date:
                selectStartDate();
                break;
            case R.id.text_select_end_date:
                selectEndDate();
                break;
            case R.id.text_select_clothes:
                selectClothesType();
                break;
            case R.id.text_select_work:
                selectWorkType();
                break;
        }
    }

    private void selectStartDate(){
        if (selectStartDatePopup == null){
            selectStartDatePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            SpinnerDatePicker datePicker = new SpinnerDatePicker(getActivity());
            datePicker.setOnDateChangedListener(new SpinnerDatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(SpinnerDatePicker view, int year, int month, int day) {
                    String date = year + "年" + month + "月" + day + "日";
                    startDateText.setText(date);
                    startYear = year;
                    startMonth = month;
                    startDay = day;
                }
            });
            selectStartDatePopup.setContentView(datePicker);
            selectStartDatePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }

        selectStartDatePopup.show(startDateText);
    }

    private void selectEndDate(){
        if (selectEndDatePopup == null){
            selectEndDatePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            SpinnerDatePicker datePicker = new SpinnerDatePicker(getActivity());
            datePicker.setOnDateChangedListener(new SpinnerDatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(SpinnerDatePicker view, int year, int month, int day) {
                    String date = year + "年" + month + "月" + day + "日";
                    endDateText.setText(date);
                    endYear = year;
                    endMonth = month;
                    endDay = day;
                }
            });
            selectEndDatePopup.setContentView(datePicker);
            selectEndDatePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }

        selectEndDatePopup.show(endDateText);
    }

    private void selectClothesType(){
        if (selectClothesTypePopup == null){
            selectClothesTypePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            ClothesTypePicker clothesTypePicker = new ClothesTypePicker(getActivity());
            clothesTypePicker.setOnClothesSelectedListener(new ClothesTypePicker.OnClothesSelectedListener() {
                @Override
                public void onClothesSelected(ClothesType clothesType) {
                    clothesTypeText.setText(clothesType.getName());
                    clothesId = clothesType.getClothesID();
                }
            });
            selectClothesTypePopup.setContentView(clothesTypePicker);
            selectClothesTypePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectClothesTypePopup.show(clothesTypeText);
    }

    private void selectWorkType(){
        if (selectWorkTypePopup == null){
            selectWorkTypePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            WorkTypePicker workTypePicker = new WorkTypePicker(getActivity());
            workTypePicker.setOnWorkSelectedListener(new WorkTypePicker.OnWorkSelectedListener() {
                @Override
                public void onWorkSelected(WorkType workType) {
                    workTypeText.setText(workType.getName());
                    workTypeId = workType.getWorkTypeID();
                }
            });
            selectWorkTypePopup.setContentView(workTypePicker);
            selectWorkTypePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectWorkTypePopup.show(workTypeText);
    }
}
