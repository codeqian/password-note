package codepig.passnote;

import android.test.InstrumentationTestCase;
import android.util.Log;

import codepig.passnote.Utils.CodeFactory;

/**
 * 编码单元测试
 * Created by QZD on 2015/12/16.
 */
public class codeTest extends InstrumentationTestCase {
    public void testInit(){
        testDecode("key",testEncode("key","test"));
    }
    public String testEncode(String _seed,String _key){
        String answer= CodeFactory.encodeWords(_seed, _key);
        Log.d("LOGCAT", CodeFactory.encodeWords(_seed,_key));
        return answer;
    }
    public String testDecode(String _seed,String _key){
        String answer= CodeFactory.decodeWords(_seed, _key);
        Log.d("LOGCAT", answer);
        return answer;
    }
}
