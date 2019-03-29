package chat.safe.safechat;

import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Created by Peter on 3/29/19.
 */

public class Hasher {
    public static String generateStrongPasswordHash(String password){
        try {
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + android.util.Base64.encodeToString(salt, android.util.Base64.DEFAULT) + ":" + android.util.Base64.encodeToString(hash, android.util.Base64.DEFAULT);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean validatePassword(String originalPassword, String storedPassword){
        try {
            String[] parts = storedPassword.split(":");
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT);
            byte[] hash = android.util.Base64.decode(parts[2], android.util.Base64.DEFAULT);

            PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            int diff = hash.length ^ testHash.length;
            for(int i = 0; i < hash.length && i < testHash.length; i++){
                diff |= hash[i] ^ testHash[i];
            }
            return diff == 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

    }

    private static byte[] getSalt(){
        try {
            SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            rand.nextBytes(salt);
            return salt;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
