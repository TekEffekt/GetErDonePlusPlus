package com.example.kylez.homework4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Arrays;

/**
 * Created by kylez on 10/30/15.
 */
public class AddTaskDialogeFragment extends DialogFragment implements AdapterView.OnItemSelectedListener
{
    EditText nameField;
    Spinner monthField;
    EditText dayField;
    EditText yearField;
    Spinner importanceField;

    String month = "January";
    String importance = "Important";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.add_task_dialogue, null);

        builder.setMessage("Add new task");
        builder.setView(view);

        setupSpinnersWithView(view);
        setupCreateButtonWithView(view);
        loadFieldsView(view);

        return builder.create();
    }

    public void loadFieldsView(View view)
    {
        nameField = (EditText)view.findViewById(R.id.taskNameField);
        monthField = (Spinner)view.findViewById(R.id.monthSpinner);
        dayField = (EditText)view.findViewById(R.id.dayTextField);
        yearField = (EditText)view.findViewById(R.id.yearTextField);
        importanceField = (Spinner)view.findViewById(R.id.importanceSpinner);
    }

    public void setupSpinnersWithView(View view)
    {
        Spinner monthSpinner = (Spinner)view.findViewById(R.id.monthSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.months_array, R.layout.spinner_view);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);
        monthSpinner.setOnItemSelectedListener(this);

        Spinner importanceSpinner = (Spinner)view.findViewById(R.id.importanceSpinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.importance_array, R.layout.spinner_view);
        importanceSpinner.setOnItemSelectedListener(this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        importanceSpinner.setAdapter(adapter);
    }

    public void setupCreateButtonWithView(View view)
    {
        Button createButton = (Button)view.findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToDoListActivity master = (ToDoListActivity) getActivity();
                master.createTask(getNewTaskCode());
                dismiss();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        if(view.getParent() == monthField)
        {
            month = getResources().getStringArray(R.array.months_array)[pos];
        } else
        {
            importance = getResources().getStringArray(R.array.importance_array)[pos];
        }
    }

    public ToDoTask getNewTaskCode()
    {
        String taskName = nameField.getText().toString();
        String date =  month +" "+dayField.getText() +", "+ yearField.getText();

        int monthIndex = (Arrays.asList(getResources().getStringArray(R.array.months_array))).indexOf(month);

        ToDoTask newTask = new ToDoTask(taskName, date, importance);

        return newTask;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
