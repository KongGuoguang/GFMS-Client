package com.zzu.gfms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zzu.gfms.data.dbflow.Worker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Worker worker = new Worker();
        worker.setWorkerID(1);
        worker.setName("zhang");
    }

    public void onClick(View view) {
    }
}
