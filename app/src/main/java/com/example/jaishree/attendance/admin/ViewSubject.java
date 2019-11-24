package com.example.jaishree.attendance.admin;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Adapter.SubjectAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.helper.StringValueHelper;
import com.example.jaishree.attendance.table.BranchTable;
import com.example.jaishree.attendance.table.Subject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Jaishree on 01-07-2017.
 */

public class ViewSubject extends Fragment {
    FloatingActionButton fab;
    ListView listViewSubject;
    ArrayList<SubjectPojo> arrayList = new ArrayList<>();
    ArrayList<BranchPojo> arraylistBranch = new ArrayList<>();
    SubjectAdapter subjectAdapter;
    BranchSpinnerAdapter branchSpinnerAdapter;
    Spinner branchSpinnerSubject, semesterSpinnerSubject;
    EditText subjectEditText,editTextSearch1;
    String enteredBranch = "", enteredSemester = "";
    Button addSubjectBtn;
    int branch_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewsubject, null);
        init(view);
        methodListener();
        fetchdatabase();
        return view;
    }

    private void init(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        listViewSubject = (ListView) view.findViewById(R.id.listViewSubject);
        editTextSearch1= (EditText) view.findViewById(R.id.editTextSearch1);
    }

    private void methodListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity(), R.style.NewDialog);
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = inflater.inflate(R.layout.subject_dialog, null);
                branchSpinnerSubject = (Spinner) v.findViewById(R.id.branchSpinnerSubject);
                semesterSpinnerSubject = (Spinner) v.findViewById(R.id.semesterSpinnerSubject);
                subjectEditText = (EditText) v.findViewById(R.id.subjectEditText);
                addSubjectBtn = (Button) v.findViewById(R.id.addSubjectBtn);
                dialog.setContentView(v);
                dialog.setTitle("Add Subject");
                dialog.show();
                branchSpinnerAdapter = new BranchSpinnerAdapter(getActivity(), R.layout.branch_spinner_list_item, arraylistBranch, ViewSubject.this);
                branchSpinnerSubject.setAdapter(branchSpinnerAdapter);
                getValue();
                branchSpinnerSubject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        BranchPojo pojo = arraylistBranch.get(position);
                        enteredBranch = pojo.getName();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                addSubjectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enteredSemester = (String) semesterSpinnerSubject.getSelectedItem();
                        addSubject();
                        dialog.cancel();
                        fetchdatabase();
                    }
                });
            }
        });
        editTextSearch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str=charSequence.toString();
                subjectAdapter.getFilter(str);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void getValue() {
        arraylistBranch.clear();
        BranchPojo pojo = new BranchPojo();
        pojo.setId(-1);
        pojo.setName("Select Branch");
        arraylistBranch.add(pojo);
        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase = mysqliteHelper.getReadableDatabase();

        Cursor cursor = BranchTable.select(sqliteDatabase, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                BranchPojo pojo1 = new BranchPojo();
                pojo1.setName(name);
                pojo1.setId(id);
                arraylistBranch.add(pojo1);
            }
            branchSpinnerAdapter.notifyDataSetChanged();
            cursor.close();
            sqliteDatabase.close();
        }
    }

    private void addSubject() {
        String enteredSubject = subjectEditText.getText().toString();
        if (enteredBranch.equals("Select Branch") | enteredSemester.equals("Select Semester") | enteredSubject.equals("")) {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(getActivity());
            SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
            String selection = BranchTable.NAME + "='" + enteredBranch + "'";
            Cursor cursor = BranchTable.select(db, selection);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    branch_id = cursor.getInt(0);
                }
            }

            MySqliteOpenHelper mySqliteOpenHelper1 = new MySqliteOpenHelper(getActivity());
            SQLiteDatabase db1 = mySqliteOpenHelper1.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(Subject.SUBJECT, enteredSubject);
            cv.put(Subject.BRANCH_ID, branch_id);
            cv.put(Subject.SEMESTER, enteredSemester);
            Log.d("1234", "addSubject: " + enteredSubject + enteredSemester + branch_id);
            long l = Subject.insert(db1, cv);
            if (l > 0) {
                Toast.makeText(getActivity(), "inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "not inserted", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
            db1.close();
            db.close();
        }
    }

    private void fetchdatabase() {
        arrayList.clear();
        MySqliteOpenHelper mySqliteOpenHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase db = mySqliteOpenHelper.getReadableDatabase();
        Cursor cursor = Subject.select(db, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                int b_id = cursor.getInt(2);
                String semester = cursor.getString(3);

                Log.d(StringValueHelper.TAG, "fetchdatabase: " + b_id);


                SubjectPojo pojo = new SubjectPojo();
                Cursor branchCurser = BranchTable.select(db, BranchTable.ID + " = '" + b_id + "'");
                if (branchCurser != null && branchCurser.moveToNext()) {
                    pojo.setBranch_name(branchCurser.getString(1));

                }

                branchCurser.close();
                Log.d(TAG, "fetchdatabase: ");
                pojo.setId(id);
                pojo.setName(name);
                pojo.setBranch_id(b_id);
                pojo.setSemester(semester);
                arrayList.add(pojo);
                Log.d(TAG, "fetchdatabase: " + pojo);
            }
            subjectAdapter = new SubjectAdapter(getContext(), R.layout.subject_list_item, arrayList, ViewSubject.this);
            listViewSubject.setAdapter(subjectAdapter);
            cursor.close();
            db.close();
        }
    }
}


