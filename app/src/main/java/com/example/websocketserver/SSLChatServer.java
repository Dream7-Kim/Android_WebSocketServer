package com.example.websocketserver;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

public class SSLChatServer {

    private Context context;
    private ChatServer chatserver;
    private int PORT;

    public SSLChatServer(Context context, int port){
        this.context = context;
        this.PORT = port;
    }

    public void startServer() throws Exception {
        chatserver = new ChatServer(PORT);

        SSLContext context = getContext_();
        if (context != null) {
            chatserver.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(getContext_()));
        }
        chatserver.setConnectionLostTimeout(30);
        chatserver.start();
    }

    public void stopServer() throws InterruptedException, IOException {
        chatserver.stop();
    }

    private SSLContext getContext_() throws Exception {
        // load up the key store
        String storePassword = "123456";
        String keyPassword = "123456";

        KeyStore ks;
        SSLContext sslContext;
        try {
            KeyStore keystore = KeyStore.getInstance("BKS");
            try (InputStream in = context.getResources().openRawResource(R.raw.keystore)) {
                keystore.load(in, storePassword.toCharArray());
            }
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keystore, keyPassword .toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(keystore);

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), tmf.getTrustManagers(), null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.toString());
        }
        return sslContext;
    }
}
