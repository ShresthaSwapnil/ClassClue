package com.helpu.classclue.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import com.helpu.classclue.utils.FirebaseHelper;

import java.util.ArrayList;
import java.util.List;

public class SubjectManagementActivity extends AppCompatActivity {

    private AdminSubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_management);

        RecyclerView recyclerView = findViewById(R.id.rvSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Subject> subjects = new ArrayList<>();
        // Add sample subjects
        subjects.add(new Subject("BIT314", "Mobile Development", "Android development basics", "2025-1",12));
        subjects.add(new Subject("BIT216", "Web Programming", "Web development fundamentals", "2025-1",24));

        adapter = new AdminSubjectAdapter(subjects);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddSubjectDialog());

    }

    private void showAddSubjectDialog() {
        AddSubjectDialog dialog = new AddSubjectDialog();
        dialog.show(getSupportFragmentManager(), "AddSubjectDialog");
    }

    public void loadSubjectsFromFirebase() {
        FirebaseHelper.getInstance().getSubjectsRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Subject> subjects = new ArrayList<>();
                        for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                            Subject subject = subjectSnapshot.getValue(Subject.class);
                            subject.setId(subjectSnapshot.getKey());
                            subjects.add(subject);
                        }
                        adapter.updateSubjects(subjects);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SubjectManagementActivity.this,
                                "Failed to load subjects: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}