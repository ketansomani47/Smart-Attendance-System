package com.example.jaishree.attendance.Student;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {
    CircleImageView circleImageSProfile;
    FrameLayout frameLayoutSProfile;
    TextView textViewSProfile,textViewNameSProfile,textViewMobileSProfile,textViewEmailSProfile;
    Button cancelButton,saveButton;
    String img="";
    int student_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        init();
        methodListener();
        fetchDatabase();
    }

    private void init() {
        frameLayoutSProfile= (FrameLayout) findViewById(R.id.frameLayoutSProfile);
        circleImageSProfile= (CircleImageView) findViewById(R.id.circleImageSProfile);
        textViewSProfile= (TextView) findViewById(R.id.textViewSProfile);
        textViewNameSProfile= (TextView) findViewById(R.id.textViewNameSProfile);
        textViewMobileSProfile= (TextView) findViewById(R.id.textViewMobileSProfile);
        textViewEmailSProfile= (TextView) findViewById(R.id.textViewEmailSProfile);
        cancelButton= (Button) findViewById(R.id.cancelButton);
        saveButton= (Button) findViewById(R.id.saveButton);
    }

    private void methodListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(StudentProfile.this,StudentPanel.class);
                startActivity(i);
                finish();
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
        frameLayoutSProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkGalleryPermission()){
                    Intent i=new Intent();
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    startActivityForResult(i,1);
                }
            }
        });
    }

    private boolean checkGalleryPermission() {
        boolean flag= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
        return  flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                Uri uri=data.getData();
                img=uri.toString();
                getImage();
            }
        }
    }

    private void fetchDatabase() {
        MySqliteOpenHelper mysqlite = new MySqliteOpenHelper(this);
        SQLiteDatabase sqliteDatabase = mysqlite.getReadableDatabase();
        getId();
        String sel = Student.ID + "='" + student_id + "'";
        Cursor cursor = Student.select(sqliteDatabase, sel);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                {
                    String name = cursor.getString(1);
                    String email = cursor.getString(5);
                    String mobile = cursor.getString(2);
                    img = cursor.getString(11);
                    textViewNameSProfile.setText(name);
                    textViewEmailSProfile.setText(email);
                    textViewMobileSProfile.setText(mobile);
                    if (img.equals("")) {
                        textViewSProfile.setVisibility(View.VISIBLE);
                        textViewSProfile.setText(("" + name.charAt(0)).toUpperCase());
                    } else {
                        getImage();
                    }
                }
            }
        }
    }
    private void saveData() {
        if(img.equals("")){
            Toast.makeText(this, "Please select pic", Toast.LENGTH_SHORT).show();
        }
        else {
            MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(this);
            SQLiteDatabase database=mysqliteHelper.getReadableDatabase();
            getId();
            String sel= Student.ID + "='" + student_id + "'";
            ContentValues cv=new ContentValues();
            cv.put(Student.IMAGE,img);
            int l=Student.update(database,cv,sel);
            if(l>0){
                Toast.makeText(this, "Update Successfully", Toast.LENGTH_SHORT).show();
                fetchDatabase();
                Intent i=new Intent(StudentProfile.this,StudentPanel.class);
                startActivity(i);
                finish();
            }
            else {
                Toast.makeText(this, "Not Update", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getId() {
        SharedPreferences pre=getSharedPreferences("myFile",MODE_PRIVATE);
        student_id=pre.getInt("StudentId",0);
    }

    private void getImage() {
        Glide.with(this)
                .load(img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(circleImageSProfile);
        textViewSProfile.setVisibility(View.GONE);
    }

}
