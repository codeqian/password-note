package codepig.passnote.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import codepig.passnote.Utils.config;


/**
 * 管理sql数据
 * Created by QZD on 2015/9/28.
 */
public class sqlHelper extends SQLiteOpenHelper {

    public sqlHelper(Context context)
    {
        super(context, config.APPDBNAME, null, 2);//this(context, name, factory, version, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        Log.d("LOGCAT", "creatDb");
        //创建数据库时创建下载信息表
        db.execSQL("CREATE TABLE " + config.LISTTABLENAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR, ac VARCHAR, pw VARCHAR, info VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
        Log.d("LOGCAT", "Version:" + oldVersion + " to " + newVersion);
        if(oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + config.LISTTABLENAME);
        }
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // TODO 每次成功打开数据库后首先被执行
    }
}
