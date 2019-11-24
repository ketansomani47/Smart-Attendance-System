package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.SubjectPojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

/**
 * Created by Lenovo G580 on 04-07-2017.
 */

public class SubjectAdapter extends ArrayAdapter {
    private Context context;
    private   int resource;
    private ArrayList<SubjectPojo> arrayList;
    private ArrayList<SubjectPojo> filterArrayList=new ArrayList<>();
    LayoutInflater inflater;
    Fragment fragment;
    public SubjectAdapter(Context context,int resource,ArrayList<SubjectPojo> arrayList,Fragment fragment){
        super(context,resource,arrayList);
        this.context=context;
        this.resource=resource;
        this.arrayList=arrayList;
        filterArrayList.addAll(arrayList);
        this.fragment=fragment;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.subject_list_item,null);
        TextView subjectTextView= (TextView) view.findViewById(R.id.subjectTextView);
        TextView semesterTextView= (TextView) view.findViewById(R.id.semesterTextView);
        TextView branchTextView= (TextView) view.findViewById(R.id.branchTextView);
        ImageView subjectDelete= (ImageView) view.findViewById(R.id.subjectDelete);
        ImageView subjectUpdate= (ImageView) view.findViewById(R.id.subjectUpdate);

        SubjectPojo subjectPojo=arrayList.get(position);
        subjectTextView.setText(subjectPojo.getName());
        semesterTextView.setText(subjectPojo.getSemester());
        branchTextView.setText(subjectPojo.getBranch_name());
        return view;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }


    public void getFilter(String charText){
        arrayList.clear();
        if(charText.length()==0){
            arrayList.addAll(filterArrayList);
        }
        else{
            for(SubjectPojo pojo:filterArrayList){
                if(pojo.getName().toUpperCase().contains(charText.toUpperCase())||pojo.getBranch_name().toUpperCase().contains(charText.toUpperCase())
                        ||pojo.getSemester().toUpperCase().contains(charText.toUpperCase())){
                    arrayList.add(pojo);
                }
            }
        }
        notifyDataSetChanged();
    }
}
