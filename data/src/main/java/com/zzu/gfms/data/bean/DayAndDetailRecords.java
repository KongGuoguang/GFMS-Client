package com.zzu.gfms.data.bean;

import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:19:55
 * Summary:封装一条日报记录和其对应的详细记录列表
 */

public class DayAndDetailRecords {

    private DayRecord dayRecord;

    private List<DetailRecord> detailRecords;

    private int type;

    public DayRecord getDayRecord() {
        return dayRecord;
    }

    public void setDayRecord(DayRecord dayRecord) {
        this.dayRecord = dayRecord;
    }

    public List<DetailRecord> getDetailRecords() {
        return detailRecords;
    }

    public void setDetailRecords(List<DetailRecord> detailRecords) {
        this.detailRecords = detailRecords;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
