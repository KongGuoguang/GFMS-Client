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
import com.zzu.gfms.data.utils.ConvertState;
import com.zzu.gfms.utils.ViewUtil;
/**
 * Author:kongguoguang
 * Date:2017-12-13
 * Time:17:18
 * Summary:
 */

public class ConvertStatePicker extends FrameLayout implements View.OnClickListener{

    private String convertState;

    private String[] convertStates = new String[]{
            ConvertState.OPERATION_RECORD_ALL,
            ConvertState.OPERATION_RECORD_MODIFY_NOT_CHECK,
            ConvertState.OPERATION_RECORD_MODIFY_NOT_PASSED,
            ConvertState.OPERATION_RECORD_MODIFY_PASSED
    };

    private String[] convertStateNames = new String[]{"全部", "待审核", "未通过", "已通过"};

    private OnConvertStateSelectedListener onConvertStateSelectedListener;

    private NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (onConvertStateSelectedListener != null){
                onConvertStateSelectedListener.onConvertStateSelected(ConvertStatePicker.this, convertStates[newVal]);
            }

            convertState = convertStates[newVal];
        }
    };

    private OnButtonClickedListener onButtonClickedListener;

    public void setOnButtonClickedListener(OnButtonClickedListener onButtonClickedListener) {
        this.onButtonClickedListener = onButtonClickedListener;
    }

    public ConvertStatePicker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ConvertStatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ConvertStatePicker(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_work_type_picker, this, true);
        NumberPicker numberPicker = (NumberPicker) findViewById(R.id.picker);
        numberPicker.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        ViewUtil.setNumberPickerDividerColor(context, numberPicker);
        numberPicker.setOnValueChangedListener(onValueChangeListener);
        numberPicker.setDisplayedValues(convertStateNames);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(convertStateNames.length -1);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setValue(0);
        if (onConvertStateSelectedListener != null){
            onConvertStateSelectedListener.onConvertStateSelected(this, convertStates[0]);
        }

        TextView confirm = findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(this);

        TextView cancel = findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);

        TextView reset = findViewById(R.id.tv_reset);
        reset.setOnClickListener(this);
    }

    public void setOnConvertStateSelectedListener(OnConvertStateSelectedListener onConvertStateSelectedListener) {
        this.onConvertStateSelectedListener = onConvertStateSelectedListener;
        if (onConvertStateSelectedListener != null){
            onConvertStateSelectedListener.onConvertStateSelected(this, convertStates[0]);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_confirm:
                onButtonClickedListener.onConfirm(convertState);
                break;
            case R.id.tv_cancel:
                onButtonClickedListener.onCancel();
                break;
            case R.id.tv_reset:
                onButtonClickedListener.onReset();
                break;
        }
    }

    public interface OnConvertStateSelectedListener{
        void onConvertStateSelected(ConvertStatePicker convertStatePicker,String convertState);
    }

    public interface OnButtonClickedListener{

        void onConfirm(String convertState);

        void onCancel();

        void onReset();
    }
}
