package com.example.jaishree.attendance.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherVerification extends AppCompatActivity {
    EditText editTextOtp;
    TextView textViewTimer,textViewTitle;
    Button buttonSendOtp,buttonSubmit;
    CircleImageView otpCircleImage;
    public static String PASSWORD="0123456789";
    int count=6,teacherId;
    String otp="",mobile="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_verification);
        init();
        Glide.with(this).load(R.drawable.otpicon).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(otpCircleImage);
        getTeacherId();
        getMobileNo();
        methodListener();
    }
    private void getTeacherId() {
        SharedPreferences pref=getSharedPreferences("myFile",MODE_PRIVATE);
        teacherId=pref.getInt("TeacherId",0);
    }

    private void getMobileNo() {
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(TeacherVerification.this);
        SQLiteDatabase database=mysqliteHelper.getReadableDatabase();
        String sel= Teacher.ID + "='" + teacherId + "'";
        Cursor cur=Teacher.select(database,sel);
        if(cur!=null && cur.moveToNext()){
            mobile=cur.getString(2);
        }
    }

    private void init() {
        editTextOtp= (EditText) findViewById(R.id.ediTextOtp);
        textViewTimer= (TextView) findViewById(R.id.textViewTimer);
        textViewTitle= (TextView) findViewById(R.id.textViewTitle);
        buttonSendOtp= (Button) findViewById(R.id.buttonSendOtp);
        buttonSubmit= (Button) findViewById(R.id.buttonSubmit);
        otpCircleImage= (CircleImageView) findViewById(R.id.otpCircleImage);
    }

    private void methodListener() {
        buttonSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generatePassword(count);
                buttonSendOtp.setText("Resend Otp");
                buttonSendOtp.setEnabled(false);
                sendMessage();
                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long l) {
                        buttonSendOtp.setEnabled(false);
                        textViewTimer.setText("Time Remaining:" + l / 1000);

                    }

                    @Override
                    public void onFinish() {
                        buttonSendOtp.setEnabled(true);
                        otp="";
                        textViewTimer.setText("");
                    }
                }.start();

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void generatePassword(int count) {
        StringBuffer buffer=new StringBuffer();
        while(  count--!=0){
            int c= (int) (Math.random()*PASSWORD.length());
            buffer.append(PASSWORD.charAt(c));
        }
        otp=buffer.toString();
        Log.d("1234", "generatePassword: "+otp);
    }

    private void sendMessage() {
        StringRequest request=new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendhttp.php?authkey=164884A6wNlivfz5965b2a9&mobiles=" + mobile + "&message=" + otp + "&sender=ABCDEF&route=4&country=91 ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TeacherVerification.this, "Message Sent", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherVerification.this, "Network Error", Toast.LENGTH_SHORT).show();

            }
        });
        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void submit() {
        String enteredOtp=editTextOtp.getText().toString();
        if(enteredOtp.equals("")){
            editTextOtp.setError("Please Enter OTP");
        }
        else {
            editTextOtp.setError(null);
            if (enteredOtp.equals(otp)) {
                Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
                editTextOtp.setText("");
                Intent i = new Intent(TeacherVerification.this, TeacherChangePassword.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Not Verified", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
