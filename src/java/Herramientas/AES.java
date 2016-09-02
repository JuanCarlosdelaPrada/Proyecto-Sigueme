package Herramientas;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Je¡ZZ¡
 */
public abstract class AES {
    
    //AESCrypt-ObjC uses CBC and PKCS5Padding
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";
    private static final String CHARSET = "UTF-8";

    //AESCrypt-ObjC uses SHA-256 (and so a 256-bit key)
    private static final String HASH_ALGORITHM = "SHA-256";

    //AESCrypt-ObjC uses IV initialisation
    private static byte[] ivBytes = new byte[16]; 
    
    //AESCrypt-ObjC uses password
    private static final String password = "El dolor es temporal, el orgullo es para siempre";
    
     /**
     * 
     * @return ivBytes
     */
    public static byte[] getIvBytes() {
        return ivBytes;
    }
    
     /**
     * 
     * @param aIvBytes the ivBytes to set
     */
    public static void setIvBytes(byte[] aIvBytes) {
        ivBytes = aIvBytes;
    }
    
    /**
     * Generates initialisation vector 
     *
     * @return iv vector
     */
    private static void generateivBytes() {
        SecureRandom random = new SecureRandom();
        random.nextBytes(ivBytes);
    }
    
    /**
     * Generates SHA256 hash of the password which is used as key
     *
     * @return SHA256 of the password
     */
    private static SecretKeySpec generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

    /**
     * Encrypt and encode message using 256-bit AES with key generated from password.
     *
     *
     * @param message the thing you want to encrypt assumed String UTF-8
     * @return Base64 encoded CipherText
     */
    public static String encrypt(String message){
        try {
            final SecretKeySpec key = generateKey();

            AES.generateivBytes();
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] cipherText = cipher.doFinal(message.getBytes(CHARSET));

            String encoded = Base64.getEncoder().encodeToString(cipherText);
            return encoded;
        } catch (UnsupportedEncodingException e) {
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Decrypt and decode ciphertext using 256-bit AES with key generated from password
     *
     * @param base64EncodedCipherText the encrpyted message encoded with base64
     * @return message in Plain text (String UTF-8)
     */
    public static String decrypt(String base64EncodedCipherText){
        try {
            final SecretKeySpec key = generateKey();

            byte[] decodedCipherText = Base64.getDecoder().decode(base64EncodedCipherText);;
            
            final Cipher cipher = Cipher.getInstance(AES_MODE);
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decryptedBytes = cipher.doFinal(decodedCipherText);

            String message = new String(decryptedBytes, CHARSET);

            return message;
        } catch (UnsupportedEncodingException e) {
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(AES.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
        /**
     * Converts byte array to hexidecimal useful for logging and fault finding
     * @param bytes
     * @return
     */
    private static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static String convert(int n) {
        return Integer.toHexString(n);
    }
    public static void main(String[] args) {
        /*
        [117, 74, 76, -73, 27, -64, -25, -77, 68, 50, -84, -116, -128, -43, 62, 88]
        BUC9QJmeMMofCOWS1UclCitgj6jbD9B28/Geqlnp7b0=
        */
        String a = AES.encrypt("Ñapá de la Cañá");
        System.out.println(a);
        
        byte[] xa = AES.getIvBytes();
        System.out.println(AES.decrypt(a));
        
        a = AES.encrypt("Ñapá de la Cañá");
        System.out.println(a);
        
        AES.setIvBytes(xa);
        System.out.println(AES.decrypt(a));
    }
}