package com.zzu.gfms.adapter;

import android.support.v7.widget.RecyclerView;
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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_operation_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OperationRecord operationRecord = operationRecords.get(position);
        switch (operationRecord.getConvertState()){
            case ConvertState.OPERATION_RECORD_MODIFY_NOT_CHECK:
                holder.titleLayout.setBackgroundResource(R.color.bg_not_check);
                holder.statusImage.setImageResource(R.mipmap.icon_not_check);
                holder.statusText.setText("待审核");
                break;
            case ConvertState.OPERATION_RECORD_MODIFY_NOT_PASSED:
                holder.titleLayout.setBackgroundResource(R.color.bg_not_pass);
                holder.statusImage.setImageResource(R.mipmap.icon_not_pass);
                holder.statusText.setText("未通过");
                break;
            case ConvertState.OPERATION_RECORD_MODIFY_PASSED:
                holder.titleLayout.setBackgroundResource(R.color.bg_pass);
                holder.statusImage.setImageResource(R.mipmap.icon_pass);
                holder.statusText.setText("已通过");
                break;
        }

//        holder.applyDateText.setText("申请时间：" + operationRecord.getApplyTime());
//        holder.workDateText.setText("工作日期：" + operationRecord.get);

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
        private ImageView arrowImage;
        private TextView workDateText;
        private TextView workCountText;

        ViewHolder(View itemView) {
            super(itemView);
            titleLayout = (RelativeLayout) itemView.findViewById(R.id.layout_title);
            statusImage = (ImageView) itemView.findViewById(R.id.image_status);
            statusText = (TextView) itemView.findViewById(R.id.text_status);
            applyDateText = (TextView) itemView.findViewById(R.id.text_apply_date);
            arrowImage = (ImageView) itemView.findViewById(R.id.image_arrow);
            workDateText = (TextView) itemView.findViewById(R.id.text_work_date);
            workCountText = (TextView) itemView.findViewById(R.id.text_work_count);
        }
    }
}
