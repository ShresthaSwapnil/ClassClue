package com.helpu.classclue.subjects;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import com.helpu.classclue.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class SubjectRegistrationActivity extends AppCompatActivity {

    private SubjectRegistrationAdapter adapter;
    private List<Subject> availableSubjects = new ArrayList<>();
    private FirebaseFirestore db;
    private SharedPrefsHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_registration);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        prefs = SharedPrefsHelper.getInstance(this);

        RecyclerView recyclerView = findViewById(R.id.rvAvailableSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new SubjectRegistrationAdapter(availableSubjects);
        recyclerView.setAdapter(adapter);

        // Load subjects from Firestore
        loadAvailableSubjects();
    }

    private void loadAvailableSubjects() {
        String userEmail = prefs.getLastEmail();
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "User not properly logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // First, get the user's document to see what subjects they're already registered for
        db.collection("students").document(userEmail)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    // Get the list of DocumentReferences the student is already registered for
                    List<com.google.firebase.firestore.DocumentReference> registeredSubjectRefs =
                            new ArrayList<>();
                    if (documentSnapshot.exists() && documentSnapshot.contains("subjects")) {
                        registeredSubjectRefs = (List<com.google.firebase.firestore.DocumentReference>)
                                documentSnapshot.get("subjects");
                    }

                    // Create a list of subject IDs the student is already registered for
                    final List<String> registeredSubjectIds = new ArrayList<>();
                    if (registeredSubjectRefs != null) {
                        for (com.google.firebase.firestore.DocumentReference ref : registeredSubjectRefs) {
                            registeredSubjectIds.add(ref.getId());
                        }
                    }

                    // Now query all subjects from Firestore
                    db.collection("subjects")
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    availableSubjects.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String code = document.getId();

                                        // Skip if student is already registered for this subject
                                        if (registeredSubjectIds.contains(code)) {
                                            continue;
                                        }

                                        String name = document.getString("name");
                                        String description = document.getString("description");
                                        String semester = document.getString("semester");
                                        long credits = document.getLong("credits") != null ? document.getLong("credits") : 0;

                                        Subject subject = new Subject(code, name, description, semester, (int)credits);
                                        availableSubjects.add(subject);
                                    }
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(SubjectRegistrationActivity.this,
                                            "Error loading subjects: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error loading user data: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    private class SubjectRegistrationAdapter extends RecyclerView.Adapter<SubjectRegistrationAdapter.ViewHolder> {

        private final List<Subject> subjects;

        public SubjectRegistrationAdapter(List<Subject> subjects) {
            this.subjects = subjects;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_subject_registration, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Subject subject = subjects.get(position);
            holder.tvSubjectCode.setText(subject.getCode());
            holder.tvSubjectName.setText(subject.getName());
            holder.btnRegister.setOnClickListener(v -> showRegistrationDialog(subject));
        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvSubjectCode, tvSubjectName;
            MaterialButton btnRegister;

            ViewHolder(View itemView) {
                super(itemView);
                tvSubjectCode = itemView.findViewById(R.id.tvSubjectCode);
                tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
                btnRegister = itemView.findViewById(R.id.btnRegister);
            }
        }
    }

    private void showRegistrationDialog(Subject subject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_register_subject, null);

        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        EditText etSemester = dialogView.findViewById(R.id.etSemester);

        // Pre-fill semester field with subject's semester if available
        if (subject.getSemester() != null && !subject.getSemester().isEmpty()) {
            etSemester.setText(subject.getSemester());
        }

        builder.setView(dialogView)
                .setTitle("Register for " + subject.getCode())
                .setPositiveButton("Register", (dialog, id) -> {
                    String password = etPassword.getText().toString();
                    String semester = etSemester.getText().toString();

                    validateRegistration(password, semester, subject);
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validateRegistration(String password, String semester, Subject subject) {
        if (password.isEmpty()) {
            Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show();
            return;
        }
        if (semester.isEmpty()) {
            Toast.makeText(this, "Semester required", Toast.LENGTH_SHORT).show();
            return;
        }

        String userEmail = prefs.getLastEmail();
        if (userEmail != null) {
            db.collection("users").document(userEmail)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String userPassword = documentSnapshot.getString("password");
                                if (userPassword != null && password.equals(userPassword)) {
                                    registerSubject(subject, semester);
                                } else {
                                    Toast.makeText(SubjectRegistrationActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SubjectRegistrationActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SubjectRegistrationActivity.this, "Error validating password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "User not properly logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void registerSubject(Subject subject, String semester) {
        // Update the subject's semester if it was changed
        subject.setSemester(semester);

        // Add subject to student's references in Firestore
        String userEmail = prefs.getLastEmail();
        if (userEmail != null && !userEmail.isEmpty()) {
            // Create a DocumentReference instead of a string path
            com.google.firebase.firestore.DocumentReference subjectRef =
                    db.collection("subjects").document(subject.getCode());

            db.collection("students").document(userEmail)
                    .update("subjects",
                            com.google.firebase.firestore.FieldValue.arrayUnion(subjectRef))
                    .addOnSuccessListener(aVoid -> {
                        // Also update local prefs
                        List<Subject> registered = prefs.getRegisteredSubjects();
                        registered.add(subject);
                        prefs.saveRegisteredSubjects(registered);

                        Toast.makeText(SubjectRegistrationActivity.this, "Successfully registered for " + subject.getCode(),
                                Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(SubjectRegistrationActivity.this, "Failed to register: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(SubjectRegistrationActivity.this, "User not properly logged in", Toast.LENGTH_SHORT).show();
        }
    }
}