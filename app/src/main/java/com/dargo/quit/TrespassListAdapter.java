package com.dargo.quit;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class TrespassListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<Trespass> trespasses;
    private TrespassListAdapterCallback callback;


    public TrespassListAdapter(Activity context, List<Trespass> trespasses) {
        super(context, R.layout.listview_row, trespasses);
        this.context = context;
        this.trespasses = trespasses;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.listview_row, null, true);
        TextView dateField = (TextView) rowView.findViewById(R.id.trespassDateField);
        dateField.setText(trespasses.get(position).getFormattedDate());

        Button deleteButton = (Button) rowView.findViewById(R.id.deleteTrespassButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SQLiteTrespasses(context).delete(trespasses.get(position));
                callback.populateListView();
            }
        });

        return rowView;
    }

    public void setCallback(TrespassListAdapterCallback callback) {
        this.callback = callback;
    }
}
