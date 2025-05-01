package com.helpu.classclue.admin;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentManagementActivity extends AppCompatActivity {

    private AdminStudentAdapter adapter;
    private List<Student> students = new ArrayList<>();

    FirebaseFirestore db;
    private static final String TAG = "StudentManagement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_management);

        db = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.rvStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AdminStudentAdapter(students);
        recyclerView.setAdapter(adapter);

        loadStudentsFromFirestore();
    }

    private void loadStudentsFromFirestore() {
        db.collection("students")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    students.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        String studentId = document.getString("studentId");
                        String name = document.getString("name");
                        String email = document.getString("email");
                        String intake = document.getString("semester");

                        // Handle null values appropriately
                        if (studentId == null) studentId = "";
                        if (name == null) name = "";
                        if (email == null) email = "";
                        if (intake == null) intake = "";

                        Student student = new Student(studentId, name, email, intake);
                        students.add(student);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error getting documents: ", e);
                });
    }
}