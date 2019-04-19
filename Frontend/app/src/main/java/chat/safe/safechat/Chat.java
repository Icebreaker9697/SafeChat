package chat.safe.safechat;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.java_websocket.drafts.Draft_6455;

public class Chat extends AppCompatActivity {
    private WebSocketClient cc;
    private String curUser;
    private String curConvo;
    private String destUser;
    ArrayList<String> conversation;
    ConverstationHistoryListAdapter adapter;
    Button bn_send;
    EditText msg_to_send;
    ListView lvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        destUser = getIntent().getStringExtra("USERTOCHAT");
        curUser = SaveSharedPreference.getUsername(getApplicationContext());
        curConvo = curUser + "$" + destUser;
        conversation = new ArrayList<>();
        connectWebSocket();

        lvData = (ListView) findViewById(R.id.lv_conversation);
        adapter = new ConverstationHistoryListAdapter(getApplicationContext(), conversation);
        lvData.setAdapter(adapter);

        bn_send = (Button)findViewById(R.id.bn_conv_send);
        msg_to_send = (EditText)findViewById(R.id.et_conv_msg);
        bn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String msg = msg_to_send.getText().toString().trim();

                msg_to_send.setText("");

                if(msg != null && !msg.equals("")){
                    cc.send("@" + destUser + " " + msg);
                }
            }
        });



        //conversation.add("PJ: hi");

        //while(!cc.isOpen()){
            
       // }
        //cc.send("Hello There!");

        TextView tv = (TextView) findViewById(R.id.tv_chat);
        tv.setText("Chatting with " + destUser);
    }

    public void close(View v){
        cc.close();
        finish();
    }

    private void connectWebSocket() {
        Draft[] drafts = {new Draft_6455()};
        String w = ServerInfo.WSIP + "/websocket/" + curConvo;

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w),(Draft) drafts[0]) {
                @Override
                public void onMessage(final String message) {
                    Log.d("", "run() returned: " + message);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            conversation.add(message);
                            adapter.notifyDataSetChanged();
                            lvData.setSelection(adapter.getCount() - 1);
                        }
                    });

                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e)
                {
                    Log.d("Exception:", e.toString());
                }
            };
        }
        catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();
    }
}
