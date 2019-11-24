package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Lenovo G580 on 06-07-2017.
 */

public class Student {
    public static String createQuery="CREATE TABLE `student_tabl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`mobile`\tTEXT,\n" +
            "\t`branch_id`\tINTEGER,\n" +
            "\t`semester`\tTEXT,\n" +
            "\t`student_email`\tTEXT,\n" +
            "\t`student_password`\tTEXT,\n" +
            "\t`parent_name`\tTEXT,\n" +
            "\t`parent_email`\tTEXT,\n" +
            "\t`parent_mobile`\tTEXT,\n" +
            "\t`parent_password`\tTEXT,\n" +
            "\t`image`\tTEXT\n" +
            ");";
    public static String TABLE_NAME="student_tabl";
    public static String ID="id";
    public static String NAME="name";
    public static String MOBILE="mobile";
    public static String BRANCH_ID="branch_id";
    public static String SEMESTER="semester";
    public static String STUDENT_EMAIL="student_email";
    public static String STUDENT_PASSWORD="student_password";
    public static String PARENT_NAME="parent_name";
    public static String PARENT_EMAIL="parent_email";
    public static String PARENT_MOBILE="parent_mobile";
    public static String PARENT_PASSWORD="parent_password";
    public static String IMAGE="image";

    public static void createTable(SQLiteDatabase sqliteDatabase){
        sqliteDatabase.execSQL(createQuery);
    }

    public static void updateTable(SQLiteDatabase sqLiteDatabase){
        String updateQuery= " drop table if exixts " + TABLE_NAME;
        sqLiteDatabase.execSQL(updateQuery);
        createTable(sqLiteDatabase);
    }

    public static long insert(SQLiteDatabase sqLiteDatabase, ContentValues contentValues){
        return  sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
    }

    public  static Cursor select(SQLiteDatabase sqLiteDatabase,String selection){
        return sqLiteDatabase.query(TABLE_NAME,null,selection,null,null,null,null,null);
    }

    public static int update(SQLiteDatabase sqLiteDatabase,ContentValues contentValues,String selection){
        return sqLiteDatabase.update(TABLE_NAME,contentValues,selection,null);
    }

    public static int delete(SQLiteDatabase sqLiteDatabase,String selection){
        return sqLiteDatabase.delete(TABLE_NAME,selection,null);
    }
}
