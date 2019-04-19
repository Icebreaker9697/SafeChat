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

import java.util.ArrayList;

/**
 * Created by Peter on 4/9/19.
 */

public class ConverstationHistoryListAdapter extends ArrayAdapter<String>{

    private Context c;

    public ConverstationHistoryListAdapter(@NonNull Context context, ArrayList<String> requests) {
        super(context, R.layout.conversation_history_list, requests);
        c = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(getContext());
        View customView = mInflater.inflate(R.layout.conversation_history_list, parent, false);
        String msg = getItem(position);

        TextView msgText = (TextView) customView.findViewById(R.id.tv_ch_msg);

        msgText.setText(msg);
        return customView;
    }
}
