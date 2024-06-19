package com.example.websocketserver;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;
import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class ChatServer extends WebSocketServer {

    private final String TAG = "WebSocketServer";
    public ChatServer(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    public ChatServer(InetSocketAddress address) {
        super(address);
    }

    public ChatServer(int port, Draft_6455 draft) {
        super(new InetSocketAddress(port), Collections.<Draft>singletonList(draft));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        conn.send("Welcome to the server!"); //This method sends a message to the new client
        broadcast("new connection: " + handshake.getResourceDescriptor()); //This method sends a message to all clients connected
//        ma.outputToTV("new connection: " + handshake.getResourceDescriptor());
        Log.i(TAG,
                conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        broadcast(conn + " has left the room!");
        Log.i(TAG,conn + " has left the room!");
//        ma.outputToTV(conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast(message);
        Log.i(TAG,conn + ": " + message);
//        ma.outputToTV(conn + ": " + message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        Log.i(TAG,conn + ": " + message);
    }


    public void startServer() throws InterruptedException, IOException {
        this.start();
        Log.i(TAG,"ChatServer started on port: " + this.getPort());
    }

    public void stopServer() throws InterruptedException, IOException {
        this.stop();
        Log.i(TAG,"ChatServer stopped on port: " + this.getPort());
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        Log.i(TAG,"Server started!");
//        ma.outputToTV("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}
