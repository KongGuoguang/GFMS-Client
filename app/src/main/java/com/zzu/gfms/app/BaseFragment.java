package com.zzu.gfms.app;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/**
 * Author:kongguoguang
 * Date:2017-11-01
 * Time:15:38
 * Summary:
 */

public class BaseFragment extends Fragment {

    protected void showErrorDialog(String message){
        new QMUIDialog.MessageDialogBuilder(getActivity())
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

    protected void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
