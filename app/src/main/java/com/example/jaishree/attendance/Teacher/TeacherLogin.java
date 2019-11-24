package com.example.jaishree.attendance.Teacher;

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
import com.example.jaishree.attendance.table.Teacher;

public class TeacherLogin extends AppCompatActivity {
    EditText editTextEmail,editTextPass;
    Button loginBtn;
    int teacher_id;
    String enteredEmail="",enteredPass="",pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isUserLogin()) {
            Intent i=new Intent(TeacherLogin.this,TeacherDashboard.class);
            finish();
            startActivity(i);
        }
    init();
    methodListener();

    }

    private void init() {
        editTextEmail= (EditText) findViewById(R.id.editTextEmail);
        editTextPass= (EditText) findViewById(R.id.editTextPass);
        loginBtn= (Button) findViewById(R.id.loginBtn);
    }

    private void methodListener() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i=new Intent(TeacherLogin.this,TeacherDashboard.class);
             // startActivity(i);
                checkUser();
            }
        });

    }

    private boolean isUserLogin() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        return preference.getBoolean("isTeacherLogin",false);
    }

    private void checkUser() {
        enteredEmail = editTextEmail.getText().toString();
        enteredPass = editTextPass.getText().toString();
        if (enteredEmail.equals("") && enteredPass.equals("")) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        } else if (enteredEmail.equals("")) {
            editTextEmail.setError("Enter Email");
        } else if (enteredPass.equals("")) {
            editTextEmail.setError(null);
            editTextPass.setError("Enter Password");
        } else {
            editTextPass.setError(null);
            MySqliteOpenHelper mysqlite = new MySqliteOpenHelper(TeacherLogin.this);
            SQLiteDatabase sqliteDatabase = mysqlite.getReadableDatabase();
            String selection = Teacher.EMAIL + "='" + enteredEmail + "'";
            Cursor cursor = Teacher.select(sqliteDatabase, selection);
            if (cursor != null && cursor.moveToNext()) {
                teacher_id=cursor.getInt(0);
                 pass = cursor.getString(6);
                if (enteredPass.equals(pass)) {
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    storeTeacherId();
                    storeUserSession();
                    Intent i=new Intent(TeacherLogin.this,TeacherDashboard.class);
                    editTextEmail.setText("");
                    editTextPass.setText("");
                    finish();
                    startActivity(i);
                    }
                else {
                    Toast.makeText(this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void storeTeacherId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putInt("TeacherId",teacher_id);
        editor.commit();
    }

    private void storeUserSession() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putBoolean("isTeacherLogin",true);
        editor.commit();
    }


}
