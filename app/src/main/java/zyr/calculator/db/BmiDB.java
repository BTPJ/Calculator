package zyr.calculator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建一个BMI查询记录的数据库
 *
 * @author LTP 2015年11月24日
 */
public class BmiDB extends SQLiteOpenHelper {
    public static final String CREATE_ZHAOZHA = "create table zhaozha(id integer primary key autoincrement,height double,weight double,bmi double,result text,currentMillis long)";

    public BmiDB(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 执行创建数据库的逻辑
        db.execSQL(CREATE_ZHAOZHA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 执行升级数据库的逻辑
    }
}
