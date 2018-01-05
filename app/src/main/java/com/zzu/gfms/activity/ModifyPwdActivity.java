package com.zzu.gfms.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.R;
import com.zzu.gfms.app.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        initView();
    }

    private void initView(){
        QMUITopBar topBar = (QMUITopBar) findViewById(R.id.top_bar);
        topBar.setTitle("修改密码");
        topBar.addLeftBackImageButton()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        oldPwdEdTx = (EditText) findViewById(R.id.edit_old_pwd);

        newPwdEdTx = (EditText) findViewById(R.id.edit_new_pwd);

        confirmPwdEdTx = (EditText) findViewById(R.id.edit_confirm_pwd);
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
                    newPwdEdTx.setText("");
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
                    confirmPwdEdTx.setText("");
                    confirmPwdEdTx.requestFocus();
                    return -1;
                }
                if ( !confirmPwdEdTx.getText().toString().equals(newPwdEdTx.getText().toString())){
                    showToast("新密码两次输入不一致");
                    clearFocus();
                    confirmPwdEdTx.setText("");
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
}
