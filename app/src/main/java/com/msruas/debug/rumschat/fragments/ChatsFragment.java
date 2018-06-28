package com.msruas.debug.rumschat.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.msruas.debug.rumschat.APIServices.RUMSService;
import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.chatActivity.ChatWithUser;
import com.msruas.debug.rumschat.landingActivity.UsersListTouchListener;
import com.msruas.debug.rumschat.model.ChatDM;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class ChatsFragment extends Fragment {
    private List<ChatDM> usersList = new ArrayList<>();
    private ChatDMsAdapter usersAdapter;
    private String regNoOfUser;
    public ChatsFragment() {
        usersAdapter = new ChatDMsAdapter(usersList);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chats_fragment, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        //ChatDMsAdapter usersAdapter = new ChatDMsAdapter(usersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new UsersListTouchListener(getActivity(), recyclerView, new UsersListTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ChatDM user = usersList.get(position);
                Toast.makeText(getActivity(), user.getName() + " is selected", Toast.LENGTH_LONG).show();
                Intent chatWithUser = new Intent(getActivity(), ChatWithUser.class);
                chatWithUser.putExtra("chatID", user.getChatID());
                chatWithUser.putExtra("userName",user.getName());
                startActivity(chatWithUser);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.setAdapter(usersAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        regNoOfUser = sharedPreferences.getString(Constants.REGNO, null);

        // Get the ChatDM List from the Server

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        RUMSAPI rumsapi = RUMSService.getAPI();
        compositeDisposable.add(rumsapi.getChatDMs(regNoOfUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    //Log.d("WARNING", response.toString());
                    for (ChatDM user: response) {
                        usersList.add(user);
                        //Log.d("USER", user.getRegNo());
                        usersAdapter.notifyDataSetChanged();
                    }
                }, (error) -> {
                    if (error instanceof HttpException) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String errBody = ((HttpException)error).response().errorBody().string();
                            ResponseMessage response = gson.fromJson(errBody,ResponseMessage.class);
                            Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                }));
        return rootView;
    }
}
