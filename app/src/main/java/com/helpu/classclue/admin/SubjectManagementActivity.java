package com.helpu.classclue.admin;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;

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
}