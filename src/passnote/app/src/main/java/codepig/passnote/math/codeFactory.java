package codepig.passnote.math;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}
