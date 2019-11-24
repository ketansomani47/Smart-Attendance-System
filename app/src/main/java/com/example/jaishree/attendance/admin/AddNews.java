package com.example.jaishree.attendance.admin;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jaishree.attendance.Adapter.NewsAdapter;
import com.example.jaishree.attendance.Pojo.NewsPojo;
import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.News;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Lenovo G580 on 03-07-2017.
 */

public class AddNews extends Fragment {
    EditText editTextNews;
    Button buttonSendNews;
    ListView listViewNews;
    ArrayList<NewsPojo> arrayList=new ArrayList<>();
    NewsAdapter newsAdapter;
    private String dateSelected="",timeSelected="";
    public static int rowId,listClickPosition;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.addnews,null);
        init(view);
        methodListener();
        newsAdapter=new NewsAdapter(getContext(),R.layout.news_list_item,arrayList,AddNews.this);
        listViewNews.setAdapter(newsAdapter);
        fetchDatabase();
return view;
    }

    private void init(View view) {
        editTextNews= (EditText) view.findViewById(R.id.editTextNews);
        buttonSendNews= (Button) view.findViewById(R.id.buttonSendNews);
        listViewNews= (ListView) view.findViewById(R.id.listViewNews);
    }

    private void methodListener() {
        buttonSendNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNews();
            }
        });
        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                rowId=arrayList.get(position).getId();
                listClickPosition=position;

            }
        });
    }

    private void sendNews() {
        String enteredNews=editTextNews.getText().toString();
        if(enteredNews.equals("")){
            Toast.makeText(getActivity(), "Enter News", Toast.LENGTH_SHORT).show();
        }
        else{
            MySqliteOpenHelper sqliteHelper=new MySqliteOpenHelper(getContext());
            SQLiteDatabase sqliteDatabase=sqliteHelper.getWritableDatabase();

            ContentValues contentValues=new ContentValues();
            contentValues.put(News.NAME,enteredNews);
            Calendar calander=Calendar.getInstance();
            String day= String.valueOf(calander.DAY_OF_MONTH);
            String month=String.valueOf(calander.MONTH);
            String year=String.valueOf(calander.YEAR);
            dateSelected=""+day+"/"+(month+1)+"/"+year;

            contentValues.put(News.DATE,dateSelected);

            String hour= String.valueOf(calander.HOUR);
            String minute=String.valueOf(calander.MINUTE);
            dateSelected=""+hour+":"+minute;
            contentValues.put(News.TIME,timeSelected);

            long l=News.insert(sqliteDatabase,contentValues);
            if(l>0){
                Toast.makeText(getActivity(), "Inserted", Toast.LENGTH_SHORT).show();
                editTextNews.setText("");
                fetchDatabase();
            }
            else{
                Toast.makeText(getActivity(), "Not inserted", Toast.LENGTH_SHORT).show();
            }
            sqliteDatabase.close();
        }
    }

    public void fetchDatabase() {
        arrayList.clear();
        MySqliteOpenHelper sqliteHelper=new MySqliteOpenHelper(getContext());
        SQLiteDatabase sqliteDatabase=sqliteHelper.getReadableDatabase();

        Cursor cursor=News.select(sqliteDatabase,null);
        if(cursor!=null){
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
            String news=cursor.getString(1);
                String date=cursor.getString(2);
                String time=cursor.getString(3);

                NewsPojo newsPojo=new NewsPojo();
                newsPojo.setId(id);
                newsPojo.setName(news);
                newsPojo.setDate(date);
                newsPojo.setTime(time);
                arrayList.add(newsPojo);
            }
            newsAdapter.notifyDataSetChanged();
        }
        cursor.close();
        sqliteDatabase.close();

    }
}
