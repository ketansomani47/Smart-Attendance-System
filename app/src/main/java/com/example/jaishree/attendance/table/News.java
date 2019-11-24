package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jaishree.attendance.database.MySqliteOpenHelper;

/**
 * Created by Lenovo G580 on 03-07-2017.
 */

public class News {
    public static final String createQuery= "CREATE TABLE `news_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`time`\tTEXT\n" +
            ");";

    public static final String TABLE_NAME="news_tbl";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String DATE="date";
    public static final String TIME="time";

    public static void createTable(SQLiteDatabase sqliteDatabase){
        sqliteDatabase.execSQL(createQuery);
    }

    public static void updateTable(SQLiteDatabase sqLiteDatabase){
        String updateQuery= " drop table if exists " + TABLE_NAME;
        sqLiteDatabase.execSQL(updateQuery);
        createTable(sqLiteDatabase);
    }

    public static long insert(SQLiteDatabase sqLiteDatabase, ContentValues contentValues){
        return sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public static Cursor select(SQLiteDatabase sqLiteDatabase, String selection){
        String orderby= News.ID ;
        return sqLiteDatabase.query(TABLE_NAME,null,selection,null,null,null,orderby+" DESC",null);
    }

    public static int update(SQLiteDatabase sqLiteDatabase,ContentValues contentValues,String seletion){
    return sqLiteDatabase.update(TABLE_NAME,contentValues,seletion,null);
    }

    public static int delete(SQLiteDatabase sqLiteDatabase,String selection){
        return sqLiteDatabase.delete(TABLE_NAME,selection,null);
    }
}
