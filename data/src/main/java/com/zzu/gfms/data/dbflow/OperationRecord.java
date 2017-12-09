package com.zzu.gfms.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Author:kongguoguang
 * Date:2017-11-10
 * Time:10:57
 * Summary:
 */

@Table(database = AppDatabase.class)
public class OperationRecord extends BaseModel {

    @PrimaryKey
    private String operationRecordID;

    @Column
    private long checkWorkerID;

    @Column
    private long workerID;

    @Column
    private String dayRecordID;

    @Column
    private String applyTime;

    @Column
    private String checkTime;

    @Column
    private String convertState;

    @Column
    private String modifyReason;

    @Column
    private String checkReason;

    public String getOperationRecordID() {
        return operationRecordID;
    }

    public void setOperationRecordID(String operationRecordID) {
        this.operationRecordID = operationRecordID;
    }

    public long getCheckWorkerID() {
        return checkWorkerID;
    }

    public void setCheckWorkerID(long checkWorkerID) {
        this.checkWorkerID = checkWorkerID;
    }

    public long getWorkerID() {
        return workerID;
    }

    public void setWorkerID(long workerID) {
        this.workerID = workerID;
    }

    public String getDayRecordID() {
        return dayRecordID;
    }

    public void setDayRecordID(String dayRecordID) {
        this.dayRecordID = dayRecordID;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getConvertState() {
        return convertState;
    }

    public void setConvertState(String convertState) {
        this.convertState = convertState;
    }

    public String getModifyReason() {
        return modifyReason;
    }

    public void setModifyReason(String modifyReason) {
        this.modifyReason = modifyReason;
    }

    public String getCheckReason() {
        return checkReason;
    }

    public void setCheckReason(String checkReason) {
        this.checkReason = checkReason;
    }
}
