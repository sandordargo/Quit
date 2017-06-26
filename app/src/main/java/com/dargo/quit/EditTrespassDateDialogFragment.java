package com.dargo.quit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditTrespassDateDialogFragment extends DialogFragment implements
        DatePickerDialog.OnDateSetListener{

  public static EditTrespassDateDialogFragment make(long trespassId, Date date, String sourceActivity) {
      Bundle bundle = new Bundle();
      bundle.putLong("TRESPASS_ID", trespassId);
      bundle.putLong("DATE", date.getTime());
      bundle.putString("SOURCE_ACTIVITY", sourceActivity);
      EditTrespassDateDialogFragment fragment = new EditTrespassDateDialogFragment();
      fragment.setArguments(bundle);
      return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar c = Calendar.getInstance();
      c.setTimeInMillis(getArguments().getLong("DATE"));

      int year = c.get(Calendar.YEAR);
      int month = c.get(Calendar.MONTH);
      int day = c.get(Calendar.DAY_OF_MONTH);

      return new DatePickerDialog(getActivity(), this, year, month, day);
  }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getArguments().getLong("DATE"));
        calendar.set(year, month, dayOfMonth);

        if (getArguments().getString("SOURCE_ACTIVITY").equals("OVERVIEW")) {
            ((ActivityOverview) getActivity()).callTimePicker(getArguments().getLong("TRESPASS_ID"),
                    new Date(calendar.getTimeInMillis()));
        } else if (getArguments().getString("SOURCE_ACTIVITY").equals("TRESPASS_LIST")) {
            ((TrespassListActivity) getActivity()).callTimePicker(getArguments().getLong("TRESPASS_ID"),
                    new Date(calendar.getTimeInMillis()));
        }
    }

}