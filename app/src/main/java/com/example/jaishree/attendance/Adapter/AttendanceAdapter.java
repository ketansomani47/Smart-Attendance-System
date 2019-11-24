package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

import static com.example.jaishree.attendance.R.id.textViewAttendance;

/**
 * Created by Lenovo G580 on 14-07-2017.
 */

public class AttendanceAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private ArrayList<SubjectPojo> attendanceArrayList;
    private LayoutInflater inflater;
    public AttendanceAdapter(Context context,int layoutResource,ArrayList<SubjectPojo> attendanceArrayList) {
        super(context, layoutResource, attendanceArrayList);
        this.context = context;
        this.layoutResource = layoutResource;
        this.attendanceArrayList = attendanceArrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
            public View getView(final int position, View convertView, ViewGroup parent) {
            View view = inflater.inflate(R.layout.teachersubject_list_item, null);
            TextView textViewTSubName = (TextView) view.findViewById(R.id.textViewTSubName);
            TextView textViewTSubSem = (TextView) view.findViewById(R.id.textViewTSubSem);
            TextView textViewTSubBranch = (TextView) view.findViewById(R.id.textViewTSubBranch);
                TextView circleImageTSub= (TextView) view.findViewById(R.id.circleImageTSub);

            SubjectPojo pojo = attendanceArrayList.get(position);
            textViewTSubName.setText(pojo.getName());
            textViewTSubSem.setText(pojo.getSemester());
            textViewTSubBranch.setText(pojo.getBranch_name());
                circleImageTSub.setText((""+pojo.getName().charAt(0)).toUpperCase());
                GradientDrawable gd= (GradientDrawable) circleImageTSub.getBackground();
                int red= (int) (Math.random()*255);
                int green= (int) (Math.random()*255);
                int blue= (int) (Math.random()*255);
                gd.setColor(Color.rgb(red,green,blue));

            return view;
        }
}

