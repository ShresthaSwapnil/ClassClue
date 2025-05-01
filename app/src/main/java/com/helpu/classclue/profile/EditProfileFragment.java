package com.helpu.classclue.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpu.classclue.R;
import com.helpu.classclue.utils.SharedPrefsHelper;

import java.util.HashMap;
import java.util.Map;

public class EditProfileFragment extends Fragment {

    private SharedPrefsHelper prefs;
    private EditText etName, etEmail;
    FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        prefs = SharedPrefsHelper.getInstance(requireContext());

        db = FirebaseFirestore.getInstance();

        etName = view.findViewById(R.id.etName);
        MaterialButton btnSave = view.findViewById(R.id.btnSave);

        // Load current values
        etName.setText(prefs.getString("user_name"));

        btnSave.setOnClickListener(v -> saveProfile());

        return view;
    }

    private void saveProfile() {
        String newName = etName.getText().toString().trim();
        String docId = prefs.getLastEmail();

        if (newName.isEmpty()) {
            etName.setError("Name cannot be empty");
            return;
        }

        DocumentReference userDocument = db.collection("users").document(docId);

        Map<String, Object> data = new HashMap<>();
        data.put("name", newName);

        userDocument.update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Update shared prefs
                        prefs.put("user_name", newName);

                        // Notify success
                        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();

                        // Update parent profile view
                        getParentFragmentManager().popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Notify error
                        Toast.makeText(
                                getContext(),
                                "Error updating profile: " + e.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}
