package com.dargo.quit;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import static com.dargo.quit.R.layout.trespasses_by_day_listview;


public class TrespassesByDayListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<TrespassCounter> trespassCounters;

    public TrespassesByDayListAdapter(Activity context, List<TrespassCounter> trespassCounters) {
        super(context, trespasses_by_day_listview, trespassCounters);
        this.context = context;
        this.trespassCounters = trespassCounters;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(
                R.layout.trespasses_by_day_listview, null, true);
        setDateTextView(position, rowView);
        setCounterTextView(position, rowView);
        return rowView;
    }

    private void setCounterTextView(int position, View rowView) {
        TextView counterField = (TextView) rowView.findViewById(R.id.trespassesByDateCounterField);
        counterField.setText(String.valueOf(trespassCounters.get(position).getTrespassesPerDay()));
    }

    private void setDateTextView(int position, View rowView) {
        TextView dateField = (TextView) rowView.findViewById(R.id.trespassesDayField);
        dateField.setText(new SimpleDateFormat("MM/dd/yyyy").
                format(trespassCounters.get(position).getDateOfDay()));
    }
}
