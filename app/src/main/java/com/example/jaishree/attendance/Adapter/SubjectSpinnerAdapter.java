package com.example.jaishree.attendance.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

/**
 * Created by Lenovo G580 on 05-07-2017.
 */

public class SubjectSpinnerAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private Fragment fragment;
    ArrayList<SubjectPojo> arrayListSubject;
    LayoutInflater inflater;
    public SubjectSpinnerAdapter(Context context,int resource,ArrayList<SubjectPojo> arrayListSubject,Fragment fragment){
        super(context,resource,arrayListSubject);
        this.context=context;
        this.resource=resource;
        this.arrayListSubject=arrayListSubject;
        this.fragment=fragment;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.branch_spinner_list_item,null);
        TextView subjectSpinner= (TextView) view.findViewById(R.id.branchSpinnerListItem);
        SubjectPojo pojo=arrayListSubject.get(position);
        subjectSpinner.setText(pojo.getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.branch_spinner_list_item,null);
        TextView subjectSpinner= (TextView) view.findViewById(R.id.branchSpinnerListItem);
        SubjectPojo pojo=arrayListSubject.get(position);
        subjectSpinner.setText(pojo.getName());
        return view;
    }
}
