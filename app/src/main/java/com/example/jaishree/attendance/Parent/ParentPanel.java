package com.example.jaishree.attendance.Parent;

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
import com.example.jaishree.attendance.Teacher.AttendanceSubListing;
import com.example.jaishree.attendance.Teacher.TeacherNewsFragment;

public class ParentPanel extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_panel);
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
        getMenuInflater().inflate(R.menu.parentmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.parentChangePassword){
            Intent i=new Intent(ParentPanel.this,ParentVerification.class);
            startActivity(i);
        }
        else if(id==R.id.parentEditProfile){

        }
        else if(id==R.id.parentNotification){

        }
        else if (id==R.id.parentLogout){
            SharedPreferences pref=getSharedPreferences("myFile",MODE_PRIVATE);
            SharedPreferences.Editor edit=pref.edit();
            edit.putBoolean("isParentLogin",false);
            edit.commit();
            Intent i=new Intent(ParentPanel.this,ParentLogin.class);
            finish();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    }
