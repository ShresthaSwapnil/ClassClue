package com.helpu.classclue.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import com.helpu.classclue.utils.FirebaseHelper;

public class AddSubjectDialog extends DialogFragment {
    EditText etCode, etName, etDesc, etSemester;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_subject, null);

        etCode = view.findViewById(R.id.etCode);
        etName = view.findViewById(R.id.etName);
        etDesc = view.findViewById(R.id.etDesc);
        etSemester = view.findViewById(R.id.etSemester);

        builder.setView(view)
                .setTitle("Add New Subject")
                .setPositiveButton("Save", (dialog, id) -> {
                    if(validateInputs(etCode, etName, etSemester)) {
                        // Create Subject object
                        Subject subject = new Subject(
                                etCode.getText().toString(),
                                etName.getText().toString(),
                                etDesc.getText().toString(),
                                etSemester.getText().toString(),
                                12
                        );

                        // Get Firebase reference
                        DatabaseReference subjectsRef = FirebaseHelper.getInstance().getSubjectsRef();
                        DatabaseReference newSubjectRef = subjectsRef.push();

                        // Set Firebase ID and save
                        subject.setId(newSubjectRef.getKey());
                        newSubjectRef.setValue(subject.toMap())
                                .addOnSuccessListener(aVoid -> {
                                    // Close dialog on success
                                    dismiss();
                                    // Refresh list in Activity
                                    ((SubjectManagementActivity) requireActivity()).loadSubjectsFromFirebase();
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(), "Error saving subject", Toast.LENGTH_SHORT).show());
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());

        return builder.create();
    }

    private boolean validateInputs(EditText etCode, EditText etName, EditText etSemester) {
        boolean isValid = true;

        if (etCode.getText().toString().trim().isEmpty()) {
            etCode.setError("Subject code required");
            isValid = false;
        }

        if (etName.getText().toString().trim().isEmpty()) {
            etName.setError("Subject name required");
            isValid = false;
        }

        if (etSemester.getText().toString().trim().isEmpty()) {
            etSemester.setError("Semester required");
            isValid = false;
        }

        return isValid;
    }
}