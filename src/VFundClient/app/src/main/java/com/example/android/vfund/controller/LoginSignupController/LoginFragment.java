package com.example.android.vfund.controller.LoginSignupController;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.vfund.R;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private static LoginFragment loginFragment = null;
    private LoginSignupActivity hostActivity;

    EditText editEmail, editPassword;
    Button btnConfirmLogin, btnFacebookLogin, btnGoogleLogin;
    TextView txtRegister, txtForgetPassword;

    public static LoginFragment getInstance() {
        if(loginFragment==null){
            loginFragment = new LoginFragment();
        }
        return loginFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hostActivity = (LoginSignupActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editEmail = (EditText)view.findViewById(R.id.edit_loginEmailPhone);
        editPassword = (EditText)view.findViewById(R.id.edit_loginPassword);

        btnConfirmLogin = (Button)view.findViewById(R.id.btnConfirmLogin);
        btnConfirmLogin.setOnClickListener(this);

        btnFacebookLogin = (Button)view.findViewById(R.id.btnFacebookLogin);
        btnFacebookLogin.setOnClickListener(this);

        btnGoogleLogin = (Button)view.findViewById(R.id.btnGoogleLogin);
        btnGoogleLogin.setOnClickListener(this);

        txtRegister = (TextView)view.findViewById(R.id.txtRegisterFromLogin);
        txtRegister.setOnClickListener(this);

        txtForgetPassword = (TextView)view.findViewById(R.id.txtForgetPassword);
        txtForgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==btnConfirmLogin.getId()){

        }
        else if(v.getId() == btnFacebookLogin.getId()) {

        }
        else if(v.getId() == btnGoogleLogin.getId()){

        }
        else if(v.getId() == txtRegister.getId()) {
            Bundle bundle = new Bundle();
            String navigate = "navRegister";
            bundle.putString("navigate", navigate);
            hostActivity.onMsgFromFragmentToMain("loginFrag", bundle);
        }
        else if(v.getId() == txtForgetPassword.getId()) {

        }
        else {
            //DO NOTHING
        }
    }
}
