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

import java.util.ArrayList;

/**
 * Created by kylez on 10/29/15.
 */
public class ListPopulator extends SQLiteOpenHelper implements ListAdapter {
    static final String TABLE_NAME = "ToDoTasks";
    static final String DATABASE_NAME = "ToDoTasks.db";
    static final String TASK_COLUMN = "taskName";
    static final String DATE_COLUMN = "date";
    static final String IMPORTANCE_COLUMN = "importance";
    static final int DATABASE_VERSION = 1;

    Context context;

    ArrayList<ToDoTask> tasks;
    ToDoListActivity master;

    public ListPopulator(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

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
        if(txt.getText().equals("Important"))
        {
            txt.setTextColor(master.getResources().getColor(R.color.colorImportant));
        } else if(txt.getText().equals("Regular"))
        {
            txt.setTextColor(master.getResources().getColor(R.color.colorRegular));
        } else
        {
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
        ContentValues values = new ContentValues();
        values.put(TASK_COLUMN, task.taskName);
        values.put(DATE_COLUMN, task.dateString);
        values.put(IMPORTANCE_COLUMN, task.importance);

        SQLiteDatabase db = getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
        db.close();

        getTaskCodesInTable();
    }

    public void removeTask(int index)
    {
        ToDoTask task = tasks.get(index);

        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME, TASK_COLUMN+" = '"+ task.taskName+"'", null);
        getTaskCodesInTable();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTableString = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"("+TASK_COLUMN+" TEXT primary key, " +
                DATE_COLUMN+" TEXT,"+ IMPORTANCE_COLUMN+" TEXT"+")";
        Log.d("Debug", createTableString);
        sqLiteDatabase.execSQL(createTableString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void getTaskCodesInTable()
    {
        String query = "SELECT * FROM " + TABLE_NAME;

        ArrayList<ToDoTask> tasks = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        ToDoTask task;

        while(c.moveToNext())
        {
            task = new ToDoTask(c.getString(0), c.getString(1), c.getString(2));
            tasks.add(task);
        }

        if(tasks.size()==0)
        {
            ToDoTask defaultTask = new ToDoTask("This is a task!", "January 15, 2015", "Important");
            addTask(defaultTask);
            tasks.add(defaultTask);
        }

        c.close();
        db.close();

        this.tasks = tasks;
    }


}
