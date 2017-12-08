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

import com.qmuiteam.qmui.widget.QMUITopBar;
import com.zzu.gfms.R;
import com.zzu.gfms.activity.LoginActivity;
import com.zzu.gfms.data.DataRepository;
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

        QMUITopBar topBar = (QMUITopBar) view.findViewById(R.id.top_bar);
        topBar.setTitle("我的信息");

        TextView loginName = (TextView) view.findViewById(R.id.text_login_name);
        loginName.setText(ConstantUtil.worker.getUserName());

        TextView name = (TextView) view.findViewById(R.id.text_name);
        name.setText(ConstantUtil.worker.getName());

        TextView sex = (TextView) view.findViewById(R.id.text_sex);
        sex.setText(ConstantUtil.worker.getSex());

        ImageView modifyPassword = (ImageView) view.findViewById(R.id.image_modify_password);
        modifyPassword.setOnClickListener(this);

        ImageView notificationMessage = (ImageView) view.findViewById(R.id.image_notification_mesage);
        notificationMessage.setOnClickListener(this);

        ImageView aboutAndHelp = (ImageView) view.findViewById(R.id.image_about_and_help);
        aboutAndHelp.setOnClickListener(this);

        TextView logout = (TextView) view.findViewById(R.id.text_logout);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_modify_password:
                modifyPassword();
                break;
            case R.id.image_notification_mesage:
                break;
            case R.id.image_about_and_help:
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

    }

    /**
     * 退出登录
     */
    private void logout(){
        DataRepository.clearPassword();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
}
