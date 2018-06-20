package com.msruas.debug.rumschat.InitialActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msruas.debug.rumschat.APIServices.RUMSService;
import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.model.User;
import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Validation;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import retrofit2.HttpException;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_regNo;
    private EditText et_name;
    private EditText et_email;
    private EditText et_password;
    private Button bt_register;
    private ProgressBar pb_register;
    private TextView tv_already_registered;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        initViews();
    }

    private void initViews() {
        et_regNo = findViewById(R.id.et_regNo);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        bt_register = findViewById(R.id.bt_register);
        pb_register = findViewById(R.id.pb_register);
        tv_already_registered = findViewById(R.id.tv_already_registered);

        bt_register.setOnClickListener((View v) -> register());
        tv_already_registered.setOnClickListener((View v) -> goToLogin());
    }

    private void register() {
        setError();

        String regNo = et_regNo.getText().toString();
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();

        if (!Validation.validateField(name)) {
            et_name.setError("Name cannot be Empty");
        } else if(!Validation.validateField(regNo)) {
            et_regNo.setError("Registration Number cannot be Empty");
        } else if (!Validation.validateEmail(email)) {
            et_email.setError("Enter a valid Email");
        } else if (!Validation.validatePassword(password)) {
            et_password.setError("Password should be minimum of 6 characters");
        } else {
            User user = new User();
            user.setRegNo(regNo);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            pb_register.setVisibility(View.VISIBLE);
            pb_register.setMax(100);
            pb_register.setProgress(25);

            registerProcess(user);
        }
    }

    public void registerProcess(User user) {
        RUMSAPI rumsapi = RUMSService.getAPI();
        compositeDisposable.add(rumsapi.register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    pb_register.setProgress(100);
                    Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                }, (error) -> {
                    pb_register.setVisibility(View.GONE);
                    if (error instanceof HttpException) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String errBody = ((HttpException)error).response().errorBody().string();
                            ResponseMessage response = gson.fromJson(errBody,ResponseMessage.class);
                            Toast.makeText(RegisterActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }));
    }

    private void goToLogin() {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    private void setError() {
        et_name.setError(null);
        et_email.setError(null);
        et_password.setError(null);
    }
}