package chat.safe.safechat;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static chat.safe.safechat.RSACipher.decryptWithPrivate;
import static chat.safe.safechat.RSACipher.decryptWithPublic;
import static chat.safe.safechat.RSACipher.encryptWithPrivate;
import static chat.safe.safechat.RSACipher.encryptWithPublic;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AddFriends.OnFragmentInteractionListener, MainFragment.OnFragmentInteractionListener{

    private static Context c;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = this;
        setContentView(R.layout.activity_main);
        setNavigationViewListener();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.nav_logout: {
                SaveSharedPreference.logout(c);
                Toast.makeText(getApplicationContext(),"Logged out!",Toast.LENGTH_SHORT).show();
                finish();
                break;
            }

            case R.id.nav_addFriend: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddFriends()).commit();
                break;
            }

            case R.id.nav_chats: {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void testButton(View v){
        String msg = "Hello";

        String publicKey = SaveSharedPreference.getPublicKey(c);
        String privateKey = SaveSharedPreference.getPrivateKey(c);

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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
