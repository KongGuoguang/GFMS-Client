package com.zzu.gfms.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-10-23
 * Time:19:07
 * Summary:
 */

@Table(database = AppDatabase.class)
public class DayRecord extends BaseModel {

    @PrimaryKey
    private long dayRecordID;

    @Column
    private long workerID;

    @Column
    private long day;

    @Column
    private long submit;

    @Column
    private String convertState;

    @Column
    private int total;

    private List<DetailRecord> detailRecords;

    public long getDayRecordID() {
        return dayRecordID;
    }

    public void setDayRecordID(long dayRecordID) {
        this.dayRecordID = dayRecordID;
    }

    public long getWorkerID() {
        return workerID;
    }

    public void setWorkerID(long workerID) {
        this.workerID = workerID;
    }

    public long getDay() {
        return day;
    }

    public void setDay(long day) {
        this.day = day;
    }

    public long getSubmit() {
        return submit;
    }

    public void setSubmit(long submit) {
        this.submit = submit;
    }

    public String getConvertState() {
        return convertState;
    }

    public void setConvertState(String convertState) {
        this.convertState = convertState;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DetailRecord> getDetailRecords() {
        return detailRecords;
    }

    public void setDetailRecords(List<DetailRecord> detailRecords) {
        this.detailRecords = detailRecords;
    }

    @Override
    public String toString() {
        return "DayRecord{" +
                "dayRecordID=" + dayRecordID +
                ", workerID=" + workerID +
                ", day=" + day +
                ", submit=" + submit +
                ", convertState='" + convertState + '\'' +
                ", total=" + total +
                '}';
    }
}
