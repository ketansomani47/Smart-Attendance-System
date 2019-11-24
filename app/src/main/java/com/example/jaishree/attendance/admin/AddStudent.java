package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.jaishree.attendance.Adapter.BranchSpinnerAdapter;
import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.Pojo.StudentPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.BranchTable;
import com.example.jaishree.attendance.table.Student;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Jaishree on 27-06-2017.
 */

public class AddStudent extends Fragment {
    EditText editTextSName,editTextSMobile,editTextSEmail,editTextSParentName,editTextSParentEmail,editTextSParentMobile;
    Spinner spinnerBranchStudent,spinnerSemesterStudent;
    Button buttonAddStudent;
    ArrayList<StudentPojo> arrayListStudent=new ArrayList<>();
    ArrayList<BranchPojo> arrayListBranch=new ArrayList<>();
    BranchSpinnerAdapter branchSpinnerAdapter;
    int enteredBranchId;
    String enteredName="",enteredMobile="",enteredEmail="",enteredPassword="",enteredParentName="",enteredParentEmail="";
    String enteredSemester="",enteredParentMobile="",enteredParentPassword="",enteredBranchName="";
    int count=8;
    public static String PASSWORD_LIST="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%^&*";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.addstudent,null);
        init(view);
        branchSpinnerAdapter=new BranchSpinnerAdapter(getContext(),R.layout.branch_spinner_list_item,arrayListBranch,AddStudent.this);
        spinnerBranchStudent.setAdapter(branchSpinnerAdapter);
        methodListener();
        getBranchValue();
        return view;
    }

    private void init(View view) {
       editTextSName= (EditText) view.findViewById(R.id.editTextSName);
        editTextSMobile= (EditText) view.findViewById(R.id.ediTextSMobile);
        editTextSEmail= (EditText) view.findViewById(R.id.editTextSEmail);
        editTextSParentName= (EditText) view.findViewById(R.id.ediTextSParentName);
        editTextSParentEmail= (EditText) view.findViewById(R.id.editTextSParentEmail);
        editTextSParentMobile= (EditText) view.findViewById(R.id.editTextSParentMobile);
        spinnerBranchStudent= (Spinner) view.findViewById(R.id.spinnerBranchStudent);
        spinnerSemesterStudent= (Spinner) view.findViewById(R.id.spinnerSemesterStudent);
        buttonAddStudent= (Button) view.findViewById(R.id.buttonAddStudent);
    }

    private void methodListener() {
        spinnerBranchStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                BranchPojo pojo=arrayListBranch.get(i);
                enteredBranchName=pojo.getName();
                enteredBranchId=pojo.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerSemesterStudent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enteredSemester=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
       buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
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

    private void addStudent() {
        enteredName=editTextSName.getText().toString();
        enteredMobile=editTextSMobile.getText().toString();
         enteredEmail=editTextSEmail.getText().toString();
        enteredParentName=editTextSParentName.getText().toString();
         enteredParentEmail=editTextSParentEmail.getText().toString();
         enteredParentMobile=editTextSParentMobile.getText().toString();
        if(enteredName.equals("")|enteredMobile.equals("")|enteredEmail.equals("")|enteredParentName.equals("")|enteredParentEmail.equals("")
                |enteredParentMobile.equals("") | enteredSemester.equals("Select Semester")|enteredBranchName.equals("Select Branch"))
        {
            Toast.makeText(getActivity(), "Fill all fields", Toast.LENGTH_SHORT).show();
        }
        else{
            generatePassword(count);
            sendParentMessage();
            storeInDatabase();
            sendStudentMessage();

        }

    }

    private void generatePassword(int count) {
        StringBuffer buffer=new StringBuffer();
        StringBuffer buffer1=new StringBuffer();
        while(count-- !=0){
            int c= (int) (Math.random()*PASSWORD_LIST.length());
            int d= (int) (Math.random()*PASSWORD_LIST.length());
            buffer.append(PASSWORD_LIST.charAt(c));
            buffer1.append(PASSWORD_LIST.charAt(d));
        }
        enteredPassword=buffer.toString();
        Log.d(TAG, "generatePassword: "+enteredPassword);
        enteredParentPassword=buffer1.toString();
        Log.d(TAG, "generatePassword:parent "+enteredParentPassword);
    }

    private void sendStudentMessage() {
        StringRequest request= new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendhttp.php?authkey=164884A6wNlivfz5965b2a9&mobiles=" + enteredMobile + "&message=" + enteredEmail  + enteredPassword + "&sender=ABCDEF&route=4&country=91 ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "msg sent", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: msg sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse: error");
            }
        });
        RequestQueue queue=Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void sendParentMessage() {
        StringRequest request= new StringRequest(Request.Method.GET, "https://control.msg91.com/api/sendhttp.php?authkey=163118A9mzh8HCaq595493f5&mobiles=" + enteredParentMobile + "&message=" + enteredParentEmail  + enteredParentPassword + "&sender=ABCDEF&route=4&country=91 ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "sent to parent", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onResponse: parent msg sent");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error to parent", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onErrorResponse: parent error");
            }
        });
        RequestQueue queue=Volley.newRequestQueue(getContext());
        queue.add(request);
    }
    private void storeInDatabase() {
        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase = mysqliteHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Student.NAME, enteredName);
        cv.put(Student.MOBILE, enteredMobile);
        cv.put(Student.STUDENT_EMAIL, enteredEmail);
        cv.put(Student.STUDENT_PASSWORD, enteredPassword);
        cv.put(Student.PARENT_NAME, enteredParentName);
        cv.put(Student.PARENT_MOBILE, enteredParentMobile);
        cv.put(Student.PARENT_EMAIL, enteredParentEmail);
        cv.put(Student.PARENT_PASSWORD, enteredParentPassword);
        cv.put(Student.SEMESTER, enteredSemester);
        cv.put(Student.BRANCH_ID, enteredBranchId);
        cv.put(Student.IMAGE,"");

        long l = Student.insert(sqliteDatabase, cv);
        if (l > 0) {
            Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_SHORT).show();
            editTextSName.setText("");
            editTextSEmail.setText("");
            editTextSMobile.setText("");
            editTextSParentName.setText("");
            editTextSParentEmail.setText("");
            editTextSParentMobile.setText("");
            spinnerBranchStudent.setSelection(0);
            spinnerSemesterStudent.setSelection(0);
        } else {
            Toast.makeText(getActivity(), "Not inserted", Toast.LENGTH_SHORT).show();
        }
    }


}
