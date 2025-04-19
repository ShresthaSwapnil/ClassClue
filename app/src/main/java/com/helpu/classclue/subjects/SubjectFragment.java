package com.helpu.classclue.subjects;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import com.helpu.classclue.utils.SharedPrefsHelper;
import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends Fragment {

    private SubjectAdapter subjectAdapter;
    private FloatingActionButton fabAddSubject;
    private SharedPrefsHelper prefsHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);

        prefsHelper = new SharedPrefsHelper(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSubjects);
        fabAddSubject = view.findViewById(R.id.fabAddSubject);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectAdapter = new SubjectAdapter(getSampleSubjects());
        recyclerView.setAdapter(subjectAdapter);


        // Show FAB only for admin
        boolean isAdmin = prefsHelper.getUserType().equals("admin");
        fabAddSubject.setVisibility(isAdmin ? View.VISIBLE : View.GONE);

        fabAddSubject.setOnClickListener(v -> showAddSubjectDialog());

        return view;
    }

    private List<Subject> getSampleSubjects() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Mobile Development", "BIT 314", 3));
        subjects.add(new Subject("Web Programming", "BIT 216", 3));
        return subjects;
    }

    private void showAddSubjectDialog() {
        // Implement your add subject dialog
    }
}