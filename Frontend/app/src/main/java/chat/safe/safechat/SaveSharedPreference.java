package chat.safe.safechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Peter on 4/7/19.
 */

public class SaveSharedPreference {
    static final String USERNAME = "username";
    static final String RSAPUBLICKEY = "rsa public key";
    static final String RSAPRIVATEKEY = "rsa private key";
    static final String PASSWORD = "password";

    static final String LOGGED_IN = "login state";

    static SharedPreferences getSharePreferences(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void login(Context ctx, String userName, String publicRSA, String privateRSA, String password){
        SharedPreferences.Editor editor = getSharePreferences(ctx).edit();
        editor.putString(USERNAME, userName);
        editor.putString(RSAPUBLICKEY, publicRSA);
        editor.putString(RSAPRIVATEKEY, privateRSA);
        editor.putString(PASSWORD, password);

        editor.putBoolean(LOGGED_IN, true);
        editor.commit();
    }

    public static String getUsername(Context ctx){
        return getSharePreferences(ctx).getString(USERNAME, "");
    }

    public static String getPublicKey(Context ctx){
        return getSharePreferences(ctx).getString(RSAPUBLICKEY, "");
    }

    public static String getPrivateKey(Context ctx){
        return getSharePreferences(ctx).getString(RSAPRIVATEKEY, "");
    }

    public static String getPassword(Context ctx){
        return getSharePreferences(ctx).getString(PASSWORD, "");
    }

    public static boolean isLoggedIn(Context ctx){
        return getSharePreferences(ctx).getBoolean(LOGGED_IN, false);
    }

    public static void logout(Context ctx){
        SharedPreferences.Editor editor = getSharePreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }
}
