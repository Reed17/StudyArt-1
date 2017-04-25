package ua.artcode.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhenia on 23.04.17.
 */
public class SecurityUtils {
    public String encryptPass(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] digest = md.digest(pass.getBytes());

            BigInteger bigInt = new BigInteger(1, digest);

            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
