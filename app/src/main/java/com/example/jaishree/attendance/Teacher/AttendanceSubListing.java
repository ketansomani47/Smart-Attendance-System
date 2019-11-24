package com.example.jaishree.attendance.Teacher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jaishree.attendance.Adapter.AttendanceAdapter;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

import java.util.ArrayList;

/**
 * Created by Lenovo G580 on 04-07-2017.
 */

public class AttendanceSubListing extends Fragment {

    ListView listViewTeacherSubject;
    ArrayList<SubjectPojo> attendanceArrayList=new ArrayList<>();
    AttendanceAdapter attendanceAdapter;
    int teacherId,rowId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.take_attendance,null);
        init(view);
        methodListener();
        fetchFromDatabase();
        return view;
    }
    private void init(View view) {
        listViewTeacherSubject= (ListView) view.findViewById(R.id.listViewTeacherSubject);
    }

    private void fetchFromDatabase() {
        attendanceArrayList.clear();
        getTeacherId();

        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db=mySqliteOpenHelper.getReadableDatabase();
        Log.d("123", "teacherId: "+teacherId);
        String selection= Teacher.ID + "='" + teacherId + "'";
        attendanceArrayList=Teacher.getTeacherSubjects(db,selection);

        Log.d("123", "fetchFromDatabase: "+attendanceArrayList);
        attendanceAdapter=new AttendanceAdapter(getActivity(),R.layout.take_attendance_list_item,attendanceArrayList);
        listViewTeacherSubject.setAdapter(attendanceAdapter);
    }

    private void methodListener() {
        listViewTeacherSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                rowId=attendanceArrayList.get(position).getId();
                Intent i=new Intent(getActivity(),TakeAttendance.class);
                i.putExtra("listClickId",rowId);
                Log.d("123", "onItemClick: "+rowId);
                startActivity(i);
            }
        });
    }

    private void getTeacherId() {
        SharedPreferences preference=getActivity().getSharedPreferences("myFile", Context.MODE_PRIVATE);
        teacherId= preference.getInt("TeacherId",0);
    }
}
