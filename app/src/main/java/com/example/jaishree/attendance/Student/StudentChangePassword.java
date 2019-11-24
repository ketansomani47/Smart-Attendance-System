package com.example.jaishree.attendance.Student;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

public class StudentChangePassword extends AppCompatActivity {
    EditText editTextChngePass,editTextCnfrmPass;
    TextView textViewEmail,textView1,textView2;
    Button saveChngeBtn;
    int studentId;
    String pass="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_change_password);
        init();
        setEmail();
        methodListener();
    }
    private void init() {
        editTextChngePass= (EditText) findViewById(R.id.editTextChngePass);
        editTextCnfrmPass= (EditText) findViewById(R.id.editTextCnfrmPass);
        textViewEmail= (TextView) findViewById(R.id.textViewEmail);
        textView1= (TextView) findViewById(R.id.textView1);
        textView2= (TextView) findViewById(R.id.textView2);
        saveChngeBtn= (Button) findViewById(R.id.saveChngeBtn);
    }

    private void setEmail() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        studentId=preference.getInt("StudentId",0);
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase database=mysqliteHelper.getReadableDatabase();
        String selection= Student.ID + "='" + studentId + "'";
        Cursor cursor=Student.select(database,selection);
        if(cursor!=null && cursor.moveToNext()){
            String email=cursor.getString(5);
            textViewEmail.setText(email);
        }
    }

    private void methodListener() {
        editTextChngePass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                pass=charSequence.toString();
                if(pass.length()<8){
                    textView1.setText("\u274C");
                }
                else{
                    textView1.setText("\u2714");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTextCnfrmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String cpass=editable.toString();
                if(cpass.equals(pass)){
                    textView2.setText("\u2714");
                }
                else{
                    textView2.setText("\u274C");
                }

            }
        });
        saveChngeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved();
            }
        });
    }

    private void saved() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        studentId =preference.getInt("StudentId",0);
        ContentValues cv=new ContentValues();
        cv.put(Student.STUDENT_PASSWORD,pass);
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase database=mysqliteHelper.getWritableDatabase();
        String selection=Student.ID + "='" + studentId + "'";
        int i=Student.update(database,cv,selection);
        if(i>0){
            Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();
            Intent i1=new Intent(StudentChangePassword.this,StudentPanel.class);
            finish();
            startActivity(i1);
        }
        else{
            Toast.makeText(this, "Not Updated", Toast.LENGTH_SHORT).show();
        }
    }
}
