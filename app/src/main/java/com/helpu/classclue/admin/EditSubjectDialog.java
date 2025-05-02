package com.helpu.classclue.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;

import java.util.HashMap;
import java.util.Map;

public class EditSubjectDialog extends DialogFragment {

    private EditText etSubjectName;
    private EditText etSubjectSemester;
    private EditText etSubjectDescription;
    private Subject subject;
    private SubjectEditedListener listener;

    // Interface for callback
    public interface SubjectEditedListener {
        void onSubjectEdited();
    }

    public static EditSubjectDialog newInstance(Subject subject) {
        EditSubjectDialog dialog = new EditSubjectDialog();
        Bundle args = new Bundle();
        args.putString("code", subject.getCode());
        args.putString("name", subject.getName());
        args.putString("semester", subject.getSemester());
        args.putString("description", subject.getDescription());
        dialog.setArguments(args);
        return dialog;
    }

    public void setSubjectEditedListener(SubjectEditedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_subject, null);

        etSubjectName = view.findViewById(R.id.etSubjectName);
        etSubjectSemester = view.findViewById(R.id.etSubjectSemester);
        etSubjectDescription = view.findViewById(R.id.etSubjectDescription);

        // Get subject data from arguments
        if (getArguments() != null) {
            String code = getArguments().getString("code");
            String name = getArguments().getString("name");
            String semester = getArguments().getString("semester");
            String description = getArguments().getString("description");

            subject = new Subject();
            subject.setCode(code);
            subject.setName(name);
            subject.setSemester(semester);
            subject.setDescription(description);

            // Pre-fill the fields
            etSubjectName.setText(name);
            etSubjectSemester.setText(semester);
            etSubjectDescription.setText(description);
        }

        builder.setView(view)
                .setTitle("Edit Subject")
                .setPositiveButton("Update", (dialog, id) -> {
                    updateSubject();
                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    dialog.dismiss();
                });

        return builder.create();
    }

    private void updateSubject() {
        String name = etSubjectName.getText().toString().trim();
        String semester = etSubjectSemester.getText().toString().trim();
        String description = etSubjectDescription.getText().toString().trim();

        if (name.isEmpty() || semester.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a map of fields to update
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("semester", semester);
        updates.put("description", description);

        db.collection("subjects").document(subject.getCode())
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Subject updated successfully", Toast.LENGTH_SHORT).show();
                    if (listener != null) {
                        listener.onSubjectEdited();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to update subject: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }
}