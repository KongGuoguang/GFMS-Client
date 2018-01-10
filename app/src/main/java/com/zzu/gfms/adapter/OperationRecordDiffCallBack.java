package com.zzu.gfms.adapter;

import android.support.v7.util.DiffUtil;

import com.zzu.gfms.data.dbflow.OperationRecord;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2018-01-10
 * Time:15:52
 * Summary:
 */

public class OperationRecordDiffCallBack extends DiffUtil.Callback {

    private List<OperationRecord> oldDataSet, newDataSet;

    public OperationRecordDiffCallBack(List<OperationRecord> oldDataSet, List<OperationRecord> newDataSet){
        this.oldDataSet = oldDataSet;
        this.newDataSet = newDataSet;
    }

    @Override
    public int getOldListSize() {
        return oldDataSet == null ? 0 : oldDataSet.size();
    }

    @Override
    public int getNewListSize() {
        return newDataSet == null ? 0 : newDataSet.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        OperationRecord oldOperationRecord = oldDataSet.get(oldItemPosition);
        OperationRecord newOperationRecord = newDataSet.get(newItemPosition);
        return oldOperationRecord.getOperationRecordID().equals(newOperationRecord.getOperationRecordID());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        OperationRecord oldOperationRecord = oldDataSet.get(oldItemPosition);
        OperationRecord newOperationRecord = newDataSet.get(newItemPosition);
        return oldOperationRecord.getConvertState().equals(newOperationRecord.getConvertState()) &&
                oldOperationRecord.getDayRecordConvertState().equals(newOperationRecord.getDayRecordConvertState());
    }
}
