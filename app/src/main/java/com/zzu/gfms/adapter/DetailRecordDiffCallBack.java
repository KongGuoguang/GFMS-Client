package com.zzu.gfms.adapter;

import android.support.v7.util.DiffUtil;

import com.zzu.gfms.data.dbflow.DetailRecord;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2018-01-10
 * Time:10:40
 * Summary:
 */

public class DetailRecordDiffCallBack extends DiffUtil.Callback {

    private List<DetailRecord> oldDataSet, newDataSet;

    public DetailRecordDiffCallBack(List<DetailRecord> oldDataSet, List<DetailRecord> newDataSet){
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
        DetailRecord oldDetailRecord = oldDataSet.get(oldItemPosition);
        DetailRecord newDetailRecord = newDataSet.get(newItemPosition);
        return oldDetailRecord.getDetailRecordID().equals(newDetailRecord.getDetailRecordID());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        DetailRecord oldDetailRecord = oldDataSet.get(oldItemPosition);
        DetailRecord newDetailRecord = newDataSet.get(newItemPosition);
        return oldDetailRecord.getDay().equals(newDetailRecord.getDay()) &&
                oldDetailRecord.getCount() == newDetailRecord.getCount() &&
                oldDetailRecord.getClothesID() == newDetailRecord.getClothesID() &&
                oldDetailRecord.getWorkTypeID() == newDetailRecord.getWorkTypeID();
    }
}
