package com.example.kylez.homework4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kylez on 11/13/15.
 */
public class RemoteListPopulator implements ListAdapter
{
    Context context;

    ArrayList<ToDoTask> tasks;
    ToDoListActivity master;

    public RemoteListPopulator(Context context) {
        this.context = context;

        tasks = new ArrayList<ToDoTask>();

        this.getTaskCodesInTable();
    }

    // List Adapter Methods
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {

        return tasks.size();
    }

    @Override
    public Object getItem(int i) {
        return tasks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.task_item, viewGroup, false);

        TextView titleView = (TextView)view.findViewById(R.id.titleText);
        TextView dateView = (TextView)view.findViewById(R.id.dateText);
        TextView importanceView = (TextView)view.findViewById(R.id.importanceText);

        ToDoTask task = (ToDoTask)getItem(i);

        titleView.setText(task.taskName);
        dateView.setText(task.dateString);
        importanceView.setText(task.importance);

        styleImportance(importanceView);

        return view;
    }

    public void styleImportance(TextView txt)
    {
        if(txt.getText().toString().equals("Important"))
        {
            Log.d("Debug", "Red");
            txt.setTextColor(master.getResources().getColor(R.color.colorImportant));
        } else if(txt.getText().equals("Regular"))
        {
            Log.d("Debug", "Yellow");
            txt.setTextColor(master.getResources().getColor(R.color.colorRegular));
        } else
        {
            Log.d("Debug", "Blue");
            txt.setTextColor(master.getResources().getColor(R.color.colorMinor));
        }
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return tasks.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    // SQL Lite methods
    public void addTask(ToDoTask task)
    {
        new Networking.AddTask(this, task).execute();
    }

    public void removeTask(int index)
    {
        new Networking.RemoveTask(this, tasks.get(index)).execute();

        tasks.remove(index);
    }

    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String createTableString = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+TASK_COLUMN+" TEXT primary key, " +
//                DATE_COLUMN+" TEXT,"+ IMPORTANCE_COLUMN+" TEXT"+")";
//        Log.d("Debug", createTableString);
//        sqLiteDatabase.execSQL(createTableString);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
//        onCreate(sqLiteDatabase);
    }

    public void getTaskCodesInTable()
    {
        new Networking.GetTaskListTask(this).execute();
    }

    public void setTasks(ArrayList<ToDoTask> tasks)
    {
        this.tasks = tasks;

        if(tasks.size() > 0)
        {
            master.finishSetup();
        }
    }

}
