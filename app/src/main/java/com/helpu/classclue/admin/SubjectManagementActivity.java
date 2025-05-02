package com.helpu.classclue.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity
        implements AddSubjectDialog.SubjectAddedListener,
        EditSubjectDialog.SubjectEditedListener,
        AdminSubjectAdapter.OnSubjectActionListener {

    private AdminSubjectAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_management);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.rvSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Subject> subjects = new ArrayList<>();

        adapter = new AdminSubjectAdapter(subjects, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddSubjectDialog());
        loadSubjectsFromFirestore();
    }

    private void showAddSubjectDialog() {
        AddSubjectDialog dialog = new AddSubjectDialog();
        dialog.show(getSupportFragmentManager(), "AddSubjectDialog");
    }

    public void loadSubjectsFromFirestore() {
        db.collection("subjects")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Subject> subjects = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Subject subject = document.toObject(Subject.class);
                            subject.setCode(document.getId());
                            subjects.add(subject);
                        }
                        adapter.updateSubjects(subjects);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SubjectManagementActivity.this,
                                "Failed to load subjects: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onSubjectAdded() {
        // Refresh the subjects when a new one is added
        loadSubjectsFromFirestore();
    }

    @Override
    public void onSubjectEdited() {
        // Refresh the subjects when one is edited
        loadSubjectsFromFirestore();
    }

    @Override
    public void onEditSubject(Subject subject, int position) {
        // Show the edit dialog
        EditSubjectDialog dialog = EditSubjectDialog.newInstance(subject);
        dialog.setSubjectEditedListener(this);
        dialog.show(getSupportFragmentManager(), "EditSubjectDialog");
    }

    @Override
    public void onDeleteSubject(Subject subject, int position) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete this subject?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteSubjectFromFirestore(subject, position);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteSubjectFromFirestore(Subject subject, int position) {
        db.collection("subjects").document(subject.getCode())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Subject deleted successfully", Toast.LENGTH_SHORT).show();
                    adapter.removeSubject(position);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to delete subject: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}