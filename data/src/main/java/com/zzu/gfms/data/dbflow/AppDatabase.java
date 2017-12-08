package com.zzu.gfms.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Author:kongguoguang
 * Date:2017-10-23
 * Time:17:30
 * Summary:
 */

@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {

    //数据库名称
    static final String NAME = "gfms";
    //数据库版本号
    static final int VERSION = 3;
}
