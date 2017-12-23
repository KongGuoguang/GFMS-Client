package com.zzu.gfms.data.dbflow;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.zzu.gfms.data.utils.DateUtil;

/**
 * Author:kongguoguang
 * Date:2017-10-23
 * Time:19:34
 * Summary:
 */
@Table(database = AppDatabase.class)
public class DetailRecord extends BaseModel implements Comparable<DetailRecord>{

    @PrimaryKey
    private String detailRecordID = "";

    @Column
    private int workTypeID;

    @Column
    private int clothesID;

    @Column
    private String dayRecordID = "";

    @Column
    private int count;

    private String day;

    public String getDetailRecordID() {
        return detailRecordID;
    }

    public void setDetailRecordID(String detailRecordID) {
        this.detailRecordID = detailRecordID;
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

    public String getDayRecordID() {
        return dayRecordID;
    }

    public void setDayRecordID(String dayRecordID) {
        this.dayRecordID = dayRecordID;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public int compareTo(@NonNull DetailRecord o) {
        int date1 = DateUtil.getDateInt(day);

        int date2 = DateUtil.getDateInt(o.getDay());

        return date2 - date1;
    }
}
