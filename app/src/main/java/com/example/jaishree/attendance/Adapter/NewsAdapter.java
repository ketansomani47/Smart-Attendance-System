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

import com.example.jaishree.attendance.Pojo.NewsPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.admin.AddNews;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.News;

import java.util.ArrayList;

/**
 * Created by Lenovo G580 on 03-07-2017.
 */

public class NewsAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<NewsPojo> arrayList;
    private LayoutInflater inflater;
    private Fragment fragment;

    public NewsAdapter(Context context, int resource, ArrayList<NewsPojo> arrayList,Fragment fragment) {
        super(context, resource, arrayList);
        this.context=context;
        this.resource=resource;
        this.arrayList=arrayList;
        this.fragment=fragment;

        inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(R.layout.news_list_item,null);

        TextView textViewNews= (TextView) view.findViewById(R.id.textViewNews);
        ImageView imageViewDelete= (ImageView) view.findViewById(R.id.imageViewDelete);
        ImageView imageViewEdit= (ImageView) view.findViewById(R.id.imageViewEdit);

        NewsPojo newsPojo=arrayList.get(position);
        textViewNews.setText(newsPojo.getName());

        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delete(AddNews.rowId);
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

        imageViewEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(AddNews.rowId,AddNews.listClickPosition);
            }
        });

        return view;
    }

    private void delete(int rowId) {
        MySqliteOpenHelper sqliteHelper=new MySqliteOpenHelper(context);
        SQLiteDatabase sqliteDatabase=sqliteHelper.getWritableDatabase();

        String selection= News.ID + "= '" + rowId +"'";
        int i=News.delete(sqliteDatabase,selection);
        if(i>0){
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            ((AddNews)fragment).fetchDatabase();
        }
        else{
            Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    private void update(final int rowId, int listClickPosition) {
        NewsPojo dialogPojo=arrayList.get(listClickPosition);
        final Dialog dialog=new Dialog(context);
        Log.d("123", "update: "+listClickPosition);
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
                    Toast.makeText(context, "fill news", Toast.LENGTH_SHORT).show();
                }
                else{
                    MySqliteOpenHelper mySqliteOpenHelper=new MySqliteOpenHelper(context);
                    SQLiteDatabase db=mySqliteOpenHelper.getWritableDatabase();

                    ContentValues cv=new ContentValues();
                    cv.put(News.NAME,dialogEnteredText);
                    String selection= News.ID + "='" + rowId + "'" ;
                    long l=News.update(db,cv,selection);
                    if(l>0){
                        dialog.cancel();
                        Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                        ((AddNews)fragment).fetchDatabase();
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

