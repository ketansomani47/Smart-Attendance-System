package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Lenovo G580 on 14-07-2017.
 */

public class Attendance {
    public static String create_Query = "CREATE TABLE `attendance_tabl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`student_id`\tINTEGER,\n" +
            "\t`teacher_id`\tINTEGER,\n" +
            "\t`sub_id`\tINTEGER,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`status`\tTEXT\n" +
            ");";

    public static final String TABLE_NAME="attendance_tabl";
    public static final String ID="id";
    public static final String STUDENT_ID="student_id";
    public static final String TEACHER_ID="teacher_id";
    public static final String SUB_ID="sub_id";
    public static final String DATE="date";
    public static final String STATUS="status";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(create_Query);
    }
    public static void upgradeTable(SQLiteDatabase db){
        String updateQuery= " drop table if exists " + TABLE_NAME;
        db.execSQL(updateQuery);
        createTable(db);
    }
    public static long insert(SQLiteDatabase db, ContentValues cv){
        return db.insert(TABLE_NAME,null,cv);
    }
    public static Cursor select(SQLiteDatabase db, String selection){
        return db.query(TABLE_NAME,null,selection,null,null,null,null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }
    public static int update(SQLiteDatabase db,ContentValues cv,String selection){
        return db.update(TABLE_NAME,cv,selection,null);
    }
}
