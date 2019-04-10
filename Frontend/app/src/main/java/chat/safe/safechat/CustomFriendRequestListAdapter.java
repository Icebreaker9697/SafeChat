package chat.safe.safechat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Peter on 4/9/19.
 */

public class CustomFriendRequestListAdapter extends ArrayAdapter<String>{

    public CustomFriendRequestListAdapter(@NonNull Context context, ArrayList<String> requests) {
        super(context, R.layout.list_view_custom_row, requests);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View customView = mInflater.inflate(R.layout.list_view_custom_row, parent, false);
        String singleFriendRequester = getItem(position);

        TextView dataText = (TextView) customView.findViewById(R.id.clv_tv1);

        dataText.setText(singleFriendRequester);
        return customView;
    }
}
