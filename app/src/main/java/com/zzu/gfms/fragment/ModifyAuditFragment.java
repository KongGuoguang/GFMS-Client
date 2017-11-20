package com.zzu.gfms.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.OperationRecord;
import com.zzu.gfms.domain.GetOperationRecordsUseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ModifyAuditFragment extends Fragment {


    public ModifyAuditFragment() {
        // Required empty public constructor
    }

    private QMUIEmptyView emptyView;

    private List<OperationRecord> operationRecordList = new ArrayList<>();

    private GetOperationRecordsUseCase getOperationRecordsUseCase;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUseCase();
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

        loadOperationRecords();
    }

    private void initUseCase(){
        getOperationRecordsUseCase = new GetOperationRecordsUseCase();
    }

    private void loadOperationRecords(){
        if (operationRecordList.size() < 1){
            emptyView.show(true);
        }


    }



}
