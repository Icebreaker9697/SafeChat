package hello;

import org.apache.commons.lang3.ArrayUtils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Peter on 3/26/19.
 */

public class SymmetricCipher {
    public static String generateRandomKey(){
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16]; // 128 bits are converted to 16 bytes;
        random.nextBytes(bytes);

        return URLEncoder.encodeToString(bytes);
    }

    private static byte[] getKey(String key, boolean isKeyGenerated){
        if(isKeyGenerated){
            return URLEncoder.decodeFromString(key);
        }
        //else decode it normally
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

    public static String encrypt(String key, String value){
        return encrypt(key, value, false);
    }

    /**
     * https://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example
     * @param key
     * @param value
     * @return
     */
    public static String encrypt(String key, String value, boolean isKeyGenerated){
        try{
            byte[] keyBytes = getKey(key, isKeyGenerated);
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

    public static String decrypt(String key, String encrypted){
        return decrypt(key, encrypted, false);
    }

    /**
     * https://stackoverflow.com/questions/15554296/simple-java-aes-encrypt-decrypt-example
     * @param key
     * @param encrypted
     * @return
     */
    public static String decrypt(String key, String encrypted, boolean isKeyGenerated){
        try{
            byte[] keyBytes = getKey(key, isKeyGenerated);
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
