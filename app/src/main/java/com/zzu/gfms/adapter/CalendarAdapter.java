package com.zzu.gfms.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.gfms.R;
import com.zzu.gfms.bean.Day;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-10-12
 * Time:11:16
 * Summary:
 */

public class CalendarAdapter extends BaseAdapter {


    private List<Day> allDays;

    public CalendarAdapter(List<Day> allDays){
        this.allDays = allDays;
    }


    @Override
    public int getCount() {
        return allDays.size();
    }

    @Override
    public Object getItem(int position) {
        return allDays.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_picker_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.day = (TextView) convertView.findViewById(R.id.text_day);
            viewHolder.right = (ImageView) convertView.findViewById(R.id.image_right);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.day.setText(String.valueOf(allDays.get(position).getDay()));

        Day day = allDays.get(position);

        if (!day.isCurrentMonth()) {//非本月的几天设置为灰色
            viewHolder.day.setTextColor(Color.rgb(204, 204, 204));
        } else {//本月的判断是否为周末
            if ((position+1)%7 == 0 || (position+2)%7 == 0){//周六周日为红色
                viewHolder.day.setTextColor(Color.RED);
            }else {
                viewHolder.day.setTextColor(Color.BLACK);
            }

            if (day.isToday()){//给今天设置背景
                viewHolder.day.setBackgroundResource(R.drawable.round_bg_blue);
            }else {
                viewHolder.day.setBackgroundColor(Color.WHITE);
            }
        }

        if (day.isHasWorkRecord()){
            viewHolder.right.setVisibility(View.VISIBLE);
        }else {
            viewHolder.right.setVisibility(View.GONE);
        }
        return convertView;
    }

    private class ViewHolder{
        TextView day;
        ImageView right;
    }
}
