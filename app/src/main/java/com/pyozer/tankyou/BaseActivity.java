package com.pyozer.tankyou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class BaseActivity extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(AppConfig.SOCKET_IO_HOST);
        } catch (URISyntaxException e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSocket.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        //mSocket.off("new message", onNewMessage);
    }
}
