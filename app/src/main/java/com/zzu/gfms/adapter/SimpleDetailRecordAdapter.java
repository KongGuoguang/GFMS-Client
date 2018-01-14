package com.zzu.gfms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.utils.ConstantUtil;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-12-09
 * Time:14:52
 * Summary:
 */

public class SimpleDetailRecordAdapter extends RecyclerView.Adapter<SimpleDetailRecordAdapter.ViewHolder>{


    private List<DetailRecord> detailRecords;


    public SimpleDetailRecordAdapter(List<DetailRecord> detailRecords){
        this.detailRecords = detailRecords;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_detail_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DetailRecord detailRecord = detailRecords.get(position);

        holder.workDate.setText(detailRecord.getDay());
        holder.workCount.setText(detailRecord.getCount() + "件");
        holder.clothesType.setText(ConstantUtil.getClothesName(detailRecord.getClothesID()));
        holder.workType.setText(ConstantUtil.getWorkName(detailRecord.getWorkTypeID()));
    }

    @Override
    public int getItemCount() {
        return detailRecords == null ? 0 : detailRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView workDate;

        TextView workCount;

        TextView clothesType;

        TextView workType;

        ViewHolder(View itemView) {
            super(itemView);
            workDate = itemView.findViewById(R.id.text_work_date);
            workCount = itemView.findViewById(R.id.text_work_count);
            clothesType = itemView.findViewById(R.id.text_clothes_type);
            workType = itemView.findViewById(R.id.text_work_type);
        }
    }

}
