package com.zzu.gfms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.zzu.gfms.R;

/**
 * Author:kongguoguang
 * Date:2017-10-12
 * Time:10:43
 * Summary:
 */

public class CalendarView extends FrameLayout{

    private LayoutInflater mLayoutInflater;
    private GridView gridView;

    public CalendarView(@NonNull Context context) {
        this(context, null);
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        init();
    }


    private void init(){

        mLayoutInflater.inflate(R.layout.calendar_picker_layout, this, true);
        gridView = (GridView) findViewById(R.id.grid_view);
    }

    public void setAdapter(ListAdapter adapter){
        gridView.setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener){
        gridView.setOnItemClickListener(onItemClickListener);
    }

}
