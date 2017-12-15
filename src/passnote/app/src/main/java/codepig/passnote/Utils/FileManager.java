package codepig.passnote.Utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import codepig.passnote.data.SqlCenter;

/**
 * 本地文件管理
 * Created by QZD on 2015/12/3.
 */
public class FileManager {
    private static File txtFile;
    /**
     * 获取SDCard根目录
     */
    private static String sdcardRoot="";
    /**
     * 要保存的目录
     */
    public static String filepath="/passnote/";
    private static String backupFile="/passnote/sqlbak.nbk";
    /**
     * 文件名
     */
    private static String fileName="passBak";
    public static Boolean recovered=false;
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
                    AccountData mData=dataCenter.dataList.get(i);
                    _msg+=mData.paperName+",";
                    _msg+=mData.account+",";
                    _msg+=mData.password+",";
                    _msg+=mData.info;
                    if(i<dataCenter.dataList.size()-1){
                        _msg+="|";
                    }
                }
                FileOutputStream outStream = new FileOutputStream(sdcardRoot+filepath+fileName+".txt",false);
                OutputStreamWriter writer = new OutputStreamWriter(outStream,"gb2312");
                writer.write(_msg);
                writer.flush();
                writer.close();
                outStream.close();
                return "导出成功，该文件为明文，您可要藏好哦。";
            }catch (Exception e){
            }
        }else {
            return "没有外部存储";
        }
        return "写入失败";
    }

    /**
     * 备份加密文件
     * @return
     */
    public static String dataBackup(){
        String _msg="";
        if(checkSdcard()){
            try{
                for (int i=0;i< dataCenter.dataList.size();i++){
                    AccountData mData=dataCenter.dataList.get(i);
                    _msg+=mData.paperName+",";
                    _msg+=mData.account+",";
                    _msg+=mData.password+",";
                    _msg+=mData.info;
                    if(i<dataCenter.dataList.size()-1){
                        _msg+="|";
                    }
                }
                FileOutputStream outStream = new FileOutputStream(sdcardRoot+backupFile,false);
                OutputStreamWriter writer = new OutputStreamWriter(outStream,"UTF-8");
                writer.write(CodeFactory.encodeWords(dataCenter.theWords,_msg));
                writer.flush();
                writer.close();
                outStream.close();
                return "备份成功。";
            }catch (Exception e){
            }
        }else {
            return "没有外部存储";
        }
        return "写入失败";
    }

    /**
     * 恢复数据
     * @return
     */
    public static String recoverData(){
        String _errWords="导入失败-";
        checkSdcard();
        String _path=sdcardRoot+backupFile;
        File file = new File(_path);
        if (file.exists())
        {
            try{
                String _msg;
                FileInputStream inStream = new FileInputStream(_path);
                byte[] buffer = new byte[inStream.available()];
                inStream.read(buffer);
                inStream.close();
                _msg = CodeFactory.decodeWords(dataCenter.theWords, new String(buffer, "UTF-8"));
                String[] dataList = _msg.split("\\|");
                for (int i=0;i< dataList.length;i++){
                    String[] infoList = dataList[i].split(",");
                    if(dataCenter.searchByName(infoList[0],0)==-1) {
                        AccountData acInfo = new AccountData();
                        acInfo.paperName = infoList[0];
                        acInfo.account = infoList[1];
                        try {
                            acInfo.password = infoList[2];
                        }catch (Exception e){
                            acInfo.password="";
                        }
                        try {
                            acInfo.info = infoList[3];
                        }catch (Exception e){
                            acInfo.info="";
                        }
                        long _id= SqlCenter.insDataInDB(acInfo.paperName,acInfo.account,acInfo.password,acInfo.info);
                        acInfo.paperId=_id;
                        dataCenter.dataList.add(acInfo);
                    }
                }
//                Log.d("LOGCAT","dataCenter.dataList:"+dataCenter.dataList.size());
                recovered=true;
                _errWords="恢复成功";
            }catch (Exception e){
            }
        }else {
            _errWords="数据文件不存在";
        }
        return _errWords;
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
