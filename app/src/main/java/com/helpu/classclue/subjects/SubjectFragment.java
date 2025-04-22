package com.helpu.classclue.subjects;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.helpu.classclue.R;
import com.helpu.classclue.models.Subject;
import com.helpu.classclue.utils.SharedPrefsHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SubjectFragment extends Fragment {
    private boolean isAdmin;

    private SubjectAdapter subjectAdapter;
    private SharedPrefsHelper prefsHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isAdmin = SharedPrefsHelper.getInstance(requireContext()).getUserType().equals("admin");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = isAdmin ? R.layout.fragment_subjects_admin : R.layout.fragment_subjects           ;
        View view = inflater.inflate(R.layout.fragment_subjects, container, false);

        prefsHelper = new SharedPrefsHelper(requireContext());

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSubjects);
        FloatingActionButton fabRegister = view.findViewById(R.id.fabRegister);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectAdapter = new SubjectAdapter(getSampleSubjects());
        recyclerView.setAdapter(subjectAdapter);


        fabRegister.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), SubjectRegistrationActivity.class));
        });



        return view;
    }

    private List<Subject> getSampleSubjects() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Mobile Development", "BIT 314", "Android development basics","2025 Feburary",12));
        subjects.add(new Subject("Web Programming", "BIT 216", "Web development fundamentals","2025 Feburary",24));
        return subjects;
    }

    private void setupStudentUI(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewSubjects);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        subjectAdapter = new SubjectAdapter(getSampleSubjects());
        recyclerView.setAdapter(subjectAdapter);
    }
}