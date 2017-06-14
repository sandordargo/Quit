package com.dargo.quit;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class TrespassListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<String> dates;


    public TrespassListAdapter(Activity context, List<String> dates) {
        super(context, R.layout.listview_row, dates);
        this.context = context;
        this.dates = dates;
    }

    public View getView(int position, View view, ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.listview_row, null, true);
        TextView dateField = (TextView) rowView.findViewById(R.id.trespassDateField);
        dateField.setText(dates.get(position));
        return rowView;
    }
}
