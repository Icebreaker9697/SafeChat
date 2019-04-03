package chat.safe.safechat;

import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Peter on 3/26/19.
 */

public class SymmetricCipher {
    private static byte[] getKey(String key){
        try {
            int keySize = 16;
            byte[] res = key.getBytes("UTF-8");

            if(res.length < keySize){
                byte[] newKey = new byte[keySize];
                for(int i = 0; i < newKey.length; i++){
                    newKey[i] = res[i%res.length];
                }
                res = newKey;
            }

            return res;
        } catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * https://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example
     * @param key
     * @param value
     * @return
     */
    public static String encrypt(String key, String value){
        try{
            byte[] keyBytes = getKey(key);
            byte[] ivBytes = keyBytes;
            ArrayUtils.reverse(ivBytes);

            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec sKeySpec = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return URLEncoder.encodeToString(encrypted);
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * https://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example
     * @param key
     * @param encrypted
     * @return
     */
    public static String decrypt(String key, String encrypted){
        try{
            byte[] keyBytes = getKey(key);
            byte[] ivBytes = keyBytes;
            ArrayUtils.reverse(ivBytes);

            IvParameterSpec iv = new IvParameterSpec(ivBytes);
            SecretKeySpec sKeySpec = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

            byte[] original = cipher.doFinal(URLEncoder.decodeFromString(encrypted));
            return new String(original);
        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
