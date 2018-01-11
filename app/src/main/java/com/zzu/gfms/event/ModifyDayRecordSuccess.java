package com.zzu.gfms.event;

/**
 * Author:kongguoguang
 * Date:2018-01-11
 * Time:13:58
 * Summary:修改日报成功事件
 */

public class ModifyDayRecordSuccess {

    private String dayRecordId;

    public ModifyDayRecordSuccess(String dayRecordId){
        this.dayRecordId = dayRecordId;
    }

    public String getDayRecordId() {
        return dayRecordId;
    }
}
