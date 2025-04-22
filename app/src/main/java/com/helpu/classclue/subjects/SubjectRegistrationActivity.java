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

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import com.helpu.classclue.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class SubjectRegistrationActivity extends AppCompatActivity {

    private SubjectRegistrationAdapter adapter;
    private List<Subject> availableSubjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_registration);

        RecyclerView recyclerView = findViewById(R.id.rvAvailableSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get available subjects (mock data - replace with actual data source)
        availableSubjects.add(new Subject("BIT314", "Mobile Development", "Basic Android Development","2024-09",12));
        availableSubjects.add(new Subject("BIT216", "Web Programming", "MEAN Stack","2025-01",24));

        adapter = new SubjectRegistrationAdapter(availableSubjects);
        recyclerView.setAdapter(adapter);
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

        builder.setView(dialogView)
                .setTitle("Register for " + subject.getCode())
                .setPositiveButton("Register", (dialog, id) -> {
                    String password = etPassword.getText().toString();
                    String semester = etSemester.getText().toString();

                    if(validateRegistration(password, semester)) {
                        registerSubject(subject, semester);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validateRegistration(String password, String semester) {
        if(password.isEmpty()) {
            Toast.makeText(this, "Password required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(semester.isEmpty()) {
            Toast.makeText(this, "Semester required", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Add actual password validation logic here
        return true;
    }

    private void registerSubject(Subject subject, String semester) {
        // Implement actual registration logic
        SharedPrefsHelper prefs = SharedPrefsHelper.getInstance(this);
        List<Subject> registered = prefs.getRegisteredSubjects();
        registered.add(subject);
        prefs.saveRegisteredSubjects(registered);

        Toast.makeText(this, "Successfully registered for " + subject.getCode(), Toast.LENGTH_SHORT).show();
    }
}
