package com.zzu.gfms.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.zzu.gfms.R;
import com.zzu.gfms.activity.LoginActivity;
import com.zzu.gfms.activity.ModifyPwdActivity;
import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.domain.LogoutUseCase;
import com.zzu.gfms.utils.ConstantUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener{


    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        QMUITopBar topBar = view.findViewById(R.id.top_bar);
        topBar.setTitle("我的信息");

        TextView loginName = view.findViewById(R.id.text_login_name);
        loginName.setText(ConstantUtil.worker.getUserName());

        TextView name = view.findViewById(R.id.text_name);
        name.setText(ConstantUtil.worker.getName());

        TextView sex = view.findViewById(R.id.text_sex);
        sex.setText(ConstantUtil.worker.getSex());

        View modifyPassword = view.findViewById(R.id.view_modify_password);
        modifyPassword.setOnClickListener(this);

        View notificationMessage = view.findViewById(R.id.view_notification_mesage);
        notificationMessage.setOnClickListener(this);

        View aboutAndHelp = view.findViewById(R.id.view_about_and_help);
        aboutAndHelp.setOnClickListener(this);

        TextView logout = view.findViewById(R.id.text_logout);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_modify_password:
                modifyPassword();
                break;
            case R.id.view_notification_mesage:
                break;
            case R.id.view_about_and_help:
                break;
            case R.id.text_logout:
                logout();
                break;
        }
    }

    /**
     * 修改密码
     */
    private void modifyPassword(){
        startActivity(new Intent(getActivity(), ModifyPwdActivity.class));
    }

    /**
     * 退出登录
     */
    private void logout(){
        new QMUIDialog.MessageDialogBuilder(getActivity())
                .setTitle("提示")
                .setMessage("确定要退出登录吗？")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "退出", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        new LogoutUseCase().logout(ConstantUtil.worker.getWorkerID()).execute();
                        dialog.dismiss();
                        DataRepository.clearPassword();
                        ConstantUtil.worker = null;
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                })
                .show();
    }
}
