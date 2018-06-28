package com.msruas.debug.rumschat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Socket socket;
        try {
            socket = IO.socket("http://10.0.2.2:1999");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                socket.disconnect();
            }
        });

        socket.connect();
    }
}