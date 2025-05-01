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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Student;

public class AddStudentDialog extends DialogFragment {
    private FirebaseFirestore db;
    private static final String TAG = "AddStudentDialog";
    private StudentAddedListener listener;

    private EditText etStudentId, etName, etEmail, etSemester;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (StudentAddedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement StudentAddedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_student, null);

        db = FirebaseFirestore.getInstance();

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
                        addStudentToFirestore(student);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());

        return builder.create();
    }

    private void addStudentToFirestore(Student student) {
        String email = student.getEmail().toLowerCase();

        db.collection("students")
                .document(email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Document already exists, handle accordingly
                            Log.w(TAG, "Student with this email already exists.");
                            Toast.makeText(getActivity(), "A student with this email already exists.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Document does not exist, add the new student
                            db.collection("students")
                                    .document(email)
                                    .set(student)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                        Toast.makeText(getActivity(), "Student added successfully!", Toast.LENGTH_SHORT).show();
                                        // Notify the activity that a student was added
                                        if (listener != null) {
                                            listener.onStudentAdded(student);
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error adding document", e);
                                        Toast.makeText(getActivity(), "Failed to add student.", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    } else {
                        Log.e(TAG, "Error getting document: ", task.getException());
                    }
                });
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