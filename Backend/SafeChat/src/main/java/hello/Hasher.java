package hello;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Peter on 3/29/19.
 */

public class Hasher {
	
	//private static final Logger log = LoggerFactory.getLogger(Hasher.class);
	
	/*public static void main(String[] args) {
		String pass = "password";
        System.out.println("Set password is " + pass);
        System.out.println();

        String hash = Hasher.generateStrongPasswordHash(pass);
        System.out.println("Hash generated from pass is: " + hash);
        System.out.println();

        String check1 = "qwertyuiop";
        boolean isSamePass1 = Hasher.validatePassword(check1, hash);
        System.out.println("Does " + check1 + "pass? " + isSamePass1);

        String check2 = "qwertyuio";
        boolean isSamePass2 = Hasher.validatePassword(check2, hash);
        System.out.println("Does " + check2 + "pass? " + isSamePass2);
	}*/
	
    /*public static String generateStrongPasswordHash(String password){
        try {
            int iterations = 1000;
            char[] chars = password.toCharArray();
            byte[] salt = getSalt();

            PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return iterations + ":" + URLEncoder.encodeToString(salt) + ":" + URLEncoder.encodeToString(hash);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }*/

    public static boolean validatePassword(String enteredPassword, String storedPassword){
        try {        	
        	
            String[] parts = storedPassword.split(":");
            
            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = URLEncoder.decodeFromString(parts[1]);
            byte[] hash = URLEncoder.decodeFromString(parts[2]);

            PBEKeySpec spec = new PBEKeySpec(enteredPassword.toCharArray(), salt, iterations, hash.length * 8);
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
    
    /*
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
    }*/
}
