package codepig.passnote.Utils;

import android.util.Log;

import java.util.List;

/**
 * 数据的公共类
 * Created by QZD on 2015/10/14.
 */
public class dataCenter {
    public static List<accountData> dataList;//数据列表
    public static String theWords;//口令

    /**
     * 遍历dataList
     */
    public static void logAllData(){
        for (int i=0;i<dataList.size();i++){
            accountData _data=dataList.get(i);
            Log.d("LOGCAT","dataList "+i+":"+_data.paperId+"-"+_data.paperName+"-"+_data.account+"-"+_data.password+"-"+_data.info);
        }
    }

    /**
     * 根据名称查询
     */
    public static int searchByName(String _name){
        for (int i=0;i<dataList.size();i++){
            if(dataList.get(i).paperName.equals(_name)){
                return i;
            }
        }
        return -1;
    }
}
