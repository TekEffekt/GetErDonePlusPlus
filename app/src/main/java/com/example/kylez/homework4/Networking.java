package com.example.kylez.homework4;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by kylez on 11/13/15.
 */
public class Networking
{
    static final String baseUrl = "http://kylezawacki.com/ToDo/";
    static final String getTasksUrl = baseUrl + "getTaskList.php";
    static final String addTaskUrl = baseUrl + "addTask.php";
    static final String removeTaskUrl = baseUrl + "removeTask.php";

    public static class GetTaskListTask extends AsyncTask<Void, Void, ArrayList>
    {
        RemoteListPopulator master;

        public GetTaskListTask(RemoteListPopulator context)
        {
            master = context;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);
            master.setTasks(result);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            HttpURLConnection connection = null;
            ArrayList<ToDoTask> result = new ArrayList<>();

            try
            {
                URL url = new URL(getTasksUrl);
                connection = (HttpURLConnection)url.openConnection();
//            connection.setDoOutput(true);
//            connection.setChunkedStreamingMode(0);
//            connection.setRequestMethod("POST");

                connection.setUseCaches(false);
                connection.setDoInput(true);

//            OutputStream out = new BufferedOutputStream(connection.getOutputStream());

                InputStream in = new BufferedInputStream(connection.getInputStream());

                StringWriter writer = new StringWriter();
                IOUtils.copy(in, writer);
                String theString = writer.toString();

                String[] taskStrings = theString.split("/");

                for(String task: taskStrings)
                {
                    String[] fields = task.split("!");

                    Log.d("Debug", Arrays.asList(fields).toString());

                    ToDoTask newTask = new ToDoTask(fields[0].trim(), fields[1].trim(), fields[2].trim());
                    result.add(newTask);
                }

                return  result;
            } catch(Exception ex){
                Log.d("Debug", ex.toString());
            } finally
            {
                connection.disconnect();
            }

            return result;
        }
    }

    public static class AddTask extends AsyncTask<Void, Void, String>
    {
        RemoteListPopulator master;
        ToDoTask task;

        public AddTask(RemoteListPopulator context, ToDoTask task)
        {
            master = context;
            this.task = task;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            master.getTaskCodesInTable();
        }

        @Override
        protected String doInBackground(Void... params) {
            URLConnection conn = null;
            String result = "";
            try
            {
                String urlParameters = String.format("taskName=%s&date=%s&importance=%s", task.taskName, task.dateString, task.importance);
                URL url = new URL(addTaskUrl);
                conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
                writer.flush();

                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                writer.close();
                reader.close();

            } catch(Exception ex){
                Log.d("Debug", ex.toString());
            }

            return result;
        }
    }


    public static class RemoveTask extends AsyncTask<Void, Void, String>
    {
        RemoteListPopulator master;
        ToDoTask task;

        public RemoveTask(RemoteListPopulator context, ToDoTask task)
        {
            master = context;
            this.task = task;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //master.getTaskCodesInTable();
        }

        @Override
        protected String doInBackground(Void... params) {
            URLConnection conn = null;
            String result = "";
            try
            {
                String urlParameters = String.format("taskName=%s", task.taskName);
                URL url = new URL(removeTaskUrl);
                conn = url.openConnection();

                conn.setDoOutput(true);

                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

                writer.write(urlParameters);
                writer.flush();

                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                writer.close();
                reader.close();

            } catch(Exception ex){
                Log.d("Debug", ex.toString());
            }

            return result;
        }
    }
}
