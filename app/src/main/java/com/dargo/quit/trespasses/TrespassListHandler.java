package com.dargo.quit.trespasses;


import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

import com.dargo.quit.habits.Habit;
import com.dargo.quit.utils.ListAdapterCallback;

import java.util.ArrayList;
import java.util.List;

public class TrespassListHandler {
    private int listViewId;
    ListAdapterCallback callback;
    private Activity activity;
    private Context context;
    private ListView listView;
    private TrespassListAdapter listAdapter;

    TrespassListHandler(int listViewId, ListAdapterCallback callback, Activity activity, Context context) {
        this.listViewId = listViewId;
        this.callback = callback;
        this.activity = activity;
        this.context = context;
    }

    public void populateListView(Habit habit) {
        listView = (ListView) activity.findViewById(listViewId);
        List<Trespass> values = new ArrayList<>();
        for (Trespass trespass : new ConstSQLiteTrespasses(context).iterate(habit)) {
            values.add(trespass);
        }
        listAdapter = new TrespassListAdapter(activity, values, callback);
        listView.setAdapter(listAdapter);
    }

    public void cleanListView() {
        listView = (ListView) activity.findViewById(listViewId);
        List<Trespass> emptyList = new ArrayList<>();
        listAdapter = new TrespassListAdapter(activity, emptyList, callback);
        listView.setAdapter(listAdapter);
    }
}
