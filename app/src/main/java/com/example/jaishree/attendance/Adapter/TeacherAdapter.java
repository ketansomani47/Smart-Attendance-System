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

import com.example.jaishree.attendance.Pojo.TeacherPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.ViewTeacher;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.Teacher;

import java.util.ArrayList;

/**
 * Created by Lenovo G580 on 05-07-2017.
 */

public class TeacherAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<TeacherPojo> arrayListTeacher;
    private ArrayList<TeacherPojo> filterArrayList=new ArrayList<>();
    private LayoutInflater inflater;
    private Fragment fragment;
    public TeacherAdapter(Context context,int resource,ArrayList<TeacherPojo> arrayListTeacher,Fragment fragment){
        super(context,resource,arrayListTeacher);
        this.context=context;
        this.resource=resource;
        this.arrayListTeacher=arrayListTeacher;
        filterArrayList.addAll(arrayListTeacher);
        Log.d("12", "TeacherAdapter: "+arrayListTeacher.size());
        Log.d("12", "TeacherAdapter: "+filterArrayList.size());
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

        TeacherPojo pojo=arrayListTeacher.get(position);
        final int listRowId=pojo.getId();
        textViewName.setText(pojo.getName());
        textViewMobile.setText(pojo.getMobile());
        textViewSubject.setText(pojo.getSubject_name());

        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete it?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(listRowId);

                                //Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
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
        circleTextView.setText((""+pojo.getName().charAt(0)).toUpperCase());
        GradientDrawable gd= (GradientDrawable) circleTextView.getBackground();
        int red= (int) (Math.random()*255);
        int green= (int) (Math.random()*255);
        int blue= (int) (Math.random()*255);
        gd.setColor(Color.rgb(red,green,blue));
        return view;
    }

    @Override
    public int getCount() {
        filterArrayList.size();
        Log.d("12", "getCount: "+filterArrayList.size());
        Log.d("12", "getCount: "+arrayListTeacher.size());
        return arrayListTeacher.size();
    }

    public void getFilter(String charText){
        arrayListTeacher.clear();
        if(charText.length()==0){
            arrayListTeacher.addAll(filterArrayList);
        }
        else{
            for(TeacherPojo pojo:filterArrayList){
                if(pojo.getName().toUpperCase().contains(charText.toUpperCase())||pojo.getSemester().toUpperCase().contains(charText.toUpperCase())
                        ||pojo.getBranch_name().toUpperCase().contains(charText.toUpperCase())|| pojo.getSubject_name().toUpperCase().contains(charText.toUpperCase())){
                    arrayListTeacher.add(pojo);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void delete(int rowId) {

        MySqliteOpenHelper mysqliteHelper=new MySqliteOpenHelper(getContext());
        SQLiteDatabase sqliteDatabase=mysqliteHelper.getReadableDatabase();
        String selection= Teacher.ID + "='" + rowId + "'";
        int d=Teacher.delete(sqliteDatabase,selection);
        if(d>0){
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            ((ViewTeacher)fragment).fetchDatabase();
        }
        else{
            Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
