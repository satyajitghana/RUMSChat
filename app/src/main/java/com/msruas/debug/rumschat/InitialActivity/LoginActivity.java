package com.msruas.debug.rumschat.InitialActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.msruas.debug.rumschat.APIServices.RUMSService;
import com.msruas.debug.rumschat.ChatActivity;
import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Constants;
import com.msruas.debug.rumschat.utils.Validation;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class LoginActivity extends AppCompatActivity {

    private EditText et_regNo;
    private EditText et_password;
    private Button bt_login;
    private TextView tv_notRegistered;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initViews();
        initSharedPreferences();
    }

    private void initSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
    }

    private void initViews() {
        et_regNo = findViewById(R.id.et_regNo);
        et_password = findViewById(R.id.et_password);
        bt_login = findViewById(R.id.bt_login);
        tv_notRegistered = findViewById(R.id.tv_toRegister);

        bt_login.setOnClickListener((View v) -> login());
        tv_notRegistered.setOnClickListener((View v) -> goToRegister());
    }

    private void login() {
        String regNo = et_regNo.getText().toString();
        String password = et_password.getText().toString();

        if(!Validation.validateField(regNo)) {
            et_regNo.setError("RegNo cannot be empty !");
        } else if (!Validation.validateField(password)){
            et_password.setError("Password cannot be empty !");
        } else {
            loginProcess(regNo, password);
        }
    }

    private void loginProcess(String regNo, String password) {
        RUMSAPI rumsapi = RUMSService.getAPI(regNo, password);

        compositeDisposable.add(rumsapi.login()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    Toast.makeText(LoginActivity.this, response.getMessage() + " Logged in !", Toast.LENGTH_LONG).show();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Constants.TOKEN, response.getToken());
                    editor.putString(Constants.EMAIL, response.getMessage());
                    editor.apply();

                    // now go to ChatActivity
                    startActivity(new Intent(LoginActivity.this, ChatActivity.class));

                }, (error) -> {
                    if (error instanceof HttpException) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String errBody = ((HttpException)error).response().errorBody().string();
                            ResponseMessage response = gson.fromJson(errBody,ResponseMessage.class);
                            Toast.makeText(LoginActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }));
    }

    private void goToRegister(){
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
}
