package com.example.kylez.homework4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToDoListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ListPopulator adapter;
    ListView toDoListView;

    ToDoListActivity mActivity = this;

    int gPosition;

    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;

    String currentSortMethod = "Importance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adapter = new ListPopulator(this);
        adapter.master = this;
        toDoListView = (ListView)findViewById(R.id.task_list);

        toDoListView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddTaskDialogeFragment frag = new AddTaskDialogeFragment();
                frag.show(getFragmentManager(), "addTask");
            }
        });

        toDoListView.setOnTouchListener(new SwipeDismissListViewTouchListener(
                        toDoListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {

                                gPosition = position;

                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                Log.d("Debug", "Removed");

                                if (adapter.tasks.size() > 1) {
                                    adapter.removeTask(gPosition);
                                    ListPopulator a = new ListPopulator(getApplicationContext());
                                    a.master = mActivity;
                                    toDoListView.setAdapter(a);
                                    adapter = a;
                                    sort();
                                }
                            }
                })
        );

        setupSortSpinner();
    }

    public void createTask(ToDoTask task) {
        adapter.addTask(task);
        ListPopulator a = new ListPopulator(getApplicationContext());
        a.master = mActivity;
        toDoListView.setAdapter(a);
        adapter = a;
        sort();
    }

    public void setupSortSpinner()
    {
        Spinner spinner = (Spinner)findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, R.layout.spinner_view_main);
        adapter.setDropDownViewResource(R.layout.spinner_view);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            currentSortMethod = "Importance";
        } else
        {
            currentSortMethod = "Date";
        }

        sort();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        sort();
    }

    public void sort()
    {
        if(currentSortMethod.equals("Importance"))
        {
            sortByImportance();
        } else
        {
            sortByDate();
        }

        toDoListView.setAdapter(adapter);
    }

    public void sortByDate()
    {
        Collections.sort(adapter.tasks, new Comparator<ToDoTask>() {
            @Override
            public int compare(ToDoTask lhs, ToDoTask rhs) {
                if(lhs.date.compareTo(rhs.date) == -1)
                {
                    return 1;
                } else if(lhs.date.compareTo(rhs.date) == 1)
                {
                    return -1;
                } else
                {
                    return 0;
                }
            }
        });
    }

    public void sortByImportance() {
        Collections.sort(adapter.tasks, new Comparator<ToDoTask>() {
            @Override
            public int compare(ToDoTask lhs, ToDoTask rhs) {
                ArrayList<String> array = new ArrayList(Arrays.asList(getResources().getStringArray(R.array.importance_array)));

                int lIndex = array.indexOf(lhs.importance);
                int rIndex = array.indexOf(rhs.importance);

                if (lIndex == rIndex) {
                    return 0;
                } else if (lIndex < rIndex) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

}
