package com.example.jaishree.attendance.Teacher;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.TakeAttendanceAdapter;
import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Attendance;
import com.example.jaishree.attendance.table.Student;
import com.example.jaishree.attendance.table.Subject;

import java.util.ArrayList;
import java.util.Arrays;

public class TakeAttendance extends AppCompatActivity {
    int clickId, branch_id;
    ListView listViewTakeAttendance;
    String semester = "";
    CheckBox checkBoxP, checkBoxA, checkBoxL;
    ArrayList<TakeAttendancePojo> arrayList = new ArrayList<>();
    TakeAttendanceAdapter takeattendanceAdapter;
    int student_id,teacher_id;
    String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);
        clickId = getIntent().getIntExtra("listClickId", 0);
        Log.d("123", "onCreate: " + clickId);

        init();
        takeattendanceAdapter = new TakeAttendanceAdapter(this, R.layout.take_attendance_list_item, arrayList);
        listViewTakeAttendance.setAdapter(takeattendanceAdapter);
        getValues();
        fetchStudents();
        methodListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.take_attendance, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.attendance){
            SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
            teacher_id=preference.getInt("TeacherId",0);
            storeAttendance();
        }
        return super.onOptionsItemSelected(item);
    }

    private void methodListener() {
        checkBoxP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxL.setChecked(false);
                    checkBoxA.setChecked(false);
                    changeStatus("present");
                }
            }
        });
        checkBoxA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxL.setChecked(false);
                    checkBoxP.setChecked(false);
                    changeStatus("absent");
                }

            }
        });
        checkBoxL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    checkBoxP.setChecked(false);
                    checkBoxA.setChecked(false);
                    changeStatus("leave");
                }

            }
        });

    }

    private void changeStatus(String status) {
        for (TakeAttendancePojo pojo : arrayList) {
            pojo.setStatus(status);
            Log.d("123", "changeStatus: " + pojo.getStatus());
        }
        takeattendanceAdapter.notifyDataSetChanged();
    }

    private void getValues() {
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        String selection = Subject.ID + "='" + clickId + "'";
        Cursor cursor = Subject.select(db, selection);
        if (cursor != null && cursor.moveToNext()) {
            branch_id = cursor.getInt(2);
            semester = cursor.getString(3);
        }
    }

    private void init() {
        listViewTakeAttendance = (ListView) findViewById(R.id.listViewTakeAttendance);
        checkBoxP = (CheckBox) findViewById(R.id.checkBoxP);
        checkBoxA = (CheckBox) findViewById(R.id.checkBoxA);
        checkBoxL = (CheckBox) findViewById(R.id.checkBoxL);
    }

    private void fetchStudents() {
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(this);
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        String sel = Student.BRANCH_ID + "='" + branch_id + "' AND " + Student.SEMESTER + "='" + semester + "'";
        Cursor cursorStudent = Student.select(db, sel);
        if (cursorStudent != null) {
            while (cursorStudent.moveToNext()) {
                TakeAttendancePojo pojo = new TakeAttendancePojo();
                pojo.setStudent_id(cursorStudent.getInt(0));
                pojo.setName(cursorStudent.getString(1));
                pojo.setMobile(cursorStudent.getString(2));
                arrayList.add(pojo);
            }
            takeattendanceAdapter.notifyDataSetChanged();
        }

    }

    private void storeAttendance() {
        for(TakeAttendancePojo pojo : arrayList) {
            student_id=pojo.getStudent_id();
            status=pojo.getStatus();
            MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(this);
            SQLiteDatabase database = mysqliteHelper.getWritableDatabase();
            java.sql.Timestamp timestamp=new java.sql.Timestamp(System.currentTimeMillis());
            String date= String.valueOf(timestamp);

            ContentValues cv = new ContentValues();
            cv.put(Attendance.STUDENT_ID,student_id);
            cv.put(Attendance.STATUS,status);
            cv.put(Attendance.SUB_ID,clickId);
            cv.put(Attendance.TEACHER_ID,teacher_id);
            cv.put(Attendance.DATE,date);
            long l=Attendance.insert(database,cv);
            Log.d("123", "storeAttendance: "+ Arrays.asList(cv));
            if(l>0){
                Toast.makeText(this, "Attendance inserted successfully", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this, "not inserted attendance", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
