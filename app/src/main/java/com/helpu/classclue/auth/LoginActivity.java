package com.helpu.classclue.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.helpu.classclue.MainActivity;
import com.helpu.classclue.R;
import com.helpu.classclue.admin.AdminDashboardActivity;
import com.helpu.classclue.utils.SharedPrefsHelper;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailInputLayout, passwordInputLayout;
    private TextInputEditText emailEditText, passwordEditText;
    private RadioGroup radioUserType;
    private TextView signupRedirectText;
    private Button btnContinue;
    private SharedPrefsHelper prefs;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs = SharedPrefsHelper.getInstance(this);

        // Initialize views
        emailInputLayout = findViewById(R.id.emailInputLayout);
        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        radioUserType = findViewById(R.id.radioUserType);
        signupRedirectText = findViewById(R.id.loginRedirectText);
        btnContinue = findViewById(R.id.btnContinue);

        auth = FirebaseAuth.getInstance();

        // Auto-fill last used email
        String lastEmail = prefs.getLastEmail();
        if (!lastEmail.isEmpty()) {
            emailEditText.setText(lastEmail);
        }

        btnContinue.setOnClickListener(v -> validateAndLogin());

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void validateAndLogin() {
        // Reset errors
        emailInputLayout.setError(null);
        passwordInputLayout.setError(null);

        // Get input values
        String email = emailEditText.getText().toString().trim();
        String pass = passwordEditText.getText().toString().trim();
        int selectedId = radioUserType.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(selectedId);
        String userType = radioButton.getText().toString().toLowerCase();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {
                auth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                if (userType.equals("admin")) {
                                    startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                                } else {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                emailInputLayout.setError("Empty fields are not allowed");
            }
        } else if (email.isEmpty()) {
            emailInputLayout.setError("Empty fields are not allowed");
        } else {
            passwordInputLayout.setError("Please enter correct email");
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
    }



    @Override
    protected void onResume() {
        super.onResume();
        // Clear password field on return
        passwordEditText.setText("");
    }


}