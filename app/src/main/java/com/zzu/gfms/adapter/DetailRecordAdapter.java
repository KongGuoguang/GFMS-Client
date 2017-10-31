package com.zzu.gfms.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.utils.ConstantUtil;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-10-31
 * Time:13:59
 * Summary:
 */

public class DetailRecordAdapter extends RecyclerView.Adapter<DetailRecordAdapter.ViewHolder> {

    private List<DetailRecord> detailRecords;

    private DeleteDetailRecordListener listener;

    public DetailRecordAdapter(List<DetailRecord> detailRecords){
        this.detailRecords = detailRecords;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_record, parent, false);
        return new ViewHolder(view);
    }

    public void setDeleteDetailRecordListener(DeleteDetailRecordListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DetailRecord detailRecord = detailRecords.get(position);
        holder.workType.setText("工作类型：" + ConstantUtil.getWorkTypeName(detailRecord.getWorkTypeID()));
        holder.clothesType.setText("衣服类型：" + ConstantUtil.getClothesTypeName(detailRecord.getClothesID()));
        holder.count.setText("完成量：" + detailRecord.getCount());
        holder.dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDetailRecordDelete(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return detailRecords.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView workType;
        TextView clothesType;
        TextView count;
        ImageView dustbin;

        public ViewHolder(View itemView) {
            super(itemView);
            workType = (TextView) itemView.findViewById(R.id.text_work_type);
            clothesType = (TextView) itemView.findViewById(R.id.text_clothes_type);
            count = (TextView) itemView.findViewById(R.id.text_count);
            dustbin = (ImageView) itemView.findViewById(R.id.image_dustbin);
        }
    }
}
