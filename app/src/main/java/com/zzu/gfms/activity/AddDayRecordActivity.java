package com.zzu.gfms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.DetailRecordAdapter;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.bean.DayAndDetailRecords;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;
import com.zzu.gfms.data.utils.ConvertState;
import com.zzu.gfms.domain.ModifyDetailRecordDraftUseCase;
import com.zzu.gfms.domain.SubmitDayRecordUseCase;
import com.zzu.gfms.domain.DeleteAllDetailRecordDraftUseCase;
import com.zzu.gfms.domain.DeleteSingleDetailRecordDraftUseCase;
import com.zzu.gfms.domain.GetAllDetailRecordDraftsUseCase;
import com.zzu.gfms.domain.SaveSingleDetailRecordDraftUseCase;
import com.zzu.gfms.event.AddDayRecordSuccess;
import com.zzu.gfms.event.AddDetailRecordFailed;
import com.zzu.gfms.event.AddDetailRecordSuccess;
import com.zzu.gfms.event.ModifyDetailRecord;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.Constants;
import com.zzu.gfms.utils.ExceptionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class AddDayRecordActivity extends BaseActivity {

    private int year, month, day;
    private String date;
    private TextView totalWorkCount;
    private TextView tips;
    private RecyclerView recyclerView;


    private List<DetailRecord> detailRecordList = new ArrayList<>();
    private List<DetailRecordDraft> detailRecordDraftList = new ArrayList<>();

    private DetailRecordAdapter adapter;

    private int totalCount;

    private SaveSingleDetailRecordDraftUseCase saveSingleDetailRecordDraftUseCase;
    private DeleteSingleDetailRecordDraftUseCase deleteSingleDetailRecordDraftUseCase;
    private GetAllDetailRecordDraftsUseCase getAllDetailRecordDraftsUseCase;
    private DeleteAllDetailRecordDraftUseCase deleteAllDetailRecordDraftUseCase;
    private SubmitDayRecordUseCase submitDayRecordUseCase;
    private ModifyDetailRecordDraftUseCase modifyDetailRecordDraftUseCase;

    private DayRecord dayRecord = new DayRecord();
    private QMUITipDialog loading;

    private Disposable loadDetailRecordDraftDisposable, submitDayRecordDisposable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day_record);
        Intent intent = getIntent();
        year = intent.getIntExtra(Constants.YEAR, 0);
        month = intent.getIntExtra(Constants.MONTH, 0);
        day = intent.getIntExtra(Constants.DAY, 0);
        DecimalFormat decimalFormat = new DecimalFormat("00");
        date = String.valueOf(year) + "-" + decimalFormat.format(month) + "-" + decimalFormat.format(day);
        initView();
        initUseCase();
        loadDetailRecordDrafts();
        EventBus.getDefault().register(this);
    }

    private void initView(){
        TextView dateText = findViewById(R.id.text_month);
        String dateStr = "工作日期：" + year + "年" + month + "月" + day + "日";
        dateText.setText(dateStr);

        totalWorkCount = findViewById(R.id.text_count);

        tips = findViewById(R.id.text_tips);

        QMUITopBar topBar = findViewById(R.id.top_bar);
        topBar.setTitle("日报填写");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        topBar.addRightImageButton(R.mipmap.icon_add, R.id.topbar_right_add_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(AddDayRecordActivity.this, AddDetailRecordActivity.class));
                    }
                });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new DetailRecordAdapter(detailRecordList, true);
        adapter.setOnDeleteListener(new DetailRecordAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                deleteDetailRecord(position);
                deleteDetailRecordDraft(position);
            }
        });

        adapter.setOnModifyListener(new DetailRecordAdapter.OnModifyListener() {
            @Override
            public void onModify(int position) {
                DetailRecord detailRecord = detailRecordList.get(position);
                Intent intent = new Intent(AddDayRecordActivity.this, ModifyDetailRecordActivity.class);
                intent.putExtra("workTypeID", detailRecord.getWorkTypeID());
                intent.putExtra("clothesID", detailRecord.getClothesID());
                intent.putExtra("count", detailRecord.getCount());
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);

        loading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在提交...")
                .create();
    }

    private void initUseCase(){
        saveSingleDetailRecordDraftUseCase = new SaveSingleDetailRecordDraftUseCase();
        deleteAllDetailRecordDraftUseCase = new DeleteAllDetailRecordDraftUseCase();
        deleteSingleDetailRecordDraftUseCase = new DeleteSingleDetailRecordDraftUseCase();
        getAllDetailRecordDraftsUseCase = new GetAllDetailRecordDraftsUseCase();
        submitDayRecordUseCase = new SubmitDayRecordUseCase();
        modifyDetailRecordDraftUseCase = new ModifyDetailRecordDraftUseCase();
    }

    /**
     * 加载本地详细记录草稿
     */
    private void loadDetailRecordDrafts(){
        loadDetailRecordDraftDisposable = getAllDetailRecordDraftsUseCase
                .get(ConstantUtil.worker.getWorkerID(), date)
                .execute(new Consumer<List<DetailRecordDraft>>() {
                    @Override
                    public void accept(List<DetailRecordDraft> detailRecordDrafts) throws Exception {
                        if (detailRecordDrafts != null && detailRecordDrafts.size() > 0){
                            for (DetailRecordDraft detailRecordDraft : detailRecordDrafts){
                                totalCount = totalCount + detailRecordDraft.getCount();
                                detailRecordDraftList.add(detailRecordDraft);
                                DetailRecord detailRecord = new DetailRecord();
                                detailRecord.setClothesID(detailRecordDraft.getClothesID());
                                detailRecord.setWorkTypeID(detailRecordDraft.getWorkTypeID());
                                detailRecord.setCount(detailRecordDraft.getCount());
                                detailRecordList.add(detailRecord);
                            }
                            adapter.notifyDataSetChanged();
                            totalWorkCount.setText(getString(R.string.work_count, totalCount));
                            tips.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAddDetailRecord(DetailRecord record){
        addDetailRecord(record);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onModifyDetailRecord(ModifyDetailRecord record){
        modifyDetailRecord(record);
        modifyDetailRecordDraft(record);
    }

    /**
     * 增加一条详细记录
     * @param detailRecord
     */
    private void addDetailRecord(DetailRecord detailRecord){
        for (DetailRecord record : detailRecordList){
            if (record.getWorkTypeID() == detailRecord.getWorkTypeID() &&
                    record.getClothesID() == detailRecord.getClothesID()){
                EventBus.getDefault().post(new AddDetailRecordFailed());
                return;
            }
        }
        EventBus.getDefault().post(new AddDetailRecordSuccess());

        detailRecordList.add(detailRecord);
        totalCount = totalCount + detailRecord.getCount();
        totalWorkCount.setText(getString(R.string.work_count, totalCount));
        tips.setVisibility(View.VISIBLE);

        adapter.notifyItemInserted(detailRecordList.size()-1);
        recyclerView.smoothScrollToPosition(detailRecordList.size()-1);
        saveDetailRecordDraft(detailRecord);
    }

    /**
     * 保存详细记录草稿
     * @param detailRecord
     */
    private void saveDetailRecordDraft(DetailRecord detailRecord){
        DetailRecordDraft detailRecordDraft = new DetailRecordDraft();
        detailRecordDraft.setWorkerId(ConstantUtil.worker.getWorkerID());
        detailRecordDraft.setWorkTypeID(detailRecord.getWorkTypeID());
        detailRecordDraft.setClothesID(detailRecord.getClothesID());
        detailRecordDraft.setCount(detailRecord.getCount());
        detailRecordDraft.setDate(date);
        detailRecordDraftList.add(detailRecordDraft);
        saveSingleDetailRecordDraftUseCase.save(detailRecordDraft).execute();
    }

    private void deleteDetailRecord(int position){
        DetailRecord detailRecord = detailRecordList.get(position);
        detailRecordList.remove(position);
        adapter.notifyItemRemoved(position);
        totalCount = totalCount - detailRecord.getCount();
        if (totalCount == 0){
            totalWorkCount.setText("");
            tips.setVisibility(View.GONE);
        }else {
            totalWorkCount.setText(getString(R.string.work_count, totalCount));
            tips.setVisibility(View.VISIBLE);
        }
        adapter.notifyItemRangeChanged(position, detailRecordList.size() - position);
    }

    private void deleteDetailRecordDraft(int position){
        DetailRecordDraft detailRecordDraft = detailRecordDraftList.get(position);
        deleteSingleDetailRecordDraftUseCase.delete(detailRecordDraft).execute();
        detailRecordDraftList.remove(position);
    }

    private void modifyDetailRecord(ModifyDetailRecord modifyDetailRecord){

        DetailRecord detailRecord = detailRecordList.get(modifyDetailRecord.getPosition());

        totalCount = totalCount - detailRecord.getCount() + modifyDetailRecord.getCount();
        totalWorkCount.setText(getString(R.string.work_count, totalCount));
        tips.setVisibility(View.VISIBLE);

        detailRecord.setCount(modifyDetailRecord.getCount());
        adapter.notifyItemChanged(modifyDetailRecord.getPosition());
    }

    private void modifyDetailRecordDraft(ModifyDetailRecord modifyDetailRecord){
        DetailRecordDraft detailRecordDraft = detailRecordDraftList.get(modifyDetailRecord.getPosition());
        detailRecordDraft.setCount(modifyDetailRecord.getCount());
        modifyDetailRecordDraftUseCase.modify(detailRecordDraft).execute();
    }

    private void submitDayRecord(){
        if (detailRecordList.size() <= 0){
            showErrorDialog("请至少添加一条记录！");
            return;
        }

        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("是否确认提交？")
                .addAction("是", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        loading.show();
                        dayRecord.setTotal(totalCount);
                        dayRecord.setWorkerID(ConstantUtil.worker.getWorkerID());
                        dayRecord.setDay(date);
                        dayRecord.setConvertState(ConvertState.DAY_RECORD_SUBMIT);

                        DayAndDetailRecords dayAndDetailRecords = new DayAndDetailRecords();
                        dayAndDetailRecords.setDayRecord(dayRecord);
                        dayAndDetailRecords.setDetailRecords(detailRecordList);
                        dayAndDetailRecords.setType(1);

                        submitDayRecordUseCase
                                .commit(dayAndDetailRecords)
                                .execute(new Observer<DayAndDetailRecords>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        submitDayRecordDisposable = d;
                                    }

                                    @Override
                                    public void onNext(DayAndDetailRecords dayAndDetailRecords) {
                                        loading.dismiss();
                                        showToast("提交成功");
                                        EventBus.getDefault().post(new AddDayRecordSuccess());
                                        deleteAllDetailRecordDraftUseCase.delete(ConstantUtil.worker.getWorkerID(),
                                                date).execute();
                                        finish();
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        loading.dismiss();
                                        showErrorDialog(ExceptionUtil.parseErrorMessage(e));
                                    }

                                    @Override
                                    public void onComplete() {

                                    }
                                });
                    }
                })
                .addAction("否", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (loadDetailRecordDraftDisposable != null &&
                !loadDetailRecordDraftDisposable.isDisposed()){
            loadDetailRecordDraftDisposable.dispose();
        }

        if (submitDayRecordDisposable != null &&
                !submitDayRecordDisposable.isDisposed()){
            submitDayRecordDisposable.dispose();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_submit:
                submitDayRecord();
                break;
            case R.id.button_cancel:
                finish();
                break;
        }
    }
}
