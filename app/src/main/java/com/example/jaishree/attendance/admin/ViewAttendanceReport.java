package com.example.jaishree.attendance.admin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.AttendanceAdapter;
import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.BranchTable;

import java.util.ArrayList;

/**
 * Created by Jaishree on 27-06-2017.
 */

public class ViewAttendanceReport extends Fragment {

    Button buttonSubmit;
    Spinner branchSpinner,semesterSpinner;
    ListView listViewViewAttendance;
    BranchSpinnerAdapter branchSpinnerAdapter;
    AttendanceAdapter attendanceAdapter;
    ArrayList<BranchPojo> arrayListBranch=new ArrayList<>();
    ArrayList<SubjectPojo> arrayListSubject=new ArrayList<>();
    Fragment fragment;
    int branch_id;
    String enteredBranch="",enteredSemester="";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewattendancereport, null);
        init(view);
        branchSpinnerAdapter=new BranchSpinnerAdapter(getActivity(),R.layout.teachersubject_list_item,arrayListBranch,fragment);
        branchSpinner.setAdapter(branchSpinnerAdapter);
        attendanceAdapter=new AttendanceAdapter(getActivity(),R.layout.teachersubject_list_item,arrayListSubject);
        listViewViewAttendance.setAdapter(attendanceAdapter);
        methodListener();
        return view;
    }


    private void init(View view) {
        buttonSubmit = (Button) view.findViewById(R.id.buttonSubmit);
        branchSpinner= (Spinner) view.findViewById(R.id.branchSpinner);
        semesterSpinner= (Spinner) view.findViewById(R.id.semesterSpinner);
        listViewViewAttendance= (ListView) view.findViewById(R.id.listViewViewAttendance);

    }

    private void methodListener() {
        getBranchValue();
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BranchPojo pojo=arrayListBranch.get(i);
                branch_id=pojo.getId();
                enteredBranch=pojo.getName();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredSemester=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enteredBranch.equals("Select Branch") || enteredSemester.equals("Select Semester")) {
                    Toast.makeText(getActivity(), "Please select appropriate fields", Toast.LENGTH_SHORT).show();
                } else {
                    getSubject();
                }
            }
        });
    }

    private void getBranchValue() {
        arrayListBranch.clear();
        BranchPojo pojo=new BranchPojo();
        pojo.setId(-1);
        pojo.setName("Select Branch");
        arrayListBranch.add(pojo);

        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(getContext());
        SQLiteDatabase sqliteDatabase=mysqliteHelper.getReadableDatabase();

        Cursor cursor= BranchTable.select(sqliteDatabase,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                BranchPojo pojo1=new BranchPojo();
                pojo1.setId(cursor.getInt(0));
                pojo1.setName(cursor.getString(1));
                arrayListBranch.add(pojo1);
            }
            branchSpinnerAdapter.notifyDataSetChanged();
        }
    }

    private void getSubject() {
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase database=mysqliteHelper.getReadableDatabase();


    }
}


