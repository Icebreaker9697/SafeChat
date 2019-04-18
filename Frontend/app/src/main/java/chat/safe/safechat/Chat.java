package chat.safe.safechat;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.drafts.Draft_6455;

public class Chat extends AppCompatActivity {
    private WebSocketClient cc;
    private String curUser;
    private String userToChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userToChat = getIntent().getStringExtra("USERTOCHAT");
        curUser = SaveSharedPreference.getUsername(getApplicationContext());

        connectWebSocket();

        TextView tv = (TextView) findViewById(R.id.tv_chat);
        tv.setText("Chatting with " + userToChat);
    }

    public void close(View v){
        finish();
    }

    private void connectWebSocket() {
        Draft[] drafts = {new Draft_6455()};
        String w = ServerInfo.WSIP + "/websocket/" + curUser;

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(w),(Draft) drafts[0]) {
                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);
                    TextView t1 = (TextView)findViewById(R.id.messages);

                    t1.setText(" Server:"+message);
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
