package com.example.android.vfund.controller.LoginSignupController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.vfund.R;

public class LoginSignupActivity extends AppCompatActivity implements ActivityCallBack{

    private FragmentManager fragmentManager;
    private FragmentTransaction ft;

    //Fragment welcome = 0
    //Fragment login = 1
    //Fragment register = 2
    private int state = 0;
    public static final String LOGIN_REQUEST_URL = "http://10.0.2.2:8080/api/users/getuser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);


        // Create an object of FragmentManager
        fragmentManager = this.getSupportFragmentManager();
        // Begin the transaction
        ft = fragmentManager.beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_intro_login_signup, WelcomeFragment.getInstance());
        //Commit
        ft.commit();
    }

    @Override
    public void onMsgFromFragmentToMain(String sender, Bundle bundle) {
        if(sender.equals("welcomeFrag")){
            String command = bundle.getString("navigate");
            if(command.equals("navRegister")){
                navigateRegisterFragment();
            }
            else if(command.equals("navLogin")) {
                navigateLoginFragment();
            }
        }
        else if(sender.equals("registerFrag")){
            String command = bundle.getString("navigate");
            if(command.equals("navWelcome")){
                navigateWelcomeFragment();
            }
            else if(command.equals("navLogin")) {
                navigateLoginFragment();
            }
        }
        else if(sender.equals("loginFrag")){
            String command = bundle.getString("navigate");
            if(command.equals("navWelcome")){
                navigateWelcomeFragment();
            }
            else if(command.equals("navRegister")) {
                navigateRegisterFragment();
            }
        }
    }

    private void navigateRegisterFragment(){
        ft = fragmentManager.beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_intro_login_signup, RegisterFragment.getInstance());
        //Commit
        ft.commit();
        state = 2;
    }

    private void navigateWelcomeFragment() {
        ft = fragmentManager.beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_intro_login_signup, WelcomeFragment.getInstance());
        //Commit
        ft.commit();
        state = 0;
    }
    private void navigateLoginFragment() {
        ft = fragmentManager.beginTransaction();
        // Replace the container with the new fragment
        ft.replace(R.id.fragment_intro_login_signup, LoginFragment.getInstance());
        //Commit
        ft.commit();
        state = 1;
    }

}