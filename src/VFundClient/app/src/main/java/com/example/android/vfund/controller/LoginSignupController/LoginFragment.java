package com.example.android.vfund.controller.LoginSignupController;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.example.android.vfund.R;
import com.example.android.vfund.controller.HomeController.HomeActivity;
import com.example.android.vfund.controller.QueryUtils;
import com.example.android.vfund.model.User;

import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<User> {
    private static LoginFragment loginFragment = null;
    private LoginSignupActivity hostActivity;
    private static final int LOGIN_LOADER = 1;

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
            final String email = editEmail.getText().toString();
            final String password = editPassword.getText().toString();
            Bundle bundle =new Bundle();
            bundle.putString("email", email);
            bundle.putString("password", password);
            //LoaderManager.getInstance(this).initLoader(LOGIN_LOADER, bundle, this);
            final ExecutorService executor = Executors.newSingleThreadExecutor();
            final Handler handler = new Handler(Looper.getMainLooper());
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    StringBuilder requestUrl = new StringBuilder();
                    requestUrl.append(hostActivity.LOGIN_REQUEST_URL);
                    requestUrl.append("?");
                    requestUrl.append("UserEmail=");
                    requestUrl.append(email);
                    requestUrl.append("&");
                    requestUrl.append("UserPassword=");
                    requestUrl.append(password);
                    String mUrl = requestUrl.toString();
                    User loginUser = QueryUtils.fetchUserData(mUrl);
                    if(loginUser != null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getContext(), HomeActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });

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

    @NonNull
    @Override
    public Loader<User> onCreateLoader(int id, @Nullable Bundle args) {
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(hostActivity.LOGIN_REQUEST_URL);
        requestUrl.append("?");
        requestUrl.append("UserEmail=");
        requestUrl.append(args.getString("email"));
        requestUrl.append("&");
        requestUrl.append("UserPassword=");
        requestUrl.append(args.getString("password"));
        return new LoginAsyncTask(hostActivity, requestUrl.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<User> loader, User data) {
        if(data != null) {
            Toast.makeText(getContext(), "Login Successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), HomeActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(getContext(),"Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<User> loader) {

    }

    private static class LoginAsyncTask extends AsyncTaskLoader<User> {
        private String mUrl;

        public LoginAsyncTask(@NonNull Context context, String url) {
            super(context);
            mUrl = url;
        }


        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            forceLoad();
        }

        @Nullable
        @Override
        public User loadInBackground() {
            if(mUrl == null){
                return null;
            }
            User loginUser = QueryUtils.fetchUserData(mUrl);
            return loginUser;

        }

    }
}
