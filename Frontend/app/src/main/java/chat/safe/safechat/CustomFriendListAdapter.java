package chat.safe.safechat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

/**
 * Created by Peter on 4/9/19.
 */

public class CustomFriendListAdapter extends ArrayAdapter<String>{

    private Context c;

    public CustomFriendListAdapter(@NonNull Context context, ArrayList<String> requests) {
        super(context, R.layout.friend_list_item, requests);
        c = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View customView = mInflater.inflate(R.layout.friend_list_item, parent, false);
        final String singleFriend = getItem(position);

        TextView dataText = (TextView) customView.findViewById(R.id.tv_fl);

        Button bnChat = (Button) customView.findViewById(R.id.bn_Chat);

        bnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = singleFriend;
                Intent intent = new Intent(c, Chat.class);
                intent.putExtra("USERTOCHAT", user);
                c.startActivity(intent);
            }
        });

        dataText.setText(singleFriend);
        return customView;
    }
}
