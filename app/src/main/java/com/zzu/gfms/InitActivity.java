package com.zzu.gfms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.domain.GetWorkerUseCase;
import com.zzu.gfms.utils.ConstantUtil;
import com.zzu.gfms.utils.Constants;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_init);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userName = preferences.getString(Constants.USER_NAME, "");
        String password = preferences.getString(Constants.PASSWORD, "");

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }else {
            new GetWorkerUseCase(userName, password)
                    .execute(new Observer<Worker>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull Worker worker) {
                            if (worker != null){
                                ConstantUtil.worker = worker;
                                startActivity(new Intent(InitActivity.this, MainActivity.class));
                            }else {
                                startActivity(new Intent(InitActivity.this, LoginActivity.class));
                            }
                            finish();
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            startActivity(new Intent(InitActivity.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }
}
