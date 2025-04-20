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

public class AddSubjectDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_subject, null);

        EditText etCode = view.findViewById(R.id.etCode);
        EditText etName = view.findViewById(R.id.etName);
        EditText etDesc = view.findViewById(R.id.etDesc);
        EditText etSemester = view.findViewById(R.id.etSemester);

        builder.setView(view)
                .setTitle("Add New Subject")
                .setPositiveButton("Save", (dialog, id) -> {
                    // Handle save logic
                })
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());

        return builder.create();
    }
}