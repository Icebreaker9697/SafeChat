package chat.safe.safechat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class LoginActivity extends AppCompatActivity {

    private static Context c;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        setContentView(R.layout.activity_login);

        TextView signupText = findViewById(R.id.tv_signup);
        String text = getString(R.string.signup);
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        signupText.setText(content);

        textView = (TextView) findViewById(R.id.centerAnchor);

        if (SaveSharedPreference.isLoggedIn(c)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void loginHandler(View v){
        final EditText etUsername = (EditText) findViewById(R.id.et_username);
        final EditText etPassword = (EditText) findViewById(R.id.et_password);

        String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        final TextView tv_error = (TextView) findViewById(R.id.tv_loginError);

        String serverMsg = "login?" + username + "?" + password;
        String url = URLEncoder.generateEncryptedURL(serverMsg);
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Display the first 500 characters of the response string
                if(response.equals("nologin")){
                    tv_error.setText("No login found for that username!");

                } else if(response.equals("wrong")){
                    tv_error.setText("Wrong password!");
                } else{
                    String[] strs = response.split("\\?");
                    String uname = strs[0];
                    String encPrivateKey = strs[1];
                    String pubKey = strs[2];
                    String priKey = SymmetricCipher.decrypt(password, encPrivateKey, false);
                    SaveSharedPreference.login(c, uname, pubKey, priKey, password);

                    etUsername.setText("");
                    etPassword.setText("");

                    Toast.makeText(getApplicationContext(),"Logged in!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv_error.setText("Error contacting the server!");
            }
        });

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void signupHandler(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void sendRequestButton(View v){
        String url = "http://10.0.2.2:8080/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Display the first 500 characters of the response string
                textView.setText("Response is: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
