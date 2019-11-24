package com.example.jaishree.attendance.Teacher;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.lib.widget.verticalmarqueetextview.VerticalMarqueeTextView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jaishree.attendance.R;
import com.example.jaishree.attendance.database.MySqliteOpenHelper;
import com.example.jaishree.attendance.table.News;

/**
 * Created by Lenovo G580 on 04-07-2017.
 */

public class TeacherNewsFragment extends Fragment {
    VerticalMarqueeTextView verticalMarqueeTextView;
    private String news = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teacher_news_fragment, null);
        verticalMarqueeTextView = (VerticalMarqueeTextView) view.findViewById(R.id.verticalMarqueeTextView);
        fetchData();

        return view;
    }

    private void fetchData() {
        MySqliteOpenHelper mysqliteHelper = new MySqliteOpenHelper(getActivity());
        SQLiteDatabase sqliteDatabase = mysqliteHelper.getReadableDatabase();

        Cursor cursor = News.select(sqliteDatabase, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String str = cursor.getString(1);
                news += "\u2022" + str + "\n\n";
                Log.d("12", "fetchData: "+news);

            }
            verticalMarqueeTextView.setText(news);
        }
        cursor.close();
        sqliteDatabase.close();
    }
}