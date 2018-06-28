package com.msruas.debug.rumschat.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.model.ChatDM;

import java.util.List;

public class ChatDMsAdapter extends RecyclerView.Adapter<ChatDMsAdapter.UserViewHolder> {
    private List<ChatDM> usersList;

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView regNo, user_name;
        public UserViewHolder(View view) {
            super(view);
            regNo = view.findViewById(R.id.tv_reg_no);
            user_name = view.findViewById(R.id.tv_name);
        }
    }

    public ChatDMsAdapter(List<ChatDM> usersList) {
        this.usersList = usersList;
    }

    @Override
    public ChatDMsAdapter.UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row, parent, false);
        return new ChatDMsAdapter.UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatDMsAdapter.UserViewHolder holder, int position) {
        ChatDM user = usersList.get(position);
        // Swapping them because the User name should be in bold, and i'm lazy to create a new layout for this.
        //holder.regNo.setText(user.getRegNo());
        //holder.user_name.setText(user.getName());
        holder.user_name.setText(user.getRegNo());
        holder.regNo.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
