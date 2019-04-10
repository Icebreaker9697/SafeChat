package chat.safe.safechat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFriends.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddFriends#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriends extends Fragment {
    Button testList;
    Button addFriend;
    EditText specifiedFriend;
    TextView tvError;
    ArrayList<String> data=new ArrayList<String>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddFriends() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddFriends.
     */
    // TODO: Rename and change types and number of parameters
    public static AddFriends newInstance(String param1, String param2) {
        AddFriends fragment = new AddFriends();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

            }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_add_friends, container, false);

        /*testList = (Button) rootView.findViewById(R.id.bn_test_list);
        testList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.add("Hello");
                ListView lvData = (ListView) rootView.findViewById(R.id.lv_Requests);
                lvData.setAdapter(new CustomFriendRequestListAdapter(getActivity().getApplicationContext(), data));
            }
        });*/

        addFriend = (Button) rootView.findViewById(R.id.bn_addFriend);
        specifiedFriend = (EditText) rootView.findViewById(R.id.et_addFriend);
        tvError = (TextView) rootView.findViewById(R.id.af_tv_error);
        addFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toUser = specifiedFriend.getText().toString();
                String fromUser = SaveSharedPreference.getUsername(getActivity().getApplicationContext());

                if(toUser.equals(fromUser)){
                    tvError.setText("Cannot add self as friend!");
                } else {
                    sendFriendRequest(fromUser, toUser, specifiedFriend, tvError, getActivity().getApplicationContext());
                }
            }
        });

        ListView lvData = (ListView) rootView.findViewById(R.id.lv_Requests);
        lvData.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, data));

        return rootView;
    }

    public static void sendFriendRequest(String fromUser, final String toUser, final EditText userSearch, final TextView tv_error, final Context c){

        String url = ServerInfo.IP + "/demo/requestFriend?from=" + fromUser + "&to=" + toUser;
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Display the first 500 characters of the response string
                if(response.equals("usernotexist")){
                    tv_error.setText("Specified user not found!");
                } else if(response.equals("alreadyfriends")){
                    tv_error.setText("Already friends with the user!");
                } else if(response.equals("alreadyrequested")){
                    tv_error.setText("Friend request already sent!");
                }else if(response.equals("becamefriends")){
                    tv_error.setText("");
                    Toast.makeText(c,"Became friends with " + toUser + "!",Toast.LENGTH_SHORT).show();
                }
                else{
                    tv_error.setText("");
                    Toast.makeText(c,"Sent request to " + toUser + "!",Toast.LENGTH_SHORT).show();
                }
                userSearch.setText("");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv_error.setText("Error contacting the server!");
            }
        });

        RequestHandler.getInstance(c).addToRequestQueue(stringRequest);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
