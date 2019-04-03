package chat.safe.safechat;

import static chat.safe.safechat.RSACipher.encryptWithPublic;

/**
 * Created by Peter on 3/30/19.
 */

public class URLEncoder {
    public static String encodeToString(byte[] arr) {
        String res = android.util.Base64.encodeToString(arr, android.util.Base64.DEFAULT);
        res = res.replace('+', '-');
        res = res.replace('/', '_');
        return res;
    }

    public static byte[] decodeFromString(String dat) {
        String tmp = dat.replace('_', '/');
        tmp = tmp.replace('-', '+');
        return android.util.Base64.decode(tmp, android.util.Base64.DEFAULT);
    }

    public static String generateURL(String serverMsg){
        String encryptedMsg = encryptWithPublic(ServerInfo.PUBLICKEY, serverMsg);
        String url = ServerInfo.IP + "/demo/enc?param=" + encryptedMsg;
        return url;
    }
}
