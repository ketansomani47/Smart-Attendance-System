package com.example.jaishree.attendance.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.jaishree.attendance.Pojo.TakeAttendancePojo;
import com.example.jaishree.attendance.R;

import java.util.ArrayList;

/**
 * Created by Lenovo G580 on 14-07-2017.
 */

public class TakeAttendanceAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResource;
    private ArrayList<TakeAttendancePojo> arrayList;
    private LayoutInflater inflater;
    int rowId;

    public TakeAttendanceAdapter(Context context, int layoutResource, ArrayList<TakeAttendancePojo> arrayList) {
        super(context, layoutResource, arrayList);
        this.context = context;
        this.layoutResource = layoutResource;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.take_attendance_list_item, null);

        TextView textViewAttendanceName = (TextView) view.findViewById(R.id.textViewAttendanceName);
        TextView textViewAttendanceMobile = (TextView) view.findViewById(R.id.textViewAttendanceMobile);
        RadioGroup radioGroupTakeAttendance = (RadioGroup) view.findViewById(R.id.radioGroupTakeAttendance);
        RadioButton radioButtonP = (RadioButton) view.findViewById(R.id.radioButtonP);
        RadioButton radioButtonA = (RadioButton) view.findViewById(R.id.radioButtonA);
        RadioButton radioButtonL = (RadioButton) view.findViewById(R.id.radioButtonL);
        TextView textViewAttendance= (TextView) view.findViewById(R.id.textViewAttendance);
        final TakeAttendancePojo pojo = arrayList.get(position);
        rowId=pojo.getId();
        textViewAttendanceName.setText(pojo.getName());
        textViewAttendanceMobile.setText(pojo.getMobile());

        textViewAttendance.setText((""+pojo.getName().charAt(0)).toUpperCase());
        GradientDrawable gd= (GradientDrawable) textViewAttendance.getBackground();
        int red= (int) (Math.random()*255);
        int green= (int) (Math.random()*255);
        int blue= (int) (Math.random()*255);
        gd.setColor(Color.rgb(red,green,blue));

        switch (pojo.getStatus()) {
            case "present":
                radioButtonP.setChecked(true);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            case "absent":
                radioButtonA.setChecked(true);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            case "leave":
                radioButtonL.setChecked(true);
                Log.d("123", "status: " + pojo.getStatus());
                break;
            default:
                radioButtonP.setChecked(false);
                Log.d("123", "status: " + pojo.getStatus());
                radioButtonA.setChecked(false);
                radioButtonL.setChecked(false);
                break;
        }
        radioGroupTakeAttendance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonP:
                        pojo.setStatus("present");
                        Log.d("123", "onCheckedChangedP: "+pojo.getStatus());
                        break;
                    case R.id.radioButtonA:
                        pojo.setStatus("absent");
                        Log.d("123", "onCheckedChangedA: "+pojo.getStatus());
                        break;
                    case R.id.radioButtonL:
                        pojo.setStatus("leave");
                        Log.d("123", "onCheckedChangedL: "+pojo.getStatus());
                        break;
                        default:
                            break;

                }

            }
        });

        return view;
    }
}
