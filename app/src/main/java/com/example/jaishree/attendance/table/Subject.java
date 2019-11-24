package com.example.jaishree.attendance.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Lenovo G580 on 04-07-2017.
 */

public class Subject {
    public static String createQuery= "CREATE TABLE `subject_tbl` (\n" +
            "\t`id`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`name`\tTEXT,\n" +
            "\t`branch_id`\tINTEGER,\n" +
            "\t`semester`\tTEXT\n" +
            ");";

    public static final String TABLE_NAME="subject_tbl";
    public static final String ID="id";
    public static final String SUBJECT="name";
    public static final String BRANCH_ID="branch_id";
    public static final String SEMESTER="semester";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(createQuery);
    }

    public static void updateTable(SQLiteDatabase db){
        String updateQuery=" drop table if exists "+ TABLE_NAME;
        db.execSQL(updateQuery);
        createTable(db);
    }

    public static long insert(SQLiteDatabase db, ContentValues contentValues){
        return db.insert(TABLE_NAME,null,contentValues);
    }

    public static Cursor select(SQLiteDatabase db,String selection){
        String order=Subject.SEMESTER;
        return db.query(TABLE_NAME,null,selection,null,null,null,order+" ASC",null);
    }
    public static int delete(SQLiteDatabase db,String selection){
        return db.delete(TABLE_NAME,selection,null);
    }

    public static int update(SQLiteDatabase db,ContentValues contentValues,String selection){
        return db.update(TABLE_NAME,contentValues,selection,null);
    }
}
