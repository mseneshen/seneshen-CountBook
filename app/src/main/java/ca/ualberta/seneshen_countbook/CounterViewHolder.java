package ca.ualberta.seneshen_countbook;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * CounterViewHolder is responsible for holding the view of each individual counter,
 * and providing an object oriented way to interact with the elements in the view.
 *
 * This code design is also required by RecyclerView.
 */
public class CounterViewHolder extends RecyclerView.ViewHolder {
    protected TextView labelText;
    protected TextView countText;
    protected TextView dateText;
    protected Button upButton;
    protected Button downButton;
    protected Button editButton;
    protected Button resetButton;
    protected Button deleteButton;
    protected Button viewButton;


    public CounterViewHolder(View itemView) {
        super(itemView);
        labelText = (TextView) itemView.findViewById(R.id.label);
        countText = (TextView) itemView.findViewById(R.id.value);
        dateText = (TextView) itemView.findViewById(R.id.date_text);
        upButton = (Button) itemView.findViewById(R.id.up_btn);
        downButton = (Button) itemView.findViewById(R.id.down_btn);
        editButton = (Button) itemView.findViewById(R.id.edit_btn);
        resetButton = (Button) itemView.findViewById(R.id.reset_btn);
        deleteButton = (Button) itemView.findViewById(R.id.delete_btn);
        viewButton = (Button) itemView.findViewById(R.id.view_btn);

    }


}
