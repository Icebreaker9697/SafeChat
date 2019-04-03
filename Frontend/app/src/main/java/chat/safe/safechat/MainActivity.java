package chat.safe.safechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import static chat.safe.safechat.RSACipher.decryptWithPrivate;
import static chat.safe.safechat.RSACipher.decryptWithPublic;
import static chat.safe.safechat.RSACipher.encryptWithPrivate;
import static chat.safe.safechat.RSACipher.encryptWithPublic;
import static chat.safe.safechat.RSACipher.generateKeyPair;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView signupText = findViewById(R.id.tv_signup);
        String text = getString(R.string.signup);
        SpannableString content = new SpannableString(text);
        content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
        signupText.setText(content);

        textView = (TextView) findViewById(R.id.centerAnchor);
    }

    public void loginHandler(View v){
        EditText etUsername = (EditText) findViewById(R.id.et_username);
        EditText etPassword = (EditText) findViewById(R.id.et_password);

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        final TextView tv_error = (TextView) findViewById(R.id.tv_loginError);

        String serverMsg = "login?username=" + username + "&enteredPassword=" + password;
        String url = URLEncoder.generateURL(serverMsg);
        System.out.println(url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Display the first 500 characters of the response string
                if(response.equals("Success")){
                    Toast.makeText(getApplicationContext(),"Logged in!",Toast.LENGTH_SHORT).show();
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

    public void testButton(View v){
        String msg = "Hello";

        String[] keys = generateKeyPair();

        String publicKey = keys[0];
        String privateKey = keys[1];

        System.out.println("Public key: " + publicKey);
        System.out.println("Private key: " + privateKey);
        System.out.println();
        System.out.println();

        String encryptedWithPublicKey = encryptWithPublic(publicKey, msg);
        System.out.println("Encrypted With Public Key: " + encryptedWithPublicKey);
        System.out.println();

        String decryptedWithPrivateKey = decryptWithPrivate(privateKey, encryptedWithPublicKey);
        System.out.println("Decrypted With Private Key: " + decryptedWithPrivateKey);
        System.out.println();
        System.out.println();

        String encryptedWithPrivateKey = encryptWithPrivate(privateKey, msg);
        System.out.println("Encrypted With Private Key: " + encryptedWithPrivateKey);
        System.out.println();

        String decryptedWithPublicKey = decryptWithPublic(publicKey, encryptedWithPrivateKey);
        System.out.println("Decrypted with Public Key: " + decryptedWithPublicKey);

        String msg2 = "Hello";
        String key = "password";
        String encrypted = SymmetricCipher.encrypt(key, msg2);
        System.out.println("Encrypted with key='" + key + "': " + encrypted);
        String dec1 = SymmetricCipher.decrypt(key, encrypted);
        String key2 = key + "d";
        String dec2 = SymmetricCipher.decrypt(key2, encrypted);
        System.out.println("Decrypted with key='" + key + "': " + dec1);
        System.out.println("Decrypted with key='" + key2 + "': " + dec2);
    }

    public void testHash(View v){
        String pass = "qwertyuiop";
        System.out.println("Set password is " + pass);
        System.out.println();

        String hash = Hasher.generateStrongPasswordHash(pass);
        System.out.println("Hash generated from pass is: " + hash);
        System.out.println();

        String check1 = "qwertyuiop";
       // boolean isSamePass1 = Hasher.validatePassword(check1, hash);
        //System.out.println("Does " + check1 + "pass? " + isSamePass1);

        String check2 = "qwertyuio";
        //boolean isSamePass2 = Hasher.validatePassword(check2, hash);
        //System.out.println("Does " + check2 + "pass? " + isSamePass2);
    }
}
