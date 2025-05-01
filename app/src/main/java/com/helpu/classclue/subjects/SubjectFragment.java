package com.helpu.classclue.subjects;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Student;
import com.helpu.classclue.models.Subject;
import com.helpu.classclue.utils.SharedPrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends Fragment {

    private SubjectAdapter subjectAdapter;
    private SharedPrefsHelper prefsHelper;
    private List<Subject> subjectList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyView;

    FirebaseFirestore db;
    private static final String TAG = "SubjectFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);

        prefsHelper = new SharedPrefsHelper(requireContext());

        // Initialize views
        recyclerView = view.findViewById(R.id.recyclerViewSubjects);
        progressBar = view.findViewById(R.id.progressBar);
        emptyView = view.findViewById(R.id.emptyView);
        FloatingActionButton fabRegister = view.findViewById(R.id.fabRegister);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectAdapter = new SubjectAdapter(subjectList);
        recyclerView.setAdapter(subjectAdapter);

        fabRegister.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), SubjectRegistrationActivity.class));
        });

        // Load subjects for the current user
        loadEnrolledSubjects();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh the subjects list when returning to this fragment
        loadEnrolledSubjects();
    }

    private void loadEnrolledSubjects() {
        showLoading(true);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            showLoading(false);
            showEmptyView(true, "You need to be logged in to view subjects");
            return;
        }

        // Query the student document using the current user's email
        db.collection("students")
                .whereEqualTo("email", currentUser.getEmail())
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        showLoading(false);
                        showEmptyView(true, "No student profile found");
                        return;
                    }

                    // Get the student document
                    DocumentSnapshot studentDoc = queryDocumentSnapshots.getDocuments().get(0);
                    Student student = studentDoc.toObject(Student.class);

                    if (student == null || student.getSubjects() == null || student.getSubjects().isEmpty()) {
                        showLoading(false);
                        showEmptyView(true, "You are not enrolled in any subjects");
                        return;
                    }

                    // Clear existing subjects
                    subjectList.clear();

                    // Create a set to track unique subject IDs to prevent duplicates
                    List<String> processedSubjectPaths = new ArrayList<>();

                    // Counter for tracking completed fetches
                    final int[] completedFetches = {0};
                    final int totalSubjects = student.getSubjects().size();

                    // Fetch each subject reference
                    for (DocumentReference subjectRef : student.getSubjects()) {
                        // Skip duplicate references
                        String subjectPath = subjectRef.getPath();
                        if (processedSubjectPaths.contains(subjectPath)) {
                            Log.d(TAG, "Skipping duplicate subject reference: " + subjectPath);
                            completedFetches[0]++;

                            // Check if this was the last one
                            if (completedFetches[0] >= totalSubjects) {
                                finalizeSubjectLoading();
                            }
                            continue;
                        }

                        // Add to processed paths
                        processedSubjectPaths.add(subjectPath);

                        subjectRef.get().addOnCompleteListener(task -> {
                            completedFetches[0]++;

                            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                                Subject subject = task.getResult().toObject(Subject.class);
                                if (subject != null) {
                                    // Check for duplicate subject by code and name
                                    boolean isDuplicate = false;
                                    for (Subject existingSubject : subjectList) {
                                        if (existingSubject.getCode().equals(subject.getCode()) &&
                                                existingSubject.getName().equals(subject.getName())) {
                                            isDuplicate = true;
                                            break;
                                        }
                                    }

                                    if (!isDuplicate) {
                                        subjectList.add(subject);
                                    } else {
                                        Log.d(TAG, "Skipping duplicate subject: " + subject.getCode());
                                    }
                                }
                            }

                            // Check if all subjects have been fetched
                            if (completedFetches[0] >= totalSubjects) {
                                finalizeSubjectLoading();
                            }
                        }).addOnFailureListener(e -> {
                            completedFetches[0]++;
                            Log.e(TAG, "Error fetching subject: ", e);

                            // Check if all subjects have been attempted
                            if (completedFetches[0] >= totalSubjects) {
                                finalizeSubjectLoading();
                            }
                        });
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading student profile: ", e);
                    showLoading(false);
                    showEmptyView(true, "Error loading student profile");
                    Toast.makeText(requireContext(), "Failed to load subjects: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void finalizeSubjectLoading() {
        showLoading(false);
        if (subjectList.isEmpty()) {
            showEmptyView(true, "No subjects found");
        } else {
            showEmptyView(false, null);
            subjectAdapter.notifyDataSetChanged();
        }
    }

    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        }
    }

    private void showEmptyView(boolean isEmpty, String message) {
        if (emptyView != null) {
            emptyView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
            if (message != null) {
                emptyView.setText(message);
            }
        }
        if (recyclerView != null) {
            recyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }
    }
}