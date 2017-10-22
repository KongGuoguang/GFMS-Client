package com.zzu.gfms.data;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/10/22.
 */

public class WorkType extends DataSupport {


    @Column(unique = true, nullable = false)
    private int workTypeID;

    private String name;

    private String remark;
}
