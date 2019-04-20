package chat.safe.safechat;

import static chat.safe.safechat.RSACipher.encryptWithPublic;
import static chat.safe.safechat.RSACipher.generateKeyPair;

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

    public static String generateEncryptedURL(String serverMsg, String symmetricKey){
        String encryptedMsg = SymmetricCipher.encrypt(symmetricKey, serverMsg, true);
        String encryptedSymmetricKey = encryptWithPublic(ServerInfo.PUBLICKEY, symmetricKey);
        String url = ServerInfo.IP + "/demo/enc?payloadCipher=" + encryptedMsg  + "&encryptedKey=" + encryptedSymmetricKey;
        return url;
    }

    public static String generateEncryptedMsg(String serverMsg, String symmetricKey, String publicKey){
        String encryptedMsg = SymmetricCipher.encrypt(symmetricKey, serverMsg, true);
        String encryptedSymmetricKey = encryptWithPublic(publicKey, symmetricKey);
        String msg = encryptedMsg + "%" + encryptedSymmetricKey;
        return msg;
    }
}
