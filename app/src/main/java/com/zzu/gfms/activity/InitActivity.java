package com.zzu.gfms.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.zzu.gfms.R;
import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.domain.GetWorkerUseCase;
import com.zzu.gfms.domain.LoginUseCase;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.Constants;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class InitActivity extends AppCompatActivity {

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSystemUIVisible(false);
        setContentView(R.layout.activity_init);
        String userName = DataRepository.getUserName();
        String password = DataRepository.getPassword();

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(InitActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1000);
        }else {
            new LoginUseCase().login(userName, password)
                    .execute(new Observer<Worker>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            disposable = d;
                        }

                        @Override
                        public void onNext(Worker worker) {
                            startActivity(new Intent(InitActivity.this, MainActivity.class));
                            ConstantUtil.worker = worker;
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            startActivity(new Intent(InitActivity.this, MainActivity.class));
                            finish();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }



//        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }else {
//            new GetWorkerUseCase(userName, password)
//                    .execute(new Observer<Worker>() {
//                        @Override
//                        public void onSubscribe(@NonNull Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(@NonNull Worker worker) {
//                            if (worker != null){
//                                ConstantUtil.worker = worker;
//                                startActivity(new Intent(InitActivity.this, MainActivity.class));
//                            }else {
//                                startActivity(new Intent(InitActivity.this, LoginActivity.class));
//                            }
//                            finish();
//                        }
//
//                        @Override
//                        public void onError(@NonNull Throwable e) {
//                            startActivity(new Intent(InitActivity.this, LoginActivity.class));
//                            finish();
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        }
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
