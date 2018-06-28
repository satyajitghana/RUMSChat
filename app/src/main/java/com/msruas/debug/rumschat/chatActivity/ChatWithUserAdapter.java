package com.msruas.debug.rumschat.chatActivity;

import android.support.v4.view.GravityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.model.Message;

import java.util.List;

public class ChatWithUserAdapter extends RecyclerView.Adapter<ChatWithUserAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private String senderUserName;

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name, message;
        public MessageViewHolder(View view) {
            super(view);
            user_name = view.findViewById(R.id.tv_name);
            message = view.findViewById(R.id.tv_message);
        }
    }

    public ChatWithUserAdapter(List<Message> usersList, String senderUserName) {
        this.messageList = usersList;
        this.senderUserName = senderUserName;
    }

    @Override
    public ChatWithUserAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_area, parent, false);
        return new ChatWithUserAdapter.MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatWithUserAdapter.MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
//        Log.d("WARNING", senderUserName);
//        Log.d("WARNING", message.getFrom());
        if (message.getFrom().equals(senderUserName)) {
            holder.user_name.setGravity(Gravity.RIGHT);
            holder.message.setGravity(Gravity.RIGHT);
        }
        holder.user_name.setText(message.getFrom());
        holder.message.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
