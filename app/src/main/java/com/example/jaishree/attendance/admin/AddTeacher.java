package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Adapter.SubjectSpinnerAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.BranchTable;
import com.example.jaishree.attendance.table.Subject;
import com.example.jaishree.attendance.table.Teacher;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Jaishree on 27-06-2017.
 */

public class AddTeacher extends Fragment {
    EditText editTextTName, editTextTMobile, editTextTEmail;
    Button buttonAddTeacher;
    TextView textViewSubjectList;
    Spinner branchSpinnerTeacher, semesterSpinnerTeacher, subjectSpinnerTeacher, qualificationTeacher;
    ArrayList<TeacherPojo> arrayListPojo = new ArrayList<>();
    ArrayList<SubjectPojo> arrayListSubject = new ArrayList<>();
    ArrayList<BranchPojo> arrayListBranch = new ArrayList<>();
    BranchSpinnerAdapter branchSpinnerAdapter;
    SubjectSpinnerAdapter subjectSpinnerAdapter;
    private int branch_id;
    String enteredSemester = "",enteredSubject="",enteredQalification="";
    String enteredName="",enteredMobile="",enteredEmail="";
    public static String PASSWORD_TEXT="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*";
    int length=8;
    String password="",selectedSubjectId="",databaseSubjectId="";
    int sub_id;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.addteacher, null);
        init(view);
        branchSpinnerAdapter = new BranchSpinnerAdapter(getActivity(), R.layout.branch_spinner_list_item, arrayListBranch, AddTeacher.this);
        branchSpinnerTeacher.setAdapter(branchSpinnerAdapter);
        subjectSpinnerAdapter = new SubjectSpinnerAdapter(getActivity(), R.layout.branch_spinner_list_item, arrayListSubject, AddTeacher.this);
//        subjectSpinnerTeacher.setAdapter(subjectSpinnerAdapter);
        methodListener();
        getBranchValue();
        return view;
    }

    private void init(View view) {
        editTextTName = (EditText) view.findViewById(R.id.editTextTName);
        editTextTMobile = (EditText) view.findViewById(R.id.editTextTMobile);
        editTextTEmail = (EditText) view.findViewById(R.id.editTextTEmail);
        buttonAddTeacher = (Button) view.findViewById(R.id.buttonAddTeacher);
        branchSpinnerTeacher = (Spinner) view.findViewById(R.id.branchSpinnerTeacher);
        semesterSpinnerTeacher = (Spinner) view.findViewById(R.id.semesterSpinnerTeacher);
        /*subjectSpinnerTeacher = (Spinner) view.findViewById(R.id.subjectSpinnerTeacher);*/
        qualificationTeacher = (Spinner) view.findViewById(R.id.qualificationSpinner);
        textViewSubjectList= (TextView) view.findViewById(R.id.textViewSubjectList);
    }

    private void methodListener() {
        branchSpinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                BranchPojo pojo = arrayListBranch.get(position);
                branch_id = pojo.getId();
                Log.d("123", "onItemClick: " + branch_id);
                getSubjectValue();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        semesterSpinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredSemester = adapterView.getItemAtPosition(i).toString();
                Log.d("123", "onClick: " + enteredSemester);
                getSubjectValue();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        /*subjectSpinnerTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //enteredSubject=adapterView.getItemAtPosition(i).toString();
                enteredSubject=arrayListSubject.get(i).getName();
                if(i!=0) {
                    textViewSubjectList.append(enteredSubject + ",");
                }
                sub_id=arrayListSubject.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        qualificationTeacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredQalification=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonAddTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredName=editTextTName.getText().toString();
                enteredMobile=editTextTMobile.getText().toString();
                enteredEmail=editTextTEmail.getText().toString();
                databaseSubjectId=selectedSubjectId.substring(0,selectedSubjectId.length()-1);
                Log.d("123", "databaseSubjectId: "+databaseSubjectId);
                if(enteredName.equals("")| enteredEmail.equals("")|enteredMobile.equals("")|enteredSemester.equals("Select Semester")|enteredSubject.equals("Select Subject")|enteredQalification.equals("Select Qualification")){
                    Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    generatePassword(length);
                    sendMessage();
                    storeInDatabase();
                }
         }
        });
        textViewSubjectList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] subNameArray = new String[arrayListSubject.size()];
                final boolean[] selectedState = new boolean[arrayListSubject.size()];
                final int[] subId = new int[arrayListSubject.size()];
                for (int i = 0; i < arrayListSubject.size(); i++) {
                    subNameArray[i] = arrayListSubject.get(i).getName();
                    selectedState[i] = false;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Subjects").setMultiChoiceItems(subNameArray, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        selectedState[i] = b;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int po) {
                        boolean flag = false;
                        for (int i = 0; i < selectedState.length; i++) {
                            if (selectedState[i]) {
                                textViewSubjectList.append(subNameArray[i] + ",");
                                subId[i] = arrayListSubject.get(i).getId();
                                Log.d("123", "subId[i]: " + subId[i]);
                                selectedSubjectId += subId[i] + ",";
                                Log.d("123", "selectedSubjectId: " + selectedSubjectId);
                                flag = true;
                            }
                        }
                        //  if (flag)
                        //    subjectTextViewTeacher.setText(subjectTextViewTeacher.getText().toString().substring(0, subjectTextViewTeacher.getText().toString().length() - 1));
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void getBranchValue() {
        arrayListBranch.clear();
        BranchPojo pojo1 = new BranchPojo();
        pojo1.setId(-1);
        pojo1.setName("Select Branch");
        arrayListBranch.add(pojo1);
        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase = mysqliteHelper.getReadableDatabase();
        Cursor cursor = BranchTable.select(sqliteDatabase, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String branch = cursor.getString(1);
                Log.d("123", "getBranchValue: " + id);
                Log.d("123", "getBranchValue: " + branch);
                BranchPojo pojo = new BranchPojo();
                pojo.setId(id);
                pojo.setName(branch);
                arrayListBranch.add(pojo);
            }
            branchSpinnerAdapter.notifyDataSetChanged();
        }
        cursor.close();
        sqliteDatabase.close();
    }

    private void getSubjectValue() {

        Toast.makeText(getActivity(), "here", Toast.LENGTH_SHORT).show();

        arrayListSubject.clear();

        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase = mysqliteHelper.getReadableDatabase();
        String selection = Subject.BRANCH_ID + "='" + branch_id + "'" + "AND " + Subject.SEMESTER + "='" + enteredSemester + "'";
        Log.d(TAG, "getSubjectValue: " + branch_id);
        Log.d(TAG, "getSubjectValue: " + enteredSemester);

        Cursor cursor = Subject.select(sqliteDatabase, selection);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                SubjectPojo pojo = new SubjectPojo();
                int id = cursor.getInt(0);
                String sub = cursor.getString(1);
                Log.d("123", "getSubjectValue: " + id);
                Log.d("123", "getSubjectValue: " + sub);
                pojo.setId(cursor.getInt(0));
                pojo.setName(cursor.getString(1));
                arrayListSubject.add(pojo);
            }
            subjectSpinnerAdapter.notifyDataSetChanged();
        }
        cursor.close();
        sqliteDatabase.close();
    }

    private void generatePassword(int count) {
        StringBuffer buffer=new StringBuffer();
        while(count--!=0){
            int c= (int) (Math.random()*PASSWORD_TEXT.length());
            buffer.append(PASSWORD_TEXT.charAt(c));
        }
        password=buffer.toString();
        Log.d(TAG, "generatePassword: "+password);
    }

    private void storeInDatabase() {
        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase = mysqliteHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Teacher.NAME, enteredName);
        contentValues.put(Teacher.MOBILE, enteredMobile);
        contentValues.put(Teacher.EMAIL, enteredEmail);
        contentValues.put(Teacher.QUALIFICATION, enteredQalification);
        contentValues.put(Teacher.PASSWORD, password);
        contentValues.put(Teacher.SUBJECT_ID, databaseSubjectId);
        contentValues.put(Teacher.IMAGE,"");

        long l = Teacher.insert(sqliteDatabase, contentValues);
        if (l > 0) {
            Log.d(TAG, "storeInDatabase: "+enteredName+enteredMobile+enteredEmail+enteredQalification+password+sub_id);
            Toast.makeText(getActivity(), "Registered Successfully", Toast.LENGTH_SHORT).show();
            editTextTName.setText("");
            editTextTMobile.setText("");
            editTextTEmail.setText("");
            branchSpinnerTeacher.setSelection(0);
            textViewSubjectList.setText("");
            semesterSpinnerTeacher.setSelection(0);
            qualificationTeacher.setSelection(0);
        } else {
            Toast.makeText(getActivity(), "Not Registered", Toast.LENGTH_SHORT).show();
        }
    }
    private void sendMessage() {
        StringRequest request=new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendhttp.php?authkey=164884A6wNlivfz5965b2a9&mobiles=" + enteredMobile + "&message=" + enteredEmail+"\n"+ password + "&sender=ABCDEF&route=4&country=91 ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: msg sent");
                Toast.makeText(getActivity(), "Msg sent", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: Error");
                Toast.makeText(getActivity(), "network error", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue queue= Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

}

