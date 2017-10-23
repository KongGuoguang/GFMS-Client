package com.zzu.gfms.data.dbflow;


import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Administrator on 2017/10/22.
 */

@Table(database = AppDatabase.class)
public class WorkType extends BaseModel{

    @PrimaryKey
    private int workTypeID;

    @Column
    private String name;

    @Column
    private String remark;
}
