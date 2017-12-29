package com.zzu.gfms.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.OperationRecord;
import com.zzu.gfms.data.utils.ConvertState;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-11-15
 * Time:16:30
 * Summary:
 */

public class OperationRecordAdapter extends RecyclerView.Adapter<OperationRecordAdapter.ViewHolder>{

    private List<OperationRecord> operationRecords;

    public OperationRecordAdapter(List<OperationRecord> operationRecords){
        this.operationRecords = operationRecords;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operation_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        OperationRecord operationRecord = operationRecords.get(holder.getAdapterPosition());
        String convertState = operationRecord.getConvertState();
        switch (convertState){
            case ConvertState.OPERATION_RECORD_MODIFY_NOT_CHECK:
                holder.titleLayout.setBackgroundResource(R.color.bg_not_check);
                holder.statusImage.setImageResource(R.mipmap.icon_not_check);
                break;
            case ConvertState.OPERATION_RECORD_MODIFY_NOT_PASSED:
                holder.titleLayout.setBackgroundResource(R.color.bg_not_pass);
                holder.statusImage.setImageResource(R.mipmap.icon_not_pass);
                break;
            case ConvertState.OPERATION_RECORD_MODIFY_PASSED:
                holder.titleLayout.setBackgroundResource(R.color.bg_pass);
                holder.statusImage.setImageResource(R.mipmap.icon_pass);

                break;
        }

        holder.statusText.setText(ConvertState.getConvertStateName(convertState));

        String applyDate = operationRecord.getApplyTime();
        if (!TextUtils.isEmpty(applyDate) && applyDate.length() >= 10){
            applyDate = applyDate.substring(0, 10);
        }
        applyDate = "申请日期：" +  applyDate;
        holder.applyDateText.setText(applyDate);

        holder.applyDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClickListener(holder.getAdapterPosition());
                }
            }
        });

        String workDate = "工作日期：" + operationRecord.getDay();
        holder.workDateText.setText(workDate);

        String workCount = "完成总量：" + operationRecord.getTotal() + "件";
        holder.workCountText.setText(workCount);

    }

    @Override
    public int getItemCount() {
        if (operationRecords == null || operationRecords.size() < 1){
            return 0;
        }else {
            return operationRecords.size();
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private RelativeLayout titleLayout;
        private ImageView statusImage;
        private TextView statusText;
        private TextView applyDateText;
        private TextView workDateText;
        private TextView workCountText;

        ViewHolder(View itemView) {
            super(itemView);
            titleLayout = (RelativeLayout) itemView.findViewById(R.id.layout_title);
            statusImage = (ImageView) itemView.findViewById(R.id.image_state);
            statusText = (TextView) itemView.findViewById(R.id.text_state);
            applyDateText = (TextView) itemView.findViewById(R.id.text_apply_date);
            workDateText = (TextView) itemView.findViewById(R.id.text_work_date);
            workCountText = (TextView) itemView.findViewById(R.id.text_work_count);
        }
    }

    public interface OnItemClickListener{
        void onItemClickListener(int position);
    }
}
