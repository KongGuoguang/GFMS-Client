package com.zzu.gfms.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Author:kongguoguang
 * Date:2017-11-03
 * Time:12:44
 * Summary:
 */

@Table(database = AppDatabase.class)
public class DetailRecordDraft extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String detailRecordID = "";

    @Column
    private long workerId;

    @Column
    private String date;

    @Column
    private int workTypeID;

    @Column
    private int clothesID;

    @Column
    private int count;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetailRecordID() {
        return detailRecordID;
    }

    public void setDetailRecordID(String detailRecordID) {
        this.detailRecordID = detailRecordID;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWorkTypeID() {
        return workTypeID;
    }

    public void setWorkTypeID(int workTypeID) {
        this.workTypeID = workTypeID;
    }

    public int getClothesID() {
        return clothesID;
    }

    public void setClothesID(int clothesID) {
        this.clothesID = clothesID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
