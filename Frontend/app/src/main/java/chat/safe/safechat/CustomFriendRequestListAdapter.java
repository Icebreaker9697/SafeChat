package chat.safe.safechat;

import android.content.Context;
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

public class CustomFriendRequestListAdapter extends ArrayAdapter<String>{

    private Context c;
    private AddFriends parentActivity;
    private View rootView;

    public CustomFriendRequestListAdapter(@NonNull Context context, ArrayList<String> requests, AddFriends parent, View view) {
        super(context, R.layout.friend_request_list_item, requests);
        c = context;
        parentActivity = parent;
        rootView = view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View customView = mInflater.inflate(R.layout.friend_request_list_item, parent, false);
        final String singleFriendRequester = getItem(position);

        TextView dataText = (TextView) customView.findViewById(R.id.clv_tv1);

        Button bnReject = (Button) customView.findViewById(R.id.bnReject);
        Button bnAccept = (Button) customView.findViewById(R.id.bn_Chat);

        bnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toUser = singleFriendRequester;
                String fromUser = SaveSharedPreference.getUsername(c);

                sendFriendReject(fromUser, toUser, c, parentActivity, rootView);
            }
        });

        bnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toUser = singleFriendRequester;
                String fromUser = SaveSharedPreference.getUsername(c);

                sendFriendAccept(fromUser, toUser, c, parentActivity, rootView);
            }
        });

        dataText.setText(singleFriendRequester);
        return customView;
    }

    public static void sendFriendAccept(String fromUser, final String toUser, final Context c, final AddFriends parentActivity, final View rootView){

        String serverMsg = "requestfriend?" + fromUser + "?" + toUser;
        final String symmetricKey = SymmetricCipher.generateRandomKey();
        String url = URLEncoder.generateEncryptedURL(serverMsg, symmetricKey);
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                String response = SymmetricCipher.decrypt(symmetricKey, res, true);
                if(response.equals("usernotexist")){
                    /*do nothing*/
                } else if(response.equals("notFound")){
                    /*do nothing*/
                } else if(response.equals("becamefriends")){
                    Toast.makeText(c,"Accepted request from " + toUser + ".",Toast.LENGTH_SHORT).show();
                    parentActivity.refreshList(c, rootView, parentActivity);
                }
                else{
                    /*do nothing*/
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,"Error contacting the server!",Toast.LENGTH_SHORT).show();
            }
        });

        RequestHandler.getInstance(c).addToRequestQueue(stringRequest);
    }

    public static void sendFriendReject(String fromUser, final String toUser, final Context c, final AddFriends parentActivity, final View rootView){

        String serverMsg = "rejectrequest?" + fromUser + "?" + toUser;
        final String symmetricKey = SymmetricCipher.generateRandomKey();
        String url = URLEncoder.generateEncryptedURL(serverMsg, symmetricKey);
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String res) {
                String response = SymmetricCipher.decrypt(symmetricKey, res, true);
                if(response.equals("usernotexist")){
                    /*do nothing*/
                } else if(response.equals("notFound")){
                    /*do nothing*/
                } else if(response.equals("rejectedfriends")){
                    Toast.makeText(c,"Rejected request from " + toUser + ".",Toast.LENGTH_SHORT).show();
                    parentActivity.refreshList(c, rootView, parentActivity);
                }
                else{
                    /*do nothing*/
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,"Error contacting the server!",Toast.LENGTH_SHORT).show();
            }
        });

        RequestHandler.getInstance(c).addToRequestQueue(stringRequest);
    }
}
