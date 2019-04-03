package hello;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.Cipher;

/**
 * Created by Peter on 3/29/19.
 */
public class RSACipher {		
	
    public static String[] generateKeyPair(){
        SecureRandom rand = new SecureRandom();
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, rand);
            KeyPair kp = kpg.generateKeyPair();

            PublicKey keyPublic = kp.getPublic();
            PrivateKey keyPrivate = kp.getPrivate();

            String[] res = new String[2];
            res[0] = savePublicKey(keyPublic);
            res[1] = savePrivateKey(keyPrivate);

            return res;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptWithPublic(String publickey, String value){
        try {
            PublicKey key = loadPublicKey(publickey);
            Cipher cipherEncrypt = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipherEncrypt.doFinal(value.getBytes());
            return URLEncoder.encodeToString(encrypted);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptWithPrivate(String privatekey, String value){
        try {
            PrivateKey key = loadPrivateKey(privatekey);
            Cipher cipherEncrypt = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC");
            cipherEncrypt.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipherEncrypt.doFinal(value.getBytes());
            return URLEncoder.encodeToString(encrypted);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptWithPublic(String publickey, String encrypted){
        try {
            PublicKey key = loadPublicKey(publickey);
            Cipher cipherDecrypt = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, key);
            byte[] original = cipherDecrypt.doFinal(URLEncoder.decodeFromString(encrypted));
            return new String(original);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptWithPrivate(String privatekey, String encrypted){
        try {
            PrivateKey key = loadPrivateKey(privatekey);
            Cipher cipherDecrypt = Cipher.getInstance("RSA/NONE/OAEPPadding", "BC");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, key);
            byte[] original = cipherDecrypt.doFinal(URLEncoder.decodeFromString(encrypted));
            return new String(original);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * https://stackoverflow.com/questions/9755057/converting-strings-to-encryption-keys-and-vice-versa-java
     * @param key64
     * @return
     */
    public static PrivateKey loadPrivateKey(String key64){
        try {
            byte[] clear = URLEncoder.decodeFromString(key64);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey priv = fact.generatePrivate(keySpec);
            Arrays.fill(clear, (byte) 0);
            return priv;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * https://stackoverflow.com/questions/9755057/converting-strings-to-encryption-keys-and-vice-versa-java
     * @param stored
     * @return
     */
    public static PublicKey loadPublicKey(String stored){
        try {
            byte[] data = URLEncoder.decodeFromString(stored);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            return fact.generatePublic(spec);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * https://stackoverflow.com/questions/9755057/converting-strings-to-encryption-keys-and-vice-versa-java
     * @param priv
     * @return
     */
    public static String savePrivateKey(PrivateKey priv){
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec spec = fact.getKeySpec(priv, PKCS8EncodedKeySpec.class);
            byte[] packed = spec.getEncoded();
            String key64 = URLEncoder.encodeToString(packed);
            Arrays.fill(packed, (byte) 0);
            return key64;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * https://stackoverflow.com/questions/9755057/converting-strings-to-encryption-keys-and-vice-versa-java
     * @param publ
     * @return
     */
    public static String savePublicKey(PublicKey publ){
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec spec = fact.getKeySpec(publ, X509EncodedKeySpec.class);
            return URLEncoder.encodeToString(spec.getEncoded());
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
