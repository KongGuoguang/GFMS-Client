package com.zzu.gfms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.utils.ConstantUtil;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-11-01
 * Time:14:25
 * Summary:
 */

public class WorkTypePicker extends FrameLayout {

    private NumberPicker numberPicker;

    private List<WorkType> workTypes;

    private String[] workNames;

    private OnWorkSelectedListener onWorkSelectedListener;

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
        numberPicker = (NumberPicker) findViewById(R.id.picker);
        numberPicker.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        workTypes = ConstantUtil.workTypes;
        if (workTypes != null){
            int size = workTypes.size();
            if (size > 0){
                workNames = new String[size];
                for (int i = 0; i < size; i++){
                    workNames[i] = workTypes.get(i).getName();
                }
                numberPicker.setOnValueChangedListener(onValueChangeListener);
                numberPicker.setDisplayedValues(workNames);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(size -1);
                numberPicker.setValue(0);
                if (onWorkSelectedListener != null){
                    onWorkSelectedListener.onWorkSelected(workTypes.get(0));
                }
            }
        }

    }

    public void setOnWorkSelectedListener(OnWorkSelectedListener listener){
        onWorkSelectedListener = listener;
        if (onWorkSelectedListener != null && workTypes != null && workTypes.size() > 0){
            onWorkSelectedListener.onWorkSelected(workTypes.get(0));
        }
    }

    private NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (onWorkSelectedListener != null){
                onWorkSelectedListener.onWorkSelected(workTypes.get(newVal));
            }
        }
    };

    public interface OnWorkSelectedListener{
        void onWorkSelected(WorkType workType);
    }
}
