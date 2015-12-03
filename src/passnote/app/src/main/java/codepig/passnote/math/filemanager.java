package codepig.passnote.math;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import codepig.passnote.Utils.accountData;
import codepig.passnote.Utils.dataCenter;

/**
 * 本地文件管理
 * Created by QZD on 2015/12/3.
 */
public class filemanager {
    private static File txtFile;
    /**
     * 获取SDCard根目录
     */
    private static String sdcardRoot="";
    /**
     * 要保存的目录
     */
    public static String filepath="/passnote/";
    /**
     * 文件名
     */
    private static String fileName="passBak";
    /**
     * 将字符串保存为txt文件
     * @param _msg
     */
    public static String saveList2txt(String _path){
        filepath=_path;
        String _msg="";
        if(checkSdcard()){
            try{
                for (int i=0;i< dataCenter.dataList.size();i++){
                    accountData mData=dataCenter.dataList.get(i);
                    _msg+=mData.paperName+",";
                    _msg+=mData.account+",";
                    _msg+=mData.password+",";
                    _msg+=mData.info+"|";
                }
                FileOutputStream outStream = new FileOutputStream(sdcardRoot+filepath+fileName+".txt",false);
                OutputStreamWriter writer = new OutputStreamWriter(outStream,"gb2312");
                writer.write(_msg);
                writer.flush();
                writer.close();
                outStream.close();
                return "导出成功.";
            }catch (Exception e){
            }
        }else {
            return "没有外部存储";
        }
        return "写入失败";
    }

    /**
     * 检查sd卡是否存在
     */
    private static boolean checkSdcard(){
        String _path="";
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            if(sdcardRoot.equals("")){
                sdcardRoot= android.os.Environment.getExternalStorageDirectory().toString();
            }
            _path=sdcardRoot+filepath;
            creatDir(_path);
            return true;
        }
        return false;
    }

    //创建文件目录
    private static boolean creatDir(String _path){
        try{
            File file = new File(_path);
            if (!file.exists())
            {
                Log.d("LOGCAT", "creat dir:" + _path);
                if (file.mkdirs())
                {
                    return true;
                }
            }else{
                return true;
            }
        }catch (Exception e){
        }
        return false;
    }
}
