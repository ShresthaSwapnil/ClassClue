package com.helpu.classclue.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.helpu.classclue.MainActivity;
import com.helpu.classclue.R;
import com.helpu.classclue.admin.AdminDashboardActivity;
import com.helpu.classclue.auth.LoginActivity;
import com.helpu.classclue.utils.SharedPrefsHelper;

public class ProfileFragment extends Fragment {

    private SharedPrefsHelper prefs;
    private TextView tvName, tvEmail, tvUserType;
    FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        prefs = SharedPrefsHelper.getInstance(requireContext());

        // Initialize views
        tvName = root.findViewById(R.id.tvName);
        tvEmail = root.findViewById(R.id.tvEmail);
        tvUserType = root.findViewById(R.id.tvUserType);
        MaterialButton btnLogout = root.findViewById(R.id.btnLogout);

        db = FirebaseFirestore.getInstance();

        // Set click listeners
        root.findViewById(R.id.optionEditProfile).setOnClickListener(v -> openEditProfile());
        root.findViewById(R.id.optionNotifications).setOnClickListener(v -> openNotificationSettings());
        root.findViewById(R.id.optionAbout).setOnClickListener(v -> openAbout());
        btnLogout.setOnClickListener(v -> logoutUser());

        loadUserData();
        return root;
    }

    private void loadUserData() {
        // Replace with actual user data from SharedPreferences/Firebase
        DocumentReference docRef = db.collection("users").document(prefs.getLastEmail());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String retrievedRole = documentSnapshot.getString("role");
                    String retrievedName = documentSnapshot.getString("name");
                    String retrievedEmail = documentSnapshot.getString("email");
                    tvName.setText(retrievedName);
                    tvEmail.setText(retrievedEmail);
                    tvUserType.setText(retrievedRole.equals("student") ? "Student" : "");
                    }
            }
        });
    }

    private void openEditProfile() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_profile_to_edit);
    }

    private void openNotificationSettings() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_profile_to_notifications);
    }

    private void openAbout() {
        Navigation.findNavController(requireView())
                .navigate(R.id.action_profile_to_about);
    }

    private void logoutUser() {
        prefs.clearAll();
        startActivity(new Intent(requireActivity(), LoginActivity.class));
        requireActivity().finish();
    }
}