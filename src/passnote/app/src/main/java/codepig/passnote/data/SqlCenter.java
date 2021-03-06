package codepig.passnote.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import codepig.passnote.Utils.AccountData;
import codepig.passnote.Utils.config;
import codepig.passnote.Utils.dataCenter;
import codepig.passnote.Utils.CodeFactory;

/**
 * 数据库操作
 * Created by QZD on 2015/9/29.
 */
public class SqlCenter {
    private static SqlHelper dbManager;
    private static SQLiteDatabase mDB;
    public static void initSqlManager(SqlHelper _dbManager){
        dbManager=_dbManager;
        mDB=dbManager.getWritableDatabase();
    }

    /**
     * 获取数据库中的所有条目
     */
    public static void getList(){
        String[] columns = new String[] { "_id", "name", "ac", "pw", "info"};
        String groupBy = "";
        String having = "";
        String orderBy = "";
        String limit = "";
        Cursor c = mDB.query(config.LISTTABLENAME, columns, null, null, groupBy, having, orderBy, limit);
        c.moveToFirst();
        int listLenth=c.getCount();
        if(listLenth>0){
            for(int i=0;i<listLenth;i++) {
                AccountData acInfo=new AccountData();
                acInfo.paperId=c.getInt(0);
                acInfo.paperName= CodeFactory.decodeWords(dataCenter.theWords,c.getString(1));
                acInfo.account= CodeFactory.decodeWords(dataCenter.theWords, c.getString(2));
                acInfo.password= CodeFactory.decodeWords(dataCenter.theWords, c.getString(3));
                acInfo.info= CodeFactory.decodeWords(dataCenter.theWords, c.getString(4));
                dataCenter.dataList.add(acInfo);
//                Log.d("LOGCAT","data:"+acInfo.paperId+"-"+acInfo.paperName);
                c.moveToNext();
            }
        }
        c.close();
    }

    /**
     *  数据库中插入条目
     */
    public static long insDataInDB(String _name,String _ac,String _pw,String _info){
        ContentValues cv=new ContentValues();
        cv.put("name", CodeFactory.encodeWords(dataCenter.theWords,_name));
        cv.put("ac", CodeFactory.encodeWords(dataCenter.theWords,_ac));
        cv.put("pw", CodeFactory.encodeWords(dataCenter.theWords,_pw));
        cv.put("info", CodeFactory.encodeWords(dataCenter.theWords,_info));
        //插入下载记录，插入失败会返回-1
        long _id=mDB.insert(config.LISTTABLENAME, "null",cv);
        if(_id>=0) {
            return _id;
        }else{
            return -1;
        }
    }

    /**
     *  更新数据库中的条目
     */
    public static long updataInDB(String _mid,String _name,String _ac,String _pw,String _info){
        String selection = "_id=?";
        String[] selectionArgs = new String[] {_mid};
        ContentValues values = new ContentValues();
        values.put("name", CodeFactory.encodeWords(dataCenter.theWords,_name));
        values.put("ac", CodeFactory.encodeWords(dataCenter.theWords, _ac));
        values.put("pw", CodeFactory.encodeWords(dataCenter.theWords,_pw));
        values.put("info", CodeFactory.encodeWords(dataCenter.theWords,_info));
        long _id=mDB.update(config.LISTTABLENAME, values, selection, selectionArgs);
        if(_id>=0) {
            return _id;
        }else{
            return -1;
        }
    }

    /**
     *  删除数据库中的条目
     */
    public static long delDataInDB(String _id){
        try{
            String table = config.LISTTABLENAME;
            String selection = "_id=?";
            String[] selectionArgs = new String[] { _id };
            long dbId=mDB.delete(table, selection, selectionArgs);
            if(dbId>=0) {
                return dbId;
            }else{
                return -1;
            }
        }catch (Exception e){}
        return -1;
    }

    /**
     * 关闭数据库对象
     */
    public static void closeDB(){
        mDB.close();
    }
}
