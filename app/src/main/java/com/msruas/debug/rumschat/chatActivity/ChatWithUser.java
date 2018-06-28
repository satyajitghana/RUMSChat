package com.msruas.debug.rumschat.chatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msruas.debug.rumschat.APIServices.RUMSService;
import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.model.Message;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.HttpException;

public class ChatWithUser extends AppCompatActivity {
//    LinearLayout layout;
//    ImageView sendButton;
//    EditText messageArea;
//    ScrollView scrollView;
    private List<Message> messageList= new ArrayList<>();
    private String receiverUserName;
    private String senderUserName;
    private String chatID;
    private EditText message;

    ChatWithUserAdapter chatWithUserAdapter;
    RecyclerView recyclerView;

    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatiwthuser_activity);

        Intent intent = getIntent();
        chatID = intent.getExtras().getString("chatID");
        receiverUserName = intent.getExtras().getString("userName");
        setTitle(receiverUserName);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChatWithUser.this);
        senderUserName = sharedPreferences.getString(Constants.NAME, null);

        // Socket Connection
        try {
            socket = IO.socket(Constants.SOCKET_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject _chatID = new JSONObject();
                try {
                    _chatID.put("chatID", chatID);
                } catch (JSONException e) {
                    Log.d("ERROR", e.toString());
                }
                socket.emit("join", _chatID);
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });

//        socket.on("test", (Object... args) -> {
//
//        });

        // Maybe multiple connections can be avoided by keeping track if the connection is active
        // somewhere in shared preferences maybe, for now on backpressed, disconnect the connection - shadowleaf (satyajit_ghana)
        socket.connect();

        // Layout Creation

        recyclerView = findViewById(R.id.recycler_view);
        chatWithUserAdapter = new ChatWithUserAdapter(messageList, senderUserName);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(chatWithUserAdapter);

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        RUMSAPI rumsapi = RUMSService.getAPI();
        compositeDisposable.add(rumsapi.getMessages(chatID)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    for (Message message: response) {
                        //Log.d("WARRNING",message.getText());
                        messageList.add(message);
                        chatWithUserAdapter.notifyDataSetChanged();
                        //recyclerView.smoothScrollToPosition(messageList.size()-1);
                        //recyclerView.smoothScrollToPosition(15);
                    }
                }, (error) -> {
                    if (error instanceof HttpException) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String errBody = ((HttpException)error).response().errorBody().string();
                            ResponseMessage response = gson.fromJson(errBody,ResponseMessage.class);
                            Toast.makeText(ChatWithUser.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(ChatWithUser.this, error.toString(), Toast.LENGTH_LONG).show();
                }));

        Button button = findViewById(R.id.bt_send);
        message = findViewById(R.id.et_message);

        socket.on("new_message", new Emitter.Listener() {
            @Override
            public void call(Object ... args) {
                //JSONObject obj = (JSONObject)args[0];
                Gson _gson = new GsonBuilder().create();
                Message _newMessage = _gson.fromJson(args[0].toString(),Message.class);
                messageList.add(_newMessage);
                ChatWithUser.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatWithUserAdapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(messageList.size()-1);
                    }
                });
                //Log.d("WARNING", _newMessage.getText());
            }
        });

        button.setOnClickListener((View v) -> sendMessage(chatID, senderUserName, receiverUserName, message.getText().toString()));

//        layout = findViewById(R.id.layout1);
//        sendButton = findViewById(R.id.sendButton);
//        messageArea = findViewById(R.id.messageArea);
//        scrollView = findViewById(R.id.scrollView);
//
//        addMessageBox("test\ntest",1);
//        addMessageBox("test2\ntestakjdhf kjasd hfjkhaksd hflaksjd fhkjas hldk fj",1);
//        addMessageBox("test3\ntestadsjfh kjadsh fj hasdjk fkjlas hdfjka hsdfjkhsajkhd fkjdsh f",1);
//        addMessageBox("test4\ntestadsfkj hasdkjf hjas hdklf hkas dhkf hkasdfk hkasd hkjf h",1);
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String messageText = messageArea.getText().toString();
//                addMessageBox("You:-\n" + messageText, 1);
//
//                if(!messageText.equals("")){
//                    Map<String, String> map = new HashMap<String, String>();
//                    map.put("message", messageText);
//                    map.put("user", UserDetails.username);
//                    reference1.push().setValue(map);
//                    reference2.push().setValue(map);
//                }
//            }
//        });

    }

    private void sendMessage(String chatID, String from, String to, String text) {
        // send all of this exact data i.e. above parameters, to the socket connection so the other user
        // gets the new message, and also the message is stored in the database - shadowleaf (satyajit_ghana)
        Message newMessage = new Message(from, to, text);
        JSONObject _newMessage = createJSONObjectFromMessage(chatID, newMessage);
        socket.emit("new_message", _newMessage);

        RUMSAPI rumsapi = RUMSService.getAPI();
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(rumsapi.sendMessage(chatID, newMessage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    Toast.makeText(ChatWithUser.this, response.getMessage(), Toast.LENGTH_LONG).show();
                    message.setText("");
                }, (error) -> {
                    if (error instanceof HttpException) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String errBody = ((HttpException)error).response().errorBody().string();
                            ResponseMessage response = gson.fromJson(errBody,ResponseMessage.class);
                            Toast.makeText(ChatWithUser.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(ChatWithUser.this, error.toString(), Toast.LENGTH_LONG).show();
                }));
    }

    public JSONObject createJSONObjectFromMessage(String chatID, Message message) {
        JSONObject _toReturn = new JSONObject();
        JSONObject _newMessage = new JSONObject();
        try {
            _toReturn.put("chatID", chatID);
            _newMessage.put("from", message.getFrom());
            _newMessage.put("to", message.getTo());
            _newMessage.put("text", message.getText());
            _toReturn.put("message", _newMessage);
        } catch (JSONException e) {
            Log.d("ERROR", e.toString());
        }
        return _toReturn;
    }

    @Override
    public void onBackPressed() {
        socket.disconnect();
        super.onBackPressed();
    }

    // Recycler View is probably not required, so use TextView instead, have to implement it
    // but for now recycler view works so havn't tried - shadowleaf (satyajit_ghana)

//    public void addMessageBox(String message, int type){
//        TextView textView = new TextView(ChatWithUser.this);
//        textView.setText(message);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.setMargins(0, 0, 0, 10);
//        textView.setLayoutParams(lp);
//        if(type == 1) {
//            textView.setBackgroundResource(R.drawable.rounded_corner1);
//        }
//        else{
//            textView.setBackgroundResource(R.drawable.rounded_corner2);
//        }
//
//        layout.addView(textView);
//        scrollView.fullScroll(View.FOCUS_DOWN);
//    }
}
