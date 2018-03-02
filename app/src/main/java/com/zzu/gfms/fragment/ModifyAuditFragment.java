package com.zzu.gfms.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.zzu.gfms.R;
import com.zzu.gfms.activity.ModifyAuditActivity;
import com.zzu.gfms.adapter.OperationRecordAdapter;
import com.zzu.gfms.adapter.OperationRecordDiffCallBack;
import com.zzu.gfms.app.BaseFragment;
import com.zzu.gfms.data.dbflow.OperationRecord;
import com.zzu.gfms.data.utils.ConvertState;
import com.zzu.gfms.domain.GetOperationRecordUseCase;
import com.zzu.gfms.domain.SaveOperationRecordUseCase;
import com.zzu.gfms.event.AddDayRecordSuccess;
import com.zzu.gfms.event.HeartbeatSuccess;
import com.zzu.gfms.event.ModifyDayRecordSuccess;
import com.zzu.gfms.event.SubmitModifyApplicationSuccess;
import com.zzu.gfms.utils.CalendarUtil;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.Constants;
import com.zzu.gfms.utils.ExceptionUtil;
import com.zzu.gfms.view.ConvertStatePicker;
import com.zzu.gfms.view.SpinnerDatePicker;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyAuditFragment extends BaseFragment implements View.OnClickListener{


    public ModifyAuditFragment() {
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

    private TextView convertStateText;

    private TextView selectConvertStateText;

    private QMUIPopup selectConvertStatePopup;

    private String convertState = ConvertState.OPERATION_RECORD_ALL;

    private RecyclerView recyclerView;

    private List<OperationRecord> operationRecordList = new ArrayList<>();

    private OperationRecordAdapter operationRecordAdapter;

    private GetOperationRecordUseCase getOperationRecordUseCase;

    private SaveOperationRecordUseCase saveOperationRecordUseCase;

    private Observer<List<OperationRecord>> operationRecordObserver;

    private Consumer<List<OperationRecord>> operationRecordConsumer;

    private Disposable disposable;

    private QMUITipDialog loading;

    private int resultCount;

    private Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUseCase();
        gson = new Gson();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_audit, container, false);
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
        topBar.setTitle("申请记录");

        startDateText = view.findViewById(R.id.text_start_date);
        selectStartDate = view.findViewById(R.id.text_select_start_date);
        selectStartDate.setOnClickListener(this);

        endDateText = view.findViewById(R.id.text_end_date);
        selectEndDate = view.findViewById(R.id.text_select_end_date);
        selectEndDate.setOnClickListener(this);

        convertStateText = view.findViewById(R.id.text_convert_state);
        selectConvertStateText  = view.findViewById(R.id.text_select_convert_state);
        selectConvertStateText.setOnClickListener(this);

        TextView select = view.findViewById(R.id.text_select);
        select.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.recycler_view);
        operationRecordAdapter = new OperationRecordAdapter(operationRecordList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(operationRecordAdapter);
        operationRecordAdapter.setOnItemClickListener(new OperationRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                OperationRecord operationRecord = operationRecordList.get(position);
                String json = gson.toJson(operationRecord);
                Intent intent = new Intent(getActivity(), ModifyAuditActivity.class);
                intent.putExtra(Constants.JSON_OPERATION_RECORD, json);
                startActivity(intent);
            }
        });

        loading = new QMUITipDialog.Builder(getActivity())
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在查询")
                .create();
    }

    private void initUseCase(){
        getOperationRecordUseCase = new GetOperationRecordUseCase();
        saveOperationRecordUseCase = new SaveOperationRecordUseCase();
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
            case R.id.text_select_convert_state:
                selectConvertState();
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
            });

            selectEndDatePopup.setContentView(datePicker);
            selectEndDatePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }

        selectEndDatePopup.show(endDateText);
    }

    /**
     * 选择审核状态
     */
    private void selectConvertState(){
        if (selectConvertStatePopup == null){
            selectConvertStatePopup = new QMUIPopup(getActivity(), QMUIPopup.DIRECTION_BOTTOM);
            ConvertStatePicker picker = new ConvertStatePicker(getActivity());
            picker.setOnConvertStateSelectedListener(new ConvertStatePicker.OnConvertStateSelectedListener() {
                @Override
                public void onConvertStateSelected(ConvertStatePicker convertStatePicker, String convertState) {
                    ModifyAuditFragment.this.convertState = convertState;
                    convertStateText.setText(ConvertState.getConvertStateName(convertState));
                }
            });
            selectConvertStatePopup.setContentView(picker);
            selectConvertStatePopup.setAnimStyle(QMUIPopup.ANIM_GROW_FROM_CENTER);
        }

        selectConvertStatePopup.show(convertStateText);
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

        operationRecordList.clear();
        operationRecordAdapter.notifyDataSetChanged();

        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        getOperationRecordUseCase.get(ConstantUtil.worker.getWorkerID(),
                CalendarUtil.formatDate(startYear, startMonth, startDay),
                CalendarUtil.formatDate(endYear, endMonth, endDay), convertState)
                .execute(getOperationRecordObserver());
    }

    private Observer<List<OperationRecord>> getOperationRecordObserver(){
        resultCount = 0;

        if (operationRecordObserver == null){
            operationRecordObserver = new Observer<List<OperationRecord>>() {

                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(List<OperationRecord> operationRecords) {
                    resultCount++;
                    if (operationRecords != null && operationRecords.size() > 0){

                        loading.dismiss();

                        Collections.sort(operationRecords);

                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                                new OperationRecordDiffCallBack(operationRecordList, operationRecords), true);
                        diffResult.dispatchUpdatesTo(operationRecordAdapter);

                        operationRecordList.clear();
                        operationRecordList.addAll(operationRecords);
                        //operationRecordAdapter.notifyDataSetChanged();

                        if (resultCount == 2){
                            saveOperationRecordUseCase.save(operationRecords).execute();
                        }
                    }

                    if (resultCount == 2){
                        loading.dismiss();
                        if (operationRecordList.size() == 0){
                            showErrorDialog("没有数据，请重新选择查询条件");
                        }
                    }

                    recyclerView.smoothScrollToPosition(0);

                }

                @Override
                public void onError(Throwable e) {
                    loading.dismiss();
                    if (operationRecordList.size() == 0){
                        showErrorDialog(ExceptionUtil.parseErrorMessage(e));
                    }
                }

                @Override
                public void onComplete() {

                }
            };
        }

        return operationRecordObserver;
    }


    /**
     * 监听修改申请提交成功事件
     * @param event
     */
    @Subscribe
    public void onSubmitModifyApplicationSuccess(SubmitModifyApplicationSuccess event){
        refreshOperationRecord();
    }

    /**
     * 监听心跳
     * @param event
     */
    @Subscribe
    public void onHeartbeatSuccess(HeartbeatSuccess event){
        refreshOperationRecord();
    }

    /**
     * 监听添加/修改日报成功事件
     * @param event
     */
    @Subscribe
    public void onModifyDayRecordSuccess(ModifyDayRecordSuccess event){
        refreshOperationRecord();
    }

    private void refreshOperationRecord(){
        int start = CalendarUtil.getDateInt(startYear, startMonth, startDay);
        int end = CalendarUtil.getDateInt(endYear, endMonth, endDay);
        if (start > end){
            return;
        }

        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }

        disposable = getOperationRecordUseCase.get(ConstantUtil.worker.getWorkerID(),
                CalendarUtil.formatDate(startYear, startMonth, startDay),
                CalendarUtil.formatDate(endYear, endMonth, endDay), convertState)
                .execute(getOperationRecordConsumer());
    }

    public Consumer<List<OperationRecord>> getOperationRecordConsumer() {

        resultCount = 0;

        if (operationRecordConsumer == null){
            operationRecordConsumer = new Consumer<List<OperationRecord>>() {
                @Override
                public void accept(List<OperationRecord> operationRecords) throws Exception {
                    if (operationRecords != null && operationRecords.size() > 0){

                        Collections.sort(operationRecords);

                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                                new OperationRecordDiffCallBack(operationRecordList, operationRecords), true);
                        diffResult.dispatchUpdatesTo(operationRecordAdapter);

                        operationRecordList.clear();
                        operationRecordList.addAll(operationRecords);
                        //operationRecordAdapter.notifyDataSetChanged();

                        if (resultCount == 2){
                            saveOperationRecordUseCase.save(operationRecords).execute();
                        }

                        recyclerView.smoothScrollToPosition(0);
                    }
                }
            };
        }
        return operationRecordConsumer;
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
