package com.dargo.quit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

import java.util.Date;

public class AddHabitDialogFragment extends DialogFragment {
    public static AddHabitDialogFragment make(String sourceActivity) {
        Bundle bundle = new Bundle();
        bundle.putString("SOURCE_ACTIVITY", sourceActivity);
        AddHabitDialogFragment fragment = new AddHabitDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(getActivity().getLayoutInflater().inflate(R.layout.manage_habit_layout, null));

    builder.setMessage(R.string.add_new_habit)
        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            EditText newHabitEditText = (EditText) getDialog().findViewById(R.id.habit);
            String value = newHabitEditText.getText().toString();
              if (getArguments().getString("SOURCE_ACTIVITY").equals("OVERVIEW")) {
                  ((ActivityOverview) getActivity()).onUserAddsNewHabit(value);
              } else if (getArguments().getString("SOURCE_ACTIVITY").equals("TRESPASS_LIST")) {
                  ((TrespassListActivity) getActivity()).onUserAddsNewHabit(value);
              }
            dialog.dismiss();
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            // User cancelled the dialog
          }
        });
    return builder.create();
  }

}