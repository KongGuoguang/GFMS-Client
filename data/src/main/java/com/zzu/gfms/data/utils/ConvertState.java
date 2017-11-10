package com.zzu.gfms.data.utils;

/**
 * Author:kongguoguang
 * Date:2017-11-10
 * Time:9:14
 * Summary:
 */

public class ConvertState {

    /*  日报记录状态 */
    public static final String DAY_RECORD_TEMPORARY = "0";//暂存

    public static final String DAY_RECORD_SUBMIT = "1";//提交

    public static final String DAY_RECORD_MODIFY_NOT_CHECK = "2";//修改申请未审核

    public static final String DAY_RECORD_MODIFY_PASSED = "9";//修改申请审核通过


    /*  审核记录状态 */
    public static final String OPERATION_RECORD_MODIFY_NOT_CHECK = "2";//修改申请未审核

    public static final String OPERATION_RECORD_MODIFY_NOT_PASSED = "3";//修改申请审核不通过

    public static final String OPERATION_RECORD_MODIFY_PASSED = "4";//修改申请审核通过
}
