package com.example.jaishree.attendance.admin;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Admin;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminProfile extends AppCompatActivity {
    EditText editTextName, editTextMobile, editTextAddress;
    TextView textViewEmail, textViewPassword, textViewImg;
    RadioGroup radioGroup;
    Button buttonSave, buttonCancel;
    FrameLayout frameLayout;
    CircleImageView circleImageView;
    String img = "";
    int id;
    int selected;
    String enteredEmail="admin@admin",enteredPassword="admin";
    String enteredName = "", enteredMobile = "", enteredAddress = "",enteredGender="";
    RadioButton radioButtonMale,radioButtonFemale,rb1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        init();
        methodListener();
        fetchDatabase();

    }

    private void init() {
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextMobile = (EditText) findViewById(R.id.editTextMobile);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        textViewImg = (TextView) findViewById(R.id.textViewImg);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        frameLayout= (FrameLayout) findViewById(R.id.frameLayout);
        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        radioButtonMale= (RadioButton) findViewById(R.id.radioButtonMale);
        radioButtonFemale= (RadioButton) findViewById(R.id.radioButtonFemale);
    }

    private void methodListener() {
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkGallaryPermission()) {
                    Toast.makeText(AdminProfile.this, "imageview", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(i, 1);
                }

            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(AdminProfile.this,Details.class);
                finish();
                startActivity(i);
            }
        });

    } private boolean checkGallaryPermission() {

        boolean flag = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return flag;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                img = uri.toString();
                getImage();
            }
        }
    }
    private void saveChanges() {
        enteredName = editTextName.getText().toString();
        enteredMobile = editTextMobile.getText().toString();
        enteredAddress = editTextAddress.getText().toString();
        selected = radioGroup.getCheckedRadioButtonId();
        rb1= (RadioButton) findViewById(selected);
        enteredGender=rb1.getText().toString();
        Log.d("1234", "saveChanges: " + enteredGender);
        if (enteredName.equals("") | enteredAddress.equals("") | editTextMobile.equals("") | enteredGender.equals("")) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(this);
            SQLiteDatabase sqliteDatabase = mysqliteHelper.getWritableDatabase();
            getId();
            String selection=Admin.ID + "='" + id + "'";
            ContentValues cv = new ContentValues();
            cv.put(Admin.NAME, enteredName);
            cv.put(Admin.MOBILE, enteredMobile);
            cv.put(Admin.ADDRESS, enteredAddress);
            cv.put(Admin.GENDER, enteredGender);
            cv.put(Admin.IMAGE, img);
            int l = Admin.update(sqliteDatabase,cv,selection);
            if (l > 0) {
                Toast.makeText(this, "Inserted", Toast.LENGTH_SHORT).show();
                fetchDatabase();
                Intent i = new Intent(AdminProfile.this, Details.class);
                startActivity(i);

            } else {
                Toast.makeText(this, "Not Inserted", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        id=preference.getInt("adminId",0);
    }

    private void fetchDatabase() {
        MySqliteOpenHelper mysqlite =new MySqliteOpenHelper(this);
        SQLiteDatabase sqliteDatabase=mysqlite.getReadableDatabase();

        Cursor cursor=Admin.select(sqliteDatabase,null);
        if(cursor!=null ) {
            while (cursor.moveToNext()) {
                int id=cursor.getInt(0);
                String name = cursor.getString(1);
                String mobile = cursor.getString(2);
                String gend = cursor.getString(3);
                String address = cursor.getString(4);
                img = cursor.getString(7);
                String email = cursor.getString(5);
                String password = cursor.getString(6);

                editTextName.setText(name);
                editTextMobile.setText(mobile);
                editTextAddress.setText(address);
                textViewEmail.setText(email);
                textViewPassword.setText(password);
                Log.d("123", "fetchFromDatabase: "+id);

               if (gend.equals(radioButtonMale.getText().toString()))
               {
                    radioButtonMale.setChecked(true);
                } else
               {
                    radioButtonFemale.setChecked(true);
                }
                if(img.equals("") && name.equals("")){
                    textViewImg.setVisibility(View.VISIBLE);
                    textViewImg.setText("A");
                }
                else if(img.equals("")){
                    textViewImg.setVisibility(View.VISIBLE);
                    textViewImg.setText(("" + name.charAt(0)).toUpperCase());
                }
                else{
                    getImage();
                }
            }
        }

    }

    private void getImage() {
        Glide.with(this)
                .load(img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade()
                .into(circleImageView);
        textViewImg.setVisibility(View.GONE);
    }
}