package com.example.android.vfund.controller.LoginSignupController;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.vfund.R;

public class WelcomeFragment extends Fragment implements View.OnClickListener {
    Button btnRegister, btnLogin;
    TextView txtAnonymous;
    private LoginSignupActivity hostActivity;
    private static WelcomeFragment welcomeFragment = null;

    public static WelcomeFragment getInstance() {
        if(welcomeFragment == null) {
            welcomeFragment = new WelcomeFragment();
        }
        return welcomeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hostActivity = (LoginSignupActivity)getActivity();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogin = (Button)view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnRegister = (Button)view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        txtAnonymous = (TextView)view.findViewById(R.id.txtAnonymous);
        txtAnonymous.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnLogin.getId()){
            Log.e("Welcome", "Login clicked");
            Bundle bundle = new Bundle();
            String navigate = "navLogin";
            bundle.putString("navigate", navigate);
            hostActivity.onMsgFromFragmentToMain("welcomeFrag", bundle);
        }
        else if(v.getId() == btnRegister.getId()) {
            Bundle bundle = new Bundle();
            String navigate = "navRegister";
            bundle.putString("navigate", navigate);
            hostActivity.onMsgFromFragmentToMain("welcomeFrag", bundle);
        }
        else if(v.getId() == txtAnonymous.getId()) {

        }
    }


}
