package com.zzu.gfms.data.dbflow;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zzu.gfms.data.utils.DateUtil;

/**
 * Author:kongguoguang
 * Date:2017-11-10
 * Time:10:57
 * Summary:
 */

@Table(database = AppDatabase.class)
public class OperationRecord extends BaseModel implements Comparable<OperationRecord>{

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
    private String convertState = "";

    @Column
    private String modifyReason;

    @Column
    private String checkReason;

    @Column
    private String day;

    @Column
    private int total;

    @Column
    private String dayRecordConvertState = "";

    @Column
    private String copyNewDayRecordID;

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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDayRecordConvertState() {
        return dayRecordConvertState;
    }

    public void setDayRecordConvertState(String dayRecordConvertState) {
        this.dayRecordConvertState = dayRecordConvertState;
    }

    public String getCopyNewDayRecordID() {
        return copyNewDayRecordID;
    }

    public void setCopyNewDayRecordID(String copyNewDayRecordID) {
        this.copyNewDayRecordID = copyNewDayRecordID;
    }

    @Override
    public int compareTo(@NonNull OperationRecord o) {

        int checkDate1, checkDate2, applyDate1, applyDate2;

        if (TextUtils.isEmpty(checkTime) || checkTime.length() < 10){
            checkDate1 = 0;
        }else {
            checkDate1 = DateUtil.getDateInt(checkTime.substring(0, 10));
        }

        if (TextUtils.isEmpty(o.checkTime) || o.checkTime.length() < 10){
            checkDate2 = 0;
        }else {
            checkDate2 = DateUtil.getDateInt(o.checkTime.substring(0, 10));
        }

        if (checkDate1 != checkDate2){
            return checkDate2 - checkDate1;
        }else {
            if (TextUtils.isEmpty(applyTime) || applyTime.length() < 10){
                applyDate1 = 0;
            }else {
                applyDate1 = DateUtil.getDateInt(applyTime.substring(0, 10));
            }

            if (TextUtils.isEmpty(o.applyTime) || o.applyTime.length() < 10){
                applyDate2 = 0;
            }else {
                applyDate2 = DateUtil.getDateInt(o.applyTime.substring(0, 10));
            }

            if (applyDate1 != applyDate2){
                return applyDate2 - applyDate1;
            }else {
                int date1 = DateUtil.getDateInt(day);

                int date2 = DateUtil.getDateInt(o.day);

                return date2 - date1;
            }


        }


    }
}
