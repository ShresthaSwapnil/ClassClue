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
import com.helpu.classclue.models.Student;

public class AddStudentDialog extends DialogFragment {

    private EditText etStudentId, etName, etEmail, etSemester;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_student, null);

        etStudentId = view.findViewById(R.id.etStudentId);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etSemester = view.findViewById(R.id.etSemester);

        builder.setView(view)
                .setTitle("Add New Student")
                .setPositiveButton("Save", (dialog, id) -> {
                    if(validateInputs()) {
                        Student student = new Student(
                                etStudentId.getText().toString(),
                                etName.getText().toString(),
                                etEmail.getText().toString(),
                                etSemester.getText().toString()
                        );
                        ((StudentManagementActivity) requireActivity()).addNewStudent(student);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());

        return builder.create();
    }

    private boolean validateInputs() {
        boolean isValid = true;
        if(etStudentId.getText().toString().isEmpty()) {
            etStudentId.setError("Student ID required");
            isValid = false;
        }
        if(etName.getText().toString().isEmpty()) {
            etName.setError("Name required");
            isValid = false;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Valid email required");
            isValid = false;
        }
        if(etSemester.getText().toString().isEmpty()) {
            etSemester.setError("Semester required");
            isValid = false;
        }
        return isValid;
    }
}