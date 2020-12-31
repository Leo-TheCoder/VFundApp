package com.example.android.vfund.controller.LoginSignupController;

import android.os.Bundle;
import android.util.Log;
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

public class RegisterFragment extends Fragment implements View.OnClickListener{
    private static RegisterFragment registerFragment = null;
    private LoginSignupActivity hostActivity;
    Button btnRegister;
    TextView txtLogin, txtHelp;
    EditText editEmail, editPassword, editPasswordConfirm;

    public static RegisterFragment getInstance() {
        if(registerFragment == null) {
            registerFragment = new RegisterFragment();
        }
        return registerFragment;
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnRegister=(Button)view.findViewById(R.id.btnConfirmRegister);
        btnRegister.setOnClickListener(this);

        txtLogin = (TextView)view.findViewById(R.id.txtLoginFromRegister);
        txtLogin.setOnClickListener(this);

        txtHelp = (TextView)view.findViewById(R.id.txtHelpFromRegister);
        txtHelp.setOnClickListener(this);

        editEmail=(EditText)view.findViewById(R.id.edit_registerEmailPhone);
        editPassword= (EditText)view.findViewById(R.id.edit_password);
        editPasswordConfirm =(EditText)view.findViewById(R.id.edit_confirmPassword);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btnRegister.getId()){

        }
        else if(v.getId() == txtLogin.getId()) {
            Bundle bundle = new Bundle();
            String navigate = "navLogin";
            bundle.putString("navigate", navigate);
            hostActivity.onMsgFromFragmentToMain("registerFrag", bundle);
        }
        else if(v.getId() == txtHelp.getId()) {

        }
        else {
            //DO NOTHING
        }
    }

}
