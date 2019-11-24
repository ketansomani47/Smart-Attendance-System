package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.Pojo.StudentPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.ViewStudent;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Student;

import java.util.ArrayList;


/**
 * Created by Lenovo G580 on 07-07-2017.
 */

public class StudentAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    ArrayList<StudentPojo> arrayListStudent;
    ArrayList<StudentPojo> filterArrayList=new ArrayList<>();
    private LayoutInflater inflater;
    private Fragment fragment;
    public StudentAdapter(Context context,int resource,ArrayList<StudentPojo> arrayListStudent,Fragment fragment){
        super(context,resource,arrayListStudent);
        this.context=context;
        this.resource=resource;
        this.arrayListStudent=arrayListStudent;
        filterArrayList.addAll(arrayListStudent);
        this.fragment=fragment;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.teacher_list_item,null);
        TextView textViewName= (TextView) view.findViewById(R.id.textViewName);
        TextView textViewMobile= (TextView) view.findViewById(R.id.textViewMobile);
        TextView textViewSubject= (TextView) view.findViewById(R.id.textViewSubject);
        TextView circleTextView= (TextView) view.findViewById(R.id.circleTextView);
        ImageView deleteImageView= (ImageView) view.findViewById(R.id.deleteImageView);
        ImageView updateImageView= (ImageView) view.findViewById(R.id.updateImageView);

        StudentPojo pojo=arrayListStudent.get(position);
        Log.d("123", "getView: "+pojo);
        final int listRowId=pojo.getId();
        textViewName.setText(pojo.getName());
        textViewMobile.setText(pojo.getMobile());
        textViewSubject.setText(pojo.getBranch_name());

        GradientDrawable d= (GradientDrawable) circleTextView.getBackground();
        int red= (int) (Math.random()*255);
        int green= (int) (Math.random()*255);
        int blue= (int) (Math.random()*255);
        d.setColor(Color.rgb(red,green,blue));

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you really want to delete this?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(listRowId);
                                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
        updateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return arrayListStudent.size();
    }


    public  void getFilter(String charText){
        arrayListStudent.clear();
        if(charText.length()==0){
            arrayListStudent.addAll(filterArrayList);
        }
        else {
            for(StudentPojo pojo:filterArrayList){
                if((pojo.getName().toUpperCase().contains(charText.toUpperCase()))||pojo.getSemester().toUpperCase().contains(charText.toUpperCase())
                        ||pojo.getBranch_name().toUpperCase().contains(charText.toUpperCase())){
                    arrayListStudent.add(pojo);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void delete(int listRowId) {
        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(getContext());
        SQLiteDatabase sqliteDatabase=mysqliteHelper.getWritableDatabase();
        String selection=Student.ID+ "='" + listRowId + "'";

        int c= Student.delete(sqliteDatabase,selection);
        if(c>0){
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            ((ViewStudent)fragment).fetchDatabase();
        }
        else {
            Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
        }
    }

}
