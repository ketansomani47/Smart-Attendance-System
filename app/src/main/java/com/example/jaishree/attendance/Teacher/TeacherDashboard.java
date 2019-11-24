package com.example.jaishree.attendance.Teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.jaishree.attendance.Adapter.ViewPagerAdapter;
import com.example.jaishree.attendance.R;

public class TeacherDashboard extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabs= (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        setIcon();

    }
    private void setIcon() {
        TextView tab1= (TextView) LayoutInflater.from(this).inflate(R.layout.tab_design,null);
        tab1.setText(" News");
        tab1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.teachernews,0,0,0);
        tabs.getTabAt(0).setCustomView(tab1);

        TextView tab2= (TextView) LayoutInflater.from(this).inflate(R.layout.tab_design,null);
        tab2.setText(" Attendance");
        tab2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.takeattendance,0,0,0);
        tabs.getTabAt(1).setCustomView(tab2);
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new TeacherNewsFragment(),"News");
        viewPagerAdapter.addFragment(new AttendanceSubListing(),"Attendance");
        viewPager.setAdapter(viewPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teachermenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.teacherLogout){
            SharedPreferences preference=getSharedPreferences("myFile",MODE_PRIVATE);
            SharedPreferences.Editor editor=preference.edit();
            editor.putBoolean("isTeacherLogin",false);
            editor.commit();
            Intent i=new Intent(TeacherDashboard.this,TeacherLogin.class);
            finish();
            startActivity(i);
        }
        else if(id==R.id.teacherChangePassword){
            Intent i=new Intent(TeacherDashboard.this,TeacherVerification.class);
            startActivity(i);

        }
        else if(id==R.id.teacherEditProfile){
            Intent i=new Intent(TeacherDashboard.this,TeacherProfile.class);
            startActivity(i);

        }
        else if(id==R.id.teacherRemovePic){

        }
        return super.onOptionsItemSelected(item);
    }
}
