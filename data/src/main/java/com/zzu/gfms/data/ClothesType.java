package com.zzu.gfms.data;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by Administrator on 2017/10/22.
 */

public class ClothesType extends DataSupport {
    @Column(unique = true)
    private int clothesID;

    @Column(nullable = false)
    private String name;

    private String remark;

    @Column(nullable = false)
    private String parentID;
}
