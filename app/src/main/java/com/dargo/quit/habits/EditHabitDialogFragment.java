package com.dargo.quit.habits;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.dargo.quit.R;

public class EditHabitDialogFragment extends DialogFragment {

  public static EditHabitDialogFragment make(long habitId, String habitName) {
      EditHabitDialogFragment fragment = new EditHabitDialogFragment();
      Bundle bundle = new Bundle();
      bundle.putLong("ID", habitId);
      bundle.putString("NAME", habitName);
      fragment.setArguments(bundle);
      return fragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    View content = getActivity().getLayoutInflater().inflate(R.layout.manage_habit_layout, null);
    final EditText newHabitEditText = (EditText) content.findViewById(R.id.habit);
    newHabitEditText.setText(getArguments().getString("NAME"));
    newHabitEditText.setSelection(newHabitEditText.getText().length());
    builder.setView(content);

    builder.setTitle(R.string.edit_habit_name)
        .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {

          public void onClick(DialogInterface dialog, int id) {
            String value = newHabitEditText.getText().toString();
            ((HabitsManagementActivity) getActivity()).updateHabitName(getArguments().getLong("ID"), value);
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