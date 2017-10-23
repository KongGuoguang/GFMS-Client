package com.zzu.gfms.data.dbflow;

import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Author:kongguoguang
 * Date:2017-10-23
 * Time:19:34
 * Summary:
 */

public class DetailRecord extends BaseModel {

    @PrimaryKey
    private String detailRecordID;

    @ForeignKey(tableClass = WorkType.class)
    private int workTypeID;

    @ForeignKey(tableClass = ClothesType.class)
    private int clothesID;

    @ForeignKey(tableClass = DayRecord.class)
    private long dayRecordID;
}
