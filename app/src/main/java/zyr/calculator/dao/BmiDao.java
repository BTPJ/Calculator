package zyr.calculator.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import zyr.calculator.bean.Record;
import zyr.calculator.db.BmiDB;

/**
 * 数据库操作类
 *
 * @author LTP 2015年11月24日
 */
public class BmiDao {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "record";
    /**
     * 数据库版本号
     */
    public static final int VERSION = 1;
    private static BmiDao bmiDao;
    private SQLiteDatabase db;

    /**
     * 私有化构造器实现单例
     *
     * @param context 上下文
     */
    private BmiDao(Context context) {
        BmiDB bmiDB = new BmiDB(context, DB_NAME, null, VERSION);
        db = bmiDB.getWritableDatabase();
    }

    /**
     * 获取BmiDao的单例
     *
     * @param context 上下文
     * @return BmiDao的单例
     */
    public synchronized static BmiDao getInstance(Context context) {
        if (bmiDao == null) {
            bmiDao = new BmiDao(context);
        }
        return bmiDao;

    }

    /**
     * 存储查询记录
     *
     * @param record 查询记录实体类对象
     */
    public void saveRecord(Record record) {
        if (record != null) {
            ContentValues values = new ContentValues();
            values.put("height", record.getHeight());
            values.put("weight", record.getWeight());
            values.put("bmi", record.getBmi());
            values.put("result", record.getResult());
            values.put("currentMillis", record.getCurrentMillis());
            db.insert("zhaozha", null, values);
        }
    }

    /**
     * 删除一整行指定id的数据
     *
     * @param record 所指定要删除的id
     */
    public void deleteRecord(Record record) {
        db.delete("zhaozha", "id=?", new String[]{String.valueOf(record.getId())});
    }

    /**
     * 删除所有数据
     */
    public void deleteAllRecord() {
        db.execSQL("delete from zhaozha");
    }

    /**
     * 查询所有的记录
     *
     * @return 所有BMI历史记录
     */
    public List<Record> queryRecord() {
        List<Record> recordList = new ArrayList<>();
        Cursor cursor = db.query("zhaozha", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Record record = new Record();
                record.setId(cursor.getInt(cursor.getColumnIndex("id")));
                record.setHeight(cursor.getDouble(cursor.getColumnIndex("height")));
                record.setWeight(cursor.getDouble(cursor.getColumnIndex("weight")));
                record.setBmi(cursor.getDouble(cursor.getColumnIndex("bmi")));
                record.setResult(cursor.getString(cursor.getColumnIndex("result")));
                record.setCurrentMillis(cursor.getLong(cursor.getColumnIndex("currentMillis")));
                recordList.add(record);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return recordList;
    }
}
