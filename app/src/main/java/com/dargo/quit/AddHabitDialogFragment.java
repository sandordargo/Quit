package com.dargo.quit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;

public class AddHabitDialogFragment extends DialogFragment {
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setView(getActivity().getLayoutInflater().inflate(R.layout.add_new_habit_layout, null));

    builder.setMessage(R.string.add_new_habit)
        .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            EditText newHabitEditText = (EditText) getDialog().findViewById(R.id.habit);
            String value = newHabitEditText.getText().toString();
            ((MainActivity) getActivity()).onUserAddsNewHabit(value);
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