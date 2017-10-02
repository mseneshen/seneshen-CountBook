package ca.ualberta.seneshen_countbook;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * CounterAdapter is responsible for keeping the counterList array in sync with the counter view list.
 * It is responsible for instantiating each counter view with proper view data
 * and setting onClick handlers for all buttons.
 */
public class CounterAdapter extends RecyclerView.Adapter<CounterViewHolder> {

    private MainActivity context;
    private ArrayList<Counter> counterList;

    public CounterAdapter(MainActivity context, ArrayList<Counter> counterList) {
        this.context = context;
        this.counterList = counterList;
    }

    @Override
    public CounterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View counterView = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_layout, parent, false);
        CounterViewHolder viewHolder = new CounterViewHolder(counterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CounterViewHolder holder, final int position) {
        final Counter counter = counterList.get(position);

        holder.labelText.setText(counter.getName());
        holder.countText.setText(Integer.toString(counter.getCountCurrent()));

        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String date_string = df.format("MMM d, yyyy 'at' hh:mm:ss a", counter.getLastUpdated()).toString();

        holder.dateText.setText("Last updated: " + date_string);

        holder.upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter.increment();
                context.saveAndUpdateData();
            }

        });

        holder.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter.decrement();
                context.saveAndUpdateData();
            }
        });

        holder.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter.resetCount();
                context.saveAndUpdateData();
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               counterList.remove(position);
               context.saveAndUpdateData();
           }
        });

        holder.downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter.decrement();
                context.saveAndUpdateData();
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.editCounter(position);
            }
        });

        holder.viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.viewCounter(position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return counterList.size();
    }
}
