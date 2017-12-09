package com.zzu.gfms.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.zzu.gfms.R;
import com.zzu.gfms.adapter.OperationRecordAdapter;
import com.zzu.gfms.data.dbflow.OperationRecord;
import com.zzu.gfms.domain.GetOperationRecordUseCase;
import com.zzu.gfms.domain.SaveOperationRecordUseCase;
import com.zzu.gfms.event.SubmitModifyApplicationSuccess;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.ExceptionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyAuditFragment extends Fragment {


    public ModifyAuditFragment() {
        // Required empty public constructor
    }

    private QMUIEmptyView emptyView;

    private QMUIPullRefreshLayout pullRefreshLayout;

    private boolean isPullRefreshing;

    private RecyclerView recyclerView;

    private List<OperationRecord> operationRecordList = new ArrayList<>();

    private OperationRecordAdapter operationRecordAdapter;

    private GetOperationRecordUseCase getOperationRecordUseCase;

    private SaveOperationRecordUseCase saveOperationRecordUseCase;

    private Observer<List<OperationRecord>> operationRecordObserver;

    private Disposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUseCase();
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
        QMUITopBar topBar = (QMUITopBar) view.findViewById(R.id.top_bar);
        topBar.setTitle("申请记录");
        emptyView = (QMUIEmptyView) view.findViewById(R.id.empty_view);

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
                isPullRefreshing = true;
                loadOperationRecords();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        operationRecordAdapter = new OperationRecordAdapter(operationRecordList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(operationRecordAdapter);

        loadOperationRecords();
    }

    private void initUseCase(){
        getOperationRecordUseCase = new GetOperationRecordUseCase();
        saveOperationRecordUseCase = new SaveOperationRecordUseCase();
    }

    private void loadOperationRecords(){

        if (!isPullRefreshing){
            emptyView.show(true);
        }

        getOperationRecordUseCase.get(ConstantUtil.worker.getWorkerID())
                .execute(getOperationRecordObserver());

    }

    private Observer<List<OperationRecord>> getOperationRecordObserver(){
        if (operationRecordObserver == null){
            operationRecordObserver = new Observer<List<OperationRecord>>() {

                int i = 0;

                @Override
                public void onSubscribe(Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(List<OperationRecord> operationRecords) {
                    i++;
                    if (operationRecords != null && operationRecords.size() > 0){

                        emptyView.hide();

                        operationRecordList.clear();
                        operationRecordList.addAll(operationRecords);
                        operationRecordAdapter.notifyDataSetChanged();

                        if (i == 2){
                            for (OperationRecord operationRecord : operationRecords){
                                operationRecord.setWorkerID(ConstantUtil.worker.getWorkerID());
                            }
                            saveOperationRecordUseCase.save(operationRecords).execute();
                        }
                    }

                    if (i == 2){
                        if (isPullRefreshing){
                            pullRefreshLayout.finishRefresh();
                            isPullRefreshing = false;
                        }

                        if (operationRecordList.size() == 0){
                            emptyView.show("没有数据",null);
                        }
                    }

                }

                @Override
                public void onError(Throwable e) {
                    if (i == 2 && operationRecordList.size() == 0){
                        emptyView.show(false, ExceptionUtil.parseErrorMessage(e),
                                null, "点击重试", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        emptyView.hide();
                                        loadOperationRecords();
                                    }
                                });
                    }
                }

                @Override
                public void onComplete() {

                }
            };
        }

        return operationRecordObserver;
    }


    @Subscribe
    public void onSubmitModifyApplicationSuccess(SubmitModifyApplicationSuccess event){
        loadOperationRecords();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
