package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.jaishree.attendance.MainActivity;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Admin;

import de.hdodenhof.circleimageview.CircleImageView;

public class Details extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FrameLayout frameLayout;
    TextView textViewHeader,textImageHeader;
    CircleImageView circleImageHeader;
    ImageView imageViewHeader;
    String img="",name="";
    int adminId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view=navigationView.getHeaderView(0);
        init(view);
        show();
        showImage();
    }

    private void init(View view) {
        frameLayout= (FrameLayout) view.findViewById(R.id.frameLayout);
        textImageHeader= (TextView) view.findViewById(R.id.textImageHeader);
        textViewHeader= (TextView) view.findViewById(R.id.textViewHeader);
        circleImageHeader= (CircleImageView) view.findViewById(R.id.circleImageHeader);
        imageViewHeader= (ImageView) view.findViewById(R.id.imageViewHeader);
    }

    private void show() {
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(this);
        SQLiteDatabase sqliteDatabase=mysqliteHelper.getReadableDatabase();

        Cursor cursor= Admin.select(sqliteDatabase,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                name=cursor.getString(1);
                 img=cursor.getString(7);

                if(img.equals("") && name.equals("")){
                    textViewHeader.setText("Admin");
                    textImageHeader.setVisibility(View.VISIBLE);
                    textImageHeader.setText((""+textViewHeader.getText().charAt(0)).toUpperCase());
                }
                else if(img.equals("")){
                        textViewHeader.setText(name);
                    textImageHeader.setVisibility(View.VISIBLE);
                        textImageHeader.setText((""+name.charAt(0)).toUpperCase());
                    }
                    else{
                     getImage();
                    textViewHeader.setText(name);
                    textImageHeader.setVisibility(View.GONE);
                    }
                }

        }
    }

    private void getImage() {
        Glide.with(this)
                .load(img)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(circleImageHeader);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.adminLogout) {
            SharedPreferences preferences=getSharedPreferences("myFile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preferences.edit();
            editor.putBoolean("isLogin",false);
            editor.commit();
            Intent i=new Intent(Details.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        else if(id ==R.id.adminEditProfile){
            Intent i=new Intent(Details.this, AdminProfile.class);
            startActivity(i);
            finish();
        }
        else if(id ==R.id.adminChangePassword){
            Intent i=new Intent(Details.this, AdminVerification.class);
            startActivity(i);
            finish();
        }
        else if(id ==R.id.adminRemoveProfile){
            img="";
            MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(this);
            SQLiteDatabase sqliteDatabase=mysqliteHelper.getWritableDatabase();
            ContentValues cv=new ContentValues();
            cv.put(Admin.IMAGE,img);
            getId();
            String selection=Admin.ID + "='" + adminId +"'";
            int l=Admin.update(sqliteDatabase,cv,selection);
            if(l>0){
                Toast.makeText(this, "Profile Pic Removed", Toast.LENGTH_SHORT).show();
                ////fetch
            }
            else {
                Toast.makeText(this, "Not Removed", Toast.LENGTH_SHORT).show();
            }
            textImageHeader.setVisibility(View.VISIBLE);
            textImageHeader.setText((""+name.charAt(0)).toUpperCase());
        }

        return super.onOptionsItemSelected(item);
    }

    private void getId() {
        SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
        adminId=preference.getInt("adminId",0);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.addNews) {
            changeFragment(new AddNews());

        } else if (id == R.id.addStudent) {
            changeFragment(new AddStudent());

        } else if (id == R.id.addTeacher) {
            changeFragment(new AddTeacher());

        } else if (id == R.id.viewFeedback) {

        } else if (id == R.id.viewReport) {
            changeFragment(new ViewAttendanceReport());

        } else if (id == R.id.viewStudentDetails) {
            changeFragment(new ViewStudent());

        }
        else if(id == R.id.viewTeacherDetails){
            changeFragment(new ViewTeacher());

        }
        else if(id == R.id.addBranch){
            changeFragment(new addBranch());

        }
        else if (id == R.id.viewSubject){
            changeFragment(new ViewSubject());

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction transaction=fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer,fragment);
        transaction.commit();
    }

    private void showImage() {
        Glide.with(this)
                .load(R.drawable.nav_header)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewHeader);
    }
}
