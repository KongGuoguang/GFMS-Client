package com.zzu.gfms.data.bean;

import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:19:55
 * Summary:
 */

public class DayAndDetailRecords {

    private DayRecord dayRecord;

    private List<DetailRecord> detailRecords;

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
}
