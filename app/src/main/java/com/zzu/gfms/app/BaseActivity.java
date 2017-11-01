package com.zzu.gfms.app;

import android.support.v7.app.AppCompatActivity;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/**
 * Author:kongguoguang
 * Date:2017-11-01
 * Time:15:36
 * Summary:
 */

public class BaseActivity extends AppCompatActivity {

    protected void showErrorDialog(String message){
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage(message)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
