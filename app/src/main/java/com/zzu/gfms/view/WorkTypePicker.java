package com.zzu.gfms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.ViewUtil;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-11-01
 * Time:14:25
 * Summary:
 */

public class WorkTypePicker extends FrameLayout implements View.OnClickListener{

    private List<WorkType> workTypes;

    private OnWorkSelectedListener onWorkSelectedListener;

    private WorkType workType;

    private OnButtonClickedListener onButtonClickedListener;

    public void setOnButtonClickedListener(OnButtonClickedListener onButtonClickedListener) {
        this.onButtonClickedListener = onButtonClickedListener;
    }

    public WorkTypePicker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public WorkTypePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_work_type_picker, this, true);
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.picker);
        numberPicker.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        ViewUtil.setNumberPickerDividerColor(context, numberPicker);
        workTypes = ConstantUtil.workTypes;
        if (workTypes != null){
            int size = workTypes.size();
            if (size > 0){
                String[] workNames = new String[size];
                for (int i = 0; i < size; i++){
                    workNames[i] = workTypes.get(i).getName();
                }
                numberPicker.setOnValueChangedListener(onValueChangeListener);
                numberPicker.setDisplayedValues(workNames);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(size -1);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setValue(0);
                workType = workTypes.get(0);
                if (onWorkSelectedListener != null){
                    onWorkSelectedListener.onWorkSelected(this, workTypes.get(0));
                }
            }
        }

        TextView confirm = findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(this);

        TextView cancel = findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);

        TextView reset = findViewById(R.id.tv_reset);
        reset.setOnClickListener(this);

    }

    public void setOnWorkSelectedListener(OnWorkSelectedListener listener){
        onWorkSelectedListener = listener;
        if (onWorkSelectedListener != null && workTypes != null && workTypes.size() > 0){
            onWorkSelectedListener.onWorkSelected(this, workTypes.get(0));
        }
    }

    private NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (onWorkSelectedListener != null){
                onWorkSelectedListener.onWorkSelected(WorkTypePicker.this, workTypes.get(newVal));
            }

            workType = workTypes.get(newVal);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                onButtonClickedListener.onConfirm(workType);
                break;
            case R.id.tv_cancel:
                onButtonClickedListener.onCancel();
                break;
            case R.id.tv_reset:
                onButtonClickedListener.onReset();
                break;
        }
    }

    public interface OnWorkSelectedListener{
        void onWorkSelected(WorkTypePicker workTypePicker, WorkType workType);
    }

    public interface OnButtonClickedListener{

        void onConfirm(WorkType workType);

        void onCancel();

        void onReset();
    }
}
