package ca.ualberta.seneshen_countbook;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * MainActivity is the activity that is first instantiated when the application is run.
 * It is responsible for loading the counter list from data storage, setting up the counter adapter and recyclerview,
 * setting the onClick handler of the floating action button, and handling all dialog boxes.
 *
 * Design rationale: MainActivity contains the view and dialog logic because it is the parent context
 * in which these views run, and the output directly affects this Activity.
 */
public class MainActivity extends AppCompatActivity {

    private TextView countText;
    private RecyclerView counterRecyclerView;
    private CounterAdapter adapter;
    private ArrayList<Counter> counterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCounter();
            }
        });

        counterRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        counterRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));

        countText = (TextView) findViewById(R.id.count_text);

        loadFromFile();

    }

    protected void editCounter(int position) {

        final Counter counter = counterList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.counter_edit_layout, null);

        final EditText nameInput = (EditText) dialogView.findViewById(R.id.name_input);
        final EditText initialValueInput =  (EditText) dialogView.findViewById(R.id.initial_value_input);
        final EditText currentValueInput =  (EditText) dialogView.findViewById(R.id.current_value_input);
        final EditText descriptionInput = (EditText) dialogView.findViewById(R.id.description_input);

        nameInput.setText(counter.getName());
        initialValueInput.setText(String.valueOf(counter.getCountInit()));
        currentValueInput.setText(String.valueOf(counter.getCountCurrent()));
        descriptionInput.setText(counter.getComment());

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // This is handled below in an click listener, in order to handle input validation. It cannot be handled here.
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No action needed to cancel dialog.
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        Button save_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Validate input:

                String name = nameInput.getText().toString();

                if(name.contentEquals("")){
                    Toast.makeText(MainActivity.this, "You must enter a name.", Toast.LENGTH_LONG).show();
                    return;
                }else if(initialValueInput.getText().toString().contentEquals("") || currentValueInput.getText().toString().contentEquals("")){
                    Toast.makeText(MainActivity.this, "You must enter an initial and current counter value.", Toast.LENGTH_LONG).show();
                    return;
                }

                counter.setName(nameInput.getText().toString());
                counter.setCountInit(Integer.valueOf(initialValueInput.getText().toString()));
                counter.setCountCurrent(Integer.valueOf(currentValueInput.getText().toString()));
                counter.setComment(descriptionInput.getText().toString());

                saveAndUpdateData();

                dialog.dismiss();

            }
        });

    }

    protected void deleteCounter(int position) {

        counterList.remove(position);
        saveAndUpdateData();

    }

    protected void addCounter() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.counter_create_layout, null);

        final EditText nameInput = (EditText) dialogView.findViewById(R.id.name_input);
        final EditText initialValueInput = (EditText) dialogView.findViewById(R.id.initial_value_input);
        final EditText descriptionInput = (EditText) dialogView.findViewById(R.id.description_input);

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // This is being handled below in another click listener, to allow for input validation.
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // No action needed to cancel dialog.
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();
        Button save_btn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Validate input:

                String name = nameInput.getText().toString();

                if (name.contentEquals("")) {
                    Toast.makeText(MainActivity.this, "You must enter a name.", Toast.LENGTH_LONG).show();
                    return;
                } else if (initialValueInput.getText().toString().contentEquals("")) {
                    Toast.makeText(MainActivity.this, "You must enter a counter value.", Toast.LENGTH_LONG).show();
                    return;
                }

                Integer initialCount = Integer.valueOf(initialValueInput.getText().toString());

                String comment = descriptionInput.getText().toString();

                Counter counter = new Counter(name, initialCount, comment);
                counterList.add(counter);

                saveAndUpdateData();

                dialog.dismiss();

            }
        });
    }

    protected void viewCounter(int position) {

        final Counter counter = counterList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.counter_view_layout, null);

        final TextView nameText = (TextView) dialogView.findViewById(R.id.name_text);
        final TextView descriptionText = (TextView) dialogView.findViewById(R.id.description_text);
        final TextView initialValueText = (TextView) dialogView.findViewById(R.id.initial_value_text);
        final TextView currentValueText = (TextView) dialogView.findViewById(R.id.current_value_text);
        final TextView lastUpdatedText = (TextView) dialogView.findViewById(R.id.last_updated_text);

        nameText.setText(counter.getName());
        descriptionText.setText(counter.getComment());
        initialValueText.setText(String.valueOf(counter.getCountInit()));
        currentValueText.setText(String.valueOf(counter.getCountCurrent()));
        lastUpdatedText.setText(counter.getLastUpdated().toString());

        builder.setView(dialogView)
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // No action to take. Dialog will automatically close.
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateCountersNumber(){
        int totalCount = counterList.size();

        String countString = String.valueOf(totalCount) + " counter";

        if (totalCount != 1) {
            countString += "s"; // To make the text "counters" plural when there is not only one counter.
        }

        countText.setText(countString);
    }

    // Adapted from lonelyTwitter repository:
    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput("counters.sav");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            // Taken from https://stackoverflow.com/questions/12384064/gson-convert-from-json-to-a-typed-arraylistt
            // 2017-09-19

            Type listType = new TypeToken<ArrayList<Counter>>(){}.getType();
            counterList = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            counterList = new ArrayList<Counter>();
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapter = new CounterAdapter(MainActivity.this, counterList);
        counterRecyclerView.setAdapter(adapter);

        updateCountersNumber();
    }

    /**
     * Saves tweet list to data file
     */
    protected void saveAndUpdateData() {

        updateCountersNumber();

        adapter.notifyDataSetChanged();

        try {
            FileOutputStream fos = openFileOutput("counters.sav",
                    Context.MODE_PRIVATE);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));

            Gson gson = new Gson();
            gson.toJson(counterList, out);
            out.flush();

            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
