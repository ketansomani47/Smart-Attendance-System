package com.example.jaishree.attendance.admin;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jaishree.attendance.Adapter.TeacherAdapter;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

import java.util.ArrayList;

/**
 * Created by Jaishree on 28-06-2017.
 */

public class ViewTeacher extends Fragment {
    private ListView listViewTeacher;
    private EditText editTextSearch;
    private ArrayList<TeacherPojo> arrayListTeacher = new ArrayList<>();
    private TeacherAdapter teacherAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewteacher, null);
        init(view);
        methodListener();
        fetchDatabase();
        return view;
    }

    private void init(View view) {
        listViewTeacher = (ListView) view.findViewById(R.id.listViewTeacher);
        editTextSearch= (EditText) view.findViewById(R.id.editTextSearch);
    }

    private void methodListener() {
       listViewTeacher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               TeacherPojo pojo=arrayListTeacher.get(i);
               Dialog dialog=new Dialog(getActivity());
               dialog.setTitle("Teacher Description");
               LayoutInflater inflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
               View view1=inflater.inflate(R.layout.teacher_desc_dialog,null);
               TextView textViewName= (TextView) view1.findViewById(R.id.textViewTName);
               TextView textViewMobile= (TextView) view1.findViewById(R.id.textViewTMobile);
               TextView textViewEmail= (TextView) view1.findViewById(R.id.textViewTEmail);
               TextView textViewQualification= (TextView) view1.findViewById(R.id.textViewTQualification);
               TextView textViewSubject= (TextView) view1.findViewById(R.id.textViewTSubject);
               //TextView textViewSemester= (TextView) view1.findViewById(R.id.textViewTSemester);
               //TextView textViewBranch= (TextView) view1.findViewById(R.id.textViewTBranch);

               textViewName.setText("Name: "+pojo.getName());
               textViewMobile.setText("Mobile: "+pojo.getMobile());
               textViewEmail.setText("Email: "+pojo.getEmail());
               textViewQualification.setText("Qualification: "+pojo.getQualification());
               textViewSubject.setText("Subject: "+pojo.getSubject_name());
              // textViewSemester.setText("Semester: "+pojo.getSemester());
              // textViewBranch.setText("Branch: "+pojo.getBranch_name());
               dialog.setContentView(view1);
               dialog.show();
           }
       });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str=charSequence.toString();
                teacherAdapter.getFilter(str);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    public void fetchDatabase() {
        arrayListTeacher.clear();
        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase = mysqliteHelper.getReadableDatabase();
        Cursor cursor = Teacher.select(sqliteDatabase, null);
        if (cursor != null) {
            while (cursor.moveToNext())
            {int id=cursor.getInt(0);
                String name=cursor.getString(1);
                String mobile=cursor.getString(2);
                String email=cursor.getString(3);
                String subject_names = "";
                String selection= Teacher.ID + "='" + id + "'";
                ArrayList<SubjectPojo> arrayListSubPojoGTeacher = Teacher.getTeacherSubjects(sqliteDatabase,selection);

                for (SubjectPojo pojo : arrayListSubPojoGTeacher)
                {
                    subject_names += pojo.getName()+",";

                }
                subject_names=subject_names.substring(0,subject_names.length()-1);
                String qualification=cursor.getString(5);
                String password=cursor.getString(6);
                TeacherPojo pojo = new TeacherPojo();
                pojo.setId(id);
                pojo.setName(name);
                pojo.setMobile(mobile);
                pojo.setEmail(email);
                pojo.setSubject_name(subject_names);
                pojo.setQualification(qualification);
                pojo.setPassword(password);

                Log.d("1234", "fetchFromDatabase: teacher"+pojo.toString());
                arrayListTeacher.add(pojo);
            }
            teacherAdapter = new TeacherAdapter(getActivity(), R.layout.teacher_list_item, arrayListTeacher, ViewTeacher.this);
            listViewTeacher.setAdapter(teacherAdapter);
            cursor.close();
            sqliteDatabase.close();
        }
    }
}
