package com.dargo.quit;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class EditTrespassTimeDialogFragment extends DialogFragment implements
        TimePickerDialog.OnTimeSetListener {

    public static EditTrespassTimeDialogFragment make(long trespassId, Date date) {
        Bundle bundle = new Bundle();
        bundle.putLong("TRESPASS_ID", trespassId);
        bundle.putLong("DATE", date.getTime());
        EditTrespassTimeDialogFragment fragment = new EditTrespassTimeDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
      final Calendar c = Calendar.getInstance();
      c.setTimeInMillis(getArguments().getLong("DATE"));
      int hour = c.get(Calendar.HOUR_OF_DAY);
      int minute = c.get(Calendar.MINUTE);

      return new TimePickerDialog(getActivity(), this, hour, minute,
              DateFormat.is24HourFormat(getActivity()));
  }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        final Calendar calendar = Calendar.getInstance();
        setNewDate(hourOfDay, minute, calendar);
        ((TrespassListActivity) getActivity()).updateTrespassDate(
                getArguments().getLong("TRESPASS_ID"), new Date(calendar.getTimeInMillis()));
    }

    private void setNewDate(int hourOfDay, int minute, Calendar calendar) {
        calendar.setTimeInMillis(getArguments().getLong("DATE"));
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day, hourOfDay, minute);
    }
}