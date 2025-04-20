package com.helpu.classclue.admin;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementActivity extends AppCompatActivity {

    private AdminStudentAdapter adapter;
    private List<Student> students = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);

        RecyclerView recyclerView = findViewById(R.id.rvStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data
        students.add(new Student("S12345", "John Doe", "john@student.help.edu.my", "2025-1"));
        students.add(new Student("S67890", "Jane Smith", "jane@student.help.edu.my", "2025-1"));

        adapter = new AdminStudentAdapter(students);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> showAddStudentDialog());
    }

    private void showAddStudentDialog() {
        AddStudentDialog dialog = new AddStudentDialog();
        dialog.show(getSupportFragmentManager(), "AddStudentDialog");
    }

    public void addNewStudent(Student student) {
        students.add(student);
        adapter.notifyItemInserted(students.size() - 1);
    }
}