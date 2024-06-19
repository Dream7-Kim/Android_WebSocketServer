package com.example.websocketserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int PORT = 8887;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChatServer chatServer = null;
        try {
            chatServer = new ChatServer(PORT);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        ChatServer finalChatServer = chatServer;

//        SSLChatServer chatServer = null;
//        chatServer = new SSLChatServer(this, PORT);
//        SSLChatServer finalChatServer = chatServer;

        TextView tv1 = findViewById(R.id.myTextView);
        tv1.setText("wss://" + Utils.getIPAddress(true)+":"+PORT);

        Button buttonOne = (Button) findViewById(R.id.supabutton);

        buttonOne.setOnClickListener(v -> {
            Log.i(TAG, "onCreate: Button Clicked!!");
            if (flag == 0) {
                flag = 1;
                try {
                    finalChatServer.startServer();
                    buttonOne.setText("STOP WEBSOCKET SERVER");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                flag = 0;
                try {
                    finalChatServer.stopServer();
                    buttonOne.setText("START WEBSOCKET SERVER");
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}