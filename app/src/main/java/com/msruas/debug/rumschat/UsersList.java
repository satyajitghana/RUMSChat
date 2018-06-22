package com.msruas.debug.rumschat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msruas.debug.rumschat.APIServices.RUMSService;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.model.User;
import com.msruas.debug.rumschat.network.RUMSAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class UsersList extends AppCompatActivity {

    private List<User> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);
        setTitle("Users List");

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        UsersAdapter usersAdapter = new UsersAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new UsersListTouchListener(getApplicationContext(), recyclerView, new UsersListTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                User user = usersList.get(position);
                Toast.makeText(getApplicationContext(), user.getName() + " is selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setAdapter(usersAdapter);

        // Get the User List from the Server

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        RUMSAPI rumsapi = RUMSService.getAPI();
        compositeDisposable.add(rumsapi.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    for (User user: response) {
                        usersList.add(user);
                        usersAdapter.notifyDataSetChanged();
                    }
                }, (error) -> {
                    if (error instanceof HttpException) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String errBody = ((HttpException)error).response().errorBody().string();
                            ResponseMessage response = gson.fromJson(errBody,ResponseMessage.class);
                            Toast.makeText(UsersList.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(UsersList.this, error.toString(), Toast.LENGTH_LONG).show();
                }));
    }
}
