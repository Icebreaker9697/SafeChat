package chat.safe.safechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        String userToChat = getIntent().getStringExtra("USERTOCHAT");

        TextView tv = (TextView) findViewById(R.id.tv_chat);
        tv.setText("Chatting with " + userToChat);
    }

    public void close(View v){
        finish();
    }
}
