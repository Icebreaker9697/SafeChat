package chat.safe.safechat;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Text;

import static chat.safe.safechat.RSACipher.encryptWithPublic;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
    }

    public void close(View v){
        finish();
    }

    public void register(View v){
        EditText etUsername = (EditText) findViewById(R.id.et_signupusername);
        EditText etPassword = (EditText) findViewById(R.id.et_signuppass);
        EditText etConfirmPass = (EditText) findViewById(R.id.et_signupconfirm);

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirm = etConfirmPass.getText().toString().trim();

        if(username.equals("")){
            Toast.makeText(getApplicationContext(),"Cannot have blank username!",Toast.LENGTH_SHORT).show();
            return;
        } else if(password.equals("")){
            Toast.makeText(getApplicationContext(),"Cannot have blank password!",Toast.LENGTH_SHORT).show();
            return;
        } else if(!password.equals(confirm)){
            Toast.makeText(getApplicationContext(),"Passwords do not match!",Toast.LENGTH_SHORT).show();
            return;
        }

        final TextView tv_error = (TextView) findViewById(R.id.tv_signuperror);

        String passHash = Hasher.generateStrongPasswordHash(password);

        String serverMsg = "add?username=" + username + "&passHash=" + passHash + "&userPublicKey=123&userPrivateKey=456";
        String url = URLEncoder.generateURL(serverMsg);
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Display the first 500 characters of the response string
                if(response.equals("Success")){
                    Toast.makeText(getApplicationContext(),"Success!",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    tv_error.setText(response);
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
}
