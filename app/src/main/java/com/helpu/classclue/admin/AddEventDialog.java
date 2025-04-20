package com.helpu.classclue.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Event;

public class AddEventDialog extends DialogFragment {

    private EditText etTitle, etSubject, etDate, etTime, etLocation;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_event, null);

        etTitle = view.findViewById(R.id.etTitle);
        etSubject = view.findViewById(R.id.etSubject);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        etLocation = view.findViewById(R.id.etLocation);

        builder.setView(view)
                .setTitle("Add New Event")
                .setPositiveButton("Save", (dialog, id) -> {
                    if(validateInputs()) {
                        Event event = new Event(
                                etTitle.getText().toString(),
                                etSubject.getText().toString(),
                                etDate.getText().toString(),
                                etTime.getText().toString(),
                                etLocation.getText().toString()
                        );
                        ((EventManagementActivity) requireActivity()).addNewEvent(event);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());

        return builder.create();
    }

    private boolean validateInputs() {
        boolean isValid = true;
        if(etTitle.getText().toString().isEmpty()) {
            etTitle.setError("Title required");
            isValid = false;
        }
        if(etSubject.getText().toString().isEmpty()) {
            etSubject.setError("Subject required");
            isValid = false;
        }
        if(etDate.getText().toString().isEmpty()) {
            etDate.setError("Date required");
            isValid = false;
        }
        if(etTime.getText().toString().isEmpty()) {
            etTime.setError("Time required");
            isValid = false;
        }
        return isValid;
    }
}