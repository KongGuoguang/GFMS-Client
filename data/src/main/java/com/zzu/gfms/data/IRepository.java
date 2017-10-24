package com.zzu.gfms.data;

import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.WorkType;

import java.util.Calendar;
import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:14:22
 * Summary:
 */

public interface IRepository {

    List<DayRecord> getDayRecordOfMonth(Calendar calendar);

    List<DetailRecord> getDatailRecordOfDay(long dayRecordId);

    List<WorkType> getAllWorkType();

    List<ClothesType> getAllClothesType();
}
