package com.helpu.classclue.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.helpu.classclue.R;
import com.helpu.classclue.auth.LoginActivity;
import com.helpu.classclue.utils.SharedPrefsHelper;

public class AdminDashboardActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize stat cards
        View cardSubjects = findViewById(R.id.cardSubjects);
        View cardEvents = findViewById(R.id.cardEvents);
        View cardStudents = findViewById(R.id.cardStudents);

        // Set up click listeners
        CardView cvSubjects = findViewById(R.id.cvSubjects);
        CardView cvEvents = findViewById(R.id.cvEvents);
        CardView cvStudents = findViewById(R.id.cvStudents);

        cvSubjects.setOnClickListener(v -> showSubjectManagement());
        cvEvents.setOnClickListener(v -> showEventManagement());
        cvStudents.setOnClickListener(v -> showStudentManagement());

        updateDashboardStats();
        MaterialButton btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> logoutAdmin());
    }

    private void updateDashboardStats() {
        // Access TextViews through their parent cards
        TextView tvSubjectsValue = ((ViewGroup) findViewById(R.id.cardSubjects))
                .findViewById(R.id.tvStatValue);
        TextView tvSubjectsTitle = ((ViewGroup) findViewById(R.id.cardSubjects))
                .findViewById(R.id.tvStatTitle);

        TextView tvEventsValue = ((ViewGroup) findViewById(R.id.cardEvents))
                .findViewById(R.id.tvStatValue);
        TextView tvEventsTitle = ((ViewGroup) findViewById(R.id.cardEvents))
                .findViewById(R.id.tvStatTitle);

        TextView tvStudentsValue = ((ViewGroup) findViewById(R.id.cardStudents))
                .findViewById(R.id.tvStatValue);
        TextView tvStudentsTitle = ((ViewGroup) findViewById(R.id.cardStudents))
                .findViewById(R.id.tvStatTitle);

        // Set values
        tvSubjectsValue.setText("12");
        tvSubjectsTitle.setText("Subjects");

        tvEventsValue.setText("45");
        tvEventsTitle.setText("Events");

        tvStudentsValue.setText("150");
        tvStudentsTitle.setText("Students");
    }

    private void showSubjectManagement() {
        startActivity(new Intent(this, SubjectManagementActivity.class));
    }

    private void showEventManagement() {
        startActivity(new Intent(this, EventManagementActivity.class));
    }

    private void showStudentManagement() {
        startActivity(new Intent(this, StudentManagementActivity.class));
    }

    private void logoutAdmin() {
        SharedPrefsHelper prefs = SharedPrefsHelper.getInstance(this);
        prefs.clearAll();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity();
    }
}