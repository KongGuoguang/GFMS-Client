package com.zzu.gfms.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.DetailRecordDiffCallBack;
import com.zzu.gfms.adapter.HorizontalPageLayoutManager;
import com.zzu.gfms.adapter.PagingScrollHelper;
import com.zzu.gfms.adapter.SimpleDetailRecordAdapter;
import com.zzu.gfms.app.BaseFragment;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.domain.GetDetailRecordsUseCase;
import com.zzu.gfms.domain.SaveDetailRecordsUseCase;
import com.zzu.gfms.utils.CalendarUtil;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.ExceptionUtil;
import com.zzu.gfms.view.ClothesTypePicker;
import com.zzu.gfms.view.SpinnerDatePicker;
import com.zzu.gfms.view.WorkTypePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkStatisticsFragment extends BaseFragment implements View.OnClickListener{


    public WorkStatisticsFragment() {
        // Required empty public constructor
    }

    private TextView startDateText;

    private TextView selectStartDate;

    private QMUIPopup selectStartDatePopup;

    private int startYear;

    private int startMonth;

    private int startDay;

    private TextView endDateText;

    private TextView selectEndDate;

    private QMUIPopup selectEndDatePopup;

    private int endYear;

    private int endMonth;

    private int endDay;

    private TextView clothesTypeText;

    private TextView selectClothType;

    private QMUIPopup selectClothesTypePopup;

    private int clothesId;

    private TextView workTypeText;

    private TextView selectWorkType;

    private QMUIPopup selectWorkTypePopup;

    private int workTypeId;

    private TextView workCountText;

    private RecyclerView recyclerView;

    private TextView tips;

    private SimpleDetailRecordAdapter simpleDetailRecordAdapter;

    private QMUITipDialog loading;

    private GetDetailRecordsUseCase getDetailRecordsUseCase;

    private SaveDetailRecordsUseCase saveDetailRecordsUseCase;

    private List<DetailRecord> detailRecordList = new ArrayList<>();

    private Observer<List<DetailRecord>> detailRecordsObserver;

    private Disposable disposable;

    private int resultCount;

    private PagingScrollHelper scrollHelper = new PagingScrollHelper();

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
        initUseCase();
        initDate();
    }

    private void initView(View view){
        QMUITopBar topBar = view.findViewById(R.id.top_bar);
        topBar.setTitle("工作查询统计");

        startDateText = view.findViewById(R.id.text_start_date);
        selectStartDate = view.findViewById(R.id.text_select_start_date);
        selectStartDate.setOnClickListener(this);

        endDateText = view.findViewById(R.id.text_end_date);
        selectEndDate = view.findViewById(R.id.text_select_end_date);
        selectEndDate.setOnClickListener(this);

        clothesTypeText = view.findViewById(R.id.text_clothes_type);
        selectClothType = view.findViewById(R.id.text_select_clothes);
        selectClothType.setOnClickListener(this);

        workTypeText = view.findViewById(R.id.text_work_type);
        selectWorkType = view.findViewById(R.id.text_select_work);
        selectWorkType.setOnClickListener(this);

        TextView select = view.findViewById(R.id.text_select);
        select.setOnClickListener(this);

        tips = view.findViewById(R.id.text_tips);

        recyclerView = view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new HorizontalPageLayoutManager(10, 1));
        simpleDetailRecordAdapter = new SimpleDetailRecordAdapter(detailRecordList);
        recyclerView.setAdapter(simpleDetailRecordAdapter);
        recyclerView.setHorizontalScrollBarEnabled(true);
        scrollHelper.setUpRecycleView(recyclerView);

        workCountText = view.findViewById(R.id.text_work_count);

        loading = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在查询")
                .create();
    }

    private void initUseCase(){
        getDetailRecordsUseCase = new GetDetailRecordsUseCase();
        saveDetailRecordsUseCase = new SaveDetailRecordsUseCase();
    }

    private void initDate(){
        Calendar calendar = Calendar.getInstance();
        endYear = CalendarUtil.getYear(calendar);
        endMonth = CalendarUtil.getMonth(calendar);
        endDay = CalendarUtil.getDayOfMonth(calendar);


        calendar.set(Calendar.YEAR, endYear - 1);
        startYear = CalendarUtil.getYear(calendar);
        startMonth = CalendarUtil.getMonth(calendar);
        startDay = CalendarUtil.getDayOfMonth(calendar);
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
            case R.id.text_select:
                startSelect();
                break;

        }
    }

    /**
     * 选择起始日期
     */
    private void selectStartDate(){
        if (selectStartDatePopup == null){
            selectStartDatePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            SpinnerDatePicker datePicker = new SpinnerDatePicker(getActivity());
            datePicker.setOnButtonClickedListener(new SpinnerDatePicker.OnButtonClickedListener() {
                @Override
                public void onConfirm(int year, int month, int day) {
                    selectStartDatePopup.dismiss();
                    String date = year + "年" + month + "月" + day + "日";
                    startDateText.setText(date);
                    startYear = year;
                    startMonth = month;
                    startDay = day;
                }

                @Override
                public void onCancel() {
                    selectStartDatePopup.dismiss();
                }

                @Override
                public void onReset() {
                    selectStartDatePopup.dismiss();
                    startDateText.setText("");
                    Calendar calendar = Calendar.getInstance();
                    startYear = CalendarUtil.getYear(calendar) - 1;
                    calendar.set(Calendar.YEAR, startYear);
                    startMonth = CalendarUtil.getMonth(calendar);
                    startDay = CalendarUtil.getDayOfMonth(calendar);
                }
            });

            selectStartDatePopup.setContentView(datePicker);
            selectStartDatePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }

        selectStartDatePopup.show(startDateText);
    }

    /**
     * 选择结束日期
     */
    private void selectEndDate(){
        if (selectEndDatePopup == null){
            selectEndDatePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            SpinnerDatePicker datePicker = new SpinnerDatePicker(getActivity());
            datePicker.setOnButtonClickedListener(new SpinnerDatePicker.OnButtonClickedListener() {
                @Override
                public void onConfirm(int year, int month, int day) {
                    selectEndDatePopup.dismiss();
                    String date = year + "年" + month + "月" + day + "日";
                    endDateText.setText(date);
                    endYear = year;
                    endMonth = month;
                    endDay = day;
                }

                @Override
                public void onCancel() {
                    selectEndDatePopup.dismiss();
                }

                @Override
                public void onReset() {
                    selectEndDatePopup.dismiss();
                    endDateText.setText("");
                    Calendar calendar = Calendar.getInstance();
                    endYear = CalendarUtil.getYear(calendar);
                    endMonth = CalendarUtil.getMonth(calendar);
                    endDay = CalendarUtil.getDayOfMonth(calendar);
                }
            });

            selectEndDatePopup.setContentView(datePicker);
            selectEndDatePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }

        selectEndDatePopup.show(endDateText);
    }

    /**
     * 选择衣服类型
     */
    private void selectClothesType(){
        if (selectClothesTypePopup == null){
            selectClothesTypePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            ClothesTypePicker clothesTypePicker = new ClothesTypePicker(getActivity());
            clothesTypePicker.setOnButtonClickedListener(new ClothesTypePicker.OnButtonClickedListener() {
                @Override
                public void onConfirm(ClothesType clothesType) {
                    selectClothesTypePopup.dismiss();
                    clothesTypeText.setText(clothesType.getName());
                    clothesId = clothesType.getClothesID();
                }

                @Override
                public void onCancel() {
                    selectClothesTypePopup.dismiss();
                }

                @Override
                public void onReset() {
                    selectClothesTypePopup.dismiss();
                    clothesTypeText.setText("");
                    clothesId = 0;
                }
            });

            selectClothesTypePopup.setContentView(clothesTypePicker);
            selectClothesTypePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectClothesTypePopup.show(clothesTypeText);
    }

    /**
     * 选择工作类型
     */
    private void selectWorkType(){
        if (selectWorkTypePopup == null){
            selectWorkTypePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            WorkTypePicker workTypePicker = new WorkTypePicker(getActivity());
            workTypePicker.setOnButtonClickedListener(new WorkTypePicker.OnButtonClickedListener() {
                @Override
                public void onConfirm(WorkType workType) {
                    selectWorkTypePopup.dismiss();
                    workTypeText.setText(workType.getName());
                    workTypeId = workType.getWorkTypeID();
                }

                @Override
                public void onCancel() {
                    selectWorkTypePopup.dismiss();
                }

                @Override
                public void onReset() {
                    selectWorkTypePopup.dismiss();
                    workTypeText.setText("");
                    workTypeId = 0;
                }
            });

            selectWorkTypePopup.setContentView(workTypePicker);
            selectWorkTypePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }
        selectWorkTypePopup.show(workTypeText);
    }

    /**
     * 开始查询
     */
    private void startSelect(){

        int start = CalendarUtil.getDateInt(startYear, startMonth, startDay);
        int end = CalendarUtil.getDateInt(endYear, endMonth, endDay);
        if (start > end){
            showToast("工作日期选择有误，请重新选择");
            return;
        }

        loading.show();

        detailRecordList.clear();
        simpleDetailRecordAdapter.notifyDataSetChanged();
        workCountText.setText("");

//        if (detailRecordList.size() > 0){
//            detailRecordList.clear();
//            simpleDetailRecordAdapter.notifyDataSetChanged();
//        }

        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        getDetailRecordsUseCase.get(ConstantUtil.worker.getWorkerID(),
                CalendarUtil.formatDate(startYear, startMonth, startDay),
                CalendarUtil.formatDate(endYear, endMonth, endDay), clothesId, workTypeId)
                .execute(getDetailRecordsObserver());
    }

    private Observer<List<DetailRecord>> getDetailRecordsObserver(){
        resultCount = 0;
        if (detailRecordsObserver == null){
            detailRecordsObserver = new Observer<List<DetailRecord>>() {

                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(List<DetailRecord> detailRecords) {
                    resultCount++;
                    if (detailRecords != null && detailRecords.size() > 0){
                        loading.dismiss();
                        tips.setVisibility(View.GONE);
                        Collections.sort(detailRecords);

                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DetailRecordDiffCallBack(detailRecordList, detailRecords));
                        diffResult.dispatchUpdatesTo(simpleDetailRecordAdapter);

                        detailRecordList.clear();
                        int sum = 0;
                        for (DetailRecord detailRecord : detailRecords){
                            sum += detailRecord.getCount();
                            detailRecordList.add(detailRecord);
                        }

                        String workCount = "总计：" + sum + "件";
                        workCountText.setText(workCount);


                        //simpleDetailRecordAdapter.notifyDataSetChanged();

                        if (resultCount == 2){
                            saveDetailRecordsUseCase.save(detailRecords).execute();
                        }
                    }

                    if (resultCount == 2 && detailRecordList.size() == 0){
                        loading.dismiss();
                        tips.setVisibility(View.VISIBLE);
                        //showErrorDialog("该段日期内没有数据，请重新选择");
                    }

                    recyclerView.smoothScrollToPosition(0);
                    scrollHelper.scrollToPosition(0);

                }

                @Override
                public void onError(Throwable e) {
                    loading.dismiss();
                    if (detailRecordList.size() == 0){
                        showErrorDialog(ExceptionUtil.parseErrorMessage(e));
                    }
                }

                @Override
                public void onComplete() {

                }
            };
        }
        return detailRecordsObserver;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
