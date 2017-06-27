package com.dargo.quit.trespasses;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dargo.quit.utils.ListAdapterCallback;
import com.dargo.quit.R;

import java.util.List;


public class TrespassListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<Trespass> trespasses;
    private ListAdapterCallback callback;
    private String source;


    public TrespassListAdapter(Activity context, List<Trespass> trespasses, ListAdapterCallback callback) {
        super(context, R.layout.trespass_listview_row, trespasses);
        this.context = context;
        this.trespasses = trespasses;
        this.callback = callback;
        if (this.callback instanceof ActivityOverview) {
            source = "OVERVIEW";
        } else if (this.callback instanceof TrespassListActivity) {
            source = "TRESPASS_LIST";
        }
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        View rowView = context.getLayoutInflater().inflate(R.layout.trespass_listview_row, null, true);
        setUpEditButton(position, rowView);
        setUpDeleteButton(position, rowView);
        return rowView;
    }

    private void setUpEditButton(final int position, View rowView) {
        TextView dateField = (TextView) rowView.findViewById(R.id.trespassDateField);
        dateField.setText(trespasses.get(position).getFormattedDate());
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTrespassDateDialogFragment fragment =
                        EditTrespassDateDialogFragment.make(trespasses.get(position).getId(),
                                trespasses.get(position).getDate(), source);
                fragment.show(context.getFragmentManager(), "Edit trespass date");
            }
        });
    }

    private void setUpDeleteButton(final int position, View rowView) {
        Button deleteButton = (Button) rowView.findViewById(R.id.deleteTrespassButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConstSQLiteTrespasses(context).delete(trespasses.get(position));
                callback.populateListView();
            }
        });
    }

    public void setCallback(ListAdapterCallback callback) {
        this.callback = callback;
    }
}
