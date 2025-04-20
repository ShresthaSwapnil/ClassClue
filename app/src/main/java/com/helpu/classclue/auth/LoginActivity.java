package com.helpu.classclue.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.helpu.classclue.MainActivity;
import com.helpu.classclue.R;
import com.helpu.classclue.admin.AdminDashboardActivity;
import com.helpu.classclue.utils.SharedPrefsHelper;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailEditText, passwordEditText;
    private RadioGroup radioUserType;
    private MaterialButton btnContinue;
    private SharedPrefsHelper prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = SharedPrefsHelper.getInstance(this);

        // Initialize views
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        radioUserType = findViewById(R.id.radioUserType);
        btnContinue = findViewById(R.id.btnContinue);

        // Auto-fill last used email
        String lastEmail = prefs.getLastEmail();
        if (!lastEmail.isEmpty()) {
            emailEditText.setText(lastEmail);
        }

        btnContinue.setOnClickListener(v -> validateAndLogin());
    }

    private void validateAndLogin() {
        // Reset errors
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);

        // Get input values
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        int selectedId = radioUserType.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String userType = radioButton.getText().toString().toLowerCase();

        // Validate email
        if (email.isEmpty()) {
            emailInputLayout.setError("Email is required");
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInputLayout.setError("Enter valid email address");
            return;
        }

        // Validate password
        if (password.isEmpty()) {
            passwordInputLayout.setError("Password is required");
            return;
        } else if (password.length() < 6) {
            passwordInputLayout.setError("Password must be at least 6 characters");
            return;
        }

        // Validate user type
        if (!userType.equals("admin") && !userType.equals("student")) {
            Toast.makeText(this, "Select user type", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save credentials and proceed
        prefs.saveLastEmail(email);
        prefs.saveUserType(userType);
        prefs.setLoggedIn(true);

        // In LoginActivity's validateAndLogin method:
        if (userType.equals("admin")) {
            startActivity(new Intent(this, AdminDashboardActivity.class));
        } else {
            startActivity(new Intent(this, MainActivity.class));
        }
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Clear password field on return
        passwordEditText.setText("");
    }
}