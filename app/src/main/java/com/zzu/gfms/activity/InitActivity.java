package com.zzu.gfms.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zzu.gfms.R;
import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.domain.LoginUseCase;
import com.zzu.gfms.utils.ConstantUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class InitActivity extends AppCompatActivity {

    private Disposable disposable;

    private boolean hasTryLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userName = DataRepository.getUserName();
        String password = DataRepository.getPassword();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            startActivity(new Intent(InitActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setSystemUIVisible(false);
        setContentView(R.layout.activity_init);

        new LoginUseCase().login(userName, password)
                .execute(new Observer<Worker>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(Worker worker) {
                        if (hasTryLogin) return;
                        hasTryLogin = true;
                        startActivity(new Intent(InitActivity.this, MainActivity.class));
                        ConstantUtil.worker = worker;
                        finish();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (hasTryLogin) return;
                        hasTryLogin = true;
                        startActivity(new Intent(InitActivity.this, MainActivity.class));
                        finish();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (hasTryLogin) return;
                hasTryLogin = true;
                if (disposable != null && !disposable.isDisposed()) disposable.dispose();
                startActivity(new Intent(InitActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }

    private void setSystemUIVisible(boolean show) {
        if (show) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            uiFlags |= 0x00001000;
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }
}
