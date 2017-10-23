package com.zzu.gfms.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

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

    @ForeignKey(tableClass = Worker.class)
    private long workerID;

    @Column
    private long day;

    @Column
    private long submit;

    @Column
    private String convertState;

    @Column
    private int total;
}
