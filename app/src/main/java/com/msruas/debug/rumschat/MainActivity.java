package com.msruas.debug.rumschat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.model.User;
import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Constants;
import com.msruas.debug.rumschat.utils.Validation;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.msruas.debug.rumschat.APIServices.RUMSService;
import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.model.User;
import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Constants;
import com.msruas.debug.rumschat.utils.Validation;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}