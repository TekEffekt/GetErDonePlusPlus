package com.example.kylez.homework4;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kylez on 10/29/15.
 */
public class ToDoTask
{
    String taskName;
    Date date;
    String dateString;
    String importance;

    public ToDoTask(String taskName, String dateString, String importance)
    {
        this.taskName = taskName;
        this.dateString = dateString;
        this.date = dateForString(dateString);
        this.importance = importance;
    }

    public Date dateForString(String dateS)
    {
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date dateObject = null;

        try {
            dateObject = format.parse(dateS);
            if(dateObject == null)
            {
                dateObject = format.parse("January 1, 0001");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Debug", "Exception");
        }

        return dateObject;
    }

}
