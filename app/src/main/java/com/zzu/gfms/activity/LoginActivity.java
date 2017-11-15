package com.zzu.gfms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.zzu.gfms.R;
import com.zzu.gfms.app.BaseActivity;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.domain.LoginUseCase;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.ExceptionUtil;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends BaseActivity {

    private EditText userNameEdit;

    private EditText passwordEdit;

    private QMUITipDialog loading;

    private Observer<Worker> loginObserver;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        userNameEdit = (EditText) findViewById(R.id.edit_text_user_name);
        userNameEdit.setText("kongguoguang");
        passwordEdit = (EditText) findViewById(R.id.edit_text_password);
        passwordEdit.setText("123456");
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                login();
                return false;
            }
        });
        loading = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在登录")
                .create();
    }

    public void onClick(View view) {
        login();
    }

    private void login(){
        userNameEdit.clearFocus();
        passwordEdit.clearFocus();
        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            showErrorDialog("用户名和密码不能为空");
        }else {
            loading.show();
            new LoginUseCase().login(username, password).execute(getLoginObserver());
        }
    }

    private Observer<Worker> getLoginObserver(){
        if (loginObserver == null){
            loginObserver = new Observer<Worker>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(@NonNull Worker worker) {
                    loading.dismiss();
                    ConstantUtil.worker = worker;
                    worker.async().save();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    loading.dismiss();
                    showErrorDialog(ExceptionUtil.parseErrorMessage(e));
                }

                @Override
                public void onComplete() {

                }
            };
        }

        return loginObserver;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
