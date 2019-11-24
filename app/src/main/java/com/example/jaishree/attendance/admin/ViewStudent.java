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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jaishree.attendance.Adapter.StudentAdapter;
import com.example.jaishree.attendance.Pojo.StudentPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.BranchTable;
import com.example.jaishree.attendance.table.Student;

import java.util.ArrayList;

/**
 * Created by Jaishree on 28-06-2017.
 */

public class ViewStudent extends Fragment {
    private ListView listViewStudent;
    ArrayList<StudentPojo> arrayListStudent=new ArrayList<>();
    StudentAdapter studentAdapter;
    EditText editTextSearch2;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.viewstudent,null);
        init(view);
        methodListener();
        fetchDatabase();
        return view;

    }

    private void init(View view) {
        listViewStudent= (ListView) view.findViewById(R.id.listViewStudent);
        editTextSearch2= (EditText) view.findViewById(R.id.editTextSearch2);
    }

    private void methodListener() {
        listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StudentPojo pojo=arrayListStudent.get(i);
                Dialog dialog=new Dialog(getActivity());
                dialog.setTitle("Student Description");
                LayoutInflater inflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1=inflater.inflate(R.layout.student_desc_dialog,null);
                TextView textName= (TextView) view1.findViewById(R.id.textViewSName);
                TextView textMobile= (TextView) view1.findViewById(R.id.textViewSMobile);
                TextView textEmail= (TextView) view1.findViewById(R.id.textViewSEmail);
                TextView textPName= (TextView) view1.findViewById(R.id.textViewParentName);
                TextView textPMobile= (TextView) view1.findViewById(R.id.textViewParentMobile);
                TextView textPEmail= (TextView) view1.findViewById(R.id.textViewParentEmail);
                TextView textBranch= (TextView) view1.findViewById(R.id.textViewSBranch);
                TextView textSemester= (TextView) view1.findViewById(R.id.textViewSSemester);

                textName.setText("Name: "+pojo.getName());
                textMobile.setText("Mobile: "+pojo.getMobile());
                textEmail.setText("Email: "+pojo.getEmail());
                textPName.setText("Parent Name: "+pojo.getParent_name());
                textPMobile.setText("Parent Mobile: "+pojo.getParent_mobile());
                textPEmail.setText("Parent Email: "+pojo.getParent_email());
                textBranch.setText("Branch: "+pojo.getBranch_name());
                textSemester.setText("Semester: "+pojo.getSemester());
                dialog.setContentView(view1);
                dialog.show();

            }
        });
        editTextSearch2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str=charSequence.toString();
                studentAdapter.getFilter(str);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void fetchDatabase() {
        arrayListStudent.clear();
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase=mysqliteHelper.getReadableDatabase();
        Cursor cursor= Student.select(sqliteDatabase,null);
        if(cursor!=null){
            while(cursor.moveToNext()) {
                StudentPojo pojo = new StudentPojo();
                pojo.setId(cursor.getInt(0));
                pojo.setName(cursor.getString(1));
                pojo.setMobile(cursor.getString(2));
                int branch_id = cursor.getInt(3);
                pojo.setBranch_id(branch_id);
                Cursor cursor1 = BranchTable.select(sqliteDatabase, null);
                if (cursor1 != null && cursor1.moveToNext()) {
                    String branch_name = cursor1.getString(1);
                    pojo.setBranch_name(branch_name);
                }
                cursor1.close();
                pojo.setSemester(cursor.getString(4));
                pojo.setEmail(cursor.getString(5));
                pojo.setPassword(cursor.getString(6));
                pojo.setParent_name(cursor.getString(7));
                pojo.setParent_email(cursor.getString(8));
                pojo.setParent_mobile(cursor.getString(9));
                pojo.setParent_password(cursor.getString(10));
                arrayListStudent.add(pojo);
            }
            studentAdapter=new StudentAdapter(getActivity(),R.layout.teacher_list_item,arrayListStudent,ViewStudent.this);
            listViewStudent.setAdapter(studentAdapter);
            cursor.close();
                sqliteDatabase.close();
            }
    }
}

