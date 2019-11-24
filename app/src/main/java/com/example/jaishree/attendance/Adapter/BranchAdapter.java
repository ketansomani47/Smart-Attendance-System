package com.example.jaishree.attendance.Adapter;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaishree.attendance.Pojo.BranchPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.addBranch;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.BranchTable;

import java.util.ArrayList;

/**
 * Created by Jaishree on 30-06-2017.
 */

public class BranchAdapter extends ArrayAdapter {

    private Context context;
    private int layoutResource;
    private ArrayList<BranchPojo> arrayList;
    private LayoutInflater inflater;
    private Fragment fragment;
    public BranchAdapter(Context context,int layoutResource,ArrayList<BranchPojo> arrayList,Fragment fragment){
        super(context,layoutResource,arrayList);
        this.context=context;
        this.layoutResource=layoutResource;
        this.arrayList=arrayList;
        this.fragment=fragment;
        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.branch_list_item,null) ;
        TextView textViewBranch= (TextView) view.findViewById(R.id.textViewBranch);
        ImageView branchDelete= (ImageView) view.findViewById(R.id.branchDelete);
        ImageView branchUpdate= (ImageView) view.findViewById(R.id.branchUpdate);

        BranchPojo pojo=arrayList.get(position);

        textViewBranch.setText(pojo.getName());

        branchDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete").setMessage("Are you sure you want to delete")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(addBranch.rowId);

                                }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        });

        branchUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(addBranch.rowId,addBranch.clickPosition);
            }
        });
        return view;
    }

    private void delete(int rowId) {
        MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(context);
        SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();
         String selection= BranchTable.ID + "='" + rowId + "'" ;

        int d= BranchTable.delete(db,selection);
        if(d>0){
            Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
            ((addBranch)fragment).fetchFromDatabase();

        }
        else {
            Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void update(final int rowId, final int clickPosition) {
        BranchPojo dialogPojo=arrayList.get(clickPosition);
        final Dialog dialog=new Dialog(context);
        Log.d("123", "update: "+clickPosition);
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.branch_dialog,null);
        final EditText branchDialogEditText= (EditText) view.findViewById(R.id.branchDialogEditText);
        ImageView branchDialogAdd= (ImageView) view.findViewById(R.id.branchDialogAdd);

        branchDialogEditText.setText(dialogPojo.getName());

        branchDialogAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialogEnteredText=branchDialogEditText.getText().toString();
                if(dialogEnteredText.equals("")){
                    Toast.makeText(context, "fill BranchTable", Toast.LENGTH_SHORT).show();
                }
                else{
                    MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(context);
                    SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();

                    ContentValues cv=new ContentValues();
                    cv.put(BranchTable.NAME,dialogEnteredText);
                    String selection= BranchTable.ID + "='" + rowId + "'" ;
                    long l= BranchTable.update(db,cv,selection);
                    if(l>0){
                        dialog.cancel();
                        Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                        ((addBranch)fragment).fetchFromDatabase();
                    }
                    else{
                        Toast.makeText(context, "not updated", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

}
