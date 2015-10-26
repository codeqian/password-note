package codepig.passnote.math;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密
 * Created by QZD on 2015/9/17.
 */
public class codeFactory {
    /**
     * 口令转md5
     * @param _key
     * @return
     */
    public static String key2Md5(String _key){
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(_key.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
//            Log.d("LOGCAT","_key:"+_key+"-"+hex.toString());
            return hex.toString();
        }catch (NoSuchAlgorithmException e) {
            return null;
        }catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * 检测口令
     */
    public static String checkWordLength(String _key){
        String msg="";
        if (_key == null) {
            _key = "";
        }
        StringBuffer sBuffer = new StringBuffer(16);
        sBuffer.append(_key);
        if(sBuffer.length()>16){
            msg="tooLong";
        }
        String regex = "^[a-z0-9A-Z]+$";
        if(!_key.matches(regex)){
            msg="notLetter";
        }
        return msg;
    }

    /**
     * 文本加密（可逆）
     * @param _info
     * @return
     */
    public static String encodeWords(String seed,String _info){
//        Log.d("LOGCAT", "加密前的seed=" + seed + ",内容为:" + _info);
        byte[] result = null;
        StringBuffer sb = new StringBuffer(16);
        sb.append(seed);
        while (sb.length() < 16) {
            sb.append("0");
        }
        try {
            result = encrypt(sb.toString().getBytes(), _info.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String content = toHex(result);
//        Log.d("LOGCAT", "加密后的内容为:" + content);
        return content;
    }

    /**
     * 文本解密
     * @param _info
     * @return
     */
    public static String decodeWords(String seed,String _info){
//        Log.d("LOGCAT", "解密前的seed=" + seed + ",内容为:" + _info);
        StringBuffer sb = new StringBuffer(16);
        sb.append(seed);
        while (sb.length() < 16) {
            sb.append("0");
        }
        try {
            byte[] enc = toByte(_info);
            byte[] result = decrypt(sb.toString().getBytes(), enc);
            String coentn = new String(result);
//            Log.d("LOGCAT", "解密后的内容为:" + coentn);
            return coentn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr);
        SecretKey sKey = kgen.generateKey();
        byte[] raw = sKey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//        Cipher cipher = Cipher.getInstance("AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),16).byteValue();
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        final String HEX = "0123456789ABCDEF";
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
