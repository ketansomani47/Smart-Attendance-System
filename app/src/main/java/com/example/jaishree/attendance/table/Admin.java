package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Lenovo G580 on 07-07-2017.
 */

public class Admin {
    public static final String createQuery="CREATE TABLE `admin_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`mobile`\tTEXT,\n" +
            "\t`gender`\tTEXT,\n" +
            "\t`address`\tTEXT,\n" +
            "\t`email`\tTEXT,\n" +
            "\t`password`\tTEXT,\n" +
            "\t`image`\tTEXT\n" +
            ");";

    public static final String TABLE_NAME="admin_tbl";
    public static final String ID="id";
    public static final String NAME="name";
    public static final String MOBILE="mobile";
    public static final String GENDER="gender";
    public static final String ADDRESS="address";
    public static final String EMAIL="email";
    public static final String PASSWORD="password";
    public static final String IMAGE="image";

    public static void createTable(SQLiteDatabase sqliteStabase){
        sqliteStabase.execSQL(createQuery);
        ContentValues cv=new ContentValues();
        cv.put(EMAIL,"admin@admin");
        cv.put(PASSWORD,"admin");
        cv.put(NAME,"");
        cv.put(MOBILE,"");
        cv.put(IMAGE,"");
        cv.put(GENDER,"");
        cv.put(ADDRESS,"");
        insert(sqliteStabase,cv);
    }

    public static void updateTable(SQLiteDatabase sqLiteDatabase){
        String updateQuery= " drop table if exists " + TABLE_NAME;
        sqLiteDatabase.execSQL(updateQuery);
        createTable(sqLiteDatabase);
    }

    public  static  long insert(SQLiteDatabase sqLiteDatabase, ContentValues contentValues){
        return sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public static Cursor select(SQLiteDatabase sqLiteDatabase,String selection){
        return sqLiteDatabase.query(TABLE_NAME,null,selection,null,null,null,null,null);
    }

    public static int update(SQLiteDatabase sqLiteDatabase,ContentValues contentValues,String selection){
        return sqLiteDatabase.update(TABLE_NAME,contentValues,selection,null);
    }

    public static int delete(SQLiteDatabase sqLiteDatabase,String selection){
        return sqLiteDatabase.delete(TABLE_NAME,selection,null);
    }
}
