package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

/**
 * Created by Lenovo G580 on 05-07-2017.
 */

public class BranchSpinnerAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<BranchPojo> arrayListBranch;
    private Fragment fragment;
    private LayoutInflater inflater;
    public BranchSpinnerAdapter(Context context,int resource,ArrayList<BranchPojo> arrayListBranch,Fragment fragment){
        super(context,resource,arrayListBranch);
        this.context=context;
        this.resource=resource;
        this.arrayListBranch=arrayListBranch;
        this.fragment=fragment;
         inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.branch_spinner_list_item,null);
        TextView textview= (TextView) view.findViewById(R.id.branchSpinnerListItem);

        BranchPojo pojo1=arrayListBranch.get(0);
        textview.setText(pojo1.getName());
        BranchPojo pojo=arrayListBranch.get(position);
        textview.setText(pojo.getName());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.branch_spinner_list_item,null);
        TextView textview= (TextView) view.findViewById(R.id.branchSpinnerListItem);

        BranchPojo pojo1=arrayListBranch.get(0);
        textview.setText(pojo1.getName());
        BranchPojo pojo=arrayListBranch.get(position);
        textview.setText(pojo.getName());
        return view;
    }
}
