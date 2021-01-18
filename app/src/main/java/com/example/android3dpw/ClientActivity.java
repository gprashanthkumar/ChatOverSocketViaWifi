package com.example.android3dpw;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientActivity extends AppCompatActivity implements View.OnClickListener {

    public static  int SERVERPORT = 1000;

   // public static final String SERVER_IP = "10.1.10.108";
   public static  String SERVER_IP = "192.168.0.1";
    private ClientThread clientThread;
    private Thread thread;
    private LinearLayout msgList;
    private Handler handler;
    private int clientTextColor;
    private EditText edMessage;
    SharedClass obj;
    Button connect_server;
    Button send_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        setTitle("Client");
       clientTextColor = ContextCompat.getColor(this, R.color.green);
        connect_server = findViewById(R.id.connect_server);
        connect_server.setBackgroundResource(R.drawable.button_pressedgreen);
        send_data = findViewById(R.id.send_data);
        send_data.setBackground(null);
        handler = new Handler();
        msgList = findViewById(R.id.msgList);
        edMessage = findViewById(R.id.edMessage);
        obj = new SharedClass();
        SearchService();

    }

    public TextView textView(String message, int color) {
        if (null == message || message.trim().isEmpty()) {
            message = "<Empty Message>";
        }
        TextView tv = new TextView(this);
        tv.setTextColor(color);
        tv.setText(message + " [" + getTime() + "]");
        tv.setTextSize(20);
        tv.setPadding(0, 5, 0, 0);
        return tv;
    }

    public void showMessage(final String message, final int color) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (msgList.getChildCount() > 5){
                    msgList.removeViewAt(2);
                }
                msgList.addView(textView(message, color));
            }
        });
    }

    public  void showConnectToServer(){
        clientThread= null;
        obj.hostAddress = null;
        obj.hostPort=0;
        connect_server.setBackgroundResource(R.drawable.button_pressedgreen);
        send_data.setBackgroundResource(R.drawable.button_pressedgrey);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.connect_server) {
            msgList.removeAllViews();
            showMessage("Connecting to Server...", clientTextColor);
            clientThread = new ClientThread();
            thread = new Thread(clientThread);
            thread.start();
            showMessage("Connected to Server...", clientTextColor);
            send_data.setBackgroundResource(R.drawable.button_pressedgreen);
            view.setBackgroundResource(R.drawable.button_pressedgrey);
            return;
        }

        if (view.getId() == R.id.send_data) {
            String clientMessage = edMessage.getText().toString().trim();

            if (null != clientThread) {
                showMessage(clientMessage, Color.BLUE);
                clientThread.sendMessage(clientMessage);
            } else {
                Toast.makeText(this,"Not connected to any server",Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ClientThread implements Runnable {

        private Socket socket;
        private BufferedReader input;

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

                while (!Thread.currentThread().isInterrupted()) {

                    this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (null == message || "Disconnect".contentEquals(message)) {
                        Thread.interrupted();
                        message = "Server Disconnected.";
                        showMessage(message, Color.RED);
                        showConnectToServer();
                        break;
                    }
                    showMessage("Server: " + message, clientTextColor);
                }

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

        void sendMessage(final String message) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (null != socket) {
                            PrintWriter out = new PrintWriter(new BufferedWriter(
                                    new OutputStreamWriter(socket.getOutputStream())),
                                    true);
                            out.println(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != clientThread) {
            clientThread.sendMessage("Disconnect");
            clientThread = null;
        }
    }

    public void SearchService(){
        obj.initializeDiscoveryListener(SharedClass._serviceName,getApplicationContext());
        final Handler _handler = new Handler(Looper.getMainLooper());
        _handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if (obj.hostPort > 0 && obj.hostAddress != null) {
                    SERVER_IP = obj.hostAddress.getHostAddress();
                    Log.i("test","Server host address is: " +obj.hostAddress.getHostAddress());
                    SERVERPORT = obj.hostPort;

                }

            }
        }, 1000);
    }
}