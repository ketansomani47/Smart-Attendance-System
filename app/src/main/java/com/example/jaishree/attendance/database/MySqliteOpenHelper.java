package com.example.jaishree.attendance.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jaishree.attendance.table.Admin;
import com.example.jaishree.attendance.table.Attendance;
import com.example.jaishree.attendance.table.BranchTable;
import com.example.jaishree.attendance.table.News;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.Teacher;

/**
 * Created by Jaishree on 30-06-2017.
 */

public class MySqliteOpenHelper extends SQLiteOpenHelper {
    private Context context;
    public MySqliteOpenHelper(Context context){
        super(context,"attendance.db",null,1);
        this.context=context;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        BranchTable.createTable(sqLiteDatabase);
        News.createTable(sqLiteDatabase);
        Subject.createTable(sqLiteDatabase);
        Teacher.createTable(sqLiteDatabase);
        Student.createTable(sqLiteDatabase);
        Admin.createTable(sqLiteDatabase);
        Attendance.createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        BranchTable.upgradeTable(sqLiteDatabase);
        News.updateTable(sqLiteDatabase);
        Subject.updateTable(sqLiteDatabase);
        Teacher.upgradeTable(sqLiteDatabase);
        Student.updateTable(sqLiteDatabase);
        Admin.updateTable(sqLiteDatabase);
        Attendance.upgradeTable(sqLiteDatabase);
    }
}
