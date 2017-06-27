package com.dargo.quit.utils;


import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import com.dargo.quit.R;
import com.dargo.quit.habits.HabitsManagementActivity;
import com.dargo.quit.trespass_counters.TrespassesByDayListActivity;
import com.dargo.quit.trespasses.ActivityOverview;
import com.dargo.quit.trespasses.TrespassListActivity;

public class OptionsItemSelector {
    Context context;

    public OptionsItemSelector(Context context) {
        this.context = context;
    }

    public Intent select(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.overview_menu_item:
                return new Intent(context, ActivityOverview.class);
            case R.id.list_habits:
                return new Intent(context, HabitsManagementActivity.class);
            case R.id.list_trespasses_by_day_menu_item:
                return new Intent(context, TrespassesByDayListActivity.class);
            case R.id.list_trespasses_for_one_habit:
                return new Intent(context, TrespassListActivity.class);
            default:
                throw new InvalidOptionSelected();
        }
    }

    public static class InvalidOptionSelected extends RuntimeException {
    }
}
