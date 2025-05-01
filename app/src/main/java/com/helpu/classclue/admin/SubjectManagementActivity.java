package com.helpu.classclue.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class SubjectManagementActivity extends AppCompatActivity implements AddSubjectDialog.SubjectAddedListener {

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

        adapter = new AdminSubjectAdapter(subjects);
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
                            subject.setId(document.getId());
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
}