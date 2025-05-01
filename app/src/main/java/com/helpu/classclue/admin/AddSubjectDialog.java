package com.helpu.classclue.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;

public class AddSubjectDialog extends DialogFragment {
    EditText etCode, etName, etDesc, etSemester;
    private FirebaseFirestore db;
    private SubjectAddedListener listener;

    // Interface to notify the activity about the added subject.
    public interface SubjectAddedListener {
        void onSubjectAdded();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SubjectAddedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SubjectAddedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_subject, null);

        db = FirebaseFirestore.getInstance();

        etCode = view.findViewById(R.id.etCode);
        etName = view.findViewById(R.id.etName);
        etDesc = view.findViewById(R.id.etDesc);
        etSemester = view.findViewById(R.id.etSemester);

        builder.setView(view)
                .setTitle("Add New Subject")
                .setPositiveButton("Save", (dialog, id) -> {
                    if (validateInputs(etCode, etName, etSemester)) {
                        String subjectCode = etCode.getText().toString();
                        // Check if document with that ID already exists
                        DocumentReference docRef = db.collection("subjects").document(subjectCode);
                        docRef.get().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    // Document already exists, show error message
                                    etCode.setError("Subject code already exists");
                                    Log.d("AddSubjectDialog", "Document exists!");
                                } else {
                                    // Create Subject object
                                    Subject subject = new Subject();
                                    subject.setCode(subjectCode);
                                    subject.setName(etName.getText().toString());
                                    subject.setDescription(etDesc.getText().toString());
                                    subject.setSemester(etSemester.getText().toString());
                                    subject.setCredit(12);

                                    // Set data with document ID
                                    db.collection("subjects").document(subjectCode).set(subject)
                                            .addOnSuccessListener(aVoid -> {
                                                // Close dialog on success
                                                dismiss();
                                                // Notify the activity to refresh
                                                listener.onSubjectAdded();
                                            })
                                            .addOnFailureListener(e ->
                                                    Toast.makeText(getContext(), "Error saving subject: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                                }
                            } else {
                                // Error occurred, show error message
                                Toast.makeText(getContext(), "Error checking subject code: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
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