package com.helpu.classclue.profile;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.helpu.classclue.R;
import com.helpu.classclue.utils.SharedPrefsHelper;

public class EditProfileFragment extends Fragment {

    private SharedPrefsHelper prefs;
    private EditText etName, etEmail;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        prefs = SharedPrefsHelper.getInstance(requireContext());

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        MaterialButton btnSave = view.findViewById(R.id.btnSave);

        // Load current values
        etName.setText(prefs.getString("user_name"));
        etEmail.setText(prefs.getString("user_email"));

        btnSave.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void saveProfile() {
        String newName = etName.getText().toString().trim();
        String newEmail = etEmail.getText().toString().trim();

        if (newName.isEmpty()) {
            etName.setError("Name cannot be empty");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            etEmail.setError("Enter valid email");
            return;
        }

        prefs.put("user_name", newName);
        prefs.put("user_email", newEmail);

        // Update parent profile view
        getParentFragmentManager().popBackStack();
    }
}
