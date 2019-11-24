package com.example.jaishree.attendance.admin;

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
import com.example.jaishree.attendance.table.Admin;

public class AdminLoginPage extends AppCompatActivity {

    EditText editTextEmail,editTextPass;
    Button loginBtn;
    int id;
    String email="admin@admin",pass="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isUserLogin()){
            Intent i=new Intent(this,Details.class);
            finish();
            //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                String enteredEmail=editTextEmail.getText().toString();
                String enteredPass=editTextPass.getText().toString();
                if(enteredEmail.equals("") || enteredPass.equals("")){
                    Toast.makeText(AdminLoginPage.this, "fill all fields", Toast.LENGTH_SHORT).show();
                }
                boolean flag=checkUserData(enteredEmail, enteredPass);
                if(flag){
                    Toast.makeText(AdminLoginPage.this, "Login Success", Toast.LENGTH_SHORT).show();
                    MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(AdminLoginPage.this);
                    SQLiteDatabase sqliteDatabase=mysqliteHelper.getReadableDatabase();
                    String selection= Admin.EMAIL + "='" + enteredEmail + "' AND " + Admin.PASSWORD + "='" + enteredPass + "'";
                    Cursor cursor=Admin.select(sqliteDatabase,selection);
                    if(cursor!=null){
                        while(cursor.moveToNext()){
                            id=cursor.getInt(0);
                            storeId();
                        }
                    }

                    storeUserSession();
                    Intent i=new Intent(AdminLoginPage.this,Details.class);
                    finish();
                    //i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
                else{
                    Toast.makeText(AdminLoginPage.this, "Invalid EMail/Password", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void storeId() {
        SharedPreferences preference =getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preference.edit();
        editor.putInt("adminId",id);
        editor.commit();
    }

    private void storeUserSession() {
        SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    private boolean checkUserData(String enteredEmail, String enteredPass) {
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase database=mysqliteHelper.getReadableDatabase();
        String selection=Admin.EMAIL + "='" + email + "'";
        Cursor cursor=Admin.select(database,selection);
        if(cursor!=null && cursor.moveToNext()){
            pass=cursor.getString(6);
        }

        if(enteredEmail.equals("") || enteredPass.equals("")){
            Toast.makeText(AdminLoginPage.this, "fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(enteredEmail.equals(email) && enteredPass.equals(pass)){
            editTextEmail.setText("");
            editTextPass.setText("");
            return true;
        }
        else
        {
            Toast.makeText(AdminLoginPage.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isUserLogin() {
        SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
        return preferences.getBoolean("isLogin",false);
    }


}
