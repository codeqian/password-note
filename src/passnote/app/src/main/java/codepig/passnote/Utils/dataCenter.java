package codepig.passnote.Utils;

import android.util.Log;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据的公共类
 * Created by QZD on 2015/10/14.
 */
public class dataCenter {
    public static List<AccountData> dataList;//数据列表
    public static String theWords;//口令

    /**
     * 遍历dataList
     */
    public static void logAllData(){
        for (int i=0;i<dataList.size();i++){
            AccountData _data=dataList.get(i);
            Log.d("LOGCAT","dataList "+i+":"+_data.paperId+"-"+_data.paperName+"-"+_data.account+"-"+_data.password+"-"+_data.info);
        }
    }

    /**
     * 根据名称查询
     */
    public static int searchByName(String _nameKey,int _index){
        Pattern pattern = Pattern.compile(_nameKey);
        Matcher matcher;
        int startIndex=_index;
        if(startIndex!=0){
            startIndex++;
        }
        Log.d("LOGCAT","start at:"+startIndex);
        for (int i=startIndex;i<dataList.size();i++){
            matcher = pattern.matcher(dataList.get(i).paperName);
            if(matcher.find()) {
                Log.d("LOGCAT","find at:"+i);
                return i;
            }
        }
        return -1;
    }
}
