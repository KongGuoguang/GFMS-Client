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

    private OnDeleteListener onDeleteListener;

    private OnModifyListener onModifyListener;

    private boolean deleteable;

    public DetailRecordAdapter(List<DetailRecord> detailRecords, boolean deleteable){
        this.detailRecords = detailRecords;
        this.deleteable = deleteable;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_record, parent, false);
        return new ViewHolder(view);
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public void setOnModifyListener(OnModifyListener onModifyListener){
        this.onModifyListener = onModifyListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        DetailRecord detailRecord = detailRecords.get(holder.getAdapterPosition());

        String workType = "工作类型：" + ConstantUtil.getWorkName(detailRecord.getWorkTypeID());
        holder.workType.setText(workType);

        String clothesType = "衣服类型：" + ConstantUtil.getClothesName(detailRecord.getClothesID());
        holder.clothesType.setText(clothesType);

        String count = "完成量：" + detailRecord.getCount() + "件";
        holder.count.setText(count);

        holder.dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteListener != null){
                    onDeleteListener.onDelete(holder.getAdapterPosition());
                }
            }
        });

        holder.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onModifyListener != null){
                    onModifyListener.onModify(holder.getAdapterPosition());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return detailRecords.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView workType;
        TextView clothesType;
        TextView count;
        ImageView dustbin;

        ViewHolder(View itemView) {
            super(itemView);
            workType = (TextView) itemView.findViewById(R.id.text_work_type);
            clothesType = (TextView) itemView.findViewById(R.id.text_clothes_type);
            count = (TextView) itemView.findViewById(R.id.text_count);
            dustbin = (ImageView) itemView.findViewById(R.id.image_dustbin);
            if (deleteable){
                dustbin.setVisibility(View.VISIBLE);
            }else {
                dustbin.setVisibility(View.GONE);
            }
        }
    }

    public interface OnDeleteListener{
        void onDelete(int position);
    }

    public interface OnModifyListener{
        void onModify(int position);
    }
}
