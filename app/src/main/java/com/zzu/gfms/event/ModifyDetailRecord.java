package com.zzu.gfms.event;

/**
 * Created by Administrator on 2017/11/12.
 */

public class ModifyDetailRecord {
    private int position;

    private int count;

    public ModifyDetailRecord(int position, int count){
        this.position = position;
        this.count = count;
    }

    public int getPosition() {

        return position;
    }

    public int getCount() {
        return count;
    }
}
