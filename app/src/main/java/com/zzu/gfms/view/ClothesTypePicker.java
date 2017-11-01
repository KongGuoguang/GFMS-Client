package com.zzu.gfms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.zzu.gfms.R;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.utils.ConstantUtil;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-11-01
 * Time:10:27
 * Summary:
 */

public class ClothesTypePicker extends FrameLayout {

    private NumberPicker fatherPicker;

    private NumberPicker childPicker;

    private List<ClothesType> fatherClothesTypes;

    private String[] fatherClothesNames;

    private List<ClothesType> childClothesTypes;

    private String[] childClothesNames;

    private OnClothesSelectedListener onClothesSelectedListener;

    public ClothesTypePicker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ClothesTypePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater.from(context).inflate(R.layout.view_clothes_type_picker,this, true);
        fatherPicker = (NumberPicker) findViewById(R.id.picker_father);
        fatherPicker.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        childPicker = (NumberPicker) findViewById(R.id.picker_child);
        childPicker.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        childPicker.setOnValueChangedListener(onValueChangeListener);
        initFatherPicker();
    }

    private void initFatherPicker(){
        fatherClothesTypes = ConstantUtil.getChildClothesType(0);
        int size = fatherClothesTypes.size();
        if (size > 0){
            fatherClothesNames = new String[size];
            for (int i = 0; i < size; i++){
                fatherClothesNames[i] = fatherClothesTypes.get(i).getName();
            }
            fatherPicker.setOnValueChangedListener(onValueChangeListener);
            fatherPicker.setDisplayedValues(fatherClothesNames);
            fatherPicker.setMinValue(0);
            fatherPicker.setMaxValue(size -1);
            fatherPicker.setValue(0);
            initChildPicker(0);
        }
    }

    private void initChildPicker(int position){
        childClothesTypes = ConstantUtil.getChildClothesType(fatherClothesTypes.get(position).getClothesID());
        int size = childClothesTypes.size();
        if (size > 0){
            childClothesNames = new String[size];
            for (int i = 0; i < size; i++){
                childClothesNames[i] = childClothesTypes.get(i).getName();
            }
            childPicker.setDisplayedValues(childClothesNames);
            childPicker.setMinValue(0);
            childPicker.setMaxValue(size -1);
            childPicker.setValue(0);
            if (onClothesSelectedListener != null)
                onClothesSelectedListener.onClothesSelected(childClothesTypes.get(0));
        }
    }

    private NumberPicker.OnValueChangeListener onValueChangeListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            if (picker == fatherPicker){
                initChildPicker(newVal);
            }

            if (picker == childPicker && onClothesSelectedListener != null){
                onClothesSelectedListener.onClothesSelected(childClothesTypes.get(newVal));
            }

        }
    };

    public interface OnClothesSelectedListener{
        void onClothesSelected(ClothesType clothesType);
    }

    public void setOnClothesSelectedListener(OnClothesSelectedListener listener){
        onClothesSelectedListener = listener;
        if (childClothesTypes != null && childClothesTypes.size() > 0 &&
                onClothesSelectedListener != null){
            onClothesSelectedListener.onClothesSelected(childClothesTypes.get(0));
        }
    }
}
