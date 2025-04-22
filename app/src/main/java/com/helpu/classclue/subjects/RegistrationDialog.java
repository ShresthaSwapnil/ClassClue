package com.helpu.classclue.subjects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.helpu.classclue.R;
import com.helpu.classclue.utils.SharedPrefsHelper;

public class RegistrationDialog extends DialogFragment {

    private EditText etPassword;
    private Runnable onSuccess;

    public RegistrationDialog(Runnable onSuccess) {
        this.onSuccess = onSuccess;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_registration, null);

        etPassword = view.findViewById(R.id.etPassword);

        builder.setView(view)
                .setTitle("Semester Registration")
                .setPositiveButton("Confirm", (dialog, id) -> validatePassword())
                .setNegativeButton("Cancel", (dialog, id) -> dismiss());

        return builder.create();
    }

    private void validatePassword() {
        String input = etPassword.getText().toString();
        SharedPrefsHelper prefs = SharedPrefsHelper.getInstance(requireContext());
        String correctPassword = prefs.getRegistrationPassword();

        if (input.equals(correctPassword)) {
            onSuccess.run();
            Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Invalid registration password", Toast.LENGTH_SHORT).show();
        }
    }
}