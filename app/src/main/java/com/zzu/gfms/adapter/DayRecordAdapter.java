package com.zzu.gfms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.DayRecord;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-12-09
 * Time:14:52
 * Summary:
 */

public class DayRecordAdapter extends RecyclerView.Adapter<DayRecordAdapter.ViewHolder>{

    private List<DayRecord> dayRecords;

    private OnRightArrowClickListener onRightArrowClickListener;

    public void setOnRightArrowClickListener(OnRightArrowClickListener onRightArrowClickListener) {
        this.onRightArrowClickListener = onRightArrowClickListener;
    }

    public DayRecordAdapter(List<DayRecord> dayRecords){
        this.dayRecords = dayRecords;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DayRecord dayRecord = dayRecords.get(position);

        holder.workDate.setText(dayRecord.getDay());
        holder.workCount.setText(dayRecord.getTotal());
        holder.rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRightArrowClickListener != null){
                    String[] array = dayRecord.getDay().split("-");
                    int year = Integer.parseInt(array[0]);
                    int month = Integer.parseInt(array[1]);
                    int day = Integer.parseInt(array[2]);
                    onRightArrowClickListener.onClick(year, month, day, dayRecord.getDayRecordID(),
                            dayRecord.getConvertState());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayRecords == null ? 0 : dayRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView workDate;

        TextView workCount;

        ImageView rightArrow;


        public ViewHolder(View itemView) {
            super(itemView);
            workDate = (TextView) itemView.findViewById(R.id.text_work_date);
            workCount = (TextView) itemView.findViewById(R.id.text_work_count);
            rightArrow = (ImageView) itemView.findViewById(R.id.image_right_arrow);
        }
    }

    public interface OnRightArrowClickListener{
        void onClick(int year, int month, int day, String dayRecordID, String convertState);
    }
}
