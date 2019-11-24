package com.example.jaishree.attendance.Parent;

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

public class ParentLogin extends AppCompatActivity {
    Button loginBtn;
    EditText editTextEmail,editTextPass;
    String enteredPEmail="",enteredPPass="",pPass="";
    int parentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(isParentLogin()){
            Intent i=new Intent(ParentLogin.this,ParentPanel.class);
            finish();
            startActivity(i);
        }
        init();
        methodListener();

    }

    private boolean isParentLogin() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        return preference.getBoolean("isParentLogin",false);
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
               checkParentData();
            }
        });
    }

    private void checkParentData() {
        enteredPEmail=editTextEmail.getText().toString();
        enteredPPass=editTextPass.getText().toString();
        if(enteredPEmail.equals("")&& enteredPPass.equals("")){
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }
        else if(enteredPEmail.equals("")){
            editTextEmail.setError("Enter Email");
        }
        else if(enteredPPass.equals("")){
            editTextEmail.setError(null);
            editTextPass.setError("Enter Password");
        }
        else{
            editTextPass.setError(null);
            MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(ParentLogin.this);
            SQLiteDatabase database=mysqliteHelper.getReadableDatabase();
            String sele= Student.PARENT_EMAIL + "='" + enteredPEmail + "'";
            Cursor curs=Student.select(database,sele);
            if(curs!=null && curs.moveToNext()){
                parentId=curs.getInt(0);
                pPass=curs.getString(10);
                if(enteredPPass.equals(pPass)){
                    Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    storeParentId();
                    storeParentSession();
                    Intent i2=new Intent(ParentLogin.this,ParentPanel.class);
                    editTextEmail.setText("");
                    editTextPass.setText("");
                    finish();
                    startActivity(i2);
                }
                else {
                    Toast.makeText(this, "Invalid Email/Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void storeParentId() {
        SharedPreferences pre=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor edit=pre.edit();
        edit.putInt("ParentId",parentId);
        edit.commit();
    }

    private void storeParentSession() {
        SharedPreferences prefer=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor edito=prefer.edit();
        edito.putBoolean("isParentLogin",true);
        edito.commit();
    }

}
