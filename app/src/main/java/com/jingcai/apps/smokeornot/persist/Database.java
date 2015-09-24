package com.jingcai.apps.smokeornot.persist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jingcai.apps.smokeornot.entity.SmokeRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JasonDing on 15/9/22.
 */
public class Database {
    private static final String TAG = "Database";
    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;
    private static Database mDatabase;
    private final Context mContext;

    private static final String DATABASE_NAME = "db_quitSmoke";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SMOKE_RECORD_NAME = "tb_smoke_record";
    private static final String COLUMN_SMOKE_RECORD_ID = "recored_id";
    private static final String COLUMN_SMOKE_RECORD_DATE = "recored_date";
    private static final String COLUMN_SMOKE_RECORD_IS_SMOKED = "is_smoked";
    private static final java.lang.String SMOKE_RECORD_TABLE_CREATE = "create table if not exists " + TABLE_SMOKE_RECORD_NAME
            +"( " + COLUMN_SMOKE_RECORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            +COLUMN_SMOKE_RECORD_DATE+" LONG NOT NULL,"
            +COLUMN_SMOKE_RECORD_IS_SMOKED +" INTEGER NOT NULL DEFAULT 0)";

    public Database(Context context) {
        mContext = context;
        mDbHelper = new DatabaseHelper(context);
    }

    private class DatabaseHelper extends SQLiteOpenHelper {


        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SMOKE_RECORD_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            switch (newVersion){
                case 2:
                    //进行新字段或新表的创建，数据迁移
                    break;
            }
        }
    }

    public static Database getInstance(Context context){
        if(null == mDatabase){
            synchronized (Database.class){
                if(null == mDatabase){
                    mDatabase = new Database(context);
                }
            }
        }
        return mDatabase;
    }

    public Database open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (Exception e) {
            mDb = mDbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        mDbHelper.close();  //It also close db
    }

    public List<SmokeRecord> getSmokeRecords(String selection,String[] args,String orderBy,String limit){
        ensureDbOpen();
        mDb.beginTransaction();
        Cursor query = mDb.query(TABLE_SMOKE_RECORD_NAME,
                new String[]{COLUMN_SMOKE_RECORD_ID, COLUMN_SMOKE_RECORD_DATE, COLUMN_SMOKE_RECORD_IS_SMOKED},
                selection, args, null, null, orderBy,limit);
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        List<SmokeRecord> smokeRecords = new ArrayList<>();
        SmokeRecord smokeRecord;
        while (query.moveToNext()){
            smokeRecord = new SmokeRecord(
                    query.getInt(query.getColumnIndex(COLUMN_SMOKE_RECORD_ID)),
                    new Date(query.getLong(query.getColumnIndex(COLUMN_SMOKE_RECORD_DATE))),
                    query.getInt(query.getColumnIndex(COLUMN_SMOKE_RECORD_IS_SMOKED)) == 1);
            smokeRecords.add(smokeRecord);
        }
        Log.i(TAG,"query success,record size : " + smokeRecords);
        query.close();
        return smokeRecords;
    }

    public List<SmokeRecord> getSmokeRecords(int maxSize,int offset,boolean hasSmoked){
        return getSmokeRecords(COLUMN_SMOKE_RECORD_IS_SMOKED+"=?",new String[]{hasSmoked ? "1" : "0"},COLUMN_SMOKE_RECORD_DATE,offset+","+maxSize);
    }

    private void ensureDbOpen() {
        if(mDb == null || !mDb.isOpen()){
            throw new IllegalStateException("Database hasn't opened,Call open First.");
        }
    }

    public int getSmokeCount(long startDate , long endDate, boolean hasSmoked){
        int count = 0;
        if(startDate > endDate){
            endDate = endDate - startDate;
            startDate = startDate + endDate;
            endDate = startDate - endDate;
        }
        ensureDbOpen();
        mDb.beginTransaction();
        String sql = "select count(*) from " + TABLE_SMOKE_RECORD_NAME + " where " + COLUMN_SMOKE_RECORD_DATE + " between ? and ? and " + COLUMN_SMOKE_RECORD_IS_SMOKED + "=?";
        Log.i(TAG,sql);
        Cursor cursor = mDb.rawQuery(sql, new String[]{String.valueOf(startDate), String.valueOf(endDate), hasSmoked ? "1" : "0"});
        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        cursor.close();
        return count;
    }

    public int getSmokeCount(boolean hasSmoked){
        int count = 0;

        ensureDbOpen();
        mDb.beginTransaction();
        String sql = "select count(*) from " + TABLE_SMOKE_RECORD_NAME + " where " + COLUMN_SMOKE_RECORD_IS_SMOKED + "=?";
        Log.i(TAG,sql);
        Cursor cursor = mDb.rawQuery(sql, new String[]{hasSmoked ? "1" : "0"});
        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
        cursor.close();
        return count;
    }

    public void addSmokeRecord(SmokeRecord smokeRecord){
        ensureDbOpen();
        if(null == smokeRecord){
            throw new NullPointerException("smokeRecord can't not be null");
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_SMOKE_RECORD_DATE,smokeRecord.getSmokeDate().getTime());
        values.put(COLUMN_SMOKE_RECORD_IS_SMOKED,smokeRecord.isSmoke()?1:0);
        mDb.beginTransaction();
        mDb.insert(TABLE_SMOKE_RECORD_NAME, null, values);
        mDb.setTransactionSuccessful();
        mDb.endTransaction();
    }




}
