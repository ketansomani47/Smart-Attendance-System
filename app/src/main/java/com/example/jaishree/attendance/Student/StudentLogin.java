package com.example.jaishree.attendance.Student;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

public class StudentLogin extends AppCompatActivity {
    Button loginBtn;
    EditText editTextEmail,editTextPass;
    String enteredSEmail="",enteredSPass="",sPass="";
    int studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isStudentLogin()){
            Intent i=new Intent(StudentLogin.this,StudentPanel.class);
            startActivity(i);
        }
        init();
        methodListener();

    }

    private boolean isStudentLogin() {
        SharedPreferences preference =getSharedPreferences("myFile",MODE_PRIVATE);
        return preference.getBoolean("isStudentLogin",false);
    }

    private void init() {
        loginBtn= (Button) findViewById(R.id.loginBtn);
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPass= (EditText) findViewById(R.id.editTextPass);
    }

    private void methodListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkStudentData();
            }
        });
    }

    private void checkStudentData() {
        enteredSEmail=editTextEmail.getText().toString();
        enteredSPass=editTextPass.getText().toString();
        if(enteredSEmail.equals("") && enteredSPass.equals("")){
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
        else if(enteredSEmail.equals("")){
            editTextEmail.setError("Enter Email");
        }
        else if(enteredSPass.equals("")){
            editTextEmail.setError(null);
            editTextPass.setError("Enter Password");
        }
        else{
            editTextPass.setError(null);
        String whereClause= Student.STUDENT_EMAIL + "='" + enteredSEmail + "'";
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(StudentLogin.this);
        SQLiteDatabase database=mysqliteHelper.getReadableDatabase();
        Cursor curs=Student.select(database,whereClause);
        if(curs!=null && curs.moveToNext()) {
            sPass=curs.getString(6);
            studentId=curs.getInt(0);
        if(enteredSPass.equals(sPass)){
            Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            storeStudentId();
            storeStudentSession();
            Intent p=new Intent(StudentLogin.this,StudentPanel.class);
            editTextEmail.setText("");
            editTextPass.setText("");
            finish();
            startActivity(p);
        }
            else{
            Toast.makeText(this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
        }
        }

        }
    }

    private void storeStudentId() {
        SharedPreferences pref=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editer=pref.edit();
        editer.putInt("StudentId",studentId);
        editer.commit();
    }

    private void storeStudentSession() {
        SharedPreferences prefer=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor edito=prefer.edit();
        edito.putBoolean("isStudentLogin",true);
        edito.commit();

    }

}
