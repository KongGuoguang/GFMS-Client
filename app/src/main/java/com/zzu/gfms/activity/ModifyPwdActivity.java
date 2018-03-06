package com.zzu.gfms.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zzu.gfms.R;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.domain.ModifyPwdUseCase;
import com.zzu.gfms.event.ModifyPwdSuccess;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.ExceptionUtil;
import com.zzu.gfms.utils.ViewUtil;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ModifyPwdActivity extends BaseActivity {

    /**
     * 输入原密码编辑框
     */
    private EditText oldPwdEdTx;
    /**
     * 输入新密码编辑框
     */
    private EditText newPwdEdTx;
    /**
     * 输入确认新密码编辑框
     */
    private EditText confirmPwdEdTx;

    private TextView confirmButton;

    private QMUITipDialog loading;

    private ModifyPwdUseCase modifyPwdUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        initView();
        modifyPwdUseCase = new ModifyPwdUseCase();
    }

    private void initView(){
        QMUITopBar topBar = findViewById(R.id.top_bar);
        topBar.setTitle("修改密码");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        oldPwdEdTx = findViewById(R.id.edit_old_pwd);

        newPwdEdTx = findViewById(R.id.edit_new_pwd);
        newPwdEdTx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    handleInput(0);
                }
            }
        });
        newPwdEdTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length < 6){
                    ViewUtil.setViewEnable(confirmButton, false);
                    return;
                }

                Editable editable = confirmPwdEdTx.getText();
                if (editable.length() == length){
                    ViewUtil.setViewEnable(confirmButton, true);
                }else {
                    ViewUtil.setViewEnable(confirmButton, false);
                }
            }
        });

        confirmPwdEdTx = findViewById(R.id.edit_confirm_pwd);
        confirmPwdEdTx.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    handleInput(1);
                }
            }
        });
        confirmPwdEdTx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length < 6){
                    ViewUtil.setViewEnable(confirmButton, false);
                    return;
                }

                Editable editable = newPwdEdTx.getText();
                if (editable.length() == length){
                    ViewUtil.setViewEnable(confirmButton, true);
                }else {
                    ViewUtil.setViewEnable(confirmButton, false);
                }
            }
        });

        confirmButton = findViewById(R.id.text_confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyPwd();
            }
        });
        ViewUtil.setViewEnable(confirmButton, false);

        loading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在修改...")
                .create();
    }


    private int handleInput(int type){
        switch(type){
            // 原密码的处理
            case 0:
                if (oldPwdEdTx.getText().length() == 0){
                    showToast("原密码不能为空！");
                    clearFocus();
                    oldPwdEdTx.requestFocus();
                    return -1;
                }
                break;
            // 新密码的处理
            case 1:
                if (newPwdEdTx.getText().length() == 0){
                    showToast("新密码不能为空");
                    clearFocus();
                    newPwdEdTx.requestFocus();
                    return -1;
                }
                if (newPwdEdTx.getText().length() < 6 ) {
                    showToast("密码由6-20位字母、数字或其组合组成");
                    clearFocus();
                    //newPwdEdTx.setText("");
                    newPwdEdTx.requestFocus();
                    return -1;
                }
                break;
            // 确认密码的处理
            case 2:
                if (confirmPwdEdTx.getText().length() == 0){
                    showToast("新密码不能为空");
                    clearFocus();
                    confirmPwdEdTx.requestFocus();
                    return -1;
                }
                if (confirmPwdEdTx.getText().length() < 6 ) {
                    showToast("密码由6-20位字母、数字或其组合组成");
                    clearFocus();
                    //confirmPwdEdTx.setText("");
                    confirmPwdEdTx.requestFocus();
                    return -1;
                }
                if ( !confirmPwdEdTx.getText().toString().equals(newPwdEdTx.getText().toString())){
                    showToast("新密码两次输入不一致");
                    clearFocus();
                    //confirmPwdEdTx.setText("");
                    confirmPwdEdTx.requestFocus();
                    return -1;
                }
                break;

        }
        return 0;
    }

    private void clearFocus(){
        oldPwdEdTx.clearFocus();
        newPwdEdTx.clearFocus();
        confirmPwdEdTx.clearFocus();
    }

    private void modifyPwd(){
        if (handleInput(0) == -1){
            return;
        }
        if (handleInput(1) == -1){
            return;
        }
        if (handleInput(2) == -1){
            return;
        }
        loading.show();

        modifyPwdUseCase
                .modify(ConstantUtil.worker.getWorkerID(), oldPwdEdTx.getText().toString(),
                        newPwdEdTx.getText().toString())
                .execute(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        loading.dismiss();
                        showToast("密码修改成功，请重新登录");
                        DataRepository.clearPassword();
                        ConstantUtil.worker = null;
                        finish();
                        EventBus.getDefault().post(new ModifyPwdSuccess());
                    }

                    @Override
                    public void onError(Throwable e) {
                        loading.dismiss();
                        showErrorDialog(ExceptionUtil.parseErrorMessage(e));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
