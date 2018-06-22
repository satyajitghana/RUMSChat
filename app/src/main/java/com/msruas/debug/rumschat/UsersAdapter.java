package com.msruas.debug.rumschat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msruas.debug.rumschat.model.User;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private List<User> usersList;

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView regNo, user_name;
        public UserViewHolder(View view) {
            super(view);
            regNo = view.findViewById(R.id.tv_reg_no);
            user_name = view.findViewById(R.id.tv_name);
        }
    }

    public UsersAdapter(List<User> usersList) {
        this.usersList = usersList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_row, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.regNo.setText(user.getRegNo());
        holder.user_name.setText(user.getName());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
}
