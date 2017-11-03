package com.zzu.gfms;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
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

    private QMUITipDialog tipDialog;

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        userNameEdit = (EditText) findViewById(R.id.edit_text_user_name);
        passwordEdit = (EditText) findViewById(R.id.edit_text_password);
        tipDialog = new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在登录")
                .create();
    }

    public void onClick(View view) {

//        startActivity(new Intent(this, MainActivity.class));

        String username = userNameEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            showErrorDialog("用户名和密码不能为空");
        }else {
            tipDialog.show();
            new LoginUseCase().login(username, password)
                    .execute(new Observer<Worker>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable = d;
                }

                @Override
                public void onNext(@NonNull Worker worker) {
                    tipDialog.dismiss();
                    ConstantUtil.worker = worker;
                    worker.async().save();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    tipDialog.dismiss();
                    showErrorDialog(ExceptionUtil.parseErrorMessage(e));
                }

                @Override
                public void onComplete() {

                }
            });
        }


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }
}
