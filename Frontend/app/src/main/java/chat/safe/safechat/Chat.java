package chat.safe.safechat;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Chat extends AppCompatActivity {

    EditText e1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String userToChat = getIntent().getStringExtra("USERTOCHAT");

        TextView tv = (TextView) findViewById(R.id.tv_chat);
        tv.setText("Chatting with " + userToChat);

        e1 = (EditText) findViewById(R.id.et_msgToSend);



        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

    }

    class MyServerThread implements Runnable{
        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;
        Handler h = new Handler();

        String message;

        @Override
        public void run() {
            try {
                ss = new ServerSocket(7801);
                Log.d("tag", "CREATED THE SOCKET PORT");
                while(true){
                    s = ss.accept();
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    message = br.readLine();

                    h.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void close(View v){
        finish();
    }

    public void sendMsg(View v){
        MessageSender messageSender = new MessageSender();
        messageSender.execute(e1.getText().toString());
    }
}
